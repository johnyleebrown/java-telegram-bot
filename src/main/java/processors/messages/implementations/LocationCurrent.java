package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendMessageDefault;
import static processors.updates.implementations.Movies.composeMovies;
import static services.Commands.CurrentLocation;
import static services.Keyboards.getLocationKeyboard;
import static services.Messages.getOptionMessage;
import static services.Messages.getSendLocation;
import static services.States.CURRENTLOCATION;
import static services.States.SHOWMOVIES;

/**
 * Created by Greg
 */
public class LocationCurrent extends AbstractMessage {
    @Override
    public SendMessage process(Message message) {
        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case CurrentLocation:
                    sendMessageRequest = messageCreator(getLocationKeyboard(),getSendLocation(),
                            message,CURRENTLOCATION,false);
                    Manager.getInstance().setFlag(message.getFrom().getId(),1);
                    break;
                default:
                    sendMessageRequest = messageCreator(getLocationKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else if(message.hasLocation()){
            final int state = Manager.getInstance().getMovieState(message.getFrom().getId());
            Manager.getInstance().setLocation(message.getFrom().getId(), message.getChatId(),
                    message.getLocation().getLatitude(), message.getLocation().getLongitude());
            sendMessageRequest = messageCreator(null,
                    composeMovies(message.getFrom().getId(),state),
                    message,SHOWMOVIES,true);
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }
}
