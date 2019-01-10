package com.iot.portal.web.vo.reply.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：产品反馈入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 14:36
 * 修改描述：
 */
@ApiModel(description = "产品反馈入参")
public class ProductFeedbackReq {

    @ApiModelProperty(name = "productId", value = "productId", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "feedbackContent", value = "反馈内容", dataType = "String")
    private String feedbackContent;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
