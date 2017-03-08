package processors.updates.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import services.Emoji;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static services.Keyboards.getMoviesInlineKeyboard;
import static services.States.SHOWSESSIONS;

/**
 * Created by Greg on 10/25/16.
 */
public class Movies {

    private static final ResourceBundle commands = ResourceBundle.getBundle("commands", Locale.getDefault());

    private static String command(String str){
        return commands.getString(str);
    }

    public static EditMessageText messageOnShowMoviesInline(Update update) {

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();
        final int state = Manager.getInstance().getMovieState(callbackQuery.getFrom().getId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);

        if (callbackQuery.getData().equals(command("NextMovie"))){

            editMessageText.setText(composeMovies(userId,state+1));
            Manager.getInstance().setMovieState(userId,state+1);

        } else if(callbackQuery.getData().equals(command("BackMovie"))) {

            editMessageText.setText(composeMovies(userId,state-1));
            Manager.getInstance().setMovieState(userId,state-1);

        } else if(callbackQuery.getData().startsWith(command("SelectMovie"))) {

            Manager.getInstance().setUserState(userId,(long)userId, SHOWSESSIONS);
            Manager.getInstance().setSessionsState(userId,1);
            return Sessions.messageOnShowSessionsInline(update);

        } else if(callbackQuery.getData().equals(command("AnotherMovie"))){
            editMessageText.setText(composeMovies(userId,state));
            Manager.getInstance().setMovieState(userId,state);

        }

        editMessageText.setReplyMarkup(getMoviesInlineKeyboard(userId));
        editMessageText.setChatId(String.valueOf(userId));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());

        return editMessageText;
    }

    public static String composeMovies(Integer userId, int state){
        System.out.println("composemovies");

        HashMap<Integer,String[]> hashMovies = Manager.getInstance().getMovies(userId);

        String jpg = "https://st.kp.yandex.net/images/film_iphone/iphone360_";
        String movieName = "";
        String text = "";
        String genre = "";

        for (HashMap.Entry<Integer, String[]> entry : hashMovies.entrySet()) {
            Integer key = entry.getKey();
            String[] arr = entry.getValue();
            if (key.equals(state)) {
                movieName = "<b>" + arr[1] + "</b>";
                text = arr[2];
                jpg += arr[0] + ".jpg";
                genre = "Жанр: " + arr[4];
            }
        }


        String[] textArray = new String[10];
        textArray = text.split("\\.");
        String newText = textArray[0]+" ...";
        String html = "<a href=\"" + jpg + "\">\u200c</a>";
        String str = html + Emoji.CAMERAEM.toString() + " " + movieName + "\n" + genre + "\n" + newText;

        return  str;
    }

}
