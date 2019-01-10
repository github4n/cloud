package com.iot.scene.domain;

import com.iot.common.beans.CommonBean;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：情景微调
 * 创建人： wujianlong
 * 创建时间：2017年11月16日 下午4:17:25
 * 修改人： wujianlong
 * 修改时间：2017年11月16日 下午4:17:25
 */
public class SceneDetail extends CommonBean {

	/** 设备id*/
	private String deviceId;

	/** 房间id*/
	private String spaceId;

	/** 目标值*/
	private String targetValue;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
