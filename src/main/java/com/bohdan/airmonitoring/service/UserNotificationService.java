package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.FcmToken;
import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.FcmTokenJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationService {

    private final FcmTokenJpaRepository fcmTokenJpaRepository;
    private final FcmNotificationService fcmNotificationService;

    public UserNotificationService(FcmTokenJpaRepository fcmTokenJpaRepository,
                                   FcmNotificationService fcmNotificationService) {
        this.fcmTokenJpaRepository = fcmTokenJpaRepository;
        this.fcmNotificationService = fcmNotificationService;
    }

    public void sendAlarmToUser(User user, String deviceId, int gasLevel) {
        List<FcmToken> tokens = fcmTokenJpaRepository.findAllByUser(user);

        System.out.println("FCM tokens found for user " + user.getEmail() + ": " + tokens.size());

        for (FcmToken fcmToken : tokens) {
            String response = fcmNotificationService.sendAlarmNotification(
                    fcmToken.getToken(),
                    "Увага! Виявлено задимлення",
                    "Пристрій " + deviceId + " зафіксував рівень газу/диму: " + gasLevel,
                    deviceId
            );

            System.out.println("FCM response: " + response);
        }
    }
}