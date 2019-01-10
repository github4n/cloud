package com.iot.center.enums;

public enum DeviceRemoteControlTypeEnum {
    OFF("开", "OFF"),
    ON("关", "ON"),
    ONOFF("开/关", "ONOFF"),
    SCENE_SITHCH("场景切换", "SCENE_SITHCH"),
    METTING("开会", "MEETING"),
    DIMMING_ADD("增大", "DIMMING_ADD"),
    DIMMING_SUB("减少", "DIMMING_SUB");

    private String code;
    private String name;

    DeviceRemoteControlTypeEnum(String name, String code) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
