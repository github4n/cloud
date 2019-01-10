package com.iot.device.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:11 2018/7/3
 * @Modify by:
 */
@ApiModel("功能模组信息")
public class AddOrUpdateServiceModuleReq implements Serializable {

    //对应的产品
    @ApiModelProperty(value = "产品id")
    private Long productId;

    //所有的功能组
    @ApiModelProperty(value = "功能组集合信息")
    private List<AddServiceModuleReq> serviceModuleList;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<AddServiceModuleReq> getServiceModuleList() {
        return serviceModuleList;
    }

    public void setServiceModuleList(List<AddServiceModuleReq> serviceModuleList) {
        this.serviceModuleList = serviceModuleList;
    }
}
