package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.Date;

public class ElectricityStatisticsRsp implements Serializable {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 用户id
     */
	private Long userId;
    /**
     * 设备id
     */
    private String deviceId;
     
    /**
     * 统计时间点
     */
    private Date day;
    /**
     * 电量值
     */
    private Double electricValue;
    /**
     * 用户所属组织id
     */
    private Long orgId;
    /**
     * 租户ID
     */
    private Long tenantId;

    private String area;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Double getElectricValue() {
		return electricValue;
	}

	public void setElectricValue(Double electricValue) {
		this.electricValue = electricValue;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
}
