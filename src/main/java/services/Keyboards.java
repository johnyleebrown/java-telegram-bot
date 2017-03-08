package services;

import database.Manager;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

/**
 * Created by Greg on 10/25/16.
 */
public class Keyboards {

    private static final ResourceBundle commands = ResourceBundle.getBundle("commands", Locale.getDefault());

    private static String command(String str){
        return commands.getString(str);
    }

    public static InlineKeyboardMarkup getMoviesInlineKeyboard(Integer userId){

        final int state = Manager.getInstance().getMovieState(userId);

        HashMap<Integer,String[]> hashMovies = Manager.getInstance().getMovies(userId);
        String link= "";
        String movieName= "";
        for ( HashMap.Entry<Integer, String[]> entry : hashMovies.entrySet()) {
            Integer key = entry.getKey();
            String[] arr = entry.getValue();
            if (key == state){
                link = "kinopoisk.ru/film/" + arr[0] + "/";
                movieName = arr[1];
            }
        }

        String[] movieNameSeparator = movieName.split(":");

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonList3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText("Kinopoisk");
        inlineKeyboardButton4.setUrl(link);
        keyboardButtonList3.add(inlineKeyboardButton4);
        keyboard.add(keyboardButtonList3);

        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        if(state!=1) {
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(command("BackMovie"));
            inlineKeyboardButton1.setCallbackData(command("BackMovie"));
            keyboardButtonList.add(inlineKeyboardButton1);
        }
        if(state!=Manager.getInstance().countEntries("Movie")) {
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText(command("NextMovie"));
            inlineKeyboardButton2.setCallbackData(command("NextMovie"));
            keyboardButtonList.add(inlineKeyboardButton2);
        }
        keyboard.add(keyboardButtonList);

        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(command("SelectMovie"));
        inlineKeyboardButton3.setCallbackData(command("SelectMovie") + "-" + movieNameSeparator[0]);
        keyboardButtonList2.add(inlineKeyboardButton3);
        keyboard.add(keyboardButtonList2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getSessionsInlineKeyboard(Integer userId,int count){

        final int session_state =  Manager.getInstance().getSessionsState(userId);

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        if(session_state!=1) {
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(command("SoonerSession"));
            inlineKeyboardButton1.setCallbackData(command("SoonerSession"));
            keyboardButtonList.add(inlineKeyboardButton1);
        }
        if(session_state!=count) {
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText(command("LaterSession"));
            inlineKeyboardButton2.setCallbackData(command("LaterSession"));
            keyboardButtonList.add(inlineKeyboardButton2);
        }
        keyboard.add(keyboardButtonList);

        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(command("AnotherMovie"));
        inlineKeyboardButton3.setCallbackData(command("AnotherMovie"));
        keyboardButtonList2.add(inlineKeyboardButton3);
        keyboard.add(keyboardButtonList2);

        List<InlineKeyboardButton> keyboardButtonList3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText(command("Map"));
        inlineKeyboardButton4.setCallbackData(command("Map"));
        keyboardButtonList3.add(inlineKeyboardButton4);
        keyboard.add(keyboardButtonList3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;


    }

    public static InlineKeyboardMarkup getNoSessionsInlineKeyboard(Integer userId){

        final int session_state =  Manager.getInstance().getSessionsState(userId);

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(command("AnotherMovie"));
        inlineKeyboardButton3.setCallbackData(command("AnotherMovie"));
        keyboardButtonList2.add(inlineKeyboardButton3);
        keyboard.add(keyboardButtonList2);

        List<InlineKeyboardButton> keyboardButtonList3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText(command("Settings"));
        inlineKeyboardButton4.setCallbackData(command("Settings"));
        keyboardButtonList3.add(inlineKeyboardButton4);
        keyboard.add(keyboardButtonList3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;


    }

    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("ShowSessions"));
        keyboardFirstRow.add(command("Settings"));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getLocationKeyboard(Message message) {

        String a = Manager.getInstance().getTypedLocation(message.getFrom().getId());
        boolean b = (a.startsWith("null"))?(false):(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        //current location button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("CurrentLocation"));
        keyboard.add(keyboardFirstRow);

        //enter location button
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command("TypedLocation"));
        keyboard.add(keyboardSecondRow);

        if(b) {
            //stored location button
            KeyboardRow keyboardThirdRow = new KeyboardRow();
            keyboardThirdRow.add(command("StoredLocation"));
            keyboard.add(keyboardThirdRow);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getMovieChosenKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("ShowMovies"));
        keyboardFirstRow.add(command("Settings"));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getSettingsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("Price"));
        keyboardFirstRow.add(command("Dist"));
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command("StoredLocation"));
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(command("ShowSessions"));
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getPriceKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        //price  button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("LowestPrice"));
        keyboard.add(keyboardFirstRow);

        //distance button
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command("Range"));
        keyboard.add(keyboardSecondRow);


        //stored location button
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(command("GoBackCommand"));
        keyboard.add(keyboardThirdRow);


        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getDistanceKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        //price  button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("Closest"));
        keyboard.add(keyboardFirstRow);

        //distance button
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command("Range"));
        keyboard.add(keyboardSecondRow);


        //stored location button
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(command("GoBackCommand"));
        keyboard.add(keyboardThirdRow);


        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getStoredLocationKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("NewLocation"));
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command("GoBackCommand"));
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getStoredNewLocationKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("EditLocation"));
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(command("GoBackCommand"));
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getLocationKeyboard() {


        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        //keyboardFirstRow.add(getSendLocationCommand());

        KeyboardButton button = new KeyboardButton();
        button.setRequestLocation(true);
        button.setText(command("SendLocation"));
        keyboardFirstRow.add(button);
        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getBlockedUserKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(command("BlockedUser"));
        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public static ForceReplyKeyboard getForceReply() {
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        return forceReplyKeyboard;
    }

}
