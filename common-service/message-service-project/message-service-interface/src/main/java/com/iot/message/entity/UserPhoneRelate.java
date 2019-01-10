package com.iot.message.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserPhoneRelate {

    /**
     * 关联ID
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;
    
    /**
     * 手机id
     */
    private String phoneId;
    
    /**
     * APP应用id
     */
    private Long appId;
    
    /**
     * 手机类型
     */
    private String phoneType;
    
    /**
     * 创建人
     */
    private String creator;
    
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

    /**
     * 是否已删除
     */
    private String dataStatus;

	/**
	 * 租户id
	 */
	private Long tenantId;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public UserPhoneRelate() {
	}

	public UserPhoneRelate(String userId, String phoneId, String phoneType, String creator,Long tenantId,Long appId) {
		super();
		this.userId = userId;
		this.phoneId = phoneId;
		this.phoneType = phoneType;
		this.creator = creator;
		this.tenantId = tenantId;
		this.appId = appId;
	}
}