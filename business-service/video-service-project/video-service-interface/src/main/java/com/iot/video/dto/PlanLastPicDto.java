package com.iot.video.dto;

import java.io.Serializable;

/**
 * 
 * 项目名称：IOT云平台 模块名称：录影计划 功能描述： 创建人： wujianlong 创建时间：2017年8月10日 上午10:37:46
 * 修改人： wujianlong 修改时间：2017年8月10日 上午10:37:46
 */
public class PlanLastPicDto implements Serializable {

	private static final long serialVersionUID = -6605703057339673972L;

	/** 计划id */
	private String planId;

	/** 文件id */
	private String fileId;

	/** 设备id */
	private String deviceID;

	private String filePath;
	
	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public PlanLastPicDto(String planId, String deviceID, String fileId,  String filePath) {
		this.planId = planId;
		this.fileId = fileId;
		this.deviceID = deviceID;
		this.filePath = filePath;
	}

	public PlanLastPicDto() {
	}
}
