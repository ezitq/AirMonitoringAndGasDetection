package com.bohdan.airmonitoring.entity.dto;

public class FcmTokenRequest {

    private String email;
    private String token;

    public FcmTokenRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }
}