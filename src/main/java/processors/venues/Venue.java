package processors.venues;

import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * Created by Greg on 10/26/16.
 */
public class Venue extends AbstractVenue {

    @Override
    public SendVenue process(Update update, ReplyKeyboardMarkup replyKeyboardMarkup) {
        return venueCreator(update,replyKeyboardMarkup);
    }
}
