package com.iot.device.vo.rsp;

import java.io.Serializable;
import java.util.Date;

public class RuntimeRsp implements Serializable {

	
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
     * 运行时间
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
	
	
}
