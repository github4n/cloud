package com.iot.boss.vo.tenant.resp;

import java.util.Date;

public class TenantAuditResp {
	
	/**
     * 名称
     */
    private Long tenantId;
    
	/**
     * 名称
     */
    private String userName;
    
	/**
     * 名称
     */
    private String tenantName;
    
    /**
     * 主营业务
     */
    private String business;
    
    /**
     * 电话
     */
    private String cellphone;
    
    /**
     * 
     */
    private String contacts;
    
    private String country;

    private String province;

    private String city;

    private String address;
    
    /**
     * 申请时间
     */
    private Date applyTime;
    
    /**
     * 租户唯一code
     */
    private String code;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
}
