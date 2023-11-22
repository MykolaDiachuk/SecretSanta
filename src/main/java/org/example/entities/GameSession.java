package org.example.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameSession {
  private List<User> users;
  private final User admin;
  private final String nameOfGameSession;

    public GameSession(User admin, String nameOfSession) {
        this.admin = admin;
        this.nameOfGameSession = nameOfSession;
        if (users == null) {
            users = new ArrayList<>();
        }
        GameSessions.add(this);
    }
    public GameSession(User admin, String nameOfSession,List<User> allUsers) {
        this.admin = admin;
        this.nameOfGameSession = nameOfSession;
        this.users = allUsers;
    }
    public String getNameOfGameSession() {
        return this.nameOfGameSession;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getAdmin() {
        return admin;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameSession that)) return false;
        return Objects.equals(users, that.users) && Objects.equals(getAdmin(), that.getAdmin()) && Objects.equals(getNameOfGameSession(), that.getNameOfGameSession());
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, getAdmin(),  getNameOfGameSession());
    }
}
