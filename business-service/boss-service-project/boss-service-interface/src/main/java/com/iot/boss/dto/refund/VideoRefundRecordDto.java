package com.iot.boss.dto.refund;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：退款申请记录Dto
 * 创建人： 490485964@qq.com
 * 创建时间：2018/5/21 15:57
 * 修改人： 490485964@qq.com
 * 修改时间：2018/5/21 15:57
 * 修改描述：
 */
@ApiModel(value = "退款申请记录实体类")
public class VideoRefundRecordDto {

    /**
     * id
     */
    private Long id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 退款操作员ID
     */
    private Long refundApplyId;

    /**
     * 退款操作员名称
     */
    private String refundApplyName;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款金额
     */
    private BigDecimal refundPrice;

    /**
     * 退款申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date refundApplyTime;

    /**
     * 审批人ID
     */
    private Long auditId;

    /**
     * 审批人名称
     */
    private String auditName;

    /**
     * 审批留言
     */
    private String auditMessage;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;

    /**
     * 审批状态(0：不通过；1、通过)
     */
    private Integer auditStatus;

    /**
     * 退款状态(0-取消,1-申请退款中，2-退款成功,3-退款失败)
     */
    private Integer refundStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getRefundApplyId() {
        return refundApplyId;
    }

    public void setRefundApplyId(Long refundApplyId) {
        this.refundApplyId = refundApplyId;
    }

    public String getRefundApplyName() {
        return refundApplyName;
    }

    public void setRefundApplyName(String refundApplyName) {
        this.refundApplyName = refundApplyName;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public BigDecimal getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Date getRefundApplyTime() {
        return refundApplyTime;
    }

    public void setRefundApplyTime(Date refundApplyTime) {
        this.refundApplyTime = refundApplyTime;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }
}
