package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/11/16
 * @Description: *
 */
public enum AlarmTypeEnum {

    NOTICE(0, "通知"),
    EARLY_WARNING(1, "预警"),
    ALARM(2, "告警");

    public Integer type;
    public String desc;

    AlarmTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
