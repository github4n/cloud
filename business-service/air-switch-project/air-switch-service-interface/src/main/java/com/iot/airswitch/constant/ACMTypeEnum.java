package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/16
 * @Description: *
 */
public enum ACMTypeEnum {

    HOUR("F1", "每小时统计数据"),
    DAY("F2", "每天统计数据");

    public String code;
    public String desc;

    ACMTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
