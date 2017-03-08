package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static services.Commands.Settings;
import static services.Commands.ShowSessions;
import static services.Keyboards.*;
import static services.Messages.*;
import static services.States.*;

/**
 * Created by Greg
 */
public class Main extends AbstractMessage {

    @Override
    public SendMessage process(Message message) {
        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case ShowSessions:
                    sendMessageRequest = messageCreator(getLocationKeyboard(message),getPickALocationMessage(),
                            message,PICKALOCATION,false);
                    break;
                case Settings:
                    sendMessageRequest = messageCreator(getSettingsKeyboard(), getSettingsMessage(), message, SETTINGS, false);
                    break;
                default:
                    sendMessageRequest = messageCreator(getMainMenuKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else {
            sendMessageRequest = messageCreator(getMainMenuKeyboard(),getHelpMessage(),message,MAINMENU,false);
        }

        return sendMessageRequest;
    }
}
