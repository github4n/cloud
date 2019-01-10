package com.iot.device.vo.req.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：重开审核入参
 * 创建人： yeshiyuan
 * 创建时间：2018/10/25 17:14
 * 修改人： yeshiyuan
 * 修改时间：2018/10/25 17:14
 * 修改描述：
 */
@ApiModel(description = "重开审核入参")
public class ReopenAuditReq {

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "String")
    private Long productId;

    @ApiModelProperty(name = "operateTime", value = "操作时间", dataType = "Date")
    private Date operateTime;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Long")
    private Long userId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
