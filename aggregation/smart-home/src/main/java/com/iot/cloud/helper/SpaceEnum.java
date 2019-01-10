package com.iot.cloud.helper;

public enum SpaceEnum {

    BUILD("BUILD", 1), HOME("HOME", 2), ROOM("ROOM", 3), FLOOR("FLOOR", 4), GROUP("GROUP", 5);

    private String code;

    private int value;

    SpaceEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

}
