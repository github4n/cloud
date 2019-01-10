package com.iot.device.vo.rsp.ota;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: linjihuang
 * @Descrpiton:
 * @Date: 17:21 2018/9/6
 * @Modify by:
 */
public class OtaFileInfoResp implements Serializable {

	/**
	 * 主键id 
	 * id
	 */
	private Long id;

	/**
	 * 产品id 
	 * productId
	 */
	private Long productId;
	
	/**
	 * 产品名称 
	 * productName
	 */
	private String productName;

	/**
	 * 版本号 
	 * version
	 */
	private String version;

	/**
	 * 租户id 
	 * tenantId
	 */
	private Long tenantId;

	/**
	 * 区域id 
	 * locationId
	 */
	private Long locationId;

	/**
	 * 创建人id 
	 * create_by
	 */
	private String createBy;
	/**
	 * 创建时间 
	 * create_time
	 */
	private Date createTime;
	/**
	 * 更新人 
	 * update_by
	 */
	private String updateBy;
	/**
	 * 更新时间 
	 * update_time
	 */
	private Date updateTime;
	/**
	 * 下载URL 
	 * download_url
	 */
	private String downloadUrl;
	
	/**
	 * 文件MD5 
	 * md5
	 */
	private String md5;
	
	/**
	 * 设备model
	 * model
	 */
	private String model;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
