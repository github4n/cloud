package com.iot.device.vo.req.ota;

import java.util.Set;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：设备搜索条件
 * 创建人： mao2080@sina.com
 * 创建时间：2018/8/17 9:34
 * 修改人： mao2080@sina.com
 * 修改时间：2018/8/17 9:34
 * 修改描述：
 */
public class DeviceQueryReq {

    /**用户id*/
    private Long userId;

    /**产品id*/
    private Long productId;

    /**设备UUID集合*/
    private Set<String> deviceIds;

    /**产品类别*/
    private String deviceType;

    public DeviceQueryReq() {
    }

    public DeviceQueryReq(Long userId, Long productId, Set<String> deviceIds, String deviceType) {
        this.userId = userId;
        this.productId = productId;
        this.deviceIds = deviceIds;
        this.deviceType = deviceType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Set<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(Set<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

}
