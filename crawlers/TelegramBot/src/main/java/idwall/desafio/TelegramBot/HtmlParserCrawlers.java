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

	public void onUpdateReceived(Update update) {

		String command = update.getMessage().getText();
		Long chatId = update.getMessage().getChatId();

		SendMessage message = new SendMessage();

		if (command.contains("/NadaPraFazer")) {
			String palavras = command.replaceAll("/NadaPraFazer ", "");

			HtmlParserCrawlers htmlParserCrawlers = null;
			htmlParserCrawlers.conexion(palavras, chatId);

		} else {
			message.setText("Comando inválido.\nPara realizar uma pesquisa digite: /NadaPraFazer +pesquisa");
			
			message.setChatId(update.getMessage().getChatId());
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}		

	}

	private Document document;

	public HtmlParserCrawlers(Document document) {
		this.document = document;
	}

	public void conexion(String pesquisa, Long chatId) {
		String[] p = pesquisa.split(";");
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

	}

	private void getCrawlers(String palavra, Long chatId) {

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
						+ "\nLink para os " + "Comentarios: " + href
						+ "\n-----------------------------------------------------------------------"
						+ "--------------------------------------------------------------------");

				message.setChatId(chatId);

				try {
					execute(message);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "JMsCrawlersBot";
	}

	public String getBotToken() {
		// TODO Auto-generated method stub
		return "704319040:AAHJeiErHoqcLBoyF4Y27wEaG5u0wTXHDRs";
	}

}
