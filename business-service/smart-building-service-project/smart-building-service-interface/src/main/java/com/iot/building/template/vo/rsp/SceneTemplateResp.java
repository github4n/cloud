package com.iot.building.template.vo.rsp;

import java.io.Serializable;
import java.util.List;

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
public class SceneTemplateResp implements Serializable{
	
	private static final long serialVersionUID = -2644110158557101062L;

	private Long id;

	/** 模板id*/
	private Long templateId;
	
	/** 模板名*/
	private String name;
	
	/** 产品目标值*/
	private List<TemplateDetailResp> devTarValueList;

	/** deviceTarValue的json形式*/
	private String deviceTarValue;

    /** 创建人*/
    private String createBy;

    /** 创建时间*/
    private String createTime;

	/** 情景状态 0-离线，1-在线*/
	private int sceneStatus;
	
	private Integer shortcut;
	
	private Integer silenceStatus;
	
	private Long locationId;
	
	private Long tenantId;
	
	private Long orgId;
	
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getShortcut() {
		return shortcut;
	}

	public void setShortcut(Integer shortcut) {
		this.shortcut = shortcut;
	}

	public Integer getSilenceStatus() {
		return silenceStatus;
	}

	public void setSilenceStatus(Integer silenceStatus) {
		this.silenceStatus = silenceStatus;
	}

	private Long deployId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<TemplateDetailResp> getDevTarValueList() {
		return devTarValueList;
	}

	public void setDevTarValueList(List<TemplateDetailResp> devTarValueList) {
		this.devTarValueList = devTarValueList;
	}

	public String getDeviceTarValue() {
		return deviceTarValue;
	}

	public void setDeviceTarValue(String deviceTarValue) {
		this.deviceTarValue = deviceTarValue;
	}

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

	public int getSceneStatus() {
		return sceneStatus;
	}

	public void setSceneStatus(int sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

	public Long getDeployId() {
		return deployId;
	}

	public void setDeployId(Long deployId) {
		this.deployId = deployId;
	}
}
