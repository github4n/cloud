package com.iot.building.template.vo.rsp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TemplateResp implements Serializable {

	private static final long serialVersionUID = -7276209311915920905L;

	/** 模板id*/
	private Long id;

	/** 模板名*/
	private String name;

	/** 产品表id*/
	private Long productId;

	/** 区域主键*/
	private Long locationId;

	/** space主键*/
	private Long spaceId;

	/** 模板类型,套包:kit,情景:scene,ifttt:ifttt*/
	private String templateType;

	/** 创建者*/
	private Long createBy;

	/** 更新者*/
	private Long updateBy;

	/** 创建时间*/
	private Date createTime;

	/** 更新时间*/
	private Date updateTime;

	/** 租户ID*/
	private Long tenantId;

	/** 情景状态 0-离线，1-在线*/
	private int sceneStatus;
	
	/** 挂载模板列表*/
	private List<TemplateResp> mountList;
	
	/** 未挂载模板列表*/
	private List<TemplateResp> unMountList;
	
	private String deployName;
	
	private String createName;
	
	private Integer silenceStatus;
	
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public Integer getSilenceStatus() {
		return silenceStatus;
	}

	public void setSilenceStatus(Integer silenceStatus) {
		this.silenceStatus = silenceStatus;
	}

	
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * 是否有快捷方式 0无 1有
	 */
	private Integer shortcut;
	
	public Integer getShortcut() {
		return shortcut;
	}

	public void setShortcut(Integer shortcut) {
		this.shortcut = shortcut;
	}
	
	public String getDeployName() {
		return deployName;
	}

	public void setDeployName(String deployName) {
		this.deployName = deployName;
	}

	private Long deployId;
	
	public Long getDeployId() {
		return deployId;
	}

	public void setDeployId(Long deployId) {
		this.deployId = deployId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSceneStatus() {
		return sceneStatus;
	}

	public void setSceneStatus(int sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
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

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<TemplateResp> getMountList() {
		return mountList;
	}

	public void setMountList(List<TemplateResp> mountList) {
		this.mountList = mountList;
	}

	public List<TemplateResp> getUnMountList() {
		return unMountList;
	}

	public void setUnMountList(List<TemplateResp> unMountList) {
		this.unMountList = unMountList;
	}
}
