package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: 告警等级
 */
public enum EVTLVEnum {

    RECORD("0", "记录"),
    REMIND("1", "提醒"),
    ALRAM("2", "告警");

    public String code;
    public String desc;

    EVTLVEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
