package com.iot.permission.vo;

public class AssignPermissionDto {
	private Long userId;
	private Long roleId;
	private String[] permissionIds;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String[] getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}
	
}
