package com.iot.shcs.scene.vo.req;

import java.io.Serializable;
import java.util.Date;


public class LocationSceneReq implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Integer pageNum =1;

	private Integer pageSize =10;

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * id
	 */
	private Long id;

	/**
	 * ？？？？？
	 */
	private String code;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 删除标志
	 */
	private int delFlag;

	/**
	 * 租户id
	 */
	private Long tenantId;

	/**
	 * 区域ID
	 */
	private Long locationId;

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
	 * 更新时间
	 */
	private Date updateTime;

	/** 栋Id */
	private Long buildId;

	/** 栋名称 */
	private String buildName;

	/** 层Id */
	private Long floorId;

	/** 层名称 */
	private String floorName;

	 /**
	 *排序
	 */
	private Integer sort;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getBuildId() {
		return buildId;
	}

	public void setBuildId(Long buildId) {
		this.buildId = buildId;
	}

	public Long getFloorId() {
		return floorId;
	}

	public void setFloorId(Long floorId) {
		this.floorId = floorId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
}
