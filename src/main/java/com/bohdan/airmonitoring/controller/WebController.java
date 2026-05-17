package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.entity.UserRole;
import com.bohdan.airmonitoring.service.DeviceService;
import com.bohdan.airmonitoring.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    private final DeviceService deviceService;


    @Autowired
    public WebController(UserService userService, DeviceService deviceService) {
        this.userService = userService;
        this.deviceService = deviceService;
    }

    @GetMapping("/dashboard")
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

    @PostMapping("/registration")
    public String registerUser(@RequestParam (name = "fullName") String fullName,
                               @RequestParam String email,
                               @RequestParam String confirmPassword,
                               @RequestParam String pairingCode,
                               @RequestParam String password,
                               Model model) {
        System.out.println("=== REGISTER CALLED ===");
        String errorMessage = userService.validateRegistration(email,password,confirmPassword);

        if(!Objects.equals(errorMessage, "Успішно!")){
            model.addAttribute("error", errorMessage);
            return "registration";
        }

        User user = new User(fullName,email,password, UserRole.USER);

        userService.saveUser(user);

        if(deviceService.pairDevice(user,pairingCode) == null){
            model.addAttribute("error", "Невірний код прив'язки пристрою");
            return "registration";
        }

        return "redirect:/login";
    }


    @GetMapping("/settings")
    public String showSettingsPage() {
        return "settings";
    }
}