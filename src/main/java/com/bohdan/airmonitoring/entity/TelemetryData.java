package com.bohdan.airmonitoring.entity;

import java.time.LocalDateTime;

public class TelemetryData {

    private int id;
    private int deviceId;
    private double temperature;
    private double humidity;
    private double pressure;
    private int gasLevel;
    private String status;
    private LocalDateTime receivedAt;
    public static final int GAS_THRESHOLD = 650;

    public TelemetryData() {
    }

    public TelemetryData(double temperature, double humidity, double pressure, int gasLevel, String status) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.gasLevel = gasLevel;
        this.status = status;
        this.receivedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public void setGasLevel(int gasLevel) {
        this.gasLevel = gasLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }
}

