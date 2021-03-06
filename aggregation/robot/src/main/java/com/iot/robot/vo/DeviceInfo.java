package com.iot.robot.vo;

import com.iot.device.vo.rsp.ServiceModulePropertyResp;

import java.io.Serializable;
import java.util.List;

public class DeviceInfo implements Serializable {

    /**
     * 设备id
     */
    private String deviceId;
    private Long tenantId;
    private Long productId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备类型
     */
    private String deviceType;


    private Long deviceTypeId;

    /**
     * 是否直连设备0否、1是
     * is_direct_device
     */
    private Integer isDirectDevice;
    /**
     * 设备功能列表
     */
    //private List<DeviceFunResp> deviceFunList;
    /**
     * 设备功能列表(功能组)
     */
    private List<ServiceModulePropertyResp> serviceModulePropertyList;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Integer getIsDirectDevice() {
        return isDirectDevice;
    }

    public void setIsDirectDevice(Integer isDirectDevice) {
        this.isDirectDevice = isDirectDevice;
    }

    /*public List<DeviceFunResp> getDeviceFunList() {
        return deviceFunList;
    }

    public void setDeviceFunList(List<DeviceFunResp> deviceFunList) {
        this.deviceFunList = deviceFunList;
    }*/

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

    public List<ServiceModulePropertyResp> getServiceModulePropertyList() {
        return serviceModulePropertyList;
    }

    public void setServiceModulePropertyList(List<ServiceModulePropertyResp> serviceModulePropertyList) {
        this.serviceModulePropertyList = serviceModulePropertyList;
    }
}
