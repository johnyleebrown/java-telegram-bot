package processors.updates;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Greg on 10/26/16.
 */
public abstract class AbstractCallBack {

    private static final ResourceBundle commands = ResourceBundle.getBundle("commands", Locale.getDefault());

    public abstract AnswerCallbackQuery process(Update update);

    protected AnswerCallbackQuery messageCreator(Update update){

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
        answerCallbackQuery.setText(commands.getString("CallBack"));

        return answerCallbackQuery;
    }
}
