package org.example.entities;

import org.example.entities.GameSession;

import java.util.ArrayList;
import java.util.List;

public class GameSessions {

    public static List<GameSession> gameSessions = new ArrayList<>();
    public static void setGameSessions(List<GameSession> gameSessions) {
        GameSessions.gameSessions = gameSessions;
    }

    public static void add(GameSession gs) {
        gameSessions.add(gs);
    }

    public static List<GameSession> get() {
        return gameSessions;
    }
}
