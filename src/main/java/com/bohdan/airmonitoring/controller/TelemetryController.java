package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.dto.TelemetryRequest;
import com.bohdan.airmonitoring.entity.dto.TelemetryResponse;
import com.bohdan.airmonitoring.entity.TelemetryData;
import com.bohdan.airmonitoring.service.TelemetryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @PostMapping
    public ResponseEntity<?> receiveTelemetry(
            @RequestHeader("X-Device-Token") String deviceToken,
            @RequestBody TelemetryRequest request
    ) {
        try {
            TelemetryData savedTelemetry = telemetryService.saveTelemetry(request, deviceToken);
            return ResponseEntity.ok(new TelemetryResponse(savedTelemetry));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestTelemetry() {
        TelemetryData latestTelemetry = telemetryService.getLatestTelemetry();

        if (latestTelemetry == null) {
            TelemetryResponse emptyResponse = new TelemetryResponse();
            emptyResponse.setTemperature(0.0);
            emptyResponse.setHumidity(0.0);
            emptyResponse.setPressure(0.0);
            emptyResponse.setGasLevel(0);
            emptyResponse.setStatus("NO_DATA");
            return ResponseEntity.ok(emptyResponse);
        }

        return ResponseEntity.ok(new TelemetryResponse(latestTelemetry));
    }
}