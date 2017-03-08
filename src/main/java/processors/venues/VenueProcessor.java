package processors.venues;

import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.objects.Update;
import processors.Processor;

import static services.Keyboards.getMainMenuKeyboard;

/**
 * Created by Greg on 10/25/16.
 */
public class VenueProcessor implements Processor<SendVenue> {

    @Override
    public SendVenue compose(Update update, int state) {
        return process(update,state);
    }

    @Override
    public SendVenue process(Update update, int state) {
       return new Venue().process(update,getMainMenuKeyboard() );
    }
}
