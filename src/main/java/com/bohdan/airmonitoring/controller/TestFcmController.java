package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.FcmToken;
import com.bohdan.airmonitoring.repository.FcmTokenJpaRepository;
import com.bohdan.airmonitoring.service.FcmNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-fcm")
public class TestFcmController {

    private final FcmTokenJpaRepository fcmTokenJpaRepository;
    private final FcmNotificationService fcmNotificationService;

    public TestFcmController(FcmTokenJpaRepository fcmTokenJpaRepository,
                             FcmNotificationService fcmNotificationService) {
        this.fcmTokenJpaRepository = fcmTokenJpaRepository;
        this.fcmNotificationService = fcmNotificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendTestNotification() {
        FcmToken token = fcmTokenJpaRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No FCM tokens saved"));

        String response = fcmNotificationService.sendAlarmNotification(
                token.getToken(),
                "Тестове сповіщення",
                "FCM успішно інтегровано у Spring Boot застосунок",
                "TEST-DEVICE"
        );

        return ResponseEntity.ok(response);
    }
}