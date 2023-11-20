package org.example.entities;

import java.util.Objects;

public class GameSession {
  private AllUsers users;
  private final User admin;
  private final String nameOfGameSession;

    public GameSession(Admin admin, String nameOfSession) {
        this.admin = admin;
        this.nameOfGameSession = nameOfSession;
        if (users == null) {
            users = new AllUsers();
        }
        GameSessions.add(this);
    }
    public GameSession(User admin, String nameOfSession,AllUsers allUsers) {
        this.admin = admin;
        this.nameOfGameSession = nameOfSession;
        this.users = allUsers;
    }
    public String getNameOfGameSession() {
        return this.nameOfGameSession;
    }

    public AllUsers getAllUsers() {
        return users;
    }

    public User getAdmin() {
        return admin;
    }

    public void setUsers(AllUsers users) {
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
