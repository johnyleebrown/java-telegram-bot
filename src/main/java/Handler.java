import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import processors.Maker;
import processors.messages.MessageMaker;
import processors.updates.UpdateMaker;
import processors.venues.VenueMaker;

import java.util.Locale;
import java.util.ResourceBundle;

import static processors.messages.implementations.Common.onSettingsChosenAfterUpdate;

/**
 * Created by Greg on 10/25/16.
 */

public class Handler extends TelegramLongPollingBot {

    private static final String LOGTAG = "Handler";
    private static final ResourceBundle commands = ResourceBundle.getBundle("commands", Locale.getDefault());

    public String getBotUsername() {
        return Config.USER;
    }

    public String getBotToken() {
        return Config.TOKEN;
    }

    public void onUpdateReceived(Update update) {

        final int state = Manager.getInstance().getUserState(update.getCallbackQuery().getFrom().getId());

        try {
            if (update.hasMessage() && update.getMessage().hasText() ) {
                Maker maker = new MessageMaker();
                sendMessage((SendMessage) maker.createMessageProcessor().compose(update,state));
            } else if (update.getCallbackQuery().getData().equals(commands.getString("Map"))) {
                Maker maker = new VenueMaker();
                sendVenue((SendVenue) maker.createMessageProcessor().compose(update,state));
            } else if (update.getCallbackQuery().getData().equals(commands.getString("Settings"))) {
                sendMessage(onSettingsChosenAfterUpdate(update.getMessage()));
            } else {
                Maker maker = new UpdateMaker();
                editMessageText((EditMessageText) maker.createMessageProcessor().compose(update,state));
            }
        } catch (TelegramApiException e){
            BotLogger.warn(LOGTAG, e);
        }


    }


}
