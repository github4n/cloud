package com.iot.building.template.vo.req;

import java.util.List;

/**
 * 描述：生成ifttt请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/4 11:37
 */
public class BuildIftttReq {
    private List<String> deviceIds;
    private Long templateId; //2C 为套包主键 2B为rule主键
    private Long userId;
    private Long tenantId;
    private Long spaceId;
    private Integer type; //0 产品 1 用途
    private Long orgId;
    
    public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
