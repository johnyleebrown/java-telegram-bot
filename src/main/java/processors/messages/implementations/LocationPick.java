package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendMessageDefault;
import static processors.updates.implementations.Movies.composeMovies;
import static services.Commands.CurrentLocation;
import static services.Commands.SavedLocation;
import static services.Commands.TypedLocation;
import static services.Keyboards.getLocationKeyboard;
import static services.Messages.getDistRangeMessage;
import static services.Messages.getOptionMessage;
import static services.Messages.getSendLocation;
import static services.States.CURRENTLOCATION;
import static services.States.ENTERLOCATION;
import static services.States.SHOWMOVIES;

/**
 * Created by Greg
 */
public class LocationPick extends AbstractMessage {
    @Override
    public SendMessage process(Message message) {

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case SavedLocation:
                    final int state = Manager.getInstance().getMovieState(message.getFrom().getId());
                    sendMessageRequest = messageCreator(null,composeMovies(message.getFrom().getId(),state),
                            message,SHOWMOVIES,false);
                    break;
                case CurrentLocation:
                    sendMessageRequest = messageCreator(getLocationKeyboard(),getSendLocation(),
                            message,CURRENTLOCATION,false);
                    Manager.getInstance().setFlag(message.getFrom().getId(),1);
                    break;
                case TypedLocation:
                    sendMessageRequest = messageCreator(null,getDistRangeMessage(),
                            message,ENTERLOCATION,true);
                    break;
                default:
                    sendMessageRequest = messageCreator(getLocationKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }

        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }
}
