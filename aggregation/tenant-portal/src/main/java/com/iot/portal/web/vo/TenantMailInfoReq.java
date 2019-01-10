package com.iot.portal.web.vo;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：邮件批量推送对象
 * 创建人： 李帅
 * 创建时间：2018年3月16日 上午11:46:36
 * 修改人：李帅
 * 修改时间：2018年3月16日 上午11:46:36
 */
public class TenantMailInfoReq {

	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 邮件服务主机Host
     */
    private String mailHost;
    
    /**
     * 邮件服务主机端口
     */
    private int mailPort;
    
    /**
     * 邮箱用户名
     */
    private String mailName;
    
    /**
     * 邮箱密码
     */
    private String passWord;
    
    /**
     * APP应用id
     */
    private Long appId;
    
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
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

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public int getMailPort() {
		return mailPort;
	}

	public void setMailPort(int mailPort) {
		this.mailPort = mailPort;
	}

	public String getMailName() {
		return mailName;
	}

	public void setMailName(String mailName) {
		this.mailName = mailName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
