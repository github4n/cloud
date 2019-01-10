package com.iot.control.device.vo;

import java.io.Serializable;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/7/25 16:35
 * 修改人:
 * 修改时间：
 */
public class DevBindNotifVo implements Serializable {

    private static final long serialVersionUID = 2025580783894328456L;


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
     * 消息体
     */
    private Object payload;

    /**
     * ack回应内容
     */
    private Ack ack;

    /**
     * 主题
     */
    private String topic;


    public DevBindNotifVo() {
    }

    public DevBindNotifVo(String service, String method, Object payload) {
        this.service = service;
        this.method = method;
        this.payload = payload;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public Ack getAck() {
        return ack;
    }

    public void setAck(Ack ack) {
        this.ack = ack;
    }

    public void setAck(int code, String desc) {
        this.ack = new Ack(code, desc);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    class Ack implements Serializable {
        /**
         * 序列
         */
        private static final long serialVersionUID = -8965296513558252959L;

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

        public Ack() {

        }

        public Ack(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Ack(int code, String desc, Object data) {
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
    }
}
