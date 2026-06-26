package com.bohdan.airmonitoring.controller;

import com.bohdan.airmonitoring.entity.User;
import com.bohdan.airmonitoring.entity.dto.TelemetryRequest;
import com.bohdan.airmonitoring.entity.dto.TelemetryResponse;
import com.bohdan.airmonitoring.entity.TelemetryData;
import com.bohdan.airmonitoring.repository.UserJpaRepository;
import com.bohdan.airmonitoring.service.TelemetryService;
import com.bohdan.airmonitoring.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    private final TelemetryService telemetryService;
    private volatile TelemetryRequest liveData;
    private final UserService userService;

    public TelemetryController(TelemetryService telemetryService, UserService userService) {
        this.telemetryService = telemetryService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> receiveTelemetry(
            @RequestHeader("X-Device-Token") String pairingCode,
            @RequestBody TelemetryRequest request
    ) {
        try {
            this.liveData = request;
            TelemetryData savedTelemetry = telemetryService.saveTelemetry(request, pairingCode);
            return ResponseEntity.ok(new TelemetryResponse(savedTelemetry));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<TelemetryData> getTelemetry(
            @RequestParam int deviceId,
            @RequestParam int limit
    ) {
        return telemetryService.getTelemetry(deviceId, limit).getContent();
    }

    // В TelemetryController — новий endpoint для поточного юзера
    @GetMapping("/my")
    public ResponseEntity<?> getMyTelemetry(
            @RequestParam(defaultValue = "20") int limit,
            java.security.Principal principal
    ) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        User user = userService.findUserByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        try {
            List<TelemetryData> data = telemetryService.getTelemetryForUser(user, limit).getContent();
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/live")
    public ResponseEntity<?> receiveLive(
            @RequestHeader("X-Device-Token") String token,
            @RequestBody TelemetryRequest request) {
        request.setReceivedAt(LocalDateTime.now()); // ← додати
        this.liveData = request;
//        TelemetryData telemetryData = new TelemetryData()
        System.out.println(liveData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/live")
    public ResponseEntity<?> getLive() {
        if (liveData == null) return ResponseEntity.noContent().build();
        System.out.println(liveData);
        return ResponseEntity.ok(liveData);
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