package com.iot.control.packagemanager.enums;

/**
 * @description： 套包类型枚举
 * @author：wucheng
 * @create 2018-11-20 21:39
 */
public enum PackageTypeEnum {
    AUTOMATION(1, "自动化类型"),
    SECURITY(2, "安防类型");

    private int value;
    private String desc;

    PackageTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
