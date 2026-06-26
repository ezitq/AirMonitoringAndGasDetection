package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.dto.TelemetryRequest;
import com.bohdan.airmonitoring.entity.Device;
import com.bohdan.airmonitoring.entity.TelemetryData;
import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.repository.DeviceJpaRepository;
import com.bohdan.airmonitoring.repository.TelemetryJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelemetryService {

    private final DeviceJpaRepository deviceJpaRepository;
    private final TelemetryJpaRepository telemetryJpaRepository;
    private final UserNotificationService userNotificationService;

    // Кеш для Live-даних (швидка віддача на фронтенд без звернення до БД)
    private volatile TelemetryData latestTelemetryCache = null;

    // Внутрішній клас для збереження таймерів кожного пристрою
    private static class DeviceState {
        boolean wasInDanger = false;
        long lastAlarmDbSaveTime = 0;
        long lastAlarmNotificationTime = 0;
    }

    private final Map<Long, DeviceState> deviceStates = new ConcurrentHashMap<>();

    public TelemetryService(DeviceJpaRepository deviceJpaRepository,
                            TelemetryJpaRepository telemetryJpaRepository,
                            UserNotificationService userNotificationService) {
        this.deviceJpaRepository = deviceJpaRepository;
        this.telemetryJpaRepository = telemetryJpaRepository;
        this.userNotificationService = userNotificationService;
    }

    @Transactional
    public TelemetryData saveTelemetry(TelemetryRequest request, String pairingCode) {
        Device device = deviceJpaRepository.findDeviceByPairingCode(pairingCode);

        if (device == null) {
            throw new IllegalArgumentException("Invalid device pairing code");
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
            status = request.getGasLevel() > TelemetryData.GAS_THRESHOLD ? "DANGER" : "NORMAL";
        }
        telemetryData.setStatus(status);

        latestTelemetryCache = telemetryData;

        DeviceState state = deviceStates.computeIfAbsent((long) device.getId(), k -> new DeviceState());

        boolean isDanger = "DANGER".equals(status) || "WARNING".equals(status);
        long currentTime = System.currentTimeMillis();
        TelemetryData savedTelemetry = telemetryData;

        // 4. Логіка фільтрації записів у БД та сповіщень
        if (isDanger) {
            if (!state.wasInDanger) {
                // ТРИВОГА ПОЧАЛАСЯ: Миттєвий запис у БД + сповіщення
                savedTelemetry = telemetryJpaRepository.save(telemetryData);
                sendAlarmNotification(device, request.getGasLevel());

                state.lastAlarmDbSaveTime = currentTime;
                state.lastAlarmNotificationTime = currentTime;
                state.wasInDanger = true;
            } else {
                // ТРИВОГА ТРИВАЄ: Перевіряємо інтервали (5000 мс = 5 секунд)
                if (currentTime - state.lastAlarmDbSaveTime >= 5000) {
                    savedTelemetry = telemetryJpaRepository.save(telemetryData);
                    state.lastAlarmDbSaveTime = currentTime;
                }

                if (currentTime - state.lastAlarmNotificationTime >= 5000) {
                    sendAlarmNotification(device, request.getGasLevel());
                    state.lastAlarmNotificationTime = currentTime;
                }
            }
        } else {
            if (state.wasInDanger) {
                // ТРИВОГА МИНУЛА: Миттєвий запис + повідомлення про безпеку
                savedTelemetry = telemetryJpaRepository.save(telemetryData);
                sendSafeNotification(device);
                state.wasInDanger = false;
            } else {
                // ЗВИЧАЙНИЙ РЕЖИМ: Зберігаємо планові дані (наприклад, ті, що приходять раз на хвилину)
                savedTelemetry = telemetryJpaRepository.save(telemetryData);
            }
        }

        return savedTelemetry;
    }

    private void sendAlarmNotification(Device device, int gasLevel) {
        User owner = device.getOwner();
        if (owner != null) {
            System.out.println("DANGER detected from device: " + device.getId() + ". Sending notification...");
            userNotificationService.sendAlarmToUser(owner, device.getDeviceId(), gasLevel);
        } else {
            System.out.println("Device owner is null. Alarm notification not sent.");
        }
    }

    private void sendSafeNotification(Device device) {
        User owner = device.getOwner();
        if (owner != null) {
            System.out.println("SAFE status restored for device: " + device.getId() + ". Sending clear notification...");

             userNotificationService.sendSafeToUser(owner, device.getDeviceId());
        }
    }

    public Page<TelemetryData> getTelemetryForUser(User user, int limit) {
        Device device = deviceJpaRepository.findDeviceByOwner(user);
        if (device == null) {
            throw new IllegalArgumentException("No device found for this user");
        }
        Pageable pageable = PageRequest.of(0, limit, Sort.by("receivedAt").descending());
        return telemetryJpaRepository.findPageByDeviceId(device.getId(), pageable);
    }

    public TelemetryData getLatestTelemetry() {
        // Віддаємо миттєві дані з кешу (навіть якщо вони відфільтрувалися і не пішли в БД)
        if (latestTelemetryCache != null) {
            return latestTelemetryCache;
        }
        // Якщо сервер щойно перезапустили і кеш порожній — дістаємо останній запис із бази
        return telemetryJpaRepository.findFirstByOrderByIdDesc();
    }

    public Page<TelemetryData> getTelemetry(int deviceId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("receivedAt").descending());
        return telemetryJpaRepository.findPageByDeviceId(deviceId, pageable);
    }
}