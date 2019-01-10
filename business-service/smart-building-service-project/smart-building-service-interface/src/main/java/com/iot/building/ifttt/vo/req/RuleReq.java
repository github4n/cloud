package com.iot.building.ifttt.vo.req;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 描述：获取规则列表请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/23 15:19
 */
public class RuleReq implements Serializable {
    private Long id;
    private String name;
    private String type;
    private Byte status;
    private Byte isMulti;
    private Long locationId;
    private Long spaceId;
    private Long tenantId;
    private Long orgId;
    private Long userId;
    private String directId;
    private Byte ruleType;
    private String securityType;
    private Byte templateFlag;
    private Calendar calendar;
    private Integer dayOfWeek;
    private String iftttType;  // IFTTT类型 _2B  _2C
    
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    private Long templateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public Byte getTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(Byte templateFlag) {
        this.templateFlag = templateFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Byte isMulti) {
        this.isMulti = isMulti;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Byte getRuleType() {
        return ruleType;
    }

    public void setRuleType(Byte ruleType) {
        this.ruleType = ruleType;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getIftttType() {
		return iftttType;
	}

	public void setIftttType(String iftttType) {
		this.iftttType = iftttType;
	}

	@Override
    public String toString() {
        return "RuleListReq{" +
                "name='" + name + '\'' +
                ", tenantId=" + tenantId +
                ", orgId=" + orgId +
                ", locationId=" + locationId +
                ", spaceId=" + spaceId +
                ", userId=" + userId +
                ", isMulti=" + isMulti +
                ", ruleType=" + ruleType +
                ", securityType='" + securityType + '\'' +
                ", templateFlag=" + templateFlag +
                ", calendar=" + calendar +
                ", dayOfWeek=" + dayOfWeek +
                ", type='" + type + '\'' +
                '}';
    }
}
