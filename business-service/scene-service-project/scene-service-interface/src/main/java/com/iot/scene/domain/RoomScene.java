package com.iot.scene.domain;

import java.io.Serializable;

public class RoomScene implements Serializable {

	private static final long serialVersionUID = 5557001108617955042L;

	/** 房间id*/
	private String spaceId;

	/** 情景id*/
	private String templateId;

	/** 情景名*/
	private String templateName;

	/** 情景状态 0-离线，1-在线*/
	private int sceneStatus;

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

}
