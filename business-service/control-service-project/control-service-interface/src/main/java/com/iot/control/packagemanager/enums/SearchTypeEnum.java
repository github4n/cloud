package com.iot.control.packagemanager.enums;

/**
 *@description 搜索类型：portal是产品，boss端是设备类型
 *@author wucheng
 *@create 2018/12/12 15:14
 */
public enum  SearchTypeEnum {
    PRODUCT("product"),
    DEVICETYPE("deviceType");

    private String code;

    SearchTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
