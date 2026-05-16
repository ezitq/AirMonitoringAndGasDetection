package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.dto.FcmTokenRequest;
import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.UserJpaRepository;
import com.bohdan.airmonitoring.service.FcmTokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;
    private final UserJpaRepository userJpaRepository;

    public FcmTokenController(FcmTokenService fcmTokenService,
                              UserJpaRepository userJpaRepository) {
        this.fcmTokenService = fcmTokenService;
        this.userJpaRepository = userJpaRepository;
    }

    @PostMapping("/token/current-user")
    public ResponseEntity<String> saveTokenForCurrentUser(@RequestBody FcmTokenRequest request,
                                                          HttpSession session) {
        Object userIdObject = session.getAttribute("userId");

        if (userIdObject == null) {
            return ResponseEntity.status(401).body("User is not logged in");
        }

        int userId = (int) userIdObject;

        User user = userJpaRepository.findUserById(userId);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        fcmTokenService.saveToken(request.getToken(), user);

        return ResponseEntity.ok("FCM token saved for current user");
    }
}