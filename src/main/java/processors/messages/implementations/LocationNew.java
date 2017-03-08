package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendChooseOptionMessage;
import static services.Commands.Settings;
import static services.Inspections.checkTypedLocation;
import static services.Keyboards.getPriceKeyboard;
import static services.Keyboards.getSettingsKeyboard;
import static services.Keyboards.getStoredNewLocationKeyboard;
import static services.Messages.getOptionMessage;
import static services.Messages.getSettingsMessage;
import static services.States.SETTINGS;

/**
 * Created by Greg
 */
public class LocationNew extends AbstractMessage {
    @Override
    public SendMessage process(Message message) {
        SendMessage sendMessageRequest;

        if (message.isReply() && checkTypedLocation(message)) {
            final String msg = message.getText();
            switch (msg) {
                case Settings:
                    Manager.getInstance().setTypedLocation(message.getFrom().getId(), message.getChatId(), message.getText());
                    sendMessageRequest = messageCreator(getSettingsKeyboard(),
                            getSettingsMessage() + "\n" + "You entered a new location: " + "\n" + message.getText(),
                            message, SETTINGS, false);
                    break;
                default:
                    sendMessageRequest = messageCreator(getStoredNewLocationKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else {
            sendMessageRequest = sendChooseOptionMessage(message, getPriceKeyboard());
        }

        return sendMessageRequest;
    }
}
