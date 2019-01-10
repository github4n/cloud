package com.iot.building.scene.vo.req;

import java.util.List;

public class SceneTemplateManualReq {

	private Long tenantId;
	
	private Long locationId;
	
	private Long userId;
	
	private List<Long> spaceIdList;
	
	private Integer interval;
	
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
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
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getSpaceIdList() {
		return spaceIdList;
	}

	public void setSpaceIdList(List<Long> spaceIdList) {
		this.spaceIdList = spaceIdList;
	}
	
}
