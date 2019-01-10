package com.iot.permission.entity;

public class Permission {
	
	/**
     * 权限id
     */
    private Long id;

    /**
     * 父节点id
     */
    private Long parentId;
    
    /**
     * 权限code
     */
    private String permissionCode;

    /**
     * 资源对应url
     */
    private String permissionUrl;
    
    /**
     * 资源名称
     */
    private String permissionName;

    /**
     * 资源类型（menu; button;other(默认)）
     */
    private String permissionType;

    /**
     * 资源排序  低的排前面
     */
    private Integer sort;
    
    /**
     * 资源图标  权限类型为菜单或按钮时才有值
     */
    private String icon;

    /**
     * 服务ID
     */
    private Long serviceId;
    
    /**
     * 系统类别
     */
    private String systemType;

    /**
     * 创建人
     */
    private Long createBy;
    
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 数据有效性（invalid;valid(默认)）
     */
    private String isDeleted;

	/**
	 * 租户ID
	 */
    private Long tenantId;

	/**
	 * 组织ID
	 */
    private Long orgId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionUrl() {
		return permissionUrl;
	}

	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
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
}
