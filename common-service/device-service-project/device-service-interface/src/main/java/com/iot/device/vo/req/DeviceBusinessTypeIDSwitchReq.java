package com.iot.device.vo.req;

import java.util.List;

public class DeviceBusinessTypeIDSwitchReq {
	
	private List<String> deviceIds;
	
	private List<String> businessTypeList;
	
	private List<String> deviceTypeList;
	
	private Long locationId;
	
	private Integer switchStatus;
	
	private Long tenantId;
	
	private Integer isDirectDevice;
	
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getIsDirectDevice() {
		return isDirectDevice;
	}

	public void setIsDirectDevice(Integer isDirectDevice) {
		this.isDirectDevice = isDirectDevice;
	}

	public Long getTenantId() {
		return tenantId;
	}
	
	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<String> getDeviceTypeList() {
		return deviceTypeList;
	}

	public void setDeviceTypeList(List<String> deviceTypeList) {
		this.deviceTypeList = deviceTypeList;
	}

	public List<String> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}
	
	public List<String> getBusinessTypeList() {
		return businessTypeList;
	}

	public void setBusinessTypeList(List<String> businessTypeList) {
		this.businessTypeList = businessTypeList;
	}

	public Integer getSwitchStatus() {
		return switchStatus;
	}

	public void setSwitchStatus(Integer switchStatus) {
		this.switchStatus = switchStatus;
	}
	
	
}