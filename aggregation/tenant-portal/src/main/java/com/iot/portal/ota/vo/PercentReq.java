package com.iot.portal.ota.vo;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： nongchongwei
 * 创建时间：2018年09月05日 14:27
 * 修改人： nongchongwei
 * 修改时间：2018年09月05日 14:27
 */
public class PercentReq {
    /**
     * 产品Id
     */
    private String productId;
    /**
     * 租户Id
     */
    private Long tenantId;
    /**
     * 版本列表
     */
    private List<String> versionList;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public List<String> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<String> versionList) {
        this.versionList = versionList;
    }
}
