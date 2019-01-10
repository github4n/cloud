package com.iot.smarthome.constant;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/8/29 11:17
 * @Modify by:
 */
public enum DeviceOnlineStatusEnum {
    CONNECTED("connected", 1), DISCONNECTED("disconnected", 0);

    private String code;
    private int value;

    DeviceOnlineStatusEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public static DeviceOnlineStatusEnum valueOf(int value) {
        for(DeviceOnlineStatusEnum onlineStatusEnum : values()) {
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
