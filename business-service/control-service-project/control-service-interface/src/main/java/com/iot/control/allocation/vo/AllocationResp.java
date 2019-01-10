package com.iot.control.allocation.vo;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
@ApiModel(value = "配置信息表", description = "配置信息详细内容")
public class AllocationResp implements Serializable {

    private Long id;

    private Long allocationId;

    private String allocationName;

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

    public String getSpaceIds() {
        return spaceIds;
    }

    public void setSpaceIds(String spaceIds) {
        this.spaceIds = spaceIds;
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

    public String getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(String paramInfo) {
        this.paramInfo = paramInfo;
    }

    public String getExeTime() {
        return exeTime;
    }

    public void setExeTime(String exeTime) {
        this.exeTime = exeTime;
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

    public String getAllocationName() {
        return allocationName;
    }

    public void setAllocationName(String allocationName) {
        this.allocationName = allocationName;
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
}
