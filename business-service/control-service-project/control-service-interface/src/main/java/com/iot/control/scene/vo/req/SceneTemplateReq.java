package com.iot.control.scene.vo.req;

import java.io.Serializable;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：
 * 功能描述：情景模板
 * 创建人： wujianlong
 * 创建时间：2017年11月15日 上午10:44:06
 * 修改人： wujianlong
 * 修改时间：2017年11月15日 上午10:44:06
 */
public class SceneTemplateReq implements Serializable{
	
	private static final long serialVersionUID = -2644110158557101062L;

	/** 模板id*/
	private Long templateId;
	
	/** 模板名*/
	private String name;
	
	/** deviceTarValue的json形式*/
	private String deviceTarValue;

	/** 创建人id*/
	private Long userId;
	
	/** 区域id*/
	private Long locationId;
	
	/** 租户id*/
	private Long tenantId;
	
	/** 创建者*/
    private Long createBy;

    /** 更新者*/
    private Long updateBy;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceTarValue() {
		return deviceTarValue;
	}

	public void setDeviceTarValue(String deviceTarValue) {
		this.deviceTarValue = deviceTarValue;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	
}
