package com.moviebuddy;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

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
            telegramBotsApi.registerBot(new BotHandlers());//register updatehandler
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}