package com.iot.center.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
public class DeviceVO implements Serializable {

    public static final Integer IS_DIRECT_DEVICE = 1;
    public static final Integer IS_NOT_DIRECT_DEVICE = 0;
    /**
     * id
     */
    private Long id;

    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 父id
     */
    private String parentId;

    /**
     *设备名称
     */
    private String name;
    /**
     * ip
     */
    private String ip;
    /**
     * mac地址
     */
    private String mac;
    /**
     * 租户ID
     * tenant_id
     */
    private Long tenantId;
    /**
     * 是否直连设备0否、1是
     * is_direct_device
     */
    private Integer isDirectDevice;
    /**
     * 业务类型
     * business_type_id
     */
    private String businessTypeId;
    /**
     * MAC 地址 hue网关用
     * reality_id
     */
    private String realityId;
    /**
     * 扩展名 hue用
     * extra_name
     */
    private String extraName;
    /**
     * 设置类型id
     * device_type_id
     */
    private Long deviceTypeId;
    /**
     * 产品id
     * product_id
     */
    private Long productId;
    /**
     * 创建人id
     * create_by
     */
    private Long createBy;
    /**
     * 创建时间
     * create_time
     */
    private String createTime;
    /**
     * 更新人
     * update_by
     */
    private Long updateBy;
    /**
     * 最后跟新时间
     * last_update_date
     */
    private String lastUpdateDate;


    private String businessType;

    /**
     * 开关状态
     */
    private int switchStatus;
    
    /**
     * 在线状态
     */
    private String onlineStatus;
    
    /**
     * 设备版本号
     */
    private String version;

    private String hwVersion;
    
    private String devType;
    
    /**
     * 产品名称
     * product_name
     */
    private String productName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getRealityId() {
        return realityId;
    }

    public void setRealityId(String realityId) {
        this.realityId = realityId;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(int switchStatus) {
        this.switchStatus = switchStatus;
    }

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHwVersion() {
		return hwVersion;
	}

	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
