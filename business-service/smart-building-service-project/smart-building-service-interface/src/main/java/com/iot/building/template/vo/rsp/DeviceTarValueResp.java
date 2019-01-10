package com.iot.building.template.vo.rsp;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：中间类
 * 创建人： wujianlong
 * 创建时间：2017年11月14日 下午3:43:15
 * 修改人： wujianlong
 * 修改时间：2017年11月14日 下午3:43:15
 */
public class DeviceTarValueResp implements Serializable{

	private static final long serialVersionUID = -4515586086678813720L;

	/** 产品分类id*/
	private Long deviceCategoryId;
	/** 产品分类名称*/
	private String deviceCategory;

	/** 产品类型id*/
	private Long deviceTypeId;

	/** 设备真实类型*/
	private String deviceType;

	/** 产品类型名称*/
	private String deviceTypeName;
	
	/** 目标*/
	private String targetValue;

	/** 设备id*/
	private String deviceId;

	/** 设备名*/
	private String deviceName;

	/** 模板详情id*/
	private Long templateDetailId;


	public Long getTemplateDetailId() {
		return templateDetailId;
	}

	public void setTemplateDetailId(Long templateDetailId) {
		this.templateDetailId = templateDetailId;
	}

	public Long getDeviceCategoryId() {
		return deviceCategoryId;
	}

	public void setDeviceCategoryId(Long deviceCategoryId) {
		this.deviceCategoryId = deviceCategoryId;
	}

	public String getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(String deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

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

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
