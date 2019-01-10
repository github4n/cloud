package com.iot.scene.domain;

import com.iot.common.beans.CommonBean;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：模板详情
 * 创建人： wujianlong
 * 创建时间：2017年11月14日 下午4:01:41
 * 修改人： wujianlong
 * 修改时间：2017年11月14日 下午4:01:41
 */
public class TemplateDetail extends CommonBean {

	/** 模板id*/
	private String templateId;

	/** 产品类型*/
	private String deviceCategoryId;

	/** 目标值*/
	private String targetValue;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getDeviceCategoryId() {
		return deviceCategoryId;
	}

	public void setDeviceCategoryId(String deviceCategoryId) {
		this.deviceCategoryId = deviceCategoryId;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
