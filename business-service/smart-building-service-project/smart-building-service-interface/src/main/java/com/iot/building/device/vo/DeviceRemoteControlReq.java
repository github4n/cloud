package com.iot.building.device.vo;

import java.io.Serializable;

public class DeviceRemoteControlReq implements Serializable {
	private static final long serialVersionUID = -3988806824488340215L;
	private Long id;
	private String deviceName;
	private Long deviceTypeId;
	private String keyCode;
	private String type;
	private String defaultValue;
	private String event;


	private Long businessTypeId;//业务类型ID
	private String function;//功能
	private String deviceId;//设备ID
	private Integer press;// 按键类型   1短按  2长按
	private Long remoteId;// 遥控器模板id  device_remote_template id
	private Long controlTemplateId;// 遥控器模板详情id  device_remote_control_template id
	
	private String scenes;
	
	public String getScenes() {
		return scenes;
	}

	public void setScenes(String scenes) {
		this.scenes = scenes;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getPress() {
		return press;
	}

	public void setPress(Integer press) {
		this.press = press;
	}

	public Long getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(Long remoteId) {
		this.remoteId = remoteId;
	}

	public Long getControlTemplateId() {
		return controlTemplateId;
	}

	public void setControlTemplateId(Long controlTemplateId) {
		this.controlTemplateId = controlTemplateId;
	}
}