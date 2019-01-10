package com.iot.shcs.ota.vo;

public class OtaDeadInfo {
    private String devId;

    private String ver;

    public OtaDeadInfo() {
    }

    public OtaDeadInfo(String devId, String ver) {
        this.devId = devId;
        this.ver = ver;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
