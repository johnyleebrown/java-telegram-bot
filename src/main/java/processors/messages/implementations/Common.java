package processors.messages.implementations;

import database.Manager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardHide;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static services.Keyboards.*;
import static services.Messages.*;
import static services.States.*;

/**
 * Created by Greg
 */
public class Common {

    public static SendMessage sendMessageDefault(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setText(getHelpMessage());

        Manager.getInstance().setUserState(message.getFrom().getId(),message.getChatId(), MAINMENU);
        return sendMessage;
    }

    public static SendMessage sendChooseOptionMessage(Message message, ReplyKeyboard replyKeyboard) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getFrom().getId());
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.setText(getOptionMessage());

        return sendMessage;
    }

    public static SendMessage sendHideKeyboard(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText("Bye bye..");

        ReplyKeyboardHide replyKeyboardHide = new ReplyKeyboardHide();
        replyKeyboardHide.setSelective(true);
        sendMessage.setReplyMarkup(replyKeyboardHide);

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), START);
        return sendMessage;
    }

    public static SendMessage handleBlockedUser(Message message){

        ReplyKeyboardMarkup replyKeyboardMarkup = getBlockedUserKeyboard();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(getBlockedUserMessage());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), BLOCKED);
        return sendMessage;
    }

    public static SendMessage onSettingsChosenAfterUpdate(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(getSettingsKeyboard());
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText(getSettingsMessage());

        Manager.getInstance().setUserState(message.getFrom().getId(), message.getChatId(), SETTINGS);
        return sendMessage;
    }

}
