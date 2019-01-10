package com.iot.building.helper;

public enum OnlineTypeEnum {
    USER("user", 0), DEVICE("device", 1);

    private String code;
    private int value;

    OnlineTypeEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
