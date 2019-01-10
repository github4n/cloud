package com.iot.boss.vo.reply.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：cloud
 * 功能描述：产品留言入参
 * 创建人： yeshiyuan
 * 创建时间：2018/11/2 14:36
 * 修改人： yeshiyuan
 * 修改时间：2018/11/2 14:36
 * 修改描述：
 */
@ApiModel(description = "产品留言入参")
public class ProductLeaveMessageReq {

    @ApiModelProperty(name = "productId", value = "productId", dataType = "Long")
    private Long productId;

    @ApiModelProperty(name = "operateDesc", value = "操作描述", dataType = "String")
    private String operateDesc;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }
}
