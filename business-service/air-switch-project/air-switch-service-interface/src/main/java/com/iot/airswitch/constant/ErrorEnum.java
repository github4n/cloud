package com.iot.airswitch.constant;

/**
 * @Author: Xieby
 * @Date: 2018/10/24
 * @Description: *
 */
public enum ErrorEnum {

    DEVICE_TYPE("E1", "设备类型错误"),
    NODE("E2", "节点号错误"),
    DEVICE_MODEL("E3", "设备型号错误"),
    DEVICE_BUSY("E4", "设备忙"),
    CACHE_FULL("E5", "缓存满"),
    STORE_OPERATE("E6", "存储操作错误"),
    PARAMETER("E7", "参数错误"),
    CHECK("E8", "检验错误"),
    MESSAGE_HEADER("E9", "消息头错误"),
    MESSAGE_LENGTH("EA", "消息长度错误"),
    OPERATE_FORBID("EB", "操作禁止");

    public String code;
    public String desc;

    ErrorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
