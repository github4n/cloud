package com.iot.control.packagemanager.enums;
/**
 *@description 安全套包策略类型 枚举
 *@author wucheng
 *@create 2018/11/27 8:49
 */
public enum SecurityTypeEnum {

    STAY("stay"),
    SOS("sos"),
    AWAY("away");

    private String type;

    SecurityTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
