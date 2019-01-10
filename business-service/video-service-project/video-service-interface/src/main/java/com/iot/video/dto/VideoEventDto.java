package com.iot.video.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：IOT云平台 模块名称： 功能描述：录影事件 创建人： wujianlong 创建时间：2017年9月1日 下午5:30:59 修改人：
 * wujianlong 修改时间：2017年9月1日 下午5:30:59
 */
public class VideoEventDto implements Serializable {

	private static final long serialVersionUID = 7638742432971810646L;

	/** 事件id */
	private String eventId;

	/** 计划id */
	private String planId;

	/** 事件代码 */
	private String eventCode;

	/** 事件描述 */
	private String eventDesc;

	/** 事件触发事件 */
	private Date eventOddurTime;
	
	/** 图片url */
	private String url;

	/** 图片文件ID */
	private String fileId;
	
	/**设备id*/
	private String deviceId;
	
	/**文件类型*/
	private String fileType;
	
	/**文件大小*/
	private String fileSize;


	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public Date getEventOddurTime() {
		return eventOddurTime;
	}

	public void setEventOddurTime(Date eventOddurTime) {
		this.eventOddurTime = eventOddurTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

}
