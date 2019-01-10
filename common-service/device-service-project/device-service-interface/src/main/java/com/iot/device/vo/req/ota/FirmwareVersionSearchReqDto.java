package com.iot.device.vo.req.ota;

import com.iot.common.beans.SearchParam;

import java.util.Set;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：ota版本查询参数
 * 创建人： maochengyuan
 * 创建时间：2018/7/25 13:54
 * 修改人： maochengyuan
 * 修改时间：2018/7/25 13:54
 * 修改描述：
 */
public class FirmwareVersionSearchReqDto extends SearchParam {

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * ota版本
     */
    private Set<String> otaVersion;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Set<String> getOtaVersion() {
        return otaVersion;
    }

    public void setOtaVersion(Set<String> otaVersion) {
        this.otaVersion = otaVersion;
    }

}