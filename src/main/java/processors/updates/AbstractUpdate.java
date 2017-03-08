package processors.updates;

import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

import static services.Keyboards.getNoSessionsInlineKeyboard;

/**
 * Created by Greg on 10/26/16.
 */
public abstract class AbstractUpdate {

    public abstract EditMessageText process(Update update,String text);

    public static EditMessageText messageOnNoSessionsInline(Update update, String text) {

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);
        editMessageText.setReplyMarkup(getNoSessionsInlineKeyboard(userId));
        editMessageText.setChatId(String.valueOf(userId));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(text);

        return editMessageText;
    }

}
