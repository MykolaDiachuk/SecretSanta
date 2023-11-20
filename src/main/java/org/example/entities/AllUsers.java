package org.example.entities;

import org.example.entities.User;

import java.util.ArrayList;
import java.util.List;

public class AllUsers {
    public List<User> users;

    public AllUsers() {
        this.users = new ArrayList<>();
    }
    public AllUsers(List<User> users) {
        this.users = users;
    }
    public  void addUsers(User user){
        users.add(user);
    }
    public List<User> getUsers(){
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
