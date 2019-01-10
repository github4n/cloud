package com.iot.portal.web.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:41 2018/7/10
 * @Modify by:
 */
public class PortalGetServiceModuleReq implements Serializable {

    private Long productId;

    private List<Long> serviceModuleList;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<Long> getServiceModuleList() {
        return serviceModuleList;
    }

    public void setServiceModuleList(List<Long> serviceModuleList) {
        this.serviceModuleList = serviceModuleList;
    }
}
