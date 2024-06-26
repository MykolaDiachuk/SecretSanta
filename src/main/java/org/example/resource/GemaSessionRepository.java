package org.example.resource;

import org.example.bot.Bot;
import org.example.dbconnection.DBConnection;
import org.example.entities.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GemaSessionRepository {
    public static String registerTheGame(String userName, Long chatId, String nameOfSession) {
        User admin = new User(userName, chatId);
        if (!DBConnection.doesUserExist(userName, chatId)) {
            DBConnection.insertUser(userName, chatId);
        }

        GameSession gameSession = findGame(nameOfSession);
        if (gameSession == null) {
            gameSession = new GameSession(admin, nameOfSession);
            gameSession.getAllUsers().add(admin);
            DBConnection.insertGameSession(nameOfSession, DBConnection.getUserId(userName, chatId));
            DBConnection.insertIntoGame(DBConnection.getUserId(userName, chatId), DBConnection.getGameSessionId(nameOfSession));
            return "Гру створено! " + userName + " адмін гри.";

        } else return "Гра з таким іменем вже існує";
    }

    public static String enterTheGame(String userName, Long chatId, String nameOfSession) {
        User user = new User(userName, chatId);
        if (!DBConnection.doesUserExist(userName, chatId)) {
            DBConnection.insertUser(userName, chatId);
        }
        GameSession gameSession = findGame(nameOfSession);
        if (gameSession != null) {
            if (isInGame(chatId, gameSession)) {
                return "Ви вже у грі";
            }
            DBConnection.insertIntoGame(DBConnection.getUserId(userName, chatId), DBConnection.getGameSessionId(nameOfSession));
            gameSession.getAllUsers().add(user);
            return "Вас додано до гри";
        } else return "Такої гри не існує";

    }

    public static void exitTheGame(String userName, Long chatId, String nameOfSession) {
        User user = new User(userName, chatId);
        GameSession gameSession = findGame(nameOfSession);
        if (gameSession != null) {
            gameSession.getAllUsers().remove(user);
            if(gameSession.getAdmin().equals(user)) {
                DBConnection.deleteGameSession(nameOfSession);
                deleteGameSession(userName, chatId, nameOfSession);
            }
            DBConnection.deleteFromGame(DBConnection.getUserId(userName, chatId), DBConnection.getGameSessionId(nameOfSession));
            if (gameSession.getAllUsers().isEmpty()) {
                DBConnection.deleteGameSession(nameOfSession);
                deleteGameSession(userName, chatId, nameOfSession);

            }
        }

    }

    public static boolean deleteGameSession(String userName, Long chatId, String nameOfSession) {
        User admin = new User(userName, chatId);
        GameSession gameSession = findGame(nameOfSession);
        if (gameSession != null) {
            if (gameSession.getAdmin().equals(admin)) {
                GameSessions.get().remove(gameSession);
                DBConnection.deleteGameSession(nameOfSession);
                return true;
            } else return false;
        } else return false;
    }

    public static List<User> listOfUsers(String nameOfSession) {
        GameSession gameSession = findGame(nameOfSession);
        if (gameSession != null) {
            return gameSession.getAllUsers();
        } else return null;
    }

    public static String deleteUser(String userNameToDelete, String nameOfSession) {
        GameSession gameSession = findGame(nameOfSession);
        if (gameSession != null && !gameSession.getAdmin().getUserName().equals(userNameToDelete)) {

                List<User> allUsers = gameSession.getAllUsers();
                List<User> updatedUsers = allUsers.stream()
                        .filter(user -> !user.getUserName().equals(userNameToDelete))
                        .collect(Collectors.toList());
                gameSession.setUsers(updatedUsers);
                DBConnection.deleteFromGame(DBConnection.getUserId(userNameToDelete), DBConnection.getGameSessionId(nameOfSession));
                if (gameSession.getAllUsers().isEmpty()) deleteGameSession(gameSession.getAdmin().getUserName(),
                        gameSession.getAdmin().getChatId(), gameSession.getNameOfGameSession());
                return "Гравця " + userNameToDelete + " видалено";


        }else return "Ви, " + userNameToDelete + ", адмін гри, вас не можна видалити";
    }

    public static GameSession findGame(String nameOfSession) {
        boolean isContains = GameSessions.get().stream().anyMatch(gs -> gs.getNameOfGameSession().equals(nameOfSession));
        if (isContains) {
            Optional<GameSession> element = GameSessions.get().stream().
                    filter(gs -> gs.getNameOfGameSession().equals(nameOfSession)).findFirst();
            return element.orElse(null);
        } else return null;
    }

    public static boolean isInGame(Long chatId, GameSession gs) {
        return gs.getAllUsers().stream().anyMatch(user1 -> user1.getChatId().equals(chatId));
    }

    public static boolean isAdmin(String userName, Long chatId) {

        return GameSessions.get().stream().anyMatch(gs ->
                gs.getAdmin().equals(new User(userName, chatId)));

    }

}
