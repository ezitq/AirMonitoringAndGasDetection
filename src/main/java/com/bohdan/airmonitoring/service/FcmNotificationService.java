package com.bohdan.airmonitoring.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmNotificationService {

    public String sendAlarmNotification(String token,
                                        String title,
                                        String body,
                                        String deviceId) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("deviceId", deviceId)
                    .putData("type", "DANGER")
                    .build();

            return FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to send FCM notification", e);
        }
    }
}