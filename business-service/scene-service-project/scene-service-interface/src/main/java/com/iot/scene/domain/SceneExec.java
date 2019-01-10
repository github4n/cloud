package com.iot.scene.domain;

import com.iot.common.beans.ModifyBean;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：情景执行
 * 创建人： wujianlong
 * 创建时间：2017年11月21日 下午5:25:29
 * 修改人： wujianlong
 * 修改时间：2017年11月21日 下午5:25:29
 */
public class SceneExec extends ModifyBean implements Serializable {

	private static final long serialVersionUID = -3625445506939203105L;

	/** 目标*/
	private String targetValue;

	private String deviceCategoryId;

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getDeviceCategoryId() {
		return deviceCategoryId;
	}

	public void setDeviceCategoryId(String deviceCategoryId) {
		this.deviceCategoryId = deviceCategoryId;
	}

	public ModifyBean getModifyBean() {
		ModifyBean m = new ModifyBean();
		m.setDeviceId(super.getDeviceId());
		m.setDeviceName(super.getDeviceName());
		m.setGatewayId(super.getGatewayId());
		m.setRealityId(super.getRealityId());
		m.setExtraName(super.getExtraName());
		return m;
	}

}
