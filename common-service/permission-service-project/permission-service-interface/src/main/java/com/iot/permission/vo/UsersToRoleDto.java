package com.iot.permission.vo;

import java.util.List;

public class UsersToRoleDto {
	
	/**
     * 创建人ID
     */
    private Long createId;
    
    /**
     * 角色ID
     */
    private Long roleId;
    
    /**
     * 用户ID
     */
    private List<Long> userIds;
    
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

}
