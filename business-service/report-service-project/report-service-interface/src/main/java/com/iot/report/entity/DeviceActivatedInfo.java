package com.iot.report.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：激活活跃明细
 * 功能描述：激活活跃明细
 * 创建人： 李帅
 * 创建时间：2019年1月3日 下午8:05:15
 * 修改人：李帅
 * 修改时间：2019年1月3日 下午8:05:15
 */
@Document(collection="device_activated_info")
public class DeviceActivatedInfo {
	
	/**
	 * 设备uuid，主键
	 */
	private String uuid;
	
	/**
	 * 租户id
	 */
	private Long tenantId;
	
	/**
	 * 活跃日期
	 */
	private Date activateDate;
	
	/**
	 * 设备类型
	 */
	private Long deviceType;
	
	/**
	 * 国家
	 */
	private String countryCode;
	
	/**
	 * 省份
	 */
	private String province;
	
	/**
	 * 城市
	 */
	private String city;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	
	public Date getActivateDate() {
		return activateDate;
	}
	public void setActivateDate(Date activateDate) {
		this.activateDate = activateDate;
	}
	public Long getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Long deviceType) {
		this.deviceType = deviceType;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
