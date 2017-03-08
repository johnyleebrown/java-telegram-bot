package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import processors.messages.AbstractMessage;

import static processors.messages.implementations.Common.sendMessageDefault;
import static processors.updates.implementations.Movies.composeMovies;
import static services.Commands.TypedLocation;
import static services.Inspections.checkTypedLocation;
import static services.Keyboards.getLocationKeyboard;
import static services.Messages.getDistRangeMessage;
import static services.Messages.getOptionMessage;
import static services.States.ENTERLOCATION;
import static services.States.SHOWMOVIES;

/**
 * Created by Greg
 */
public class LocationTyped extends AbstractMessage {
    @Override
    public SendMessage process(Message message) {

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            final String msg = message.getText();
            switch (msg) {
                case TypedLocation:
                    sendMessageRequest = messageCreator(null,getDistRangeMessage(),
                            message,ENTERLOCATION,true);
                    break;
                default:
                    sendMessageRequest = messageCreator(getLocationKeyboard(), getOptionMessage(),
                            message, Manager.getInstance().getUserState(message.getFrom().getId()), false);
                    break;
            }
        } else if(message.isReply() && checkTypedLocation(message)){
            final int state = Manager.getInstance().getMovieState(message.getFrom().getId());
            Manager.getInstance().setTypedLocation(message.getFrom().getId(), message.getChatId(), message.getText());
            sendMessageRequest = messageCreator(null,
                    composeMovies(message.getFrom().getId(),state),
                    message,SHOWMOVIES,false);
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }
}
