package com.iot.control.space.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class DeviceVo implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * parent_id  extends device_id
     */
    private String parentId;
    /**
     * 设备id
     */
    private String deviceId;

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
     * 设置类型id  extend device_type table id
     * device_type_id
     */
    private Long deviceTypeId;
    /**
     * 产品id  extend product table id
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
     * 最后跟新时间
     * last_update_date
     */
    private Date lastUpdateDate;

    ////////////////////////////////////////////
    private String onlineStatus;

    private Long parentSpaceId;

    private Long spaceId;

    private String devType;

    //排序
    private Integer order;

    private Map<String, Object> stateProperty;


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

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Long getParentSpaceId() {
        return parentSpaceId;
    }

    public void setParentSpaceId(Long parentSpaceId) {
        this.parentSpaceId = parentSpaceId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
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
}
