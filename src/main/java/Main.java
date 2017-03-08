import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Created by Greg on 10/25/16.
 */
public class Main {

    private static final String LOGTAG = "MAIN";

    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Handler());
        } catch (TelegramApiException e) {
            BotLogger.warn(LOGTAG, e);
        }
    }

}
