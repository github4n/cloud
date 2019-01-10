package com.iot.permission.vo;

import java.util.List;

public class PermissionDto {
	
	/**
     * 权限id
     */
    private String id;

    /**
     * 父节点id
     */
    private String parentId;
    
    /**
     * 权限对应url
     */
    private String permissionCode;
    
    /**
     * 权限对应url
     */
    private String permissionUrl;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限类型
     */
    private String permissionType;

    /**
     * 权限类型
     */
    private int sort;

    private Long tenantId;

    private Long orgId;
    
    /**
     * 子目录
     */
    private List<PermissionDto> childs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public List<PermissionDto> getChilds() {
		return childs;
	}

	public void setChilds(List<PermissionDto> childs) {
		this.childs = childs;
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
