package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.FcmToken;
import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.FcmTokenJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FcmTokenService {

    private final FcmTokenJpaRepository fcmTokenJpaRepository;

    public FcmTokenService(FcmTokenJpaRepository fcmTokenJpaRepository) {
        this.fcmTokenJpaRepository = fcmTokenJpaRepository;
    }

    public void saveToken(String token, User user) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("FCM token is empty");
        }

        Optional<FcmToken> existingTokenOptional = fcmTokenJpaRepository.findByToken(token);

        if (existingTokenOptional.isPresent()) {
            FcmToken existingToken = existingTokenOptional.get();
            existingToken.setUser(user);
            fcmTokenJpaRepository.save(existingToken);
        } else {
            FcmToken newToken = new FcmToken();
            newToken.setToken(token);
            newToken.setUser(user);
            fcmTokenJpaRepository.save(newToken);
        }

    }
}