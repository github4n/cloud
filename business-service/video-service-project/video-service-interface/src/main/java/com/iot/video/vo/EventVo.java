package com.iot.video.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：事件
 * 创建人： wujianlong
 * 创建时间：2017年9月6日 下午6:22:47
 * 修改人： wujianlong
 * 修改时间：2017年9月6日 下午6:22:47
 */
public class EventVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 事件id */
	private Long eventId;

	/** 计划id */
	private String planId;

	/** 文件id */
	private String fileId;
	
	/** 文件id */
	private String url;
	
	/** 事件触发事件 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date eventOddurTime;

	/**事件uuid*/
	private String eventUuid;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getEventOddurTime() {
		return eventOddurTime;
	}

	public void setEventOddurTime(Date eventOddurTime) {
		this.eventOddurTime = eventOddurTime;
	}

	public String getEventUuid() {
		return eventUuid;
	}

	public void setEventUuid(String eventUuid) {
		this.eventUuid = eventUuid;
	}

	public EventVo() {
	}

	public EventVo(String planId, Date eventOddurTime) {
		this.planId = planId;
		this.eventOddurTime = eventOddurTime;
	}


}
