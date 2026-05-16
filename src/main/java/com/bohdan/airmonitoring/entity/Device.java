package com.bohdan.airmonitoring.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "pairing_code")
    private String pairingCode;

    @Column(name = "paired")
    private boolean paired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TelemetryData> telemetryDataList = new ArrayList<>();

    public Device() {
    }

    public Device(String deviceId, String deviceToken, String pairingCode) {
        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
        this.pairingCode = pairingCode;
        this.paired = false;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getPairingCode() {
        return pairingCode;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setPairingCode(String pairingCode) {
        this.pairingCode = pairingCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<TelemetryData> getTelemetryDataList() {
        return telemetryDataList;
    }

    public void setTelemetryDataList(List<TelemetryData> telemetryDataList) {
        this.telemetryDataList = telemetryDataList;
    }
}
