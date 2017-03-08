package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static services.Commands.*;
import static services.Keyboards.*;
import static services.Messages.*;
import static services.States.*;

/**
 * Created by Greg
 */
public class Settings extends AbstractMessage {

    @Override
    public SendMessage process(Message message) {
        SendMessage sendMessageRequest;

        if (message.hasText()) {
            String msg = message.getText();
            switch (msg){
                case Price:
                    sendMessageRequest = messageCreator(getPriceKeyboard(),getPriceMessage(),message,PRICE,false);
                    break;
                case Dist:
                    sendMessageRequest = messageCreator(getDistanceKeyboard(),getDistanceMessage(),message,DISTANCE,false);
                    break;
                case StoredLocation:
                    sendMessageRequest = messageCreator(getPriceKeyboard(),
                            getStoredLocationMessage() + "( " + Manager.getInstance().getTypedLocation(message.getFrom().getId()) + " )",
                            message,EDITLOCATION,true);
                    break;
                case ShowSessions:
                    sendMessageRequest = messageCreator(getLocationKeyboard(message),getPickALocationMessage(),
                            message,PICKALOCATION,false);
                    break;
                default:
                    sendMessageRequest = messageCreator(getSettingsKeyboard(),getOptionMessage(),
                            message,Manager.getInstance().getUserState(message.getFrom().getId()),false);
                    break;

            }
        } else {
            sendMessageRequest = messageCreator(getMainMenuKeyboard(),getHelpMessage(),message,MAINMENU,false);
        }

        return sendMessageRequest;
    }

}
