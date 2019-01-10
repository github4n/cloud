package com.iot.permission.entity;

public class Role {
	
	/**
     * 角色id
     */
    private Long id;

    /**
     * 角色编码:Owner,Manager,ProductManager,APPDeveloper,EmbeddedDeveloper,Tester,Operator,DataQueryer
     */
    private String roleCode;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色类型:（Boss;2B(默认)；2C）
     */
    private String roleType;

    /**
     * 租户id
     */
    private Long tenantId;
    
    /**
     * 角色描述
     */
    private String roleDesc;

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

    private Long orgId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
