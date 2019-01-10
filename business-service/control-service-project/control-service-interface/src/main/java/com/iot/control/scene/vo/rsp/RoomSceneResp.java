package com.iot.control.scene.vo.rsp;

import java.io.Serializable;

public class RoomSceneResp implements Serializable{
	
	private static final long serialVersionUID = 5557001108617955042L;

	/** 房间id*/
	private Long spaceId;
	
	/** 情景id*/
	private Long templateId;
	
	/** 情景名*/
	private String templateName;
	
	/** 情景状态 0-离线，1-在线*/
	private int sceneStatus;

	/** 目标值*/
	private String targetValue;

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getSceneStatus() {
		return sceneStatus;
	}

	public void setSceneStatus(int sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
