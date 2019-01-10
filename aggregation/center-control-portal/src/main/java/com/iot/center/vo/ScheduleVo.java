package com.iot.center.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
public class ScheduleVo {
	
	private String[] templateIds;
	private String[] startTimes;
	private String[] endTimes;
	private String week;
	private String runTime;
	private String templateType;
	private String loopType;
	private String business;
	private Long id;
	private Long spaceId;
	private String templateName;
	private String zone;
	
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String[] getTemplateIds() {
		return templateIds;
	}
	public void setTemplateIds(String[] templateIds) {
		this.templateIds = templateIds;
	}
	public String[] getStartTimes() {
		return startTimes;
	}
	public void setStartTimes(String[] startTimes) {
		this.startTimes = startTimes;
	}
	public String[] getEndTimes() {
		return endTimes;
	}
	public void setEndTimes(String[] endTimes) {
		this.endTimes = endTimes;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getLoopType() {
		return loopType;
	}
	public void setLoopType(String loopType) {
		this.loopType = loopType;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}
	
}
