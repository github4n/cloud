package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/23
 * @Description: *
 */
public enum OpenCloseEnum {

    CLOSE("0", "关"),
    OPEN("1", "开");

    public String code;
    public String desc;

    OpenCloseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
