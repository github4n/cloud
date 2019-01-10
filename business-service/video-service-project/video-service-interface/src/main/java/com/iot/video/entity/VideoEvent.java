package com.iot.video.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：cloud
 * 模块名称：视频微服务
 * 功能描述：录影事件实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/4/8 10:04
 * 修改人： yeshiyuan
 * 修改时间：2018/4/8 10:04
 * 修改描述：
 */
public class VideoEvent implements Serializable{
	
	/***/
	private static final long serialVersionUID = -2883906329305480369L;

	/**租户id*/
    private Long tenantId;

    /** 计划id */
    private String planId;

    /** 事件代码 */
    private String eventCode;

    /** 事件名称 */
    private String eventName;

    /** 事件描述 */
    private String eventDesc;

    /** 事件触发时间 */
    private Date eventOddurTime;

    /** 事件状态 */
    private String eventStatus;

    /** 事件uuid */
    private String eventUuid;

    /** 创建事件 */
    private Date createTime;

    /** 修改事件 */
    private Date updateTime;

    /**
     * 数据状态
     */
    private Integer dataStatus;

    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
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
}
