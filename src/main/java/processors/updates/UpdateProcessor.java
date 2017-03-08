package processors.updates;

import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import processors.Processor;

import static processors.updates.implementations.Movies.messageOnShowMoviesInline;
import static processors.updates.implementations.Sessions.messageOnShowSessionsInline;
import static services.States.*;

/**
 * Created by Greg on 10/25/16.
 */
public class UpdateProcessor implements Processor<EditMessageText> {

    @Override
    public EditMessageText compose(Update update, int state) {
        return null;
    }

    @Override
    public EditMessageText process(Update update, int state) {

        EditMessageText editMessageText = new EditMessageText();

        switch(state) {
            case SHOWMOVIES:
                editMessageText = messageOnShowMoviesInline(update);
                break;
            case SHOWSESSIONS:
                editMessageText = messageOnShowSessionsInline(update);
                break;
            default:
//                answerCallbackQuery(alertCallBackQuery(update));
                break;
        }

        return editMessageText;
    }

}
