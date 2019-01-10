package com.iot.device.vo.req.uuid;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：退款结果操作入参
 * 功能描述：退款结果操作入参
 * 创建人： 李帅
 * 创建时间：2018年11月15日 下午3:45:42
 * 修改人：李帅
 * 修改时间：2018年11月15日 下午3:45:42
 */
@ApiModel(description = "退款结果操作入参")
public class UUIDRefundOperateReq {

	@ApiModelProperty(name = "refundSum", value = "退款金额", dataType = "BigDecimal")
    private BigDecimal refundSum;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "refundReason", value = "退款原因", dataType = "String")
    private String refundReason;

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "Long")
    private Long tenantId;
    
	@ApiModelProperty(name = "userId", value = "购买用户ID", dataType = "Long")
    private Long userId;

    @ApiModelProperty(name = "refundObjectId", value = "退款对象id", dataType = "Long")
    private Long refundObjectId;

    public BigDecimal getRefundSum() {
		return refundSum;
	}

	public void setRefundSum(BigDecimal refundSum) {
		this.refundSum = refundSum;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
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

    public Long getRefundObjectId() {
        return refundObjectId;
    }

    public void setRefundObjectId(Long refundObjectId) {
        this.refundObjectId = refundObjectId;
    }
}
