package com.iot.device.vo.rsp;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:57 2018/4/17
 * @Modify by:
 */
@Data
@ToString
public class DeviceResp implements Serializable {

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
     * 设备名称
     */
    private String name;
    /**
     * 设备图片
     */
    private String icon;
    /**
     * ip
     */
    private String ip;
    /**
     * mac地址
     */
    private String mac;
    /**
     * 设备序列号
     */
    private String sn;
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
    private Long businessTypeId;
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
     * 设置分类id
     * device_catalog_id
     */
    private Long deviceCatalogId;
    /**
     * 产品id
     * product_id
     */
    private Long productId;
    /**
     * wifi名称
     * ssid
     */
    private String ssid;
    /**
     * 重置标识
     * reset_random
     */
    private String resetRandom;
    /**
     * 设备版本号
     */
    private String version;
    /**
     * 创建人id
     * create_by
     */
    private Long createBy;
    /**
     * 创建时间
     * create_time
     */
    private Date createTime;
    /**
     * 更新人
     * update_by
     */
    private Long updateBy;
    /**
     * 最后跟新时间
     * last_update_date
     */
    private Date lastUpdateDate;


    private String businessType;

    /**
     * 设备属性信息
     */
    private List<DeviceStateResp> stateRsps;

    /**
     * 设备在线状态
     */
    private String onlineStatus;

    /**
     * 开关状态
     */
    private int switchStatus;


    private Integer activeStatus;

    /**
     * 对应 device_type 表的 type类型
     */
    private String devType;

    private Long locationId;

    /**
     * 局域网访问IPC需要的密码
     */
    private String password;

    private String hwVersion;

    private String devModel;

    /**
     * 配置网络方式
     * config_net_mode
     */
    private String configNetMode;

    private List<ProductConfigNetmodeRsp> configNetmodeRsps;
    /**
     * 设备操作地址
     */
    private Long address;

    private String supplier;

    private Integer order;

    private String conditional;

    private String p2pId;

    private Integer isAppDev;
    /**
     * 设备状态属性
     */
    private Map<String, Object> stateProperty;
    /**
     * 产品名称
     * productName
     */
    private String productName;

    public String getConditional() {
        return conditional;
    }

    public void setConditional(String conditional) {
        this.conditional = conditional;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
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

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
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

    public Long getDeviceCatalogId() {
        return deviceCatalogId;
    }

    public void setDeviceCatalogId(Long deviceCatalogId) {
        this.deviceCatalogId = deviceCatalogId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getResetRandom() {
        return resetRandom;
    }

    public void setResetRandom(String resetRandom) {
        this.resetRandom = resetRandom;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }


    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public List<DeviceStateResp> getStateRsps() {
        return stateRsps;
    }

    public void setStateRsps(List<DeviceStateResp> stateRsps) {
        this.stateRsps = stateRsps;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public int getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(int switchStatus) {
        this.switchStatus = switchStatus;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Map<String, Object> getStateProperty() {
        return stateProperty;
    }

    public void setStateProperty(Map<String, Object> stateProperty) {
        this.stateProperty = stateProperty;
    }

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }
}
