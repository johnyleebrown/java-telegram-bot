package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendChooseOptionMessage;
import static services.Commands.Settings;
import static services.Inspections.checkPriceRange;
import static services.Keyboards.getPriceKeyboard;
import static services.Keyboards.getSettingsKeyboard;
import static services.Messages.getOptionMessage;
import static services.Messages.getSettingsMessage;
import static services.States.SETTINGS;

/**
 * Created by Greg
 */
public class PriceRange extends AbstractMessage {

    @Override
    public SendMessage process(Message message) {
        SendMessage sendMessageRequest;

        if (message.isReply() && checkPriceRange(message)) {
            final String msg = message.getText();
            switch (msg) {
                case Settings:
                    Manager.getInstance().setPrice(message.getFrom().getId(), message.getChatId(), message.getText());
                    sendMessageRequest = messageCreator(getSettingsKeyboard(),
                            getSettingsMessage() + "\n" + "Range selected: " + message.getText(), message, SETTINGS, false);
                    break;
                default:
                    sendMessageRequest = messageCreator(getPriceKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else {
            sendMessageRequest = sendChooseOptionMessage(message, getPriceKeyboard());
        }

        return sendMessageRequest;
    }

}
