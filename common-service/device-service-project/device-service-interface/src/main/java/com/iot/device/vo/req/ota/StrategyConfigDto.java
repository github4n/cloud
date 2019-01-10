package com.iot.device.vo.req.ota;

import com.iot.device.vo.req.ota.StrategyConfigReq;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人： nongchongwei
 * 创建时间：2018年11月15日 14:31
 * 修改人： nongchongwei
 * 修改时间：2018年11月15日 14:31
 */
public class StrategyConfigDto {

    private String productId;

    private Long tenantId;

    private Long planId;

    private List<StrategyConfigReq> strategyConfigList;

    public List<StrategyConfigReq> getStrategyConfigList() {
        return strategyConfigList;
    }

    public void setStrategyConfigList(List<StrategyConfigReq> strategyConfigList) {
        this.strategyConfigList = strategyConfigList;
    }

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

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}
