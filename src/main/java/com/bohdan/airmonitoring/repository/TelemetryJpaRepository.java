package com.bohdan.airmonitoring.repository;

import com.bohdan.airmonitoring.entity.TelemetryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryJpaRepository extends JpaRepository<TelemetryData, Integer> {

    TelemetryData findFirstByOrderByReceivedAtDesc();
}
