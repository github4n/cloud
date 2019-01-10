package com.iot.ifttt.common;

/**
 * 描述：ifttt启动状态枚举
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/29 14:15
 */
public enum IftttStatusEnum {
    OFF("off", 1), ON("on", 0);

    private String code;
    private int value;

    IftttStatusEnum(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }
}
