package com.iot.boss.vo.permission;

public class AssignPermissionVo {
	private Long roleId;
	private String[] permissionIds;
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
