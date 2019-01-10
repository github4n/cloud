package com.iot.control.ifttt.entity;

import java.util.Date;

public class Rule {
    private Long id;

    private String name;

    private String icon;

    private String type;

    private Byte status;

    private Byte isMulti;

    private Long locationId;

    private Long spaceId;

    private Long tenantId;

    private Long userId;

    private String directId;

    private Long orgId;

    private Date createTime;

    private Date updateTime;

    private Byte templateFlag;

    private Long productId;

    private Byte ruleType;

    private String securityType;

    private Integer delay;
    
    private Long templateId;
    
    private String iftttType;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Byte isMulti) {
        this.isMulti = isMulti;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId == null ? null : directId.trim();
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getTemplateFlag() {
        return templateFlag;
    }

    public void setTemplateFlag(Byte templateFlag) {
        this.templateFlag = templateFlag;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
        this.securityType = securityType == null ? null : securityType.trim();
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getIftttType() {
		return iftttType;
	}

	public void setIftttType(String iftttType) {
		this.iftttType = iftttType;
	}

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", isMulti=" + isMulti +
                ", locationId=" + locationId +
                ", spaceId=" + spaceId +
                ", tenantId=" + tenantId +
                ", userId=" + userId +
                ", directId='" + directId + '\'' +
                ", orgId=" + orgId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", templateFlag=" + templateFlag +
                ", productId=" + productId +
                ", ruleType=" + ruleType +
                ", securityType='" + securityType + '\'' +
                ", delay=" + delay +
                ", templateId=" + templateId +
                ", iftttType='" + iftttType + '\'' +
                '}';
    }
}