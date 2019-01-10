package com.iot.mqttploy.entity;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PloyInfo {

    /**MQTTclientId*/
    private String clientId;

    /**MQTT-UserName*/
    private String uuid;

    /**MQTT登录密码*/
    private String password;

    public PloyInfo() {

    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
