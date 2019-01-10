package com.iot.building.template.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述： 生成模板dto
 */
public class TemplateReq implements Serializable{
	
	private static final long serialVersionUID = -7276209311915920905L;

	/** 模板表id */
	private Long id;

	/** 产品表id*/
	private Long productId;

	/** 区域主键*/
	private Long locationId;

	/** space主键*/
	private Long spaceId;

	/** 模板名*/
	private String name;
	
	/** 租户ID*/
	private Long tenantId;
	
	private Integer pageNum;
	
	private Integer pageSize;
	
	private Long deployId;
	
	/** 更新者*/
	private Long updateBy;
	
	/**
	 * 是否有快捷方式 0无 1有
	 */
	private Integer shortcut;
	
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

	public Integer getShortcut() {
		return shortcut;
	}

	public void setShortcut(Integer shortcut) {
		this.shortcut = shortcut;
	}
	
	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getDeployId() {
		return deployId;
	}

	public void setDeployId(Long deployId) {
		this.deployId = deployId;
	}
	
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/** 模板列表*/
	private List<Long> templateList;
	
	/** 挂载标识*/
	private Integer mountLogo;

	/** 模板类型,套包:kit,情景:scene,ifttt:ifttt*/
	private String templateType;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<Long> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Long> templateList) {
		this.templateList = templateList;
	}

	public Integer getMountLogo() {
		return mountLogo;
	}

	public void setMountLogo(Integer mountLogo) {
		this.mountLogo = mountLogo;
	}
}
