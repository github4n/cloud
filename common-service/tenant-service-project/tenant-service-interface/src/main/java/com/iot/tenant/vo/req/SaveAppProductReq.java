package com.iot.tenant.vo.req;

import java.util.List;

/**
 * 描述：保存app关联产品请求
 * 创建人： LaiGuiMing
 * 创建时间： 2018/7/10 9:42
 */
public class SaveAppProductReq {
    private Long id;
    private Long appId;
    private Long productId;
    private String productName;
    private String iconFileId;
    private Long tenantId;
    private Long deviceTypeId;
    private List<Long> networkTypeIds;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIconFileId() {
        return iconFileId;
    }

    public void setIconFileId(String iconFileId) {
        this.iconFileId = iconFileId;
    }

    public Long getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Long deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public List<Long> getNetworkTypeIds() {
        return networkTypeIds;
    }

    public void setNetworkTypeIds(List<Long> networkTypeIds) {
        this.networkTypeIds = networkTypeIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SaveAppProductReq{" +
                "id=" + id +
                ", appId=" + appId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", iconFileId='" + iconFileId + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
