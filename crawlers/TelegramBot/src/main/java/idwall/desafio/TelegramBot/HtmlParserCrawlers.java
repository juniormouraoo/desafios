package idwall.desafio.TelegramBot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HtmlParserCrawlers extends TelegramLongPollingBot {

	private Document document;

	public HtmlParserCrawlers(Document document) {
		this.document = document;
	}

	public void onUpdateReceived(Update update) {

		// Captura da mensagem do telegram
		String command = update.getMessage().getText();
		Long chatId = update.getMessage().getChatId();

		SendMessage message = new SendMessage();

		// Condicao de funcionamento apenas por comendo especifico
		if (command.contains("/NadaPraFazer")) {
			// Exclusao do comando deixando apenas as palavras para pesquisa
			String palavras = command.replaceAll("/NadaPraFazer ", "");
			// Separa as palavras passadas por parametro
			String[] p = palavras.split(";");
			String url = "https://old.reddit.com/r/";

			for (String palavra : p) {

				try {
					// Conexao e captura das informacoes no old.reddit
					Document document = Jsoup.connect(url + palavra + "/top/").get();
					HtmlParserCrawlers parserCrawlers = new HtmlParserCrawlers(document);
					parserCrawlers.getCrawlers(palavra, chatId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			// Mensagem de comando invalido
			message.setText(
					"Comando inválido.\nPara realizar uma pesquisa digite: \n/NadaPraFazer +pesquisa *separada por ;*");

			message.setChatId(update.getMessage().getChatId());

			// Retorno da mensagem para o telegram
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	private void getCrawlers(String palavra, Long chatId) {

		Boolean vote = false;
		SendMessage message = new SendMessage();
		// Captura dos elementos
		Elements elements = document.getElementsByClass("thing");
		for (Element element : elements) {
			// Captura de upvotes
			String upvote = element.getElementsByClass("score unvoted").attr("title");
			int upvote_int = Integer.parseInt(upvote);
			if (upvote_int >= 5000) {
				// Condicao para os dois nomes de titulos encontrados
				String title = element.getElementsByClass("title may-blank ").text();
				if (title == null || title.trim().isEmpty()) {
					title = element.getElementsByClass("title may-blank outbound").text();
				}
				String href = element.getElementsByClass("bylink comments may-blank").attr("href");

				message.setText("Subreddit: " + palavra + "\nTítulo: " + title + "\nUpvotes: " + upvote
						+ "\nLink para os " + "Comentarios: " + href);

				message.setChatId(chatId);

				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				vote = true;
			}
		}
		// Retorno caso a pesquisa de uma das palavras não retorne algo que se encaixa
		// nas condicoes
		if (vote == false) {
			message.setText("A pesquisa " + palavra + " não possui nenhuma thread com mais de 5000 upvotes");

			message.setChatId(chatId);

			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public String getBotUsername() {

		return "JMsCrawlersBot";
	}

	public String getBotToken() {

		return "704319040:AAHJeiErHoqcLBoyF4Y27wEaG5u0wTXHDRs";
	}
}
