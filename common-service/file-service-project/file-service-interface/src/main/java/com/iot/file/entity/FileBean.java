package com.iot.file.entity;

import java.util.Date;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：文件bean
 * 创建人： zhouzongwei
 * 创建时间：2018年3月9日 下午2:41:58
 * 修改人： zhouzongwei
 * 修改时间：2018年3月9日 下午2:41:58
 */
public class FileBean {

	/**
	 * 预签名url
	 */
	private String presignedUrl;

	/**
	 * 文件id
	 */
	private Long id;
	
	/**
	 * 文件uuid
	 */
	private String fileId;
	
	/**
	 * 文件类型
	 */
	private String fileType;
	
	/**
	 * 租户id
	 */
	private Long tenantId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 文件路径
	 */
	private String filePath;
	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 文件名称
	 */
	private String fileName;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPresignedUrl() {
		return presignedUrl;
	}

	public void setPresignedUrl(String presignedUrl) {
		this.presignedUrl = presignedUrl;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public FileBean() {
	}


	public FileBean(String presignedUrl, String fileId, String fileType, String filePath) {
		this.presignedUrl = presignedUrl;
		this.fileId = fileId;
		this.fileType = fileType;
		this.filePath = filePath;
	}

	public FileBean(String fileId, String fileType, Long tenantId, Date createTime, String filePath) {
		this.fileId = fileId;
		this.fileType = fileType;
		this.tenantId = tenantId;
		this.createTime = createTime;
		this.filePath = filePath;
	}
}