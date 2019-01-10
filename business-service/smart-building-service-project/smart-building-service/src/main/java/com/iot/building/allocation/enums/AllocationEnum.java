package com.iot.building.allocation.enums;

/**
 * @Author: Xieby
 * @Date: 2018/9/12
 * @Description: *
 */
public enum AllocationEnum {

    SCENE("SCENE", 1), OTA("OTA", 2), REMOTE("REMOTE", 3), SYNC("SYNC", 4),IFTTT("IFTTT",5),
    AIR_SWITCH_EVENT("AIR_SWITCH_EVENT",6),AIR_SWITCH_REPORT("AIR_SWITCH_REPORT",7),
    GOURP("GOURP",8);

    private String code;
    private int value;

    AllocationEnum(String code, int value) {
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
