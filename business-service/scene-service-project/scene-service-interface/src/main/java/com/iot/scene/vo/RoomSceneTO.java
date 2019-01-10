package com.iot.scene.vo;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： wujianlong
 * 创建时间：2017年12月1日 上午11:55:24
 * 修改人： wujianlong
 * 修改时间：2017年12月1日 上午11:55:24
 */
public class RoomSceneTO implements Serializable {

	private static final long serialVersionUID = -3144532801476949051L;

	/** 房间id*/
	private String spaceId;

	/** 模板id*/
	private String templateId;

	/** 目标值*/
	private String targetValue;

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
