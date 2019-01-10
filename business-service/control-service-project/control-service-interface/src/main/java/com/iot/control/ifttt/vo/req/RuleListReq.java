package com.iot.control.ifttt.vo.req;

import java.io.Serializable;

/**
 * 描述：获取规则列表请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/23 15:19
 */
public class RuleListReq implements Serializable {
    private String name;
    private Long tenantId;
    private Long orgId;
    private Long locationId;
    private Long spaceId;
    private Long userId;
    private Byte isMulti;
    private Integer pageNum;
    private Integer pageSize;
    private Byte ruleType;
    private String securityType;
    private Byte templateFlag;
    private Long templateId;
    private Integer delay;
    private Boolean showTime; //是否显示执行时间
    private String type; 
    private String iftttType;  // IFTTT类型 _2B  _2C

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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Boolean getShowTime() {
        return showTime;
    }

    public void setShowTime(Boolean showTime) {
        this.showTime = showTime;
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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", ruleType=" + ruleType +
                ", securityType='" + securityType + '\'' +
                ", templateFlag=" + templateFlag +
                ", delay=" + delay +
                ", showTime=" + showTime +
                ", type='" + type + '\'' +
                '}';
    }
}
