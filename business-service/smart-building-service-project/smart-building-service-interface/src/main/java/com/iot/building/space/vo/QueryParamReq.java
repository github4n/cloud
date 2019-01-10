package com.iot.building.space.vo;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 */
public class QueryParamReq {

    private Long spaceId;
    
    private String deviceId;
    
    private List<String> businessTypeIds;
    private List<String> deviceTypeIds;
    
    private List<Long> spaceIds;
    
    private Long tenantId;

    private Long locationId;
    
    private Long orgId;
    
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public List<Long> getSpaceIds() {
		return spaceIds;
	}

	public void setSpaceIds(List<Long> spaceIds) {
		this.spaceIds = spaceIds;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<String> getBusinessTypeIds() {
		return businessTypeIds;
	}

	public void setBusinessTypeIds(List<String> businessTypeIds) {
		this.businessTypeIds = businessTypeIds;
	}

	public List<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(List<String> deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
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
    
}
