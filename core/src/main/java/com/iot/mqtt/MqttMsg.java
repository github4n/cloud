package com.iot.mqtt;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：常用工具
 * 功能描述：MQTT返回实体
 * 创建人: yuChangXing
 * 创建时间: 2018/3/22 20:02
 * 修改人:
 * 修改时间：
 */
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
     * 返回信息
     */
    private String errMsg = "Success.";


    public MqttMsg() {
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

    @JSONField(name = "errMsg")
    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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

    @Override
    public String toString() {
        return "MqttMsgObj{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", requestId='" + requestId + '\'' +
                ", srcAddr='" + srcAddr + '\'' +
                ", payload=" + payload +
                ", seq=" + seq +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
