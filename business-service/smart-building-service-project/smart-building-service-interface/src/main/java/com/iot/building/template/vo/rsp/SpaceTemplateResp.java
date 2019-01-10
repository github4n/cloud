package com.iot.building.template.vo.rsp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：模板
 * 创建人： fenglijian
 * 创建时间：2018年05月03日 下午20:20:53
 */
public class SpaceTemplateResp implements Serializable{
	
	private static final long serialVersionUID = -5878069268795798649L;
	
	private Long id;
	/** 空间id */
	private Long spaceId;
	/** 模板id */
	private Long templateId;
	/** 类型 */
	private String templateType;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Date updateTime;
	/** 创建者id */
	private Long createBy;
	/** 更新者id */
	private Long updateBy;
	/** 租户id */
	private Long tenantId;
	/** 区域id */
	private Long locationId;
	/** quartz cron 表达式 */
	private String startCron;
	/** quartz cron 表达式 */
	private String endCron;
	/** 是否设置执行星期 */
	private String loopType;
	/** 定时时间设置 */
	private String properties;
	
	private Long startTime;
	
	private Long endTime;
	
	private String runTime;
	
	private String business;
	
	private Integer flag;//1已经运行 0未运行
	
	private String reason;//
	
	private String week;
	
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	private String templateName;
	private List<String> cronTimeList;
	
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
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public String getStartCron() {
		return startCron;
	}
	public void setStartCron(String startCron) {
		this.startCron = startCron;
	}
	public String getEndCron() {
		return endCron;
	}
	public void setEndCron(String endCron) {
		this.endCron = endCron;
	}
	public String getLoopType() {
		return loopType;
	}
	public void setLoopType(String loopType) {
		this.loopType = loopType;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public List<String> getCronTimeList() {
		return cronTimeList;
	}
	public void setCronTimeList(List<String> cronTimeList) {
		this.cronTimeList = cronTimeList;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
}
