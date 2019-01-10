package com.iot.shcs.helper;

import com.iot.mqttsdk.mqtt.MqttProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = MqttPropertyHelper.MQTT_PREFIX)
public class MqttPropertyHelper {

	public static final String MQTT_PREFIX = "mqtt";
	private String host;
	private String port;
	private String username;
	private String password;
	private Integer qos;
	private String clientBroadcast;
	private String clientTopic;
	private String clientId;
	private String type;
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
	public Integer getQos() {
		return qos;
	}
	public void setQos(Integer qos) {
		this.qos = qos;
	}
	public String getClientBroadcast() {
		return clientBroadcast;
	}
	public void setClientBroadcast(String clientBroadcast) {
		this.clientBroadcast = clientBroadcast;
	}
	public String getClientTopic() {
		return clientTopic;
	}
	public void setClientTopic(String clientTopic) {
		this.clientTopic = clientTopic;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static String getMqttPrefix() {
		return MQTT_PREFIX;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public MqttProperty buildPros(){
		MqttProperty mqttProperty = new MqttProperty(host, clientId, username, password, port);
		return mqttProperty;
	}
}
