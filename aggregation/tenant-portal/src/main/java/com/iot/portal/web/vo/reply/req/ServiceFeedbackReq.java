package com.iot.portal.web.vo.reply.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：第三方接入留言入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 14:36
 * 修改描述：
 */
@ApiModel(description = "第三方接入留言入参")
public class ServiceFeedbackReq {

    @ApiModelProperty(name = "productId", value = "productId", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "serviceId", value = "serviceId", dataType = "Long")
    private Long serviceId;

    @ApiModelProperty(name = "operateDesc", value = "操作描述", dataType = "String")
    private String operateDesc;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }
}
