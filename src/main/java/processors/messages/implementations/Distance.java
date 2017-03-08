package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendMessageDefault;
import static services.Commands.*;
import static services.Keyboards.getDistanceKeyboard;
import static services.Keyboards.getSettingsKeyboard;
import static services.Messages.*;
import static services.States.DISTANCERANGE;
import static services.States.SETTINGS;

public class Distance extends AbstractMessage {
    @Override
    public SendMessage process(Message message) {

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case Closest:
                    Manager.getInstance().setDistance(message.getFrom().getId(),message.getChatId(),"closest");
                    sendMessageRequest = messageCreator(getSettingsKeyboard(),getSettingsSetDistance(),
                            message,SETTINGS,false);
                    break;
                case Range:
                    sendMessageRequest = messageCreator(null,getRangeMessage(), message,DISTANCERANGE,false);
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
