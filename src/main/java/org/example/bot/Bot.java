package org.example.bot;

import org.example.dbconnection.DBConnection;
import org.example.entities.*;
import org.example.entities.GameSessions;
import org.example.resource.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return System.getenv("BotUsername");
       // return ConfigReader.loadConfig().getProperty("BotUsername");

    }

    @Override
    public String getBotToken() {
        return System.getenv("BotToken");
        //return ConfigReader.loadConfig().getProperty("BotToken");
    }

    Map<Long, UserOperationState> userOperationState = new ConcurrentHashMap<>();
    DialogService dialogService = new DialogService();
    String tempNameOfGameSession;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleMessage(Message message) {
        String textFromUser = message.getText();
        Long chatId = message.getChatId();

        String userName;
        userName = (message.getFrom().getUserName() != null) ? message.getFrom().getUserName() :
                (message.getFrom().getLastName() != null) ? message.getFrom().getFirstName() + " " + message.getFrom().getLastName() :
                        message.getFrom().getFirstName();


        switch (textFromUser) {
            case "/start" -> {

                dialogService.sendKeyboard(chatId, "Кнопка \"Створити гру\" створює нову гру у яку" +
                        " можуть заходити учасники. \"Зайти у гру\" дозіоляє зайти у гру, потрібно знати її назву. " +
                        "\"Вийти з гри\" дозволяє вийти з гри, потрібно натиснути на назву гри", Keyboard.getMainMenu());
            }
            case "Створити гру" -> {
                userOperationState.put(chatId, UserOperationState.CREATE_GAME);
                dialogService.sendMessage(chatId, "Введи назву нової гри");
            }
            case "Видалити гру" -> {
                userOperationState.put(chatId, UserOperationState.DELETE_GAME);
                dialogService.sendIfYouAdmin(chatId, userName, "Натисни, щоб видалити гру");
            }
            case "Зайти у гру" -> {
                userOperationState.put(chatId, UserOperationState.ENTER_GAME);
                dialogService.sendMessage(chatId, "Введи назву гри");
            }
            case "Вийти з гри" -> {
                userOperationState.put(chatId, UserOperationState.EXIT_GAME);
                dialogService.sendIfYouInGame(chatId, "Натисни щоб видалити. Ти у іграх:");
            }
            case "Список учасників" -> {
                userOperationState.put(chatId, UserOperationState.GET_LIST_USERS);
                dialogService.sendIfYouAdmin(chatId, userName, "Вибери гру, щоб отримати список учасників з неї");
            }
            case "Видалити учасника" -> {
                userOperationState.put(chatId, UserOperationState.SELECT_USER);
                dialogService.sendIfYouAdmin(chatId, userName, "Вибери гру, щоб отримати список учасників з для видалення");
            }
            case "Надіслати гравцям імена" -> {
                userOperationState.put(chatId, UserOperationState.RUN_CAROUSEL);
                dialogService.sendIfYouAdmin(chatId, userName, "Натисни, щоб роздати імена");
            }
            default -> handleUserOperation(chatId, userName, textFromUser);
        }
    }

    private void handleCallbackQuery(CallbackQuery message) {
        Long chatId = message.getMessage().getChatId();
        String userName;
        userName = (message.getFrom().getUserName() != null) ? message.getFrom().getUserName() :
                (message.getFrom().getLastName() != null) ? message.getFrom().getFirstName() + " " + message.getFrom().getLastName() :
                        message.getFrom().getFirstName();
        String text = message.getData();

        if (userOperationState.containsKey(chatId)) {
            UserOperationState operationState = userOperationState.get(chatId);
            switch (operationState) {
                case EXIT_GAME -> {

                    GemaSessionRepository.exitTheGame(userName, chatId, text);
                    if (!GemaSessionRepository.isAdmin(userName, chatId)) {
                        dialogService.sendKeyboard(chatId, "Вас успішно видалено", Keyboard.getMainMenu());
                    } else {
                        dialogService.sendMessage(chatId, "Вас успішно видалено");
                    }
                }
                case DELETE_GAME -> {

                    if (GemaSessionRepository.deleteGameSession(userName, chatId, text)) {
                        if (!GemaSessionRepository.isAdmin(userName, chatId)) {
                            dialogService.sendKeyboard(chatId, "Гру видалено", Keyboard.getMainMenu());
                        } else {
                            dialogService.sendMessage(chatId, "Гру видалено");
                        }
                    }
                }
                case GET_LIST_USERS -> {

                    String joinedNames = Objects.requireNonNull(GemaSessionRepository.listOfUsers(text)).stream()
                            .map(User::getUserName)
                            .collect(Collectors.joining("\n"));
                    joinedNames += "\nКідькість учасників: " + Objects.requireNonNull(GemaSessionRepository.listOfUsers(text)).size();
                    dialogService.sendMessage(chatId, joinedNames);
                }
                case SELECT_USER -> {

                    userOperationState.put(chatId, UserOperationState.DELETE_USER);
                    tempNameOfGameSession = text;
                    dialogService.sendKeyboard(chatId, "Вибери користувача, якого треба видалити",
                            Keyboard.userListKeyboard(GameSessions.get().stream()
                                    .filter(gameSession -> gameSession.getNameOfGameSession().equals(text))
                                    .map(GameSession::getAllUsers)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toList())));
                }
                case DELETE_USER -> {

                    GemaSessionRepository.deleteUser(text, tempNameOfGameSession);
                    dialogService.sendMessage(chatId, text + " видалено");
                    tempNameOfGameSession = null;
                }
                case RUN_CAROUSEL -> {
                    List<User> newList = PeopleCarousel.runCarousel(Objects.requireNonNull(GemaSessionRepository.listOfUsers(text)));

                    GameSessions.get().forEach(gs -> {
                        if (gs.getNameOfGameSession().equals(text)) {
                            gs.setUsers(newList);
                        }
                    });
                     DBConnection.setResipient(newList,text);
                    dialogService.sendToAll(Objects.requireNonNull(GemaSessionRepository.listOfUsers(text)));
                }
            }
        }
    }

    private void handleUserOperation(Long chatId, String userName, String textFromUser) {
        if (userOperationState.get(chatId) == UserOperationState.CREATE_GAME) {
            userOperationState.remove(chatId);
            dialogService.sendKeyboard(chatId, GemaSessionRepository.registerTheGame(userName, chatId, textFromUser),
                    Keyboard.getMainMenuForAdmin());
        } else if (userOperationState.get(chatId) == UserOperationState.ENTER_GAME) {

            if (GemaSessionRepository.isAdmin(userName, chatId)) {
                dialogService.sendKeyboard(chatId, GemaSessionRepository.enterTheGame(userName, chatId, textFromUser)
                        , Keyboard.getMainMenuForAdmin());
            } else {
                dialogService.sendMessage(chatId, GemaSessionRepository.enterTheGame(userName, chatId, textFromUser));

            }
        }
    }


    public  class DialogService {
        public void sendKeyboard(Long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendKeyboard(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(text);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendMessage(Long chatId, String textToSend) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(textToSend);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendToAll(List<User> users) {
            for (User user : users) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(user.getChatId()));
                message.setText(user.getObjectOfDonation());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void sendIfYouAdmin(Long chatId, String userName, String textIfTrue) {
            if (GemaSessionRepository.isAdmin(userName, chatId)) {
                sendKeyboard(chatId, textIfTrue, Keyboard.gameSessionsInl(
                        GameSessions.get().stream().filter(gs -> gs.getAdmin().equals(new User(userName, chatId)))
                                .collect(Collectors.toList())));
            } else sendKeyboard(chatId, "Ви не є адміном для жодної гри", Keyboard.getMainMenu());
        }

        public void sendIfYouInGame(Long chatId, String textIfTrue) {
            if (GameSessions.get().stream().anyMatch(gs -> GemaSessionRepository.isInGame(chatId, gs))) {
                sendKeyboard(chatId, textIfTrue, Keyboard.gameSessionsInl(
                        GameSessions.get().stream().filter(gs -> GemaSessionRepository.isInGame(chatId, gs))
                                .collect(Collectors.toList())));
            } else sendMessage(chatId, "Ти не береш участь в жодній грі");
        }
    }
}
