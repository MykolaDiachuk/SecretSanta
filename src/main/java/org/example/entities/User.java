package org.example.entities;

import org.example.bot.Bot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

public class User {
   private String userName;
   private Long chatId;
   private String objectOfDonation;

public User(){}
    public User(String userName, Long chatId) {
        this.userName = userName;
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getObjectOfDonation() {
        return objectOfDonation;
    }

    public void setObjectOfDonation(String objectOfDonation) {
        this.objectOfDonation = objectOfDonation;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userName, user.userName) && Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, chatId);
    }
}
