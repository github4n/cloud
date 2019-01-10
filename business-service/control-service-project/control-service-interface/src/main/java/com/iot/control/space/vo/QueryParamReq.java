package com.iot.control.space.vo;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：告警
 * 创建人： zhouzongwei
 * 创建时间：2017年11月30日 下午2:35:27
 * 修改人： zhouzongwei
 * 修改时间：2017年11月30日 下午2:35:27
 */
public class QueryParamReq {

    private Long userId;
    private String businessType;
    private Long spaceId;
    
    private List<SpaceReq> spaceList;
    
    private String deviceId;
    
    private List<String> strIds;
    
    private List<Long> longIds;
    
    private Long tenantId;

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<SpaceReq> getSpaceList() {
		return spaceList;
	}

	public void setSpaceList(List<SpaceReq> spaceList) {
		this.spaceList = spaceList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<String> getStrIds() {
		return strIds;
	}

	public void setStrIds(List<String> strIds) {
		this.strIds = strIds;
	}

	public List<Long> getLongIds() {
		return longIds;
	}

	public void setLongIds(List<Long> longIds) {
		this.longIds = longIds;
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

}
