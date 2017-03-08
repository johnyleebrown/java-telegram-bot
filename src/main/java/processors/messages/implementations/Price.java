package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendMessageDefault;
import static services.Commands.*;
import static services.Keyboards.getPriceKeyboard;
import static services.Keyboards.getSettingsKeyboard;
import static services.Messages.*;
import static services.States.PRICERANGE;
import static services.States.SETTINGS;

/**
 * Created by Greg
 */
public class Price extends AbstractMessage {

    @Override
    public SendMessage process(Message message){

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case LowestPrice:
                    Manager.getInstance().setPrice(message.getFrom().getId(),message.getChatId(),"cheapest");
                    sendMessageRequest = messageCreator(getSettingsKeyboard(),getSettingsSetPrice(),
                            message,SETTINGS,false);
                    break;
                case Range:
                    sendMessageRequest = messageCreator(null,getRangeMessage(), message,PRICERANGE,false);
                    break;
                case Settings:
                    sendMessageRequest = messageCreator(getSettingsKeyboard(), getSettingsMessage(), message, SETTINGS, false);
                    break;
                default:
                    sendMessageRequest = messageCreator(getPriceKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;

    }


}
