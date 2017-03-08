package processors.updates.implementations;

import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import processors.updates.AbstractUpdate;

/**
 * Created by Greg on 10/25/16.
 */
public class NoSessions extends AbstractUpdate{

    @Override
    public EditMessageText process(Update update, String text) {
        return messageOnNoSessionsInline(update,text);
    }
}
