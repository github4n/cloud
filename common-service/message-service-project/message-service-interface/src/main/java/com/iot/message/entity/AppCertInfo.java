package com.iot.message.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class AppCertInfo {

	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * APP应用id
     */
    private Long appId;
    
    /**
     * 证书信息
     */
    private byte[] certInfo;
    
    /**
     * 证书密码
     */
    private String certPassWord;

    /**
     * 服务主机
     */
    private String serviceHost;
    
    /**
     * 服务端口
     */
    private int servicePort;
    
    /**
     * 服务主机
     */
    private String androidPushUrl;
    
    /**
     * 服务主机
     */
    private String androidPushKey;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public byte[] getCertInfo() {
		return certInfo;
	}

	public void setCertInfo(byte[] certInfo) {
		this.certInfo = certInfo;
	}

	public String getCertPassWord() {
		return certPassWord;
	}

	public void setCertPassWord(String certPassWord) {
		this.certPassWord = certPassWord;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	public String getAndroidPushUrl() {
		return androidPushUrl;
	}

	public void setAndroidPushUrl(String androidPushUrl) {
		this.androidPushUrl = androidPushUrl;
	}

	public String getAndroidPushKey() {
		return androidPushKey;
	}

	public void setAndroidPushKey(String androidPushKey) {
		this.androidPushKey = androidPushKey;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}