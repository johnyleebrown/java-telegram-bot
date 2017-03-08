package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendMessageDefault;
import static services.Commands.*;
import static services.Commands.Settings;
import static services.Keyboards.getDistanceKeyboard;
import static services.Keyboards.getSettingsKeyboard;
import static services.Messages.*;
import static services.States.EDITADDLOCATION;
import static services.States.SETTINGS;

/**
 * Created by Greg
 */
public class LocationEdit extends AbstractMessage {
    @Override
    public SendMessage process(Message message) {
        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case NewLocation:
                case EditLocation:
                    sendMessageRequest = messageCreator(null,getDistRangeMessage(), message,EDITADDLOCATION,false);
                    break;
                case Settings:
                    sendMessageRequest = messageCreator(getSettingsKeyboard(), getSettingsMessage(), message, SETTINGS, false);
                    break;
                default:
                    sendMessageRequest = messageCreator(getDistanceKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }
}
