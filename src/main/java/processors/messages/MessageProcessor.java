package processors.messages;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import processors.Processor;
import processors.messages.implementations.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static processors.messages.implementations.Common.*;
import static services.States.*;

public class MessageProcessor implements Processor<SendMessage> {

    public SendMessage compose(Update update, int state) {

        Message message = update.getMessage();

        if (message.isUserMessage() && message.hasText()) {
            if (isCommandForOther(message.getText())) {
                return sendMessageDefault(message);
            } else if (message.getText().startsWith("/stop") ||
                    message.getText().startsWith("/stop@MovieBuddyBot")){
                return sendHideKeyboard(message);
            } else {
                return process(update, state);
            }
        } else {
            return sendMessageDefault(message);
        }
    }

    public SendMessage process(Update update, int state) {
        Message message = update.getMessage();
        setDate(message);
        SendMessage sendMessageRequest;

        switch(state) {
            case BLOCKED:
                sendMessageRequest = handleBlockedUser(message);
                break;
            case START:
                sendMessageRequest = new Main().process(message);
                break;
            case PICKALOCATION:
                sendMessageRequest = new LocationPick().process(message);
                break;
            case CURRENTLOCATION:
                sendMessageRequest = new LocationCurrent().process(message);
                break;
            case ENTERLOCATION:
                sendMessageRequest = new LocationNew().process(message);
                break;
            case SETTINGS:
                sendMessageRequest = new Settings().process(message);
                break;
            case PRICE:
                sendMessageRequest = new Price().process(message);
                break;
            case PRICERANGE:
                sendMessageRequest = new PriceRange().process(message);
                break;
            case DISTANCE:
                sendMessageRequest = new Main().process(message);
                break;
            case DISTANCERANGE:
                sendMessageRequest = new DistanceRange().process(message);
                break;
            case EDITLOCATION:
                sendMessageRequest = new LocationEdit().process(message);
                break;
            case EDITADDLOCATION:
                sendMessageRequest = new LocationNew().process(message);
                break;
            default:
                sendMessageRequest = sendMessageDefault(message);
                break;
        }

        return sendMessageRequest;
    }

    private boolean isCommandForOther(String text) {
        boolean isSimpleCommand = text.equals("/start") || text.equals("/help");
        boolean isCommandForMe = text.equals("/start@MovieBuddyBot") || text.equals("/help@MovieBuddyBot");
        return text.startsWith("/") && !isSimpleCommand && !isCommandForMe;
    }

    private void setDate(Message message){
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Manager.getInstance().setLastSeen(message.getFrom().getId(),message.getChatId(),day);
    }
}
