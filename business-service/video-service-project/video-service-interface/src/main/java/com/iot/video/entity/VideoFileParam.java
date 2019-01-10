package com.iot.video.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * 项目名称：IOT云平台 模块名称： 功能描述：视频列表参数 创建人： wujianlong 创建时间：2017年9月7日 下午2:05:59
 * 修改人： wujianlong 修改时间：2017年9月7日 下午2:05:59
 */
public class VideoFileParam implements Serializable {

	private static final long serialVersionUID = -2861301668793487924L;

	/** 租户ID */
	private Long tenantId;

	/** 用户id */
	private String userId;

	/** 计划id */
	private String planId;

	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/** 结束时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/** 文件类型 */
	private String fileType;

	/** 设备id */
	private String deviceId;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
