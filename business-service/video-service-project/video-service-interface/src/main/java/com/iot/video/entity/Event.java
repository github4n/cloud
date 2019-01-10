package com.iot.video.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 事件id */
	private Long eventId;

	/** 计划id */
	private String planId;

	/** 事件代码 */
	private String eventCode;

	/** 事件描述 */
	private String eventDesc;

	/** 事件触发事件 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date eventOddurTime;

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

}
