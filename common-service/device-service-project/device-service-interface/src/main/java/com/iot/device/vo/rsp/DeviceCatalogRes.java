package com.iot.device.vo.rsp;

public class DeviceCatalogRes {

	private Long id;
	/**
	 * 父级id
	 */
	private Long parentId;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 描述
     */
	private String description;

	private Long tenantId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id.toString();
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
}
