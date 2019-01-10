package com.iot.building.common.vo;

import java.util.Map;

import com.iot.common.enums.APIType;

public class ProtocolParamVo {
	
	private String deviceId;
	
	private String spaceId;
	
	private String userId;
	
	private APIType apiType;
	
	private String method;
	
	private Long tenantId;
	
	private Long locationId;
	
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	private Map<String,Object> controlParams;
	
	public Map<String, Object> getControlParams() {
		return controlParams;
	}

	public void setControlParams(Map<String, Object> controlParams) {
		this.controlParams = controlParams;
	}

	public APIType getApiType() {
		return apiType;
	}

	public void setApiType(APIType apiType) {
		this.apiType = apiType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
