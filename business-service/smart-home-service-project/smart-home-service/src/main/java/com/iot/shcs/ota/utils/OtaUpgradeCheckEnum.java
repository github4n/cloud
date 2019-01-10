package com.iot.shcs.ota.utils;

/**
 * @Author: nongchongwei
 * @Descrpiton:
 * @Date: 13:47 2018/5/22
 * @Modify by:
 */
public enum OtaUpgradeCheckEnum {

    F("F", "强制升级时升级"),

    P("P", "推送升级时升级"),

    FP("FP", "强制与推送都升级");

    private String type;

    private String message;


    OtaUpgradeCheckEnum(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
