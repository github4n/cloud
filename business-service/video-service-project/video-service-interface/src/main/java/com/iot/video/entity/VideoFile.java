package com.iot.video.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "视频文件信息")
public class VideoFile implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name = "tenantId", value = "租户ID", dataType = "Long")
	private Long tenantId;
	@ApiModelProperty(name = "planId", value = "计划id", dataType = "String")
	private String planId;
	@ApiModelProperty(name = "deviceId", value = "设备id", dataType = "String")
	private String deviceId;
	@ApiModelProperty(name = "videoType", value = "视频类型", dataType = "String")
	private String videoType;
	@ApiModelProperty(name = "videoStartTime", value = "视频开始时间", dataType = "Date")
	private Date videoStartTime;
	@ApiModelProperty(name = "videoEndTime", value = "视频结束时间", dataType = "Date")
	private Date videoEndTime;
	@ApiModelProperty(name = "fileExpDate", value = "文件失效日期", dataType = "Date")
	private Date fileExpDate;
	@ApiModelProperty(name = "eventUuid", value = "事件UUID", dataType = "String")
	private String eventUuid;
	@ApiModelProperty(name = "videoLength", value = "视频时常", dataType = "Float")
	private Float videoLength;
	@ApiModelProperty(name = "fileId", value = "文件id", dataType = "String")
	private String fileId;
	@ApiModelProperty(name = "fileSize", value = "文件大小", dataType = "Integer")
	private Integer fileSize;
	@ApiModelProperty(name = "fileType", value = "文件类型", dataType = "String")
	private String fileType;
	@ApiModelProperty(name = "filePath", value = "文件存储路径", dataType = "String")
	private String filePath;
	@ApiModelProperty(name = "dataStatus", value = "数据状态", dataType = "Integer(1：有效)")
	private Integer dataStatus;
	@ApiModelProperty(name = "createTime", value = "创建时间", dataType = "Date")
	private Date createTime;
	@ApiModelProperty(name = "updateTime", value = "修改时间", dataType = "Date")
	private Date updateTime;
	@ApiModelProperty(name = "rotation", value = "旋转角度", dataType = "Integer")
	private Integer rotation;


	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public Date getVideoStartTime() {
		return videoStartTime;
	}

	public void setVideoStartTime(Date videoStartTime) {
		this.videoStartTime = videoStartTime;
	}

	public Date getVideoEndTime() {
		return videoEndTime;
	}

	public void setVideoEndTime(Date videoEndTime) {
		this.videoEndTime = videoEndTime;
	}

	public Date getFileExpDate() {
		return fileExpDate;
	}

	public void setFileExpDate(Date fileExpDate) {
		this.fileExpDate = fileExpDate;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getEventUuid() {
		return eventUuid;
	}

	public void setEventUuid(String eventUuid) {
		this.eventUuid = eventUuid;
	}

	public Float getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(Float videoLength) {
		this.videoLength = videoLength;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}

	public VideoFile() {
	}

	public VideoFile(Date videoStartTime, Date videoEndTime, Integer fileSize, Float videoLength, String fileId, String filePath) {
		this.videoStartTime = videoStartTime;
		this.videoEndTime = videoEndTime;
		this.fileId = fileId;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.videoLength = videoLength;
	}
}
