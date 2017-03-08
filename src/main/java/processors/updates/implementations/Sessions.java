package processors.updates.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static services.Distance.distFrom;
import static services.Keyboards.getSessionsInlineKeyboard;
import static services.Messages.getSessionMessage;
import static services.Messages.noSessionsMessage;
import static services.States.SHOWMOVIES;

/**
 * Created by Greg on 10/25/16.
 */
public class Sessions {

    private static int sizeOfHash;
    private static HashMap<Integer,String[]> hashSessions = new HashMap<>();
    private static final ResourceBundle commands = ResourceBundle.getBundle("commands", Locale.getDefault());

    private static String command(String str){
        return commands.getString(str);
    }

    public static EditMessageText messageOnShowSessionsInline(Update update) {
        System.out.println(" messageOnShowSessionsInline");
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();
        final int state = Manager.getInstance().getMovieState(callbackQuery.getFrom().getId());
        final int sessions_state = Manager.getInstance().getSessionsState(callbackQuery.getFrom().getId());

        //final int count = Manager.getSessions(movieName, userId).size();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);

        if (callbackQuery.getData().equals(command("LaterSession"))) {

            editMessageText.setText(composeSession(update, sessions_state + 1));
            Manager.getInstance().setSessionsState(userId, sessions_state + 1);

        } else if (callbackQuery.getData().equals(command("SoonerSession"))) {

            editMessageText.setText(composeSession(update, sessions_state - 1));
            Manager.getInstance().setSessionsState(userId, sessions_state - 1);

        } else if (callbackQuery.getData().equals(command("AnotherMovie"))) {

            Manager.getInstance().setUserState(userId, (long)userId, SHOWMOVIES);
            return Movies.messageOnShowMoviesInline(update);

        } else if (callbackQuery.getData().startsWith(command("SelectMovie"))) {
            System.out.println("messageOnShowSessionsInline - getSelectMovieCommand");
            String composeSessionAnswer = composeSession(update, 1);
            if(sizeOfHash == 0){
                return NoSessions.messageOnNoSessionsInline(update,noSessionsMessage(composeSessionAnswer));
            } else {
                editMessageText.setText(composeSessionAnswer);
                Manager.getInstance().setSessionsState(userId, 1);
            }

        }

        editMessageText.setReplyMarkup(getSessionsInlineKeyboard(userId, sizeOfHash));
        editMessageText.setChatId(String.valueOf(userId));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());

        return editMessageText;

    }


    public static String composeSession(Update update,int sessions_state){

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        final String[] venueArray = Manager.getInstance().getVenueInfo(userId);

        final int state = Manager.getInstance().getMovieState(userId);
        HashMap<Integer,String[]> hashMovies = Manager.getInstance().getMovies(userId);
        String movieName= "";
        for ( HashMap.Entry<Integer, String[]> entry : hashMovies.entrySet()) {
            Integer key = entry.getKey();
            String[] arr = entry.getValue();
            if (key == state){
                movieName = arr[1];
            }
        }

        if(callbackQuery.getData().startsWith(command("SelectMovie"))) {
            hashSessions = Manager.getInstance().getSessions(movieName, userId);
        }
        sizeOfHash = hashSessions.size();
        String result = "";

        if(sizeOfHash == 0){
            return movieName;
        } else {
            for (HashMap.Entry<Integer, String[]> entry : hashSessions.entrySet()) {
                Integer key = entry.getKey();
                String[] arr = entry.getValue();
                if (key.equals(sessions_state)) {
                    venueArray[0] = arr[3];
                    venueArray[1] = arr[4];
                    String timeProper = arr[1].substring(0, arr[1].length() - 3);
                    double distance = distFrom(userId,arr[4]);
                    double time = (distance/1000) * 12;
                    result = getSessionMessage(movieName, arr[2], timeProper, arr[0], distance, time, arr[3]);
                }
            }
        }

        return result;
    }

}
