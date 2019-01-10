package com.iot.center.enums;

public enum DeviceRemoteControlParamEnum {
    GROUP("组", "GROUP"),
    SCENE("场景", "SCENE"),
    NULL("空", "NULL");

    private String code;
    private String name;

    DeviceRemoteControlParamEnum(String name, String code) {
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
