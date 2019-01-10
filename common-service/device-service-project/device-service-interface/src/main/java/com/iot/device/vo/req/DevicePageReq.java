package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:51 2018/4/25
 * @Modify by:
 */
public class DevicePageReq implements Serializable {

    private Integer pageNum =1;

    private Integer pageSize =10;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;

    private Integer deviceTypeId;
    private Long locationId;
    /**
     * 是否直连设备0否、1是
     * is_direct_device
     */
    private Integer isDirectDevice;

    private String deviceId;
    
    private Long businessTypeId;
    
    private List<String> MountDeviceList;
    /**
     *设备名称
     */
    private String deviceName;
    
    /**
     *  是否检查版本
     */
    private Integer isCheckVersion;
    
    /**
     *  产品ID
     */
    private Long productId;
    
    /**
     *  组织ID
     */
    private Long orgId;

    public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(Integer isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

	public List<String> getMountDeviceList() {
		return MountDeviceList;
	}

	public void setMountDeviceList(List<String> mountDeviceList) {
		MountDeviceList = mountDeviceList;
	}

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getIsCheckVersion() {
		return isCheckVersion;
	}

	public void setIsCheckVersion(Integer isCheckVersion) {
		this.isCheckVersion = isCheckVersion;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
}
