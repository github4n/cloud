package com.iot.building.space.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：情景 实体
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 16:12
 * 修改人:
 * 修改时间：
 */
public class LocationResp implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Long id;

	/** 名称*/
	private String name;

	private String icon;
	
	private Date createTime;

	private Long tenantId;
	
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	
}
