package com.iot.tenant.enums;

/**
 * 项目名称：cloud
 * 功能描述：回复消息类型
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:58
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:58
 * 修改描述：
 */
public enum ReplyMessageType {

    FEEDBACK("feedback","反馈"),
    REPLY("reply","回复");


    private String value;

    private String desc;

    ReplyMessageType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
