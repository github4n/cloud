package com.iot.message.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：系统推送控制
 * 功能描述：系统推送控制
 * 创建人： 李帅
 * 创建时间：2018年11月16日 下午3:45:51
 * 修改人：李帅
 * 修改时间：2018年11月16日 下午3:45:51
 */
public class SystemPushControl {

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
     * 用户id
     */
    private String userId;

    /**
     * 证书密码
     */
    private String switCh;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**
     * 创建人
     */
    private Long createBy;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 数据有效性（invalid;valid(默认)）
     */
    private String isDeleted;
    
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSwitCh() {
		return switCh;
	}

	public void setSwitCh(String switCh) {
		this.switCh = switCh;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

}