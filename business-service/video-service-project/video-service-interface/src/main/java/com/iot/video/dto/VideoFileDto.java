package com.iot.video.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 项目名称：IOT云平台 模块名称： 功能描述：视频文件 创建人： wujianlong 创建时间：2017年9月6日 下午5:58:32 修改人：
 * wujianlong 修改时间：2017年9月6日 下午5:58:32
 */
public class VideoFileDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 文件id */
	private String fileId;

	/** 视频开始时间 */
	private Date videoStartTime;

	/** 视频结束时间 */
	private Date videoEndTime;

	private String url;

	/** 视频时常 */
	private Float videoLength;
	
	/**文件大小*/
	private int fileSize;

	/**
	 * 文件路径
	 */
	private String filePath;

	/**
	 * 选择角度
	 */
	private Integer rotation;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Float getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(Float videoLength) {
		this.videoLength = videoLength;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
}
