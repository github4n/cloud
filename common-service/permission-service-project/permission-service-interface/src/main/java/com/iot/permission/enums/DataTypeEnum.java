package com.iot.permission.enums;

/**
 * @Author: Xieby
 * @Date: 2018/10/8
 * @Description: *
 */
public enum DataTypeEnum {

    PROJECT(1, "Project"),
    BUILD(2, "Build"),
    FLOOR(3, "Floor"),
    ROOM(4, "Room");

    DataTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
