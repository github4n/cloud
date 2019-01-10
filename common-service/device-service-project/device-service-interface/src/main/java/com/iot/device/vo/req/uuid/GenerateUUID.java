package com.iot.device.vo.req.uuid;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：视频管理模块
 * 功能描述：设备扩展
 * 创建人： 李帅
 * 创建时间：2017年9月12日 下午1:47:01
 * 修改人：李帅
 * 修改时间：2017年9月12日 下午1:47:01
 */
public class GenerateUUID implements Serializable{

	private static final long serialVersionUID = -6605703057339673972L;
	
	/**
	 *  批次号
	 */
	private Long batchNum;
	
	/**
	 * 租户ID
	 */
	private Long tenantId;
	
	/**
	 * 支付状态
	 */
	private int payStatus;
	
	/**
	 * 产品ID
	 */
	private Long productId;
	
	/**
	 *  有效时长
	 */
	private BigDecimal uuidValidityDays;
	
	/**
	 *  uuid生成量
	 */
	private int createNum;

    /**
     * 设备类型id
     */
    private Long deviceTypeId;

    /**
     * 是否直连设备0否、1是
     */
    private Long isDirectDevice;

    /**
	 *  设备真实类型
	 */
	private String deviceType;

	public Long getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(Long batchNum) {
		this.batchNum = batchNum;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getUuidValidityDays() {
		return uuidValidityDays;
	}

	public void setUuidValidityDays(BigDecimal uuidValidityDays) {
		this.uuidValidityDays = uuidValidityDays;
	}

	public int getCreateNum() {
		return createNum;
	}

	public void setCreateNum(int createNum) {
		this.createNum = createNum;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public Long getIsDirectDevice() {
		return isDirectDevice;
	}

	public void setIsDirectDevice(Long isDirectDevice) {
		this.isDirectDevice = isDirectDevice;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}
