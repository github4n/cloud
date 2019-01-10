package com.iot.file.dto;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：文件服务
 * 功能描述：文件dto
 * 创建人： zhouzongwei
 * 创建时间：2018年3月17日 下午5:27:52
 * 修改人： zhouzongwei
 * 修改时间：2018年3月17日 下午5:27:52
 */
public class FileDto {
	
	/**
	 * 预签名url
	 */
	private String presignedUrl;

	/**
	 * 文件id
	 */
	private String fileId;
	
	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 描述：默认构造
	 * @author mao2080@sina.com
	 * @created 2018/3/26 14:43
	 * @param presignedUrl
 * @param fileId
 * @param fileName 文件名称
	 * @return
	 */
	public FileDto(String presignedUrl, String fileId, String fileName) {
		this.presignedUrl = presignedUrl;
		this.fileId = fileId;
		this.fileName = fileName;
	}

	public FileDto() {
	}

	public String getPresignedUrl() {
		return presignedUrl;
	}

	public void setPresignedUrl(String presignedUrl) {
		this.presignedUrl = presignedUrl;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
