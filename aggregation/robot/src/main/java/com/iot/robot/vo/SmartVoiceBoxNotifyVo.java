package com.iot.robot.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Map;


public class SmartVoiceBoxNotifyVo implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;
	
	private Map<String, Object> attrMap;
	
	private Integer onlineStatus;
	
	private String deviceId;
	
	private Long userId;

	private String userUUID;

	private String mqttPw;
	
	private String token;

	private Long sceneId;

	// 是否新增
	private Boolean newAdd;

	// 第三方类型
	private int smartType;

	private Long tenantId;
	
	private SmartVoiceBoxNotifyVo() {
		
	}
	
	public static SmartVoiceBoxNotifyVo getInstance() {
		return new SmartVoiceBoxNotifyVo();
	}

	public String getUserUUID() {
		return userUUID;
	}
	
	public String getMqttPw() {
		return mqttPw;
	}

	public SmartVoiceBoxNotifyVo setAttrMap(Map<String, Object> attrMap) {
	    this.attrMap = attrMap;
		return this;
	}
	public SmartVoiceBoxNotifyVo setOnlineStatus(Integer onlineStatus) {
		this.onlineStatus = onlineStatus;
		return this;
	}
	public SmartVoiceBoxNotifyVo setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}
	public SmartVoiceBoxNotifyVo setUserUUID(String userUUID) {
		this.userUUID = userUUID;
		return this;
	}
	
	public SmartVoiceBoxNotifyVo setMqttPw(String mqttPw) {
		this.mqttPw = mqttPw;
		return this;
	}
	
	public SmartVoiceBoxNotifyVo setToken(String token) {
		this.token = token;
		return this;
	}
	public JSONObject build() {
		return (JSONObject) JSONObject.toJSON(this);
	}
	public Map<String, Object> getAttrMap() {
		return attrMap;
	}
	
	public Integer getOnlineStatus() {
		return onlineStatus;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public String getToken() {
		return token;
	}

	public Long getUserId() {
		return userId;
	}

	public SmartVoiceBoxNotifyVo setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public int getSmartType() {
		return smartType;
	}

	public SmartVoiceBoxNotifyVo setSmartType(int smartType) {
		this.smartType = smartType;
		return this;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public SmartVoiceBoxNotifyVo setTenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public Long getSceneId() {
		return sceneId;
	}

	public SmartVoiceBoxNotifyVo setSceneId(Long sceneId) {
		this.sceneId = sceneId;
		return this;
	}

	public Boolean getNewAdd() {
		return (newAdd == null ? false : newAdd);
	}

	public SmartVoiceBoxNotifyVo setNewAdd(Boolean newAdd) {
		this.newAdd = newAdd;
		return this;
	}
}
