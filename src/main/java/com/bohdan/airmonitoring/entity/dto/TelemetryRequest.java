package com.bohdan.airmonitoring.entity.dto;

public class TelemetryRequest {

    private String deviceId;
    private double temperature;
    private double humidity;
    private double pressure;
    private int gasLevel;
    private String status;

    public TelemetryRequest() {
    }

    public String getDeviceId() {
        return deviceId;
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

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
}