package com.iot.device.vo.req;

import java.io.Serializable;
import java.util.List;

public class TimerConfigReq implements Serializable {

    /**定时配置*/
    private List<String> timerTypes;

    /**产品id*/
    private Long productId;

    /**租户id*/
    private Long tenantId;

    /**用户id*/
    private Long userId;

    public TimerConfigReq() {

    }

    public TimerConfigReq(List<String> timerTypes, Long productId, Long tenantId, Long userId) {
        this.timerTypes = timerTypes;
        this.productId = productId;
        this.tenantId = tenantId;
        this.userId = userId;
    }

    public List<String> getTimerTypes() {
        return timerTypes;
    }

    public void setTimerTypes(List<String> timerTypes) {
        this.timerTypes = timerTypes;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
