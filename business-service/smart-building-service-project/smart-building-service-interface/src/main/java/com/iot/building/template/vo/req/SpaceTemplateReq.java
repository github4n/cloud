package com.iot.building.template.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：空间-模板
 * 创建人： fenglijian
 * 创建时间：2018年05月03日 下午20:20:53
 */
public class SpaceTemplateReq implements Serializable{
	
	private static final long serialVersionUID = -5878069268795798649L;

	private String business;

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
	
	private int pageSize;
	
	private int pageNum;
	
	private String week;
	private List<Long> templateIds;
	private List<Long> startTimes;
	private List<Long> endTimes;
	
	private Long startTime;
	private Long endTime;
	private List<Long> spaceIds;
	private String runTime;
	
	private String templateName;
	
	private String zone;
	
	private Long timeDiff;//前端用户和0时区时间差
	
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public Long getTimeDiff() {
		return timeDiff;
	}

	public void setTimeDiff(Long timeDiff) {
		this.timeDiff = timeDiff;
	}

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

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}

	public List<Long> getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(List<Long> startTimes) {
		this.startTimes = startTimes;
	}

	public List<Long> getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(List<Long> endTimes) {
		this.endTimes = endTimes;
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
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
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
	public List<Long> getSpaceIds() {
		return spaceIds;
	}
	public void setSpaceIds(List<Long> spaceIds) {
		this.spaceIds = spaceIds;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
}
