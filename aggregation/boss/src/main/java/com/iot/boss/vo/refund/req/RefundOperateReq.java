package com.iot.boss.vo.refund.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 功能描述：退款入参
 * 创建人： mao2080@sina.com
 * 创建时间：2018/11/14 9:17
 * 修改人： mao2080@sina.com
 * 修改时间：2018/11/14 9:17
 * 修改描述：
 */
@ApiModel(description = "退款操作入参")
public class RefundOperateReq extends RefundReq{

    @ApiModelProperty(name = "userId", value = "购买用户ID", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "refundObjectId", value = "退款对象id", dataType = "Long")
    private Long refundObjectId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRefundObjectId() {
        return refundObjectId;
    }

    public void setRefundObjectId(Long refundObjectId) {
        this.refundObjectId = refundObjectId;
    }



}
