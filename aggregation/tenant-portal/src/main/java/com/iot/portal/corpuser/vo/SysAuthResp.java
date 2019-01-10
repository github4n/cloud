package com.iot.portal.corpuser.vo;

import java.util.List;

public class SysAuthResp {
	
	/**
     * 权限id
     */
    private String id;

    /**
     * 权限对应url
     */
    private String authUrl;

    /**
     * 权限名称
     */
    private String authName;
    
    /**
     * 权限类型
     */
    private int authType;

    /**
     * 子目录
     */
    private List<SysAuthResp> childs;

	/**
	 * 排序
	 */
	private int sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthUrl() {
		return authUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public List<SysAuthResp> getChilds() {
		return childs;
	}

	public void setChilds(List<SysAuthResp> childs) {
		this.childs = childs;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
