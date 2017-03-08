package processors.messages;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Locale;
import java.util.ResourceBundle;

import static services.Keyboards.*;

/**
 * Created by Greg
 */
public abstract class AbstractMessage {

    private final ResourceBundle commands = ResourceBundle.getBundle("commands", Locale.getDefault());

    protected String command(String str){
        return commands.getString(str);
    }

    private static boolean optional(Message message){
        String typedLocation = Manager.getInstance().getTypedLocation(message.getFrom().getId());
        return (typedLocation.startsWith("null"))?(false):(true);
    }

    protected SendMessage messageCreator(ReplyKeyboardMarkup replyKeyboardMarkup,
                                                String text, Message message, int state, boolean keyboardFlag){
        SendMessage sendMessage = new SendMessage();

        if (replyKeyboardMarkup == null && keyboardFlag){
            sendMessage.setReplyMarkup(getForceReply());
        }
        if (replyKeyboardMarkup == null && !keyboardFlag){
            sendMessage.setReplyMarkup(getMoviesInlineKeyboard(message.getFrom().getId()));
        }
        if(keyboardFlag && replyKeyboardMarkup != null){
            replyKeyboardMarkup = (optional(message)) ? (getStoredNewLocationKeyboard()) : (getStoredLocationKeyboard());
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getFrom().getId());
        sendMessage.setText(text);

        Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), state);

        return new SendMessage();
    }

    public abstract SendMessage process(Message message);

}
