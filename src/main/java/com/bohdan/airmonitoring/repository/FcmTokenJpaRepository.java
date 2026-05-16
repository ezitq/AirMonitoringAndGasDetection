package com.bohdan.airmonitoring.repository;

import com.bohdan.airmonitoring.entity.FcmToken;
import com.bohdan.airmonitoring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenJpaRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByToken(String token);

    List<FcmToken> findAllByUser(User user);
}
