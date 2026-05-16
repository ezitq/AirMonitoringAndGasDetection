package com.bohdan.airmonitoring.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetries")
public class TelemetryData {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device", nullable = false)
    private Device device;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "pressure")
    private double pressure;

    @Column(name = "gas_level")
    private int gasLevel;

    @Column(name = "status")
    private String status;

    @Column(name = "receivedAt")
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

    public Device getDeviceId() {
        return device;
    }

    public void setDeviceId(Device device) {
        this.device = device;
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

