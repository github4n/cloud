package com.iot.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目名称: IOT云平台
 * 模块名称：常用工具
 * 功能描述：Mqtt 属性信息
 * 创建人: yuChangXing
 * 创建时间: 2018/3/22 21:00
 * 修改人:
 * 修改时间：
 */
@Component
@ConfigurationProperties(prefix = MqttProperties.PREFIX)
public class MqttProperties {
    public static final String PREFIX = "mqtt";

    // 主机名(如:192.168.0.1:1883 或者 www.leedarson.com:1883)
    private String host;
    // 设置连接的用户名
    private String username;
    // 设置连接的密码
    private String password;
    // 客戶端id
    private String clientId;

    // 传输消息等级
    // level 0：最多一次的传输
    // level 1：至少一次的传输
    // level 2： 只有一次的传输
    private int qos = 1;

    // 设置超时时间 单位为秒
    private int connectionTimeout = 20;
    // 设置会话心跳时间,单位为秒.服务器会每隔 1.5*keepAliveInterval 秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
    private int keepAliveInterval = 20;
    // 是否清空session,false:表示服务器会保留客户端的连接记录,true:表示每次连接到服务器都以新的身份连接
    private boolean cleanSession = false;
    // 是否自动重新连接
    private boolean automaticReconnect = false;
    // 是否緩存 MqttClient
    private boolean cacheMqttClient = true;

    // ------------------------------
    // 消息主题
    private String clientTopic;
    // 广播的消息主题
    private String clientBroadcast;

    private String type;

    public MqttProperties() {

    }

    public MqttProperties(String host, String clientId, String username, String password) {
        this.host = host;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientTopic() {
        return clientTopic;
    }

    public void setClientTopic(String clientTopic) {
        this.clientTopic = clientTopic;
    }

    public String getClientBroadcast() {
        return clientBroadcast;
    }

    public void setClientBroadcast(String clientBroadcast) {
        this.clientBroadcast = clientBroadcast;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isCacheMqttClient() {
        return cacheMqttClient;
    }

    public void setCacheMqttClient(boolean cacheMqttClient) {
        this.cacheMqttClient = cacheMqttClient;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }
}
