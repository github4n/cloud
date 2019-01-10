package com.iot.portal.ota.vo;

import com.iot.common.beans.SearchParam;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public class UpgradeLogSearchReq extends SearchParam {

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 产品id
     */
    private String productId;

    /**
     * 升级结果
     */
    private String upgradeResult;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUpgradeResult() {
        return upgradeResult;
    }

    public void setUpgradeResult(String upgradeResult) {
        this.upgradeResult = upgradeResult;
    }
}