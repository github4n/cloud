package com.iot.user.util;

public class OAuthIssuer {
    private final MD5Generator vg;

    public OAuthIssuer() {
        this.vg = new MD5Generator();
    }

    public String accessToken() {
        return this.vg.generateValue();
    }

    public String refreshToken() {
        return this.vg.generateValue();
    }

    public String authorizationCode() {
        return this.vg.generateValue();
    }
}
