package com.bohdan.airmonitoring.service;

import com.bohdan.airmonitoring.entity.TelemetryData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TelemetryService {

    private final AtomicReference<TelemetryData> latestData = new AtomicReference<>(
            new TelemetryData(0.0, 0.0, 0.0, 0, "NO_DATA")
    );

    public TelemetryData saveTelemetry(TelemetryData data) {
        if (data == null) {
            throw new IllegalArgumentException("Telemetry data cannot be null");
        }

        if (data.getStatus() == null || data.getStatus().isBlank()) {
            data.setStatus(data.getGasLevel() > TelemetryData.GAS_THRESHOLD ? "ALARM" : "NORMAL");
        }

        data.setReceivedAt(LocalDateTime.now());
        latestData.set(data);

        return data;
    }

    public TelemetryData getLatestData() {
        return latestData.get();
    }
}