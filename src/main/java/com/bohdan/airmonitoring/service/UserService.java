package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository repository = new UserRepository();

    public void saveUser(User user){
        repository.saveUser(user);
    }

    public User findUserById(int id){
        return repository.findUserById(id);
    }

    public User findUserByEmail(String email){
        return repository.findUserByEmail(email);
    }

    public boolean validateUser(String email, String password){
        User user = repository.findAllUsers().stream().filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);

        return user != null && user.getPassword().equals(password);
    }

    public String validateRegistration(String email, String password){

        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            return "Неправильний формат електронної пошти";
        }

        if(password.length() < 8){
            return "Неправильний формат пароля";
        }

        if(findUserByEmail(email) != null){
            return "Користувач з такою поштою вже існує";
        }

            return "Успішно!";
    }
}
