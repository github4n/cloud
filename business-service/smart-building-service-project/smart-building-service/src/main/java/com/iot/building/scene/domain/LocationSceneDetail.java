package com.iot.building.scene.domain;

import java.io.Serializable;
import java.util.Date;


public class LocationSceneDetail implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * locationSceneId
	 */
	private Long locationSceneId;

	/**
	 * 情景
	 */
	private Long sceneId;

	/**
	 * 租户id
	 */
	private Long tenantId;

	/**
	 * 删除标志
	 */
	private int delFlag;

	/**
	 * 创建者
	 */
	private Long createBy;

	/**
	 * 更新者
	 */
	private Long updateBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 排序
	 */
	private Integer sort;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLocationSceneId() {
		return locationSceneId;
	}

	public void setLocationSceneId(Long locationSceneId) {
		this.locationSceneId = locationSceneId;
	}

	public Long getSceneId() {
		return sceneId;
	}

	public void setSceneId(Long sceneId) {
		this.sceneId = sceneId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
