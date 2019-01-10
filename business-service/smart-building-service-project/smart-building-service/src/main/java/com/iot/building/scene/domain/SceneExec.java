package com.iot.building.scene.domain;

import com.iot.common.beans.ModifyBean;
import com.iot.common.enums.APIType;

import java.io.Serializable;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：情景执行
 * 创建人： wujianlong
 * 创建时间：2017年11月21日 下午5:25:29
 * 修改人： wujianlong
 * 修改时间：2017年11月21日 下午5:25:29
 */
public class SceneExec implements Serializable{
	
	private static final long serialVersionUID = -3625445506939203105L;
	
	/** 目标*/
	private String targetValue;
	
	//private Long deviceCategoryId;
	private Long deviceTypeId;

	private String deviceId;
	private String realityId;
	private String gatewayId;
	private String deviceName;
	private String extraName;
	private APIType apiType;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public APIType getApiType() {
		return apiType;
	}

	public void setApiType(APIType apiType) {
		this.apiType = apiType;
	}

	public String getRealityId() {
		return realityId;
	}

	public void setRealityId(String realityId) {
		this.realityId = realityId;
	}

	public String getExtraName() {
		return extraName;
	}

	public void setExtraName(String extraName) {
		this.extraName = extraName;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public ModifyBean getModifyBean() {
		ModifyBean m = new ModifyBean();
		m.setDeviceId(this.getDeviceId());
		m.setDeviceName(this.getDeviceName());
		m.setGatewayId(this.getGatewayId());
		m.setRealityId(this.getRealityId());
		m.setExtraName(this.getExtraName());
		//m.setApiType(APIType.HubGateway);
		return m;
	}

}
