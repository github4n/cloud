package com.iot.device.vo;

import java.io.Serializable;

public class ProductUUIDVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 产品id
     */
    private Long productId;

    /**
     * 是否直连设备0否、1是
     */
    private Long isDirectDevice;

    /**
     * 租户id
     */
    private Long tenantId;
    
    /**
     * 设备类型id
     */
    private Long deviceTypeId;
    
    /**
	 *  设备真实类型
	 */
	private String deviceType;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getIsDirectDevice() {
		return isDirectDevice;
	}

	public void setIsDirectDevice(Long isDirectDevice) {
		this.isDirectDevice = isDirectDevice;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}