package com.iot.device.vo.req.ota;

import com.iot.common.beans.SearchParam;
import lombok.Data;
import lombok.ToString;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Data
@ToString
public class UpgradeLogReq  extends SearchParam {

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 升级结果
     */
    private String upgradeResult;
    /**
     * 租户id
     */
    private Long tenantId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUpgradeResult() {
        return upgradeResult;
    }

    public void setUpgradeResult(String upgradeResult) {
        this.upgradeResult = upgradeResult;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "UpgradeLogReq{" +
                "productId=" + productId +
                ", upgradeResult='" + upgradeResult + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}