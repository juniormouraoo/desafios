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

		String command = update.getMessage().getText();
		Long chatId = update.getMessage().getChatId();

		SendMessage message = new SendMessage();

		if (command.contains("/NadaPraFazer")) {
			String palavras = command.replaceAll("/NadaPraFazer ", "");
			String[] p = palavras.split(";");
			String url = "https://old.reddit.com/r/";

			for (String palavra : p) {

				try {
					Document document = Jsoup.connect(url + palavra + "/top/").get();
					HtmlParserCrawlers parserCrawlers = new HtmlParserCrawlers(document);
					parserCrawlers.getCrawlers(palavra, chatId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			message.setText(
					"Comando inválido.\nPara realizar uma pesquisa digite: \n/NadaPraFazer +pesquisa *separada por ;*");

			message.setChatId(update.getMessage().getChatId());

			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

	}

	public void conexion(String pesquisa) {

	}

	private void getCrawlers(String palavra, Long chatId) {

		Boolean vote = false;
		SendMessage message = new SendMessage();
		Elements elements = document.getElementsByClass("thing");
		for (Element element : elements) {
			String upvote = element.getElementsByClass("score unvoted").attr("title");
			int upvote_int = Integer.parseInt(upvote);
			if (upvote_int >= 5000) {
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
