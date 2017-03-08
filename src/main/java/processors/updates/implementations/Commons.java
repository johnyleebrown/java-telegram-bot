package processors.updates.implementations;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import processors.updates.AbstractCallBack;

/**
 * Created by Greg
 */
public class Commons extends AbstractCallBack{

    public AnswerCallbackQuery process(Update update){
        return messageCreator(update);
    }

}
