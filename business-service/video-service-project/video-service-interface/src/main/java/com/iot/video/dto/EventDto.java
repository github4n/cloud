package com.iot.video.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：事件
 * 创建人： wujianlong
 * 创建时间：2017年9月6日 下午6:22:47
 * 修改人： wujianlong
 * 修改时间：2017年9月6日 下午6:22:47
 */
public class EventDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 事件id(uuid) */
	private String eventId;
	/**
	 * 文件id
	 */
	private String fileId;

	/** 文件路径 */
	private String filePath;

	/** 事件触发事件 */
	private Date eventOddurTime;

	private String url;

	/**
	 * 旋转角度
	 */
	private Integer rotation;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getEventOddurTime() {
		return eventOddurTime;
	}

	public void setEventOddurTime(Date eventOddurTime) {
		this.eventOddurTime = eventOddurTime;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
}
