package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.dto.TelemetryRequest;
import com.bohdan.airmonitoring.entity.Device;
import com.bohdan.airmonitoring.entity.TelemetryData;
import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.DeviceJpaRepository;
import com.bohdan.airmonitoring.repository.TelemetryJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TelemetryService {

    private final DeviceJpaRepository deviceJpaRepository;
    private final TelemetryJpaRepository telemetryJpaRepository;
    private final UserNotificationService userNotificationService;

    public TelemetryService(DeviceJpaRepository deviceJpaRepository,
                            TelemetryJpaRepository telemetryJpaRepository,
                            UserNotificationService userNotificationService) {
        this.deviceJpaRepository = deviceJpaRepository;
        this.telemetryJpaRepository = telemetryJpaRepository;
        this.userNotificationService = userNotificationService;
    }

    @Transactional
    public TelemetryData saveTelemetry(TelemetryRequest request, String pairingCode) {
        Device device = deviceJpaRepository.findDeviceByPairingCode(
                pairingCode
        );

        if (device == null) {
            throw new IllegalArgumentException("Invalid deviceId or deviceToken");
        }

        TelemetryData telemetryData = new TelemetryData();
        telemetryData.setDeviceId(device);
        telemetryData.setTemperature(request.getTemperature());
        telemetryData.setHumidity(request.getHumidity());
        telemetryData.setPressure(request.getPressure());
        telemetryData.setGasLevel(request.getGasLevel());
        telemetryData.setReceivedAt(LocalDateTime.now());

        String status = request.getStatus();

        if (status == null || status.isBlank()) {
            status = request.getGasLevel() > TelemetryData.GAS_THRESHOLD ? "ALARM" : "NORMAL";
        }

        telemetryData.setStatus(status);

        TelemetryData savedTelemetry = telemetryJpaRepository.save(telemetryData);

        boolean isAlarm = "ALARM".equalsIgnoreCase(status)
                || request.getGasLevel() > TelemetryData.GAS_THRESHOLD;

        if (isAlarm) {
            System.out.println("ALARM detected from device: " + device.getDeviceId());

            User owner = device.getOwner();

            if (owner == null) {
                System.out.println("Device owner is null. Notification was not sent.");
            } else {
                userNotificationService.sendAlarmToUser(
                        owner,
                        device.getDeviceId(),
                        request.getGasLevel()
                );
            }
        }

        return savedTelemetry;
    }

    public TelemetryData getLatestTelemetry() {
        return telemetryJpaRepository.findFirstByOrderByIdDesc();
    }
}