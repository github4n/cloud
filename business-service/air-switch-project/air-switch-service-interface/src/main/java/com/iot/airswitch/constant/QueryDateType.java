package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/11/7
 * @Description: *
 */
public enum QueryDateType {

    DAY("day", "天"),
    MONTH("month", "月"),
    YEAR("year", "年");

    public String code;
    public String desc;

    QueryDateType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
