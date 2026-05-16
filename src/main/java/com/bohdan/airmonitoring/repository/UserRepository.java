package com.bohdan.airmonitoring.repository;

import com.bohdan.airmonitoring.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public void saveUser(User user){

        if(!users.contains(user)){
            users.add(user);
        }
    }

    public List<User> findAllUsers() {
        return users;
    }

    public User findUserById(int id){

        return users.get(id);
    }

    public User findUserByEmail(String email){

        return users.stream()
                .filter(usr -> usr.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
