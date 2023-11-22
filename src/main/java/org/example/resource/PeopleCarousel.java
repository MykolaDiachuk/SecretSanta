package org.example.resource;

import org.example.entities.User;

import java.util.*;

public class PeopleCarousel {

    public static List<User> runCarousel(List<User> users) {

        List<User> originalOrder = new ArrayList<>(users);
        List<User> newList = new ArrayList<>();

        String chainOfNames = null;
        Collections.shuffle(users);

        for (int i = 0; i <= users.size() - 1; i++) {
            User user = users.get(i);
            if (i == 0) {
                user.setObjectOfDonation(users.get(users.size() - 1).getUserName());
                chainOfNames = user.getUserName();
                newList.add(user);
            }else {
                user.setObjectOfDonation(chainOfNames);
                newList.add(user);
                chainOfNames = user.getUserName();
            }

        }
        newList.sort(Comparator.comparingInt(originalOrder::indexOf));
        return newList;
    }

}
