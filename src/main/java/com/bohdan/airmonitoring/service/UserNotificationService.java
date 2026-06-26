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

    // Метод для тривоги (викликається з TelemetryService)
    public void sendAlarmToUser(User user, String deviceId, int gasLevel) {
        List<FcmToken> tokens = fcmTokenJpaRepository.findAllByUser(user);

        String title = "🚨 Увага! Виявлено задимлення";
        String body = "Пристрій " + deviceId + " зафіксував рівень газу/диму: " + gasLevel;

        for (FcmToken fcmToken : tokens) {
            String response = fcmNotificationService.sendAlarmNotification(
                    fcmToken.getToken(), title, body, deviceId
            );
            System.out.println("FCM Alarm response for token " + fcmToken.getId() + ": " + response);
        }
    }

    public void sendSafeToUser(User user, String deviceId) {
        List<FcmToken> tokens = fcmTokenJpaRepository.findAllByUser(user);

        String title = "✅ Безпека";
        String body = "Пристрій " + deviceId + " повідомляє, що повітря чисте. Тривога минула.";

        for (FcmToken fcmToken : tokens) {
            String response = fcmNotificationService.sendAlarmNotification( // Якщо у fcmNotificationService є окремий метод для звичайних повідомлень, краще використати його
                    fcmToken.getToken(), title, body, deviceId
            );
            System.out.println("FCM Safe response for token " + fcmToken.getId() + ": " + response);
        }
    }
}