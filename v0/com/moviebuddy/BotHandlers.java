package moviebuddy;

import com.moviebuddy.database.Manager;
import com.moviebuddy.services.Emoji;
import com.moviebuddy.services.ToCoordinates;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendVenue;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.*;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.moviebuddy.services.ToCoordinates.getCoord;

/**
 * @author Greg
 * @version 1.0
 * @brief Handler for updates
 * @date 07/14/16
 */

public class BotHandlers extends TelegramLongPollingBot {

    //???where to
    private static final String LOGTAG = "BOTHANDLERS";

    private static final int STARTSTATE = 0;
    private static final int MAINMENU = 1; // SHOWSESSIONS, SETTINGS
    private static final int PICKALOCATION = 2;//CURRENTLOCATION, ENTERLOCATION
    private static final int CURRENTLOCATION = 3;//ASKS TO SEND YOUR LOCATION
    private static final int ENTERLOCATION = 4; //ENTER LOCATION IN FORMAT - BUILDING NUMBER, STREET, CITY

    private static final int SHOWMOVIES = 5;// PICK A MOVIE
    private static final int SHOWSESSIONS = 6;//SHOWS DISTANCE, PRICE, A POINT ON A MAP(OR WITH A PATH TO A LOCATION)
    private static final int SHOWNOSESSIONS = 14;
    private static final int SETTINGS = 7;// LOCATION, PRICE, DISTANCE

    private static final int PRICE = 8; //DROPDOWN - LOWEST,HIGHEST,DOESNT MATTER
    private static final int PRICERANGE = 9;

    private static final int DISTANCE = 10;//DROPDOWN - CLOSEST, DOESNT MATTER
    private static final int DISTANCERANGE = 11;//DROPDOWN - CLOSEST, DOESNT MATTER

    private static final int EDITLOCATION = 12;
    private static final int EDITADDLOCATION = 13;

    private static boolean flagWhichLocationChosen = false;
    private static int sizeOfHash;
    private static HashMap<Integer,String[]> hashSessions = new HashMap<>();
    private static String[] venueInfoArray = new String[2];

    @Override
    public String getBotUsername() {
        return BotConfig.USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                //CallbackQuery callbackQuery = update.getCallbackQuery();
                //Integer userId = callbackQuery.getFrom().getId();
                final int state = Manager.getInstance().getUserState(message.getFrom().getId());
                if (message.hasText() || message.hasLocation()) {
                    switch(state) {
                        case SHOWMOVIES:
                            sendMessage(sendHelpMessage(message.getChatId().toString(),message.getFrom().getId(),getSettingsKeyboard()));
                            break;
                        case SHOWSESSIONS:
                            sendMessage(sendHelpMessage(message.getChatId().toString(),message.getFrom().getId(),getSettingsKeyboard()));
                            break;
//                        case SHOWNOSESSIONS:
//                            sendMessage(sendHelpMessage(message.getChatId().toString(),message.getFrom().getId(),getSettingsKeyboard()));
//                            break;
                        default:
                            handleIncomingMessage(message,update);
                            break;
                    }
                }
            } else if (update.hasCallbackQuery()){
                CallbackQuery callbackQuery = update.getCallbackQuery();
                Integer userId = callbackQuery.getFrom().getId();

                if (callbackQuery.getData().equals(getMapCommand())){
                    sendVenue(sendVenue(update));
                }
                else if (callbackQuery.getData().equals(getSettingsCommand())) {
                    sendMessage(onSettingsChosenAfterUpdate(update));
                }
                else {
                    handleInlineQueries(update);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            BotLogger.error(LOGTAG, e);
        }
    }


    private void handleInlineQueries(Update update) throws TelegramApiException{

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        final int state = Manager.getInstance().getUserState(userId);

        switch(state) {
            case SHOWMOVIES:
                try {
                    editMessageText(messageOnShowMoviesInline(update));
                } catch (org.telegram.telegrambots.exceptions.TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case SHOWSESSIONS:
                try {
                    editMessageText(messageOnShowSessionsInline(update));
                } catch (org.telegram.telegrambots.exceptions.TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
//            case SHOWNOSESSIONS:
//                editMessageText(messageOnNoSessionsInline(update));
//                break;
            default:
                try {
                    answerCallbackQuery(alertCallBackQuery(update));
                } catch (org.telegram.telegrambots.exceptions.TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static SendVenue sendVenue(Update update){

        System.out.println("sendVenue");

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();
        final int state = Manager.getInstance().getMovieState(userId);

        double[] cinema = getCoord(venueInfoArray[1]);

        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        SendVenue sendVenue = new SendVenue();
        sendVenue.setLatitude((float) cinema[0]);
        sendVenue.setLongitude((float) cinema[1]);
        sendVenue.setTitle(venueInfoArray[0]);
        sendVenue.setAddress(venueInfoArray[1]);
        sendVenue.setChatId(String.valueOf(userId));
        sendVenue.setReplyMarkup(replyKeyboardMarkup);
        com.moviebuddy.database.Manager.getInstance().setUserState(userId, (long)userId, MAINMENU);

        return sendVenue;
    }

    private static EditMessageText messageOnShowMoviesInline(Update update) {

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();
        final int state = Manager.getInstance().getMovieState(callbackQuery.getFrom().getId());

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);

        if (callbackQuery.getData().equals(getNextMovieCommand())){

            editMessageText.setText(composeMovies(state+1));
            Manager.getInstance().setMovieState(userId,state+1);

        } else if(callbackQuery.getData().equals(getBackMovieCommand())) {

            editMessageText.setText(composeMovies(state-1));
            Manager.getInstance().setMovieState(userId,state-1);

        } else if(callbackQuery.getData().startsWith(getSelectMovieCommand())) {

            com.moviebuddy.database.Manager.getInstance().setUserState(userId,(long)userId, SHOWSESSIONS);
            com.moviebuddy.database.Manager.getInstance().setSessionsState(userId,1);
            return messageOnShowSessionsInline(update);

        } else if(callbackQuery.getData().equals(getAnotherMovieCommand())){
            editMessageText.setText(composeMovies(state));
            Manager.getInstance().setMovieState(userId,state);

        }

        editMessageText.setReplyMarkup(getMoviesInlineKeyboard(userId));
        editMessageText.setChatId(String.valueOf(userId));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());


        return editMessageText;
    }


    private static String composeMovies(int state){
        System.out.println("composemovies ");

        HashMap<Integer,String[]> hashMovies = Manager.getInstance().getMovies();

        String jpg = "https://st.kp.yandex.net/images/film_iphone/iphone360_";
        String movieName = "";
        String text = "";
        String genre = "";

        for ( HashMap.Entry<Integer, String[]> entry : hashMovies.entrySet()) {
            Integer key = entry.getKey(); String[] arr = entry.getValue();
            if (key.equals(state)){
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


    private static EditMessageText messageOnShowSessionsInline(Update update) {
        System.out.println(" messageOnShowSessionsInline");
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();
        final int state = Manager.getInstance().getMovieState(callbackQuery.getFrom().getId());
        final int sessions_state = Manager.getInstance().getSessionsState(callbackQuery.getFrom().getId());

        //final int count = Manager.getSessions(movieName, userId).size();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);

        if (callbackQuery.getData().equals(getLaterSessionCommand())) {

            editMessageText.setText(composeSession(update, sessions_state + 1));
            Manager.getInstance().setSessionsState(userId, sessions_state + 1);

        } else if (callbackQuery.getData().equals(getSoonerSessionCommand())) {

            editMessageText.setText(composeSession(update, sessions_state - 1));
            Manager.getInstance().setSessionsState(userId, sessions_state - 1);

        } else if (callbackQuery.getData().equals(getAnotherMovieCommand())) {

            com.moviebuddy.database.Manager.getInstance().setUserState(userId, (long)userId, SHOWMOVIES);
            return messageOnShowMoviesInline(update);

        } else if (callbackQuery.getData().startsWith(getSelectMovieCommand())) {
            System.out.println("messageOnShowSessionsInline - getSelectMovieCommand");
            String composeSessionAnswer = composeSession(update, 1);
            if(sizeOfHash == 0){
                return messageOnNoSessionsInline(update,noSessionsMessage(composeSessionAnswer));
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

    private static String composeSession(Update update,int sessions_state){

        System.out.println("compose session");
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        final int state = Manager.getInstance().getMovieState(userId);
        HashMap<Integer,String[]> hashMovies = Manager.getInstance().getMovies();
        String movieName= "";
        for ( HashMap.Entry<Integer, String[]> entry : hashMovies.entrySet()) {
            Integer key = entry.getKey();
            String[] arr = entry.getValue();
            if (key == state){
                movieName = arr[1];
            }
        }

        if(callbackQuery.getData().startsWith(getSelectMovieCommand())) {
            hashSessions = Manager.getInstance().getSessions(movieName, userId);
        }
        sizeOfHash = hashSessions.size();
        System.out.println("got sessions");
        System.out.println("sessions_state "+sessions_state);
        System.out.println("sizeOfHash "+sizeOfHash);
        String result = "";

        if(sizeOfHash == 0){
            return movieName;
        } else {
            for (HashMap.Entry<Integer, String[]> entry : hashSessions.entrySet()) {
                Integer key = entry.getKey();
                String[] arr = entry.getValue();
                if (key.equals(sessions_state)) {
                    venueInfoArray[0] = arr[3];
                    venueInfoArray[1] = arr[4];
                    String timeProper = arr[1].substring(0, arr[1].length() - 3);
                    double distance = distFrom(userId,arr[4]);
                    result = getSessionMessage(movieName, arr[2], timeProper, arr[0], distance, timeTo(distance), arr[3]);
                }
            }
        }

        return result;
    }

    private static EditMessageText messageOnNoSessionsInline(Update update,String text) {

        System.out.println("no sessions");

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();
        final int state = Manager.getInstance().getMovieState(callbackQuery.getFrom().getId());
        final int sessions_state = Manager.getInstance().getSessionsState(callbackQuery.getFrom().getId());

        //final int count = Manager.getSessions(movieName, userId).size();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.enableHtml(true);
        editMessageText.setReplyMarkup(getNoSessionsInlineKeyboard(userId));
        editMessageText.setChatId(String.valueOf(userId));
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setText(text);

        return editMessageText;
    }


    private static AnswerCallbackQuery alertCallBackQuery(Update update){

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();

        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setText("Use buttons to navigate");

        return answerCallbackQuery;
    }

    private static InlineKeyboardMarkup getMoviesInlineKeyboard(Integer userId){

        final int state = Manager.getInstance().getMovieState(userId);

        HashMap<Integer,String[]> hashMovies = Manager.getInstance().getMovies();
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
            inlineKeyboardButton1.setText(getBackMovieCommand());
            inlineKeyboardButton1.setCallbackData(getBackMovieCommand());
            keyboardButtonList.add(inlineKeyboardButton1);
        }
        if(state!=Manager.getInstance().countEntries("Movie")) {
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText(getNextMovieCommand());
            inlineKeyboardButton2.setCallbackData(getNextMovieCommand());
            keyboardButtonList.add(inlineKeyboardButton2);
        }
        keyboard.add(keyboardButtonList);

        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(getSelectMovieCommand());
        inlineKeyboardButton3.setCallbackData(getSelectMovieCommand() + "-" + movieNameSeparator[0]);
        keyboardButtonList2.add(inlineKeyboardButton3);
        keyboard.add(keyboardButtonList2);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;

    }

    private static InlineKeyboardMarkup getSessionsInlineKeyboard(Integer userId,int count){

        final int session_state =  Manager.getInstance().getSessionsState(userId);

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
        if(session_state!=1) {
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText(getSoonerSessionCommand());
            inlineKeyboardButton1.setCallbackData(getSoonerSessionCommand());
            keyboardButtonList.add(inlineKeyboardButton1);
        }
        if(session_state!=count) {
            InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
            inlineKeyboardButton2.setText(getLaterSessionCommand());
            inlineKeyboardButton2.setCallbackData(getLaterSessionCommand());
            keyboardButtonList.add(inlineKeyboardButton2);
        }
        keyboard.add(keyboardButtonList);

        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(getAnotherMovieCommand());
        inlineKeyboardButton3.setCallbackData(getAnotherMovieCommand());
        keyboardButtonList2.add(inlineKeyboardButton3);
        keyboard.add(keyboardButtonList2);

        List<InlineKeyboardButton> keyboardButtonList3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText(getMapCommand());
        inlineKeyboardButton4.setCallbackData(getMapCommand());
        keyboardButtonList3.add(inlineKeyboardButton4);
        keyboard.add(keyboardButtonList3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;


    }

    private static InlineKeyboardMarkup getNoSessionsInlineKeyboard(Integer userId){

        final int session_state =  Manager.getInstance().getSessionsState(userId);

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(getAnotherMovieCommand());
        inlineKeyboardButton3.setCallbackData(getAnotherMovieCommand());
        keyboardButtonList2.add(inlineKeyboardButton3);
        keyboard.add(keyboardButtonList2);

        List<InlineKeyboardButton> keyboardButtonList3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText(getSettingsCommand());
        inlineKeyboardButton4.setCallbackData(getSettingsCommand());
        keyboardButtonList3.add(inlineKeyboardButton4);
        keyboard.add(keyboardButtonList3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;


    }


    private static String getBackMovieCommand() {
        return String.format("%s Back",
                Emoji.LEFTTRIANGLE.toString());
    }

    private static String getNextMovieCommand() {
        return String.format("Next %s",
                Emoji.BLACK_RIGHT_POINTING_TRIANGLE.toString());
    }

    private static String getSelectMovieCommand() {
//        return String.format("%s Select"
//                Emoji.HIT.toString(),
//                Emoji.HIT.toString());
        return "Select";
    }

    private static String getLaterSessionCommand() {
        return String.format("Later %s",
                Emoji.BLACK_RIGHT_POINTING_TRIANGLE.toString());
    }

    private static String getSoonerSessionCommand() {
        return String.format("%s Sooner",
                Emoji.LEFTTRIANGLE.toString());
    }

    private static String getAnotherMovieCommand() {
        return String.format("%s Change movie",
                Emoji.CHANGE.toString());
    }

    private static String getMapCommand() {
        return String.format("%s Show on a map",
                Emoji.MAP.toString());
    }


    private static String getSessionMessage(String movieName, String format, String movieTime, String price,
                                            double dist, double travelTime, String cinema){
        String distance = "";
        if(dist<1000){
            NumberFormat formatter = new DecimalFormat("#0");
            distance = formatter.format(dist) + " meters";
        } else {
            NumberFormat formatter = new DecimalFormat("#0.0");
            distance = formatter.format(dist/1000) + " km";
        }
        String traveltim = "";
        if(travelTime>60){
            NumberFormat formatter = new DecimalFormat("#0.0");
            traveltim = formatter.format(travelTime/60) + " h";
        } else {
            traveltim = (int)travelTime + " min";
        }
        return String.format("%s %s %s\n%s %s\n%s %s\n%s %s / %s %s\n%s %s",
                Emoji.CAMERAEM.toString(),movieName,format,
                Emoji.ALARM_CLOCK.toString(),movieTime,
                Emoji.CASH.toString(),price,
                Emoji.FINISH.toString(),distance,
                Emoji.WALKING.toString(),traveltim,
                Emoji.CASTLE.toString(),cinema
        );
    }

    private static String noSessionsMessage(String movieName){
        return String.format("%s No sessions was found for %s %s\n%s Try another movie or change the parameters in %sSettings",
                Emoji.EXCLWARNING.toString(),movieName,Emoji.CRYING_FACE.toString(),
                Emoji.HAPPY.toString(),Emoji.SETT.toString()
        );
    }



    //--------------------------Incoming messages handlers-----------------------------

    private void handleIncomingMessage(Message message,Update update) throws TelegramApiException, org.telegram.telegrambots.exceptions.TelegramApiException {

        final int state = Manager.getInstance().getUserState(message.getFrom().getId());
        if (message.isUserMessage() && message.hasText()) {
            if (isCommandForOther(message.getText())) {
                sendMessage(sendMessageDefault(message));
                return;
            } else if (message.getText().startsWith("/stop")){
                //sendMessage(sendMessageDefault(message));
                sendHideKeyboard(message.getFrom().getId(), message.getChatId(), message.getMessageId());
                return;
            }
        }
        SendMessage sendMessageRequest;
        switch(state) {
            case MAINMENU:
                sendMessageRequest = messageOnMainMenu(message);
                break;
            case PICKALOCATION:
            case CURRENTLOCATION:
            case ENTERLOCATION:
                sendMessageRequest = messageOnPickALocation(message,state,update);
                break;
            case SHOWMOVIES:
                sendMessageRequest = messageOnShowMovies(message);
                break;
            case SHOWSESSIONS:
                sendMessageRequest = messageOnShowSessions(message);
                break;
            case SETTINGS:
                sendMessageRequest = messageOnSettings(message);
                break;
            case PRICE:
            case PRICERANGE:
                sendMessageRequest = messageOnPrice(message,state);
                break;
            case DISTANCE:
            case DISTANCERANGE:
                sendMessageRequest = messageOnDistance(message,state);
                break;
            case EDITLOCATION:
            case EDITADDLOCATION:
                sendMessageRequest = messageOnEitLocation(message,state);
                break;
            default:
                sendMessageRequest = sendMessageDefault(message);
                break;
        }
        sendMessage(sendMessageRequest);

    }

    private void sendHideKeyboard(Integer userId, Long chatId, Integer messageId) throws TelegramApiException {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.setText(Emoji.WAVING_HAND_SIGN.toString());

        ReplyKeyboardHide replyKeyboardHide = new ReplyKeyboardHide();
        replyKeyboardHide.setSelective(true);
        sendMessage.setReplyMarkup(replyKeyboardHide);

        try {
            sendMessage(sendMessage);
        } catch (org.telegram.telegrambots.exceptions.TelegramApiException e) {
            e.printStackTrace();
        }
        Manager.getInstance().setUserState(userId, chatId, STARTSTATE);
    }

    private static boolean isCommandForOther(String text) {
        boolean isSimpleCommand = text.equals("/start") || text.equals("/help") || text.equals("/stop");
        boolean isCommandForMe = text.equals("/start@MovieBuddyBot") || text.equals("/help@MovieBuddyBot") || text.equals("/stop@MovieBuddyBot");
        return text.startsWith("/") && !isSimpleCommand && !isCommandForMe;
    }

    //--------------------------Incoming messages handlers-----------------------------



    //-----------------------MainMenu selected--------------------

    private static boolean checkTheLocation(Message message){
        String a = com.moviebuddy.database.Manager.getInstance().getTypedLocation(message.getFrom().getId());
        return (a.startsWith("null"))?(false):(true);
    }

    private static SendMessage messageOnMainMenu(Message message) {

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            if (message.getText().equals(getShowSessionsCommand())) {
                sendMessageRequest = onPickALocation(message);
            } else if (message.getText().equals(getSettingsCommand())) {
                sendMessageRequest = onSettingsChosen(message);
            } else {
                sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                        getMainMenuKeyboard());
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }

    private static SendMessage onPickALocation(Message message) {

        ReplyKeyboardMarkup replyKeyboardMarkup = getLocationKeyboard(checkTheLocation(message));

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getPickALocationMessage());

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), PICKALOCATION);
        return sendMessage;
    }

    private static SendMessage onSettingsChosen(Message message) {

        ReplyKeyboardMarkup replyKeyboardMarkup = getSettingsKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getSettingsMessage());

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), SETTINGS);
        return sendMessage;
    }

    private static SendMessage onSettingsChosenAfterUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer userId = callbackQuery.getFrom().getId();

        ReplyKeyboardMarkup replyKeyboardMarkup = getSettingsKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
        sendMessage.setText(getSettingsMessage());

        com.moviebuddy.database.Manager.getInstance().setUserState(userId, Long.valueOf(userId), SETTINGS);
        return sendMessage;
    }
    //-----------------------MainMenu selected--------------------



    //-----------------------Settings selected--------------------
    //-----------------------Settings selected--------------------

    private static SendMessage messageOnSettings(Message message) {
        SendMessage sendMessageRequest;

        if (message.hasText()) {
            if (message.getText().equals(getPriceCommand())) {
                sendMessageRequest = onPriceChosen(message);
            } else if (message.getText().equals(getDistCommand())) {
                sendMessageRequest = onDistanceChosen(message);
            } else if (message.getText().equals(getStoredLocationCommand())) {
                sendMessageRequest = onStoredChosen(message);
            } else if (message.getText().equals(getShowSessionsCommand())) {
                sendMessageRequest = onPickALocation(message);
            } else {
                sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                        getSettingsKeyboard());
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }

    private static SendMessage onPriceChosen(Message message) {

        ReplyKeyboardMarkup replyKeyboardMarkup = getPriceKeyboard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getPriceMessage());

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), PRICE);
        return sendMessage;
    }

    private static SendMessage onDistanceChosen(Message message) {

        ReplyKeyboardMarkup replyKeyboardMarkup = getDistanceKeyboard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getDistanceMessage());

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), DISTANCE);
        return sendMessage;
    }

    private static SendMessage onStoredChosen(Message message) {

        ReplyKeyboardMarkup replyKeyboardMarkup = (checkTheLocation(message)) ? (getStoredNewLocationKeyboard()) : (getStoredLocationKeyboard());
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getStoredLocationMessage() + "( " + Manager.getInstance().getTypedLocation(message.getFrom().getId()) + " )");

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), EDITLOCATION);
        return sendMessage;
    }

    //-----------------------Settings selected--------------------



    //-----------------------Price selected--------------------

    private static SendMessage messageOnPrice(Message message,int state) {

        SendMessage sendMessageRequest = null;
        switch(state) {
            case PRICE:
                sendMessageRequest = onPriceCommand(message);
                break;
            case PRICERANGE:
                sendMessageRequest = onPriceRangeCommand(message);
                break;
        }
        return sendMessageRequest;
    }

    private static SendMessage onPriceCommand(Message message){

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            if (message.getText().equals(getLowestPriceCommand())) {
                sendMessageRequest = onLowestChosen(message);
            } else if (message.getText().equals(getRangeCommand())) {
                sendMessageRequest = onPriceRangeChosen(message);
            }else if (message.getText().equals(getGoBackCommand())) {
                sendMessageRequest = onBackFromPriceChosen(message);
            } else {
                sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                        getPriceKeyboard());
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;

    }

    private static SendMessage onPriceRangeCommand(Message message){
        if (message.isReply() && checkPriceRange(message)) {
            return onPriceRangeReceived(message);
        } else {
            return onPriceRangeChosen(message);
        }
    }

    private static SendMessage onPriceRangeReceived(Message message) {

        Manager.getInstance().setPrice(message.getFrom().getId(), message.getChatId(), message.getText());
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.enableMarkdown(true);
        sendMessageRequest.setReplyMarkup(getSettingsKeyboard());
        sendMessageRequest.setReplyToMessageId(message.getMessageId());
        sendMessageRequest.setText(getSettingsMessage() + "\n" + "Range selected: " + message.getText());
        sendMessageRequest.setChatId(message.getChatId().toString());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SETTINGS);
        return sendMessageRequest;
    }


    private static SendMessage onLowestChosen(Message message) {
        com.moviebuddy.database.Manager.getInstance().setPrice(message.getFrom().getId(),message.getChatId(),"lowest");

        ReplyKeyboardMarkup replyKeyboardMarkup = getSettingsKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getSettingsSetPrice());

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), SETTINGS);
        return sendMessage;
    }

    private static SendMessage onPriceRangeChosen(Message message) {

        ForceReplyKeyboard forceReplyKeyboard = getForceReply();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId( message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(forceReplyKeyboard);
        sendMessage.setText("Type range (LOW-HIGH): ");

        Manager.getInstance().setUserState(message.getFrom().getId(),  message.getChatId(), PRICERANGE);
        return sendMessage;
    }

    private static SendMessage onBackFromPriceChosen(Message message) {
        return onSettingsChosen(message);
    }

    //-----------------------Price selected--------------------



    //-----------------------Distance selected--------------------

    private static SendMessage messageOnDistance(Message message, int state) {

        SendMessage sendMessageRequest = null;
        switch(state) {
            case DISTANCE:
                sendMessageRequest = onDistanceCommand(message);
                break;
            case DISTANCERANGE:
                sendMessageRequest = onDistanceRangeCommand(message);
                break;
        }
        return sendMessageRequest;
    }

    private static SendMessage onDistanceRangeCommand(Message message) {
        if (message.isReply() && checkDistRange(message)) {
            return onDistanceRangeReceived(message);
        } else {
            return sendMessageDefault(message);
        }
    }

    private static SendMessage onDistanceRangeReceived(Message message) {

        Manager.getInstance().setDistance(message.getFrom().getId(), message.getChatId(), message.getText());
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.enableMarkdown(true);
        sendMessageRequest.setReplyMarkup(getSettingsKeyboard());
        sendMessageRequest.setReplyToMessageId(message.getMessageId());
        sendMessageRequest.setText(getSettingsMessage() + "\n" + "Range selected: " + message.getText());
        sendMessageRequest.setChatId(message.getChatId().toString());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SETTINGS);
        return sendMessageRequest;
    }

    private static SendMessage onDistanceCommand(Message message) {

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            if (message.getText().equals(getClosestCommand())) {
                sendMessageRequest = onClosestChosen(message);
            } else if (message.getText().equals(getRangeCommand())) {
                sendMessageRequest = onDistanceRangeChosen(message);
            }else if (message.getText().equals(getGoBackCommand())) {
                sendMessageRequest = onBackFromDistanceChosen(message);
            } else {
                sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                        getDistanceKeyboard());
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }

    private static SendMessage onClosestChosen(Message message) {

        com.moviebuddy.database.Manager.getInstance().setDistance(message.getFrom().getId(),message.getChatId(),"closest");

        ReplyKeyboardMarkup replyKeyboardMarkup = getSettingsKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getSettingsSetDistance());

        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), SETTINGS);
        return sendMessage;
    }

    private static SendMessage onDistanceRangeChosen(Message message) {

        ForceReplyKeyboard forceReplyKeyboard = getForceReply();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId( message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(forceReplyKeyboard);
        sendMessage.setText("Type range (LOW-HIGH)km: ");

        Manager.getInstance().setUserState(message.getFrom().getId(),  message.getChatId(), DISTANCERANGE);
        return sendMessage;
    }

    private static SendMessage onBackFromDistanceChosen(Message message) {
        return onSettingsChosen(message);
    }

    //-----------------------Distance selected--------------------



    //-----------------------StoredLocation selected--------------------

    private static SendMessage messageOnEitLocation(Message message,int state) {


        SendMessage sendMessageRequest = null;
        switch(state) {
            case EDITLOCATION:
                sendMessageRequest = onEditLocationCommand(message);
                break;
            case EDITADDLOCATION:
                sendMessageRequest = onEditAddLocationCommand(message);
                break;
        }
        return sendMessageRequest;
        

    }

    private static SendMessage onEditAddLocationCommand(Message message) {
        if (message.isReply() && checkTypedLocation(message)) {
            return onNewLocationReceived(message);
        } else {
            return sendMessageDefault(message);
        }
    }

    private static SendMessage onNewLocationReceived(Message message) {

        Manager.getInstance().setTypedLocation(message.getFrom().getId(), message.getChatId(), message.getText());
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.enableMarkdown(true);
        sendMessageRequest.setReplyMarkup(getSettingsKeyboard());
        sendMessageRequest.setReplyToMessageId(message.getMessageId());
        sendMessageRequest.setText(getSettingsMessage() + "\n" + "You entered a new location: " + "\n" + message.getText());
        sendMessageRequest.setChatId(message.getChatId().toString());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SETTINGS);
        return sendMessageRequest;

    }

    private static SendMessage onEditLocationCommand(Message message) {

        SendMessage sendMessageRequest;

        if (message.hasText()) {
            if (message.getText().equals(getNewLocationCommand()) || message.getText().equals(getEditLocationCommand())) {
                sendMessageRequest = onNewOrEditLocationChosen(message);
            } else if (message.getText().equals(getGoBackCommand())) {
                sendMessageRequest = onBackFromEditChosen(message);
            } else {
                sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                        getStoredLocationKeyboard());
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }

        return sendMessageRequest;
    }

    private static SendMessage onNewOrEditLocationChosen(Message message) {

        ForceReplyKeyboard forceReplyKeyboard = getForceReply();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId( message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(forceReplyKeyboard);
        sendMessage.setText("Type an address(STREET,BUILDING): ");

        Manager.getInstance().setUserState(message.getFrom().getId(),  message.getChatId(), EDITADDLOCATION);
        return sendMessage;
    }

    private static SendMessage onBackFromEditChosen(Message message) {
        return onSettingsChosen(message);
    }

    //-----------------------StoredLocation selected--------------------
    //-----------------------Settings selected--------------------



    //-----------------------Sessions selected--------------------

    private static SendMessage messageOnPickALocation(Message message, int state,Update update) {

        SendMessage sendMessageRequest = null;
        switch(state) {
            case PICKALOCATION:
                sendMessageRequest = onPickALocationCommand(message);
                break;
            case CURRENTLOCATION:
                sendMessageRequest = onCurrentLocationChoosen(message,update);
                break;
            case ENTERLOCATION:
                sendMessageRequest = onTypedLocationChoosen(message,update);
                break;
        }
        return sendMessageRequest;
    }

    private static SendMessage onPickALocationCommand(Message message) {
        SendMessage sendMessageRequest;
        if (message.hasText()) {
            if (message.getText().equals(getCurrentLocationCommand())) {
                sendMessageRequest = onCurrentLocationCommand(message);
                flagWhichLocationChosen = true;
            } else if (message.getText().equals(getTypedLocationCommand())) {
                sendMessageRequest = onTypedLocationCommand(message);
            } else if (message.getText().equals(getStoredLocationCommand())) {
                sendMessageRequest = onStoredLocationChoosen(message);
            } else {
                sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),
                        getLocationKeyboard(checkTheLocation(message)));
            }
        } else {
            sendMessageRequest = sendMessageDefault(message);
        }
        return sendMessageRequest;
    }

    private static SendMessage onCurrentLocationCommand(Message message) {

        //ForceReplyKeyboard forceReplyKeyboard = getForceReply();
        ReplyKeyboardMarkup replyKeyboardMarkup = getLocationKeyboard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId( message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText("Send us your location: ");

        Manager.getInstance().setUserState(message.getFrom().getId(),  message.getChatId(), CURRENTLOCATION);
        return sendMessage;
    }

    private static SendMessage onCurrentLocationChoosen(Message message,Update update) {
        if (message.hasLocation()) {
            return onCurrentLocationReceived(message, update);
        } else {
            return sendMessageDefault(message);
        }
    }

    private static SendMessage onCurrentLocationReceived(Message message,Update update) {

        final int state = Manager.getInstance().getMovieState(message.getFrom().getId());

        Manager.getInstance().setLocation(message.getFrom().getId(), message.getChatId(), message.getLocation().getLatitude(),
                message.getLocation().getLongitude());
        String locationReceived= String.valueOf(message.getLocation().getLongitude()
                +"\n"+ message.getLocation().getLatitude());
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.enableHtml(true);
        sendMessageRequest.setReplyMarkup(getMoviesInlineKeyboard(message.getFrom().getId()));
        sendMessageRequest.setText(composeMovies(state));
        sendMessageRequest.setChatId(message.getChatId().toString());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SHOWMOVIES);
        return sendMessageRequest;
    }

    private static SendMessage onTypedLocationCommand(Message message) {

        ForceReplyKeyboard forceReplyKeyboard = getForceReply();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId( message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setReplyMarkup(forceReplyKeyboard);
        sendMessage.setText("Type location (STREET,BUILDING): ");

        Manager.getInstance().setUserState(message.getFrom().getId(),  message.getChatId(), ENTERLOCATION);
        return sendMessage;
    }

    private static SendMessage onTypedLocationChoosen(Message message,Update update) {
        if (message.isReply() && checkTypedLocation(message)) {
            return onTypedLocationReceived(message,update);
        } else {
            return sendMessageDefault(message);
        }
    }

    private static SendMessage onTypedLocationReceived(Message message,Update update) {
        final int state = Manager.getInstance().getMovieState(message.getFrom().getId());
        Manager.getInstance().setTypedLocation(message.getFrom().getId(), message.getChatId(), message.getText());
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.enableHtml(true);
        sendMessageRequest.setReplyMarkup(getMoviesInlineKeyboard(message.getFrom().getId()));
        sendMessageRequest.setText(composeMovies(state));
        sendMessageRequest.setChatId(message.getChatId().toString());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SHOWMOVIES);
        return sendMessageRequest;
    }

    private static SendMessage onStoredLocationChoosen(Message message) {
        final int state = Manager.getInstance().getMovieState(message.getFrom().getId());
        String storedLocation = com.moviebuddy.database.Manager.getInstance().getTypedLocation(message.getFrom().getId());
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.enableHtml(true);
        sendMessageRequest.setReplyMarkup(getMoviesInlineKeyboard(message.getFrom().getId()));
        sendMessageRequest.setText(composeMovies(state));
        //"Your stored location: " + "\n" + storedLocation
        sendMessageRequest.setChatId(message.getChatId().toString());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SHOWMOVIES);
        return sendMessageRequest;
    }

    //-----------------------Sessions selected--------------------



    //-----------------------ShowMovies selected--------------------

    private static SendMessage onShowMoviesChosen(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMoviesKeyboard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(getShowMoviesMessage());
        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), SHOWSESSIONS);

        return sendMessage;
    }

    private static SendMessage messageOnShowMovies(Message message) {

        SendMessage sendMessageRequest = null;

        if (message.hasText()) {
            for (String movieName : Manager.getInstance().getrecentMovies()) {
                if (message.getText().equals(movieName)){
                    sendMessageRequest = onMovieSelected(movieName, message);
                    break;
                }
                else {
                    sendMessageRequest = sendChooseOptionMessage(message.getChatId(), message.getMessageId(),//change to default
                            getMoviesKeyboard());
                }
            }
        }
        else {
            sendMessageRequest = sendMessageDefault(message);
        }
        return sendMessageRequest;
    }




    private static SendMessage onMovieSelected(String movieName, Message message){

        String strDist = "";

        String html = getDirections(message);
        String str1 = com.moviebuddy.database.Manager.getInstance().getPrice(message.getFrom().getId());
        String str2 = com.moviebuddy.database.Manager.getInstance().getDistance(message.getFrom().getId());

        String mes = String.format(getCelebMessage() + "\nYou can watch " + movieName + " at 10:45" +
                 "\n%s Price : $$$" + "\n%sDistance to the cinema: " + strDist + "\n%s " + html,
                Emoji.CASH.toString(),Emoji.FINISH.toString(),Emoji.ROCKET.toString());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //inlineKeyboardMarkup = getSessionsInlineKeyboard();

        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = getMovieChosenKeyboard();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(mes);
        sendMessage.enableHtml(true);


        com.moviebuddy.database.Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), MAINMENU);
        return sendMessage;
    }


    //-----------------------ShowMovies selected--------------------


    //-----------------------ShowSessions selected--------------------

    private SendMessage messageOnShowSessions(Message message) {
        return new SendMessage();
    }

    //-----------------------ShowSessions selected--------------------



    //--------------------Reply keyboards--------------------

    private static ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getShowSessionsCommand());
        keyboardFirstRow.add(getSettingsCommand());
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getLocationKeyboard(boolean a) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        //current location button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getCurrentLocationCommand());
        keyboard.add(keyboardFirstRow);

        //enter location button
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getTypedLocationCommand());
        keyboard.add(keyboardSecondRow);

        if(a) {
            //stored location button
            KeyboardRow keyboardThirdRow = new KeyboardRow();
            keyboardThirdRow.add(getStoredLocationCommand());
            keyboard.add(keyboardThirdRow);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getMoviesKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String keys : Manager.getInstance().getrecentMovies()) {
            KeyboardRow row = new KeyboardRow();
            row.add(keys);
            keyboard.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getMovieChosenKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getShowMoviesCommand());
        keyboardFirstRow.add(getSettingsCommand());
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ForceReplyKeyboard getForceReply() {
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        return forceReplyKeyboard;
    }

    private static ReplyKeyboardMarkup getSettingsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getPriceCommand());
        keyboardFirstRow.add(getDistCommand());
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getStoredLocationCommand());
        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(getShowSessionsCommand());
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getPriceKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        //price  button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getLowestPriceCommand());
        keyboard.add(keyboardFirstRow);

        //distance button
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getRangeCommand());
        keyboard.add(keyboardSecondRow);


        //stored location button
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(getGoBackCommand());
        keyboard.add(keyboardThirdRow);


        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getDistanceKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        //price  button
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getClosestCommand());
        keyboard.add(keyboardFirstRow);

        //distance button
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getRangeCommand());
        keyboard.add(keyboardSecondRow);


        //stored location button
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(getGoBackCommand());
        keyboard.add(keyboardThirdRow);


        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getStoredLocationKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getNewLocationCommand());
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getGoBackCommand());
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getStoredNewLocationKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getEditLocationCommand());
        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getGoBackCommand());
        keyboard.add(keyboardSecondRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup getLocationKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        //keyboardFirstRow.add(getSendLocationCommand());

        KeyboardButton button = new KeyboardButton();
        button.setRequestLocation(true);
        button.setText(getSendLocationCommand());
        keyboardFirstRow.add(button);
        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    //--------------------Reply keyboards--------------------



    //-----------------------Get messages--------------------

    private static String getHelpMessage() {
        String baseString = "You need to know the closest %s and the cheapest %s movies sessions?\n" +
                "Just send this commands %s and you will receive all information instantly %s";
        return String.format(baseString,Emoji.FINISH.toString(),
                Emoji.CASH.toString(),
                Emoji.FINGDOWN.toString(), Emoji.OK.toString());
    }

    private static String getPickALocationMessage() {
        String baseString = "Pick a location %s";
        return String.format(baseString,
                Emoji.WHITE_HEAVY_CHECK_MARK.toString());
    }

    private static String getShowMoviesMessage() {
        String baseString = "Pick a movie below %s%s";
        return String.format(baseString,
                Emoji.FINGDOWN.toString(),
                Emoji.WHITE_HEAVY_CHECK_MARK.toString());
    }

    private static String getSettingsMessage() {
        return String.format("%s Set up a bot ",
                Emoji.WRENCH.toString());
    }

    private static String getSettingsSetPrice() {
        return String.format("Sessions with the lowest price will be shown ");
    }

    private static String getSettingsSetDistance() {
        return String.format("Closest sessions will be shown ");
    }

    private static String getPriceMessage() {
        return String.format("Change price properties ");
    }

    private static String getDistanceMessage() {
        return String.format("Change distance properties ");
    }

    private static String getStoredLocationMessage() {
        return String.format("Save a new location or \nEdit a current one ");
    }

    private static String getCelebMessage() {
        return String.format("We found a movie for you %s",
                Emoji.CELEB.toString());
    }

    //-----------------------Get messages--------------------



    //-----------------------Get commands--------------------

    private static String getShowSessionsCommand() {
        return String.format("%s Sessions",
                Emoji.MOVIECAMERA.toString());
    }

    private static String getSettingsCommand() {
        return String.format("%s Settings",
                Emoji.SETT.toString());
    }

    private static String getCurrentLocationCommand() {
        return String.format("%s Current location",
                Emoji.CURRENTLOC.toString());
    }

    private static String getTypedLocationCommand() {
        return String.format("%s Enter an address",
                Emoji.NEWTAG.toString());
    }

    private static String getStoredLocationCommand() {
        return String.format("%s Saved location",
                Emoji.STORED.toString());
    }

    private static String getPriceCommand() {
        return String.format("%s Price",
                Emoji.CASH.toString());
    }

    private static String getDistCommand() {
        return String.format("%s Distance",
                Emoji.FINISH.toString());
    }

    private static String getSavedLocationCommand() {
        return String.format("%s Stored location",
                Emoji.SAVEDLOC.toString());
    }

    private static String getLowestPriceCommand() {
        return String.format("%s Lowest",
                Emoji.LOWEST.toString());
    }

    private static String getRangeCommand() {
        return String.format("%s Choose range",
                Emoji.RANGE.toString());
    }

    private static String getClosestCommand() {
        return String.format("%s Closest",
                Emoji.LOWEST.toString());
    }

    private static String getEditLocationCommand() {
        return String.format("%s Edit",
                Emoji.PENENTERLOC.toString());
    }

    private static String getNewLocationCommand() {
        return String.format("%s Add",
                Emoji.HEAVY_PLUS_SIGN.toString());
    }

    private static String getGoBackCommand() {
        return String.format("%s Back",
                Emoji.LEFTARROW.toString());
    }

    private static String getShowMoviesCommand() {
        return String.format("%s Movies",
                Emoji.MOVIETIME.toString());
    }

    private static String getSendLocationCommand() {
        return String.format("%s Send current location",
                Emoji.ROUND_PUSHPIN.toString());
    }

    //-----------------------Get commands--------------------



    //-----------------------Common messages--------------------

    private static SendMessage sendMessageDefault(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), MAINMENU);
        return sendHelpMessage(message.getChatId().toString(), message.getMessageId(), replyKeyboardMarkup);
    }

    private static SendMessage sendChooseOptionMessage(Long chatId, Integer messageId,
                                                       ReplyKeyboard replyKeyboard) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setReplyToMessageId(messageId);
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText("Please, select an option from the menu.");

        return sendMessage;
    }

    private static SendMessage sendHelpMessage(String chatId, Integer messageId, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(messageId);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setText(getHelpMessage());
        return sendMessage;
    }

    //-----------------------Common messages--------------------



    //-----------------------Calculations--------------------
    private static String getDirections(Message message){

        String link;
        String html;
        String beg = "https://www.google.ru/maps/dir/";
        String city = "Saint-Petersburg";
        String cp = ",";
        String sep = "/";
        String zoom = "17z";
        String end = "/?hl=ru";

        double[] cinema = new double[]{59.93166,30.353736};
        double[] coord = Manager.getInstance().getLocation(message.getFrom().getId());
        String typed = Manager.getInstance().getTypedLocation(message.getFrom().getId());

        link = (flagWhichLocationChosen)?(beg + coord[0] + cp + coord[1] + sep + cinema[0] + cp + cinema[1] + end)
                :(beg + typed + cp + city + sep + cinema[0] + cp + cinema[1]);
        flagWhichLocationChosen = false;
        html = "<a href=\"" + link + "\">Directions to the cinema</a>";

        return html;
    }

    private static boolean checkTypedLocation(Message message){

        if(!message.getText().contains(",")) return false;

        String pat1 = "^([A-Za-z]){4,25},(\\d{1,3})$";
        String pat2 = "^([1-9\\s]){1,3}([A-Za-z]){4,25},(\\d{1,3})$";

        if(Pattern.matches(pat1,message.getText()) || Pattern.matches(pat2,message.getText())) {
            try {
                String[] str = message.getText().split(",");
                int n = Integer.parseInt(str[1]);
                if (n < 0 || n > 380) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else return false;

        return true;
    }

    private static boolean checkPriceRange(Message message){

        if(!message.getText().contains("-")) return false;

        String pat = "^(\\d{1,4})-(\\d{1,4})$";

        if(Pattern.matches(pat,message.getText())){
            try {
                String[] str = message.getText().split("-");
                int x = Integer.parseInt(str[0]);
                int y = Integer.parseInt(str[1]);
                if (x < 80 || x > 2000 || y < 80 || y > 2000 ) return false;
                if ( x >= y ) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        } else return false;

        return true;
    }

    private static boolean checkDistRange(Message message){

        String pat1 = "^(\\d){1,2}.(\\d){1,2}-(\\d){1,2}.(\\d){1,2}$";
        String pat2 = "^(\\d){1,2}.(\\d){1,2}-(\\d){1,2}$";
        String pat3 = "^(\\d){1,2}-(\\d){1,2}.(\\d){1,2}$";
        String pat4 = "^(\\d){1,2}-(\\d){1,2}$";


        if(message.getText().contains("-")) {
            if (Pattern.matches(pat1, message.getText()) || Pattern.matches(pat2, message.getText())
                    || Pattern.matches(pat3, message.getText()) || Pattern.matches(pat4, message.getText())) {
                try {
                    String[] str = message.getText().split("-");
                    double x = Double.parseDouble(str[0]);
                    double y = Double.parseDouble(str[1]);
                    if (x < 0.1 || x > 49 || y < 0.1 || y > 50) return false;
                    if (x >= y) return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            } else return false;
        } else return false;

        return true;
    }


    //-----------------------Calculations--------------------

    public static double distFrom(int userId, String cinemaAddress) {

        String str = Manager.getInstance().getTypedLocation(userId) + " ,Санкт-Петербург";
        System.out.println(str);
        //getCoord = lat,lng from param
        double[] cinema = ToCoordinates.getInstance().getCoord(cinemaAddress + " ,Санкт-Петербург");
        double[] cord;
        cord = (flagWhichLocationChosen)?(Manager.getInstance().getLocation(userId)):
                (ToCoordinates.getInstance().getCoord(str));
        double earthRadius = 6371000; // 3958.75 miles ,6371.0 km
        double dLat = Math.toRadians(cinema[0]-cord[0]);
        double dLng = Math.toRadians(cinema[1]-cord[1]);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(cord[0])) * Math.cos(Math.toRadians(cinema[0]));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public static double timeTo(double meters){
        //by foot
        //avg spped = 5km/h ; 1km = 12 min avg
        double n = meters/1000;
        double time = n * 12;
        return time;
    }

}
