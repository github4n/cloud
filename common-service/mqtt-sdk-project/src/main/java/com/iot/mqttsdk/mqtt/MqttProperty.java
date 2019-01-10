package com.iot.mqttsdk.mqtt;

import com.iot.mqttsdk.common.CallBackProcessor;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：mqtt配置信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:42
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:42
 */
public class MqttProperty {
	// 主机名(如:192.168.0.1:1883 或者 www.leedarson.com:1883)
	private String host;
	// 客戶端id
	private String clientId;
	// 设置连接的用户名
	private String username;
	// 设置连接的密码
	private String password;

	private String port;

	private CallBackProcessor callBackProcessor;

	public MqttProperty() {
	}

	public MqttProperty(String host, String clientId, String username, String password, String port) {
		this.host = host;
		this.clientId = clientId;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public MqttProperty(String host, String username, String password, String port) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public MqttProperty(String host, String clientId, String username, String password, String port, CallBackProcessor callBackProcessor) {
		this.host = host;
		this.clientId = clientId;
		this.username = username;
		this.password = password;
		this.port = port;
		this.callBackProcessor = callBackProcessor;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
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

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public CallBackProcessor getCallBackProcessor() {
		return callBackProcessor;
	}

	public void setCallBackProcessor(CallBackProcessor callBackProcessor) {
		this.callBackProcessor = callBackProcessor;
	}
}
