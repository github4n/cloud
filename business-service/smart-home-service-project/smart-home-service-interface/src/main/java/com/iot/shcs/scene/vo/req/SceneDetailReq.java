package com.iot.shcs.scene.vo.req;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称: 立达信IOT云平台
 * 模块名称：
 * 功能描述：情景详情 实体
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 16:12
 * 修改人:
 * 修改时间：
 */
public class SceneDetailReq implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Long id;

	/** 情景id*/
	private Long sceneId;

	/** 设备id*/
	private String deviceId;

	/** 空间id*/
	private Long spaceId;

	/** 设备类型id*/
	private Long deviceTypeId;

	/** 目标值json格式*/
	private String targetValue;

	/** 创建者*/
	private Long createBy;

	/** 更新者*/
	private Long updateBy;

	/** 创建时间*/
	private Date createTime;

	/** 更新时间*/
	private Date updateTime;

	/** 租户ID*/
	@NotNull
	private Long tenantId;

	/** 排序*/
	private Integer sort;

	/** 设备名称*/
	private String deviceName;

	/** 设备类型名称*/
	private String deviceTypeName;

	/** 设备真实类型 device_type.type */
	private String deviceType;

	/** 情景名称*/
	private String sceneName;

	/** 设备类型*/
	private Long deviceCategoryId;
	
	/** 区域ID*/
	private Long locationId;
	
	/** 业务类型ID*/
	private Long businessTypeId;
	
	/** 业务类型*/
	private String businessType;

	/** 调用设备事件的方法*/
	private String method;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSceneId() {
		return sceneId;
	}

	public void setSceneId(Long sceneId) {
		this.sceneId = sceneId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
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

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Long getDeviceCategoryId() {
		return deviceCategoryId;
	}

	public void setDeviceCategoryId(Long deviceCategoryId) {
		this.deviceCategoryId = deviceCategoryId;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
