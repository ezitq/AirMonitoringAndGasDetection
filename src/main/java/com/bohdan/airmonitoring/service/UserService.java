package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserJpaRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserJpaRepository repository,BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
        );
    }
}
