package com.iot.permission.vo;

import java.util.List;

public class UserToPermissionDto {
	
	/**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 权限URL
     */
    private List<String> permissionUrls;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<String> getPermissionUrls() {
		return permissionUrls;
	}

	public void setPermissionUrls(List<String> permissionUrls) {
		this.permissionUrls = permissionUrls;
	}
    
}
