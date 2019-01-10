package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ElectricityStatisticsReq implements Serializable {

	
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
    private Double electricValue;
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

	private String timeStr;

	/**
	 * 电压
	 */
	private Double voltage;

	/**
	 * 功率
	 */
	private Double power;

	/**
	 * 温度
	 */
	private Double temperature;

	/**
	 * 电流
	 */
	private Double current;

	/**
	 * 是否总路 0:总路  1:分类
	 */
	private Integer isMaster;

	private List<String> deviceIds;

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

	public Date getLocaltime() {
		return localtime;
	}

	public void setLocaltime(Date localtime) {
		this.localtime = localtime;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public List<String> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}

	public Double getVoltage() {
		return voltage;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getPower() {
		return power;
	}

	public void setPower(Double power) {
		this.power = power;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getCurrent() {
		return current;
	}

	public void setCurrent(Double current) {
		this.current = current;
	}

	public Integer getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(Integer isMaster) {
		this.isMaster = isMaster;
	}
}
