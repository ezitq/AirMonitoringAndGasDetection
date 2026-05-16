package com.bohdan.airmonitoring.entity;

public class Device {

    private final String deviceID;
    private final String deviceToken;
    private final int pairingCode;
    private int ownerUserId;
    private boolean paired;

    public Device(int pairingCode, String deviceToken, String deviceID) {
        this.pairingCode = pairingCode;
        this.deviceToken = deviceToken;
        this.deviceID = deviceID;
    }

    public Device(String deviceID, String deviceToken, int pairingCode, int ownerUserId, boolean paired) {
        this.deviceID = deviceID;
        this.deviceToken = deviceToken;
        this.pairingCode = pairingCode;
        this.ownerUserId = ownerUserId;
        this.paired = paired;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public int getPairingCode() {
        return pairingCode;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }
}
