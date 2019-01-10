package com.iot.scene.domain;

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
public class DeviceTarValue implements Serializable {

	private static final long serialVersionUID = -4515586086678813720L;

	/** 产品类型id*/
	private String deviceCategoryId;

	/** 产品类型*/
	private String deviceCategory;

	/** 目标*/
	private String targetValue;

	public String getDeviceCategoryId() {
		return deviceCategoryId;
	}

	public void setDeviceCategoryId(String deviceCategoryId) {
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

}
