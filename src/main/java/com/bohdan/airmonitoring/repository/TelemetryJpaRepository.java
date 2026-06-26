package com.bohdan.airmonitoring.repository;

import com.bohdan.airmonitoring.entity.Device;
import com.bohdan.airmonitoring.entity.TelemetryData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TelemetryJpaRepository extends JpaRepository<TelemetryData, Integer> {

    TelemetryData findFirstByOrderByIdDesc();

    List<TelemetryData> findAllByDeviceOrderByReceivedAtDesc(Device device);

    // Пошук останнього запису по device.deviceId (String)
    @Query("SELECT t FROM TelemetryData t WHERE t.device.deviceId = :deviceId ORDER BY t.receivedAt DESC LIMIT 1")
    Optional<TelemetryData> findLatestByDeviceStringId(@Param("deviceId") String deviceId);

    // Пагінація по device.id (int — первинний ключ)
    @Query("SELECT t FROM TelemetryData t WHERE t.device.id = :deviceId ORDER BY t.receivedAt DESC")
    Page<TelemetryData> findPageByDeviceId(@Param("deviceId") int deviceId, Pageable pageable);
}
