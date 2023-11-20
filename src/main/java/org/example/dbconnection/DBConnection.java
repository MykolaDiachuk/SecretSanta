package org.example.dbconnection;

import org.example.entities.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    protected static String dbHost = "DESKTOP-O74CBO1";
    protected static String dbPort = "1433";
    protected static String dbUser = "niko";
    protected static String dbPass = "asertan12";
    protected static String dbName = "SecretSanta";


    public static Connection getDBConnection() {
        String connectionString = "jdbc:sqlserver://" + dbHost + ":" + dbPort + ";" +
                "databaseName=" + dbName + ";user=" + dbUser + ";password=" + dbPass;
        Connection dbConnection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dbConnection = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return dbConnection;
    }

    public static void insertUser(String userName, Long chatId) {
        String insertTableSQL = "INSERT INTO Users ([userName], [chatId]) VALUES (?, ?)";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setLong(2, chatId);

            // execute insert SQL statement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into Users table!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertGameSession(String nameOfSession, Integer adminId) {
        String insertTableSQL = "INSERT INTO GameSessions ([nameGameSessions],[adminId]) VALUES (?,?)";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL)) {

            preparedStatement.setString(1, nameOfSession);
            preparedStatement.setInt(2, adminId);

            // execute insert SQL statement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into GameSession table!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertIntoGame(Integer userId, Integer gameSessionId) {
        String insertTableSQL = "INSERT INTO Games ([idGameSession], [idUser]) VALUES (?,?)";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(insertTableSQL)) {

            preparedStatement.setLong(1, gameSessionId);
            preparedStatement.setLong(2, userId);

            // execute insert SQL statement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into GameSession table!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Integer> getAllUsersId(Integer idgs) {
        List<Integer> users = new ArrayList<>();
        String selectSQL = "SELECT idUser FROM Games WHERE idGameSession = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idgs);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("idUser");
                users.add(id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public static List<Integer> getAllGSId() {
        List<Integer> idgs = new ArrayList<>();
        String selectSQL = "SELECT idGameSessions FROM GameSessions";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Integer id = rs.getInt("idGameSessions");
                idgs.add(id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return idgs;
    }

    public static Integer getUserId(String userName, Long chatId) {
       Integer userId = null;
        String selectSQL = "SELECT idUser FROM Users WHERE userName = ? AND chatId = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setLong(2, chatId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("idUser");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userId;
    }
    public static String getNameOfRecipient(Integer idUser, Integer idgs) {
        String  recipient = null;
        String selectSQL = "SELECT nameOfRecipient FROM Games WHERE idUser = ? AND  idGameSession = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, idUser);
            preparedStatement.setInt(2, idgs);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
               recipient = resultSet.getString("nameOfRecipient");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return recipient;
    }

    public static User getUserById(Integer idUser) {
        User user = new User();
        String selectSQL = "SELECT * FROM Users WHERE idUser = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, idUser);


            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user.setUserName(resultSet.getString("userName"));
                user.setChatId(resultSet.getLong("chatId"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public static Integer getUserId(String userName) {
       Integer userId = null;
        String selectSQL = "SELECT idUser FROM Users WHERE userName = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("idUser");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userId;
    }

    public static Integer getGameSessionId(String nameGameSessions) {
        Integer gsId = null;
        String selectSQL = "SELECT idGameSessions FROM GameSessions WHERE nameGameSessions = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, nameGameSessions);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                gsId = resultSet.getInt("idGameSessions");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gsId;
    }

    public static String getGameSessionName(Integer idgs) {
        String gsName = null;
        String selectSQL = "SELECT nameGameSessions FROM GameSessions WHERE idGameSessions = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idgs);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                gsName = resultSet.getString("nameGameSessions");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gsName;
    }

    public static Integer getGameSessionAdmin(Integer id) {
        Integer gsAdmin = null;
        String selectSQL = "SELECT adminId FROM GameSessions WHERE idGameSessions = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                gsAdmin = resultSet.getInt("adminId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gsAdmin;
    }

    public static boolean doesUserExist(String userName, Long chatId) {
        boolean userExists = false;
        String selectSQL = "SELECT COUNT(*) as count FROM Users WHERE userName = ? AND chatId = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setLong(2, chatId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                userExists = (count > 0);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return userExists;
    }

    public static boolean doesGameExist(String nameOfSession) {
        boolean gameExists = false;
        String selectSQL = "SELECT COUNT(*) as count FROM GameSessions WHERE  nameGameSessions = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, nameOfSession);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                gameExists = (count > 0);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gameExists;
    }

    public static void deleteUser(Long id_of_user) {
        String deleteSQL = "DELETE FROM Users WHERE idUser = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteSQL)) {

            preparedStatement.setLong(1, id_of_user);
            preparedStatement.executeUpdate();
            System.out.println("User with ID " + id_of_user + " is deleted from Users table!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteFromGame(Integer idUser, Integer idGameSession) {
        String deleteSQL = "DELETE FROM Games WHERE idUser = ? AND idGameSession = ? ";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteSQL)) {

            preparedStatement.setLong(1, idUser);
            preparedStatement.setLong(2, idGameSession);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void deleteGame(Integer idGameSession) {
        String deleteSQL = "DELETE FROM Games WHERE  idGameSession = ? ";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteSQL)) {

            preparedStatement.setLong(1, idGameSession);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void deleteGameSession(String nameGameSession) {

        Integer isGS = getGameSessionId(nameGameSession);
        deleteGame(isGS);
        String deleteSQL = "DELETE FROM GameSessions WHERE  nameGameSessions = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteSQL)) {

            preparedStatement.setString(1, nameGameSession);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setResipient( List<User> newList, String nameGameSessions ){
        Integer isGS = getGameSessionId(nameGameSessions);
        for (User user:newList) {
            Integer idUser = getUserId(user.getUserName(), user.getChatId());

            String deleteSQL = "UPDATE Games SET nameOfRecipient = ? " +
                    "WHERE idGameSession = ? AND idUser = ?";
            try (Connection dbConnection = getDBConnection();
                 PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteSQL)) {

                preparedStatement.setString(1, user.getObjectOfDonation());
                preparedStatement.setInt(2, isGS);
                preparedStatement.setInt(3, idUser);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }



    }


}