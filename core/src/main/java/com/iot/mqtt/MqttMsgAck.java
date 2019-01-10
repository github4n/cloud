package com.iot.mqtt;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/10 10:55
 * 修改人:
 * 修改时间：
 */
public class MqttMsgAck implements Serializable {
    /**
     * 序列
     */
    private static final long serialVersionUID = -8965296513558252959L;

    public static final int SUCCESS = 200;

    public static final int BUSINESS_ERROR = 400;

    public static final int ERROR = 500;


    /**
     * 返回代码
     */
    private int code = 200;

    /**
     * 返回描述
     */
    private String desc;

    public MqttMsgAck() {

    }

    public MqttMsgAck(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static MqttMsgAck successAck() {
        return failureAck(SUCCESS, "success");
    }

    public static MqttMsgAck failureAck(int code, String desc) {
        return new MqttMsgAck(code, desc);
    }
}
