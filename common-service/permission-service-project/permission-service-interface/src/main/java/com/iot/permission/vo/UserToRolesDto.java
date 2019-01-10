package com.iot.permission.vo;

import java.util.List;

public class UserToRolesDto {
	
	/**
     * 创建人ID
     */
    private Long createId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 角色ID
     */
    private List<Long> roleIds;

	public UserToRolesDto() {
	}

	public UserToRolesDto(Long createId, Long userId, List<Long> roleIds) {
		this.createId = createId;
		this.userId = userId;
		this.roleIds = roleIds;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

}
