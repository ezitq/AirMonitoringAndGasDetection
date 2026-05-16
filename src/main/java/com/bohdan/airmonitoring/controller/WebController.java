package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.entity.UserRole;
import com.bohdan.airmonitoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class WebController {

    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showMainDashboard() {
        return "dashboard";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam (name = "fullName") String fullName,
                               @RequestParam String email,
                               @RequestParam String confirmPassword,
                               @RequestParam String pairingCode,
                               @RequestParam String password,
                               Model model) {


        String errorMessage = userService.validateRegistration(email,password);

        if(!Objects.equals(errorMessage, "Успішно!")){
            model.addAttribute("error", errorMessage);
            return "registration";
        }

        userService.saveUser(new User(0,fullName,email,password, UserRole.USER));

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        if (userService.validateUser(username, password)) {
            return "dashboard";
        }

        model.addAttribute("error", "Неправильний email або пароль.");
        return "login";
    }
}