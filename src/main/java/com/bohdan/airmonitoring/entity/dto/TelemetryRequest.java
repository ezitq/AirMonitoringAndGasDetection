package com.bohdan.airmonitoring.entity.dto;

import java.time.LocalDateTime;

public class TelemetryRequest {

    private String pairingCode;
    private double temperature;
    private double humidity;
    private double pressure;
    private int gasLevel;
    private String status;
    private LocalDateTime receivedAt;

    public TelemetryRequest() {
    }

    public String getPairingCode() {
        return pairingCode;
    }

    public void setPairingCode(String pairingCode) {
        this.pairingCode = pairingCode;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public int getGasLevel() {
        return gasLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setGasLevel(int gasLevel) {
        this.gasLevel = gasLevel;
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