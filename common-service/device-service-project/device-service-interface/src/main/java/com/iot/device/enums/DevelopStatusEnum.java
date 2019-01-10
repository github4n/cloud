package com.iot.device.enums;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 21:03 2018/7/2
 * @Modify by:
 */
public enum DevelopStatusEnum {
    IDLE(0, "未开发"),
    OPERATING(1, "开发中"),
    OPERATED(2, "已上线"),
    RELEASE(3,"发布中");
    private int value;
    private String desc;

    DevelopStatusEnum(int value, String desc) {
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
