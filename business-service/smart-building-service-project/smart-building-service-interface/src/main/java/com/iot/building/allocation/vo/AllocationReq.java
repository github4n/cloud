package com.iot.building.allocation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
public class AllocationReq implements Serializable {

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

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	// *******
    private Integer pageSize;
    private Integer pageNum;

    public AllocationReq() {
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
        this.selectWeek = selectWeek;
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

    public String getSpaceIds() {
        return spaceIds;
    }

    public void setSpaceIds(String spaceIds) {
        this.spaceIds = spaceIds;
    }

    public String getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(String paramInfo) {
        this.paramInfo = paramInfo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public Integer getDeviceInterval() {
        return deviceInterval;
    }

    public void setDeviceInterval(Integer deviceInterval) {
        this.deviceInterval = deviceInterval;
    }

    public String getExeTime() {
        return exeTime;
    }

    public void setExeTime(String exeTime) {
        this.exeTime = exeTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
