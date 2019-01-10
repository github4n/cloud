package com.iot.message.dto;

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
public class SystemPushControlDto {
    
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
     * 创建人
     */
    private Long createBy;

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
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

}