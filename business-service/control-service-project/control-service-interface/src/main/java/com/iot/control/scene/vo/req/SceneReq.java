package com.iot.control.scene.vo.req;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 项目名称: 立达信IOT云平台
 * 模块名称：
 * 功能描述：情景 实体
 * 创建人: yuChangXing
 * 创建时间: 2018/4/16 16:12
 * 修改人:
 * 修改时间：
 */
public class SceneReq implements Serializable {

	private static final long serialVersionUID = 2025580783894328456L;

	private Long id;

	private String ids;

	private List sceneIds;

	/** 情景名称*/
	private String sceneName;

	/** 空间ID*/
	private Long spaceId;

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

	/** 组织机构id*/
	private Long orgId;

	/** 图标*/
	private String icon;

	// 1.设备类型  2.全量设置 3.业务类型
	private Integer setType;

	// 排序
	private Integer sort;

	private Integer uploadStatus;

	private Long locationId;

	private Long templateId;

	/** Ble设备控制场景ID*/
	private Long devSceneId;
	
	private Integer silenceStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSetType() {
		return setType;
	}

	public void setSetType(Integer setType) {
		this.setType = setType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Long getDevSceneId() {
		return devSceneId;
	}

	public void setDevSceneId(Long devSceneId) {
		this.devSceneId = devSceneId;
	}

	public Integer getSilenceStatus() {
		return silenceStatus;
	}

	public void setSilenceStatus(Integer silenceStatus) {
		this.silenceStatus = silenceStatus;
	}

	public List getSceneIds() {
		return sceneIds;
	}

	public void setSceneIds(List sceneIds) {
		this.sceneIds = sceneIds;
	}
}
