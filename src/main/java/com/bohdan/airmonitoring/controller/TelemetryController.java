package com.bohdan.airmonitoring.controller;

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
    public ResponseEntity<TelemetryData> receiveTelemetry(@RequestBody TelemetryData telemetryData) {
        TelemetryData savedData = telemetryService.saveTelemetry(telemetryData);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/latest")
    public ResponseEntity<TelemetryData> getLatestTelemetry() {
        return ResponseEntity.ok(telemetryService.getLatestData());
    }
}
