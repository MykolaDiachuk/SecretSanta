package org.example.resource;

import org.example.entities.GameSession;
import org.example.entities.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    public static ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Створити гру");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Зайти у гру");
        row2.add("Вийти з гри");
        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getMainMenuForAdmin() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Створити гру");
        row1.add("Видалити гру");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Зайти у гру");
        row2.add("Вийти з гри");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Список учасників");
        row3.add("Видалити учасника");
        KeyboardRow row4 = new KeyboardRow();
        row4.add("Надіслати гравцям імена");
        ArrayList<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup gameSessionsInl(List<GameSession> gameSessions) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (GameSession gs : gameSessions) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(gs.getNameOfGameSession());
            button.setCallbackData(gs.getNameOfGameSession());
            row.add(button);
            keyboard.add(row);
        }

        markup.setKeyboard(keyboard);
        return markup;
    }
    public static InlineKeyboardMarkup userListKeyboard(List<User> users) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        // Кількість імен у рядку перед розбивкою на новий рядок
        int namesPerRow = 3;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(user.getUserName());
            button.setCallbackData(user.getUserName());
            row.add(button);

            // Якщо досягнуто кількість імен на рядку або це останній користувач
            if (row.size() == namesPerRow || i == users.size() - 1) {
                keyboard.add(row);
                row = new ArrayList<>();
            }
        }

        markup.setKeyboard(keyboard);
        return markup;
    }

}
