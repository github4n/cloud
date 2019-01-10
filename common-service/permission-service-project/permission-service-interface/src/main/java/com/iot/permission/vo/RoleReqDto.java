package com.iot.permission.vo;

public class RoleReqDto {
	
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色类型:（Boss;2B(默认)；2C）
     */
    private String roleType;

    /**
     * 租户id
     */
    private Long tenantId;

    private String roleName;

    private Long orgId;
    
	public RoleReqDto() {
	}

	public RoleReqDto(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
