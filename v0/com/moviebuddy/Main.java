package moviebuddy;

import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

/**
 * @author Greg
 * @version 1.0
 * @brief com.moviebuddy.Main
 * @date 07/14/16
 */

public class Main {

    private static final String LOGTAG = "BOTHANDLERS";

    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new com.moviebuddy.BotHandlers());//register updatehandler
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}