package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/23
 * @Description: *
 */
public enum CommandEnum {

    OPEN("A1", "合闸"),
    CLOSE("A2", "分闸"),
    LEAKAGE_TEST("A3", "漏电测试"),
    RTVI("D1", "修改实时数据上传间隔（时间:秒）"),
    SERVER_SET("D7", "修改服务器指向");

    public String code;
    public String desc;

    CommandEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
