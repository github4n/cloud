package com.iot.mqttsdk.common;

import java.io.Serializable;
import java.util.Map;


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
    
    /**
     * 返回数据
     */
    private Object data;

    public MqttMsgAck() {

    }

    public MqttMsgAck(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public MqttMsgAck(int code, String desc, Object data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
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

    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static MqttMsgAck successAck() {
        return failureAck(SUCCESS, "success");
    }

    public static MqttMsgAck failureAck(int code, String desc) {
        return new MqttMsgAck(code, desc);
    }
    
    public static MqttMsgAck successAck(Map<String, Object> ack) {
        return messageAck((int)ack.get("code"), ack.get("desc").toString(), ack.get("data"));
    }

    public static MqttMsgAck messageAck(int code, String desc, Object data) {
        return new MqttMsgAck(code, desc, data);
    }

    @Override
    public String toString() {
        return "MqttMsgAck{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                '}';
    }
}
