package com.iot.message.dto;

public class AppCertInfoDto {

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
     * 证书密码
     */
    private String certPassWord;

    /**
     * 服务主机
     */
    private String testOrReleaseMark;
    
	public String getTestOrReleaseMark() {
		return testOrReleaseMark;
	}

	public void setTestOrReleaseMark(String testOrReleaseMark) {
		this.testOrReleaseMark = testOrReleaseMark;
	}

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

	public String getCertPassWord() {
		return certPassWord;
	}

	public void setCertPassWord(String certPassWord) {
		this.certPassWord = certPassWord;
	}
}