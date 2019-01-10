package com.iot.building.warning.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：告警
 * 创建人： zhouzongwei
 * 创建时间：2017年11月30日 下午2:35:27
 * 修改人： zhouzongwei
 * 修改时间：2017年11月30日 下午2:35:27
 */
public class WarningReq implements Serializable {

    /***/
    private static final long serialVersionUID = -2049824218258542162L;

    private Long id;

    private String deviceId;

    private String content;

    private Date createTime;

    private String type;

    private String status;

    private Long tenantId;
    
    private Long locationId;

    private String timeType;

    private String count;

    private String use;
    
    private Long orgId;
    
    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    private String spaceName;

    private String purpose;

    private String eventName;

    private String eventType;

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
    private String timeType2;
    private String timeType3;
    public String getTimeType2() {
        return timeType2;
    }

    public void setTimeType2(String timeType2) {
        this.timeType2 = timeType2;
    }

    public String getTimeType3() {
        return timeType3;
    }

    public void setTimeType3(String timeType3) {
        this.timeType3 = timeType3;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }


    public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}
