package com.iot.building.allocation.entity;

import java.util.Date;

public class Allocation {
    private Long id;

    private Long allocationId;

    private Integer exeStatus;

    private Integer exeResult;

    private Integer isLoop;

    private String selectWeek;

    private String spaceIds;

    private String spaceName;

    private Integer deviceInterval;

    private String paramInfo;

    private String exeTime;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;
    
    private Long tenantId;
    
    private Long locationId;
    
    private Integer concurrent;
    
    private Long orgId;
    
    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getConcurrent() {
		return concurrent;
	}

	public void setConcurrent(Integer concurrent) {
		this.concurrent = concurrent;
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

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    public Integer getExeStatus() {
        return exeStatus;
    }

    public void setExeStatus(Integer exeStatus) {
        this.exeStatus = exeStatus;
    }

    public Integer getExeResult() {
        return exeResult;
    }

    public void setExeResult(Integer exeResult) {
        this.exeResult = exeResult;
    }

    public Integer getIsLoop() {
        return isLoop;
    }

    public void setIsLoop(Integer isLoop) {
        this.isLoop = isLoop;
    }

    public String getSelectWeek() {
        return selectWeek;
    }

    public void setSelectWeek(String selectWeek) {
        this.selectWeek = selectWeek == null ? null : selectWeek.trim();
    }

    public String getSpaceIds() {
        return spaceIds;
    }

    public void setSpaceIds(String spaceIds) {
        this.spaceIds = spaceIds == null ? null : spaceIds.trim();
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName == null ? null : spaceName.trim();
    }

    public Integer getDeviceInterval() {
        return deviceInterval;
    }

    public void setDeviceInterval(Integer deviceInterval) {
        this.deviceInterval = deviceInterval;
    }

    public String getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(String paramInfo) {
        this.paramInfo = paramInfo == null ? null : paramInfo.trim();
    }

    public String getExeTime() {
        return exeTime;
    }

    public void setExeTime(String exeTime) {
        this.exeTime = exeTime == null ? null : exeTime.trim();
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }
}