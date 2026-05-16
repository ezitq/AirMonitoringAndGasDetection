package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.UserJpaRepository;
import com.bohdan.airmonitoring.service.UserNotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-fcm")
public class TestFcmController {

    private final UserJpaRepository userJpaRepository;
    private final UserNotificationService userNotificationService;

    public TestFcmController(UserJpaRepository userJpaRepository,
                             UserNotificationService userNotificationService) {
        this.userJpaRepository = userJpaRepository;
        this.userNotificationService = userNotificationService;
    }

    @PostMapping("/send-current-user")
    public ResponseEntity<String> sendTestToCurrentUser(HttpSession session) {
        Object userIdObject = session.getAttribute("userId");

        if (userIdObject == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }

        int userId = (int) userIdObject;

        User user = userJpaRepository.findUserById(userId);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        userNotificationService.sendAlarmToUser(
                user,
                "TEST-DEVICE",
                999
        );

        return ResponseEntity.ok("Test notification sent");
    }
}