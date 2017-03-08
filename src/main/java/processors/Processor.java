package processors;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by Greg on 10/25/16.
 */
public interface Processor<T extends BotApiMethod<Message>> {

    T compose(Update update, int state);

    T process(Update update, int state);

}
