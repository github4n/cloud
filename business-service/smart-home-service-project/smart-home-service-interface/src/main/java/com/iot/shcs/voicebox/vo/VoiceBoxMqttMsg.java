package com.iot.shcs.voicebox.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 *  音箱发出的mqttMsg 封装
 */
public class VoiceBoxMqttMsg implements Serializable {

    /**
     * 序列
     */
    private static final long serialVersionUID = -8965296513558252957L;

    /**
     * 消息内容
     */
    private String mqttMsgContent;
    /**
     * 用户uuid
     */
    private String userUuid;

    public VoiceBoxMqttMsg() {

    }

    public VoiceBoxMqttMsg(String mqttMsgContent, String userUuid) {
        this.mqttMsgContent = mqttMsgContent;
        this.userUuid = userUuid;
    }


    public String getMqttMsgContent() {
        return mqttMsgContent;
    }

    public void setMqttMsgContent(String mqttMsgContent) {
        this.mqttMsgContent = mqttMsgContent;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
}
