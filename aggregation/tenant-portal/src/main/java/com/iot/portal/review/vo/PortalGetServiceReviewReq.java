package com.iot.portal.review.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：语音服务审核请求对象
 * 功能描述：语音服务审核请求对象
 * 创建人： 李帅
 * 创建时间：2018年10月25日 上午11:24:23
 * 修改人：李帅
 * 修改时间：2018年10月25日 上午11:24:23
 */
@ApiModel(value="PortalGetServiceReviewReq", description="语音服务审核请求对象")
public class PortalGetServiceReviewReq{

    @ApiModelProperty(name="语音服务id",value="serviceId")
    private String serviceId;

    @ApiModelProperty(name="产品id",value="productId")
    private Long productId;

    public PortalGetServiceReviewReq() {

    }

	public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

}
