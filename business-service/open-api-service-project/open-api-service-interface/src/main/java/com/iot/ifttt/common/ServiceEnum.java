package com.iot.ifttt.common;

/**
 * 描述：服务枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/29 10:55
 */
public enum ServiceEnum {

    //trigger
    SCENE("scene"), //情景执行
    SECURITY("security"), //安防报警
    SECURITY_MODE("securityMode"), //安防类型设置触发
    DEVICE_STATUS("deviceStatus"), //设备状态变化
    DEVICE_RANGE("deviceRange"), //设备状态范围判断

    //action
    SET_SECURITY("setSecurity"), //设置安防类型
    CONTROL_DEVICE("controlDevice"); //控制设备

    private String code;

    ServiceEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ServiceEnum getEnum(String code) {
        for (ServiceEnum vo : ServiceEnum.values()) {
            if (vo.code.equals(code)) {
                return vo;
            }
        }
        return null;
    }
}
