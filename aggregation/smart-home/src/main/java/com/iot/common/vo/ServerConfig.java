package com.iot.common.vo;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：系统配置
 * 创建人： mao2080@sina.com
 * 创建时间：2018/5/11 9:46
 * 修改人： mao2080@sina.com
 * 修改时间：2018/5/11 9:46
 * 修改描述：
 */
public class ServerConfig {

    /**请求时间*/
    private long reqTimestamp;

    /**相应时间*/
    private long resTimestamp;

    /**NTP服务器地址*/
    private String ntpServerUrl;

    /**MQTT服务器地址*/
    private String mqttServerUrl;

    /**MQTT心跳周期*/
    private byte heartbeat;

    private  String httpHeader;

    private String ip;

    public ServerConfig() {

    }

    public ServerConfig(long reqTimestamp) {
        this.reqTimestamp = reqTimestamp;
    }

    public long getReqTimestamp() {
        return reqTimestamp;
    }

    public void setReqTimestamp(long reqTimestamp) {
        this.reqTimestamp = reqTimestamp;
    }

    public long getResTimestamp() {
        return resTimestamp;
    }

    public void setResTimestamp(long resTimestamp) {
        this.resTimestamp = resTimestamp;
    }

    public String getNtpServerUrl() {
        return ntpServerUrl;
    }

    public void setNtpServerUrl(String ntpServerUrl) {
        this.ntpServerUrl = ntpServerUrl;
    }

    public String getMqttServerUrl() {
        return mqttServerUrl;
    }

    public void setMqttServerUrl(String mqttServerUrl) {
        this.mqttServerUrl = mqttServerUrl;
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public byte getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(byte heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
