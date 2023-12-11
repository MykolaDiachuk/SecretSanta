package org.example.resource;

import org.example.entities.User;

import java.util.*;

public class PeopleCarousel {

    public static List<User> runCarousel(List<User> users) {
        List<User> originalOrder = new ArrayList<>(users);
        List<User> newList = new ArrayList<>();

        Collections.shuffle(users, new Random());

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (i == users.size()-1){
                user.setObjectOfDonation(users.get(0).getUserName());
            }else user.setObjectOfDonation(users.get(i+1).getUserName());
            newList.add(user);
        }
        newList.sort(Comparator.comparingInt(originalOrder::indexOf));
        return newList;
    }

}
