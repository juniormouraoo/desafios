package idwall.desafio.TelegramBot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MainTelegram {

	public static void main(String[] args) {

		ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new HtmlParserCrawlers(null));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

	}

}
