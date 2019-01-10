package com.iot.portal.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 11:30 2018/7/4
 * @Modify by:
 */
public class PortalServiceModuleReq implements Serializable {

    private Long productId;

    private List<Long> serviceModuleIds;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<Long> getServiceModuleIds() {
        return serviceModuleIds;
    }

    public void setServiceModuleIds(List<Long> serviceModuleIds) {
        this.serviceModuleIds = serviceModuleIds;
    }
}
