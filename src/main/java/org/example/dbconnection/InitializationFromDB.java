package org.example.dbconnection;

import org.example.entities.*;

import java.util.ArrayList;
import java.util.List;

public class InitializationFromDB {
    public static List<GameSession> getGameSessionslist(){
        List<GameSession> result = new ArrayList<>();
        List<Integer> idGameSessions = DBConnection.getAllGSId();

        for (Integer idGS: idGameSessions) {
            GameSession gs = new GameSession( DBConnection.getUserById(DBConnection.getGameSessionAdmin(idGS)),
                    DBConnection.getGameSessionName(idGS), getUsersInGame(idGS));
            result.add(gs);
        }
        return result;
    }


 public static List<User> getUsersInGame(Integer idgs){
     List<User> result = new ArrayList<>();
     List<Integer> idUsers = DBConnection.getAllUsersId(idgs);

     for (Integer idUser: idUsers) {
         User user = DBConnection.getUserById(idUser);

         user.setObjectOfDonation(DBConnection.getNameOfRecipient(idgs,idUser));
         result.add(user);
     }
     return result;
 }
}
