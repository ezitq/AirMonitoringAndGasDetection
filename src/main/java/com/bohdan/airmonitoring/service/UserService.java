package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private UserJpaRepository repository;

    @Autowired
    public UserService(UserJpaRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user){
        repository.save(user);
    }

    public User findUserById(int id){
        return repository.findUserById(id);
    }

    public User findUserByEmail(String email){
        return repository.findUserByEmail(email);
    }

    public boolean validateUser(String email, String password){
        User user = repository.findAll().stream().filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);

        return user != null && user.getPassword().equals(password);
    }

    public String validateRegistration(String email, String password, String confirmPassword){

        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            return "Неправильний формат електронної пошти";
        }

        if(password.length() < 8){
            return "Неправильний формат пароля";
        }

        if(!password.equals(confirmPassword)){
            return "Паролі не збігаються";
        }

        if(findUserByEmail(email) != null){
            return "Користувач з такою поштою вже існує";
        }

            return "Успішно!";
    }
}
