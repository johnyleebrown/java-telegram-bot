package processors.venues;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static services.States.MAINMENU;
import static services.ToCoordinates.getCoord;

/**
 * Created by Greg
 */

public abstract class AbstractVenue {

    protected SendVenue venueCreator(Update update,ReplyKeyboardMarkup replyKeyboardMarkup){
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        final String[] venueArray = Manager.getInstance().getVenueInfo(userId);
        final double[] cinema = getCoord(venueArray[1]);

        SendVenue sendVenue = new SendVenue();
        sendVenue.setLatitude((float) cinema[0]);
        sendVenue.setLongitude((float) cinema[1]);
        sendVenue.setTitle(venueArray[0]);
        sendVenue.setAddress(venueArray[1]);
        sendVenue.setChatId(String.valueOf(userId));
        sendVenue.setReplyMarkup(replyKeyboardMarkup);

        Manager.getInstance().setUserState(userId, (long)userId, MAINMENU);

        return sendVenue;
    }

    public abstract SendVenue process(Update update,ReplyKeyboardMarkup replyKeyboardMarkup);
}
