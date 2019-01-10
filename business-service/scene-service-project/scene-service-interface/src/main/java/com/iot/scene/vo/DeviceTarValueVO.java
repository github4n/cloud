package com.iot.scene.vo;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：设备名目标值
 * 创建人： wujianlong
 * 创建时间：2017年11月16日 下午3:58:35
 * 修改人： wujianlong
 * 修改时间：2017年11月16日 下午3:58:35
 */
public class DeviceTarValueVO implements Serializable {

	private static final long serialVersionUID = -1887651448393364234L;

	/** 设备id*/
	private String deviceId;

	/** 设备名*/
	private String deviceName;

	/** 目标*/
	private String targetValue;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
