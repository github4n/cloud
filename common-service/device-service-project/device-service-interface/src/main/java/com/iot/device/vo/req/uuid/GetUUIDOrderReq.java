package com.iot.device.vo.req.uuid;

import com.iot.common.beans.SearchParam;

import java.util.Date;

/**
 * 查询UUID订单请求
 */
public class GetUUIDOrderReq extends SearchParam {
	
    private Long userId;
    
    private Long tenantId;
    
    /** 订单Id,批次号 */
    private Long batchNumId;

    /** 产品ID */
    private Long productId;

    /** 订单状态 */
    private Integer applyStatus;

    /** 支付状态 */
    private Integer payStatus;
    
    /** 开始时间*/
    private Date start;

    /** 结束时间 */
    private Date end;

    /**产品model*/
    private String model;

    /**产品model*/
    private String refundFlag;
    
    public String getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(String refundFlag) {
		this.refundFlag = refundFlag;
	}

	public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
