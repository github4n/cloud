package com.iot.building.helper;

public enum OnlineStatusEnum {
    CONNECTED("connected", 1), DISCONNECTED("disconnected", 0);

    private String code;
    private int value;

    OnlineStatusEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public static OnlineStatusEnum valueOf(int value) {
        for(OnlineStatusEnum onlineStatusEnum : values()) {
            if (onlineStatusEnum.getValue() == value) {
                return onlineStatusEnum;
            }
        }
        return DISCONNECTED;
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
