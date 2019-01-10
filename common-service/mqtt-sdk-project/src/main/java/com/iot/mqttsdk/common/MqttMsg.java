package com.iot.mqttsdk.common;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;


public class MqttMsg implements Serializable {

    /**
     * 序列
     */
    private static final long serialVersionUID = -8965296513558252958L;

    /**
     * 服务名称
     */
    private String service;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 会话Id
     */
    private String seq;

    /**
     * 源地址(发送端)
     */
    private String srcAddr;

    /**
     * 请求标识
     */
    private String requestId;

    /**
     * 消息体
     */
    private Object payload;

    /**
     * ack回应内容
     */
    private MqttMsgAck ack;

    /**
     * 主题
     */
    @JSONField(serialize = false)
    private String topic;
    /**
     * clientId
     */
    private String clientId;



    public MqttMsg() {

    }

    public MqttMsg(Object payload) {
        this.payload = payload;
    }

    public MqttMsg(String service, String method, Object payload) {
        this.service = service;
        this.method = method;
        this.payload = payload;
    }

    @JSONField(name = "service")
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @JSONField(name = "method")
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @JSONField(name = "requestId")
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JSONField(name = "payload")
    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @JSONField(name = "seq")
    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @JSONField(name = "srcAddr")
    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    @JSONField(name = "ack")
    public MqttMsgAck getAck() {
        return ack;
    }

    public void setAck(MqttMsgAck ack) {
        this.ack = ack;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "MqttMsg{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", seq='" + seq + '\'' +
                ", srcAddr='" + srcAddr + '\'' +
                ", requestId='" + requestId + '\'' +
                ", payload=" + payload +
                ", ack=" + ack +
                ", topic='" + topic + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
