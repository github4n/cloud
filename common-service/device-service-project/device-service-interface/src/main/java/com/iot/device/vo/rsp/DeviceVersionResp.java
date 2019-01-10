package com.iot.device.vo.rsp;

import java.io.Serializable;

/**
 * 模块名称：设备
 * 功能描述：设备版本
 * 创建人： zhouzongwei
 * 创建时间：2017年5月18日 下午1:52:26
 * 修改人： zhouzongwei
 * 修改时间：2017年5月18日 下午1:52:26
 */
public class DeviceVersionResp implements Serializable {

	/**序列id*/
	private static final long serialVersionUID = 6470113662647064053L;
	
	/**版本序号*/
	private String versionNumber;
	
	/**序列号*/
	private String versionCode;
	
	/**配置类型*/
	private String configType;
	
	/**是否更新*/
	private String isUpdate;
	
	/**创建时间*/
	private String createdTime;
	
	/**配置版本号*/
	private String configVersionCode;
	
	/**产品id*/
	private String productId;
	
	/**设备id*/
	private String deviceId;
	
	/**用户id*/
	private String userId;

	/**文件key***/
	private String fileKey;
	
	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	
	public String getConfigVersionCode() {
		return configVersionCode;
	}

	public void setConfigVersionCode(String configVersionCode) {
		this.configVersionCode = configVersionCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getFileKey() {
		return fileKey;
	}

	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	
	@Override
	public String toString() {
		return "DeviceVersion [versionNumber=" + versionNumber
				+ ", versionCode=" + versionCode + ", configType=" + configType
				+ ", isUpdate=" + isUpdate + ", createdTime=" + createdTime
				+ ", configVersionCode=" + configVersionCode + ", productId="
				+ productId + ", deviceId=" + deviceId + ", userId=" + userId
				+ ", fileKey=" + fileKey + "]";
	}
}
