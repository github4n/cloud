package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;

public class RuntimeReq implements Serializable {

	
    private Long userId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 统计间隔时长
     */
    private Integer step;    
    /**
     * 统计时间点
     */
    private Date time;
    /**
     * 电量值
     */
    private Long runtime;
    /**
     * 用户所属组织id
     */
    private Long orgId;
    /**
     * 租户ID
     */
    private Long tenantId;
	/**
	 * 地区
	 */
	private String area;
	/**
	 * 本地时间
	 */
	private Date localtime;

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
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Long getRuntime() {
		return runtime;
	}
	public void setRuntime(Long runtime) {
		this.runtime = runtime;
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

	public Date getLocaltime() {
		return localtime;
	}

	public void setLocaltime(Date localtime) {
		this.localtime = localtime;
	}
	
}
