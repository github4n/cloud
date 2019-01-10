package com.iot.building.group.vo;

import java.util.List;

public class GroupCommonVo {

	private Long id;
	
	private String groupId;
	
	private String name;
	
	private String gatewayId;
	
	private String model;
	
	private String remoteId;
	
	private String tenantId;
	
	private String deviceId;
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	private List<String> deviceIds;
	
	public List<String> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}

	public String getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public GroupCommonVo(String groupId, String name, String gatewayId, String model, String deviceId) {
		this.groupId = groupId;
		this.name = name;
		this.gatewayId = gatewayId;
		this.model = model;
		this.deviceId = deviceId;
	}

	public GroupCommonVo() {
	}
}
