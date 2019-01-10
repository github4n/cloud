package com.iot.boss.entity.refund;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：退款申请记录实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 9:57
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 9:57
 * 修改描述：
 */
@ApiModel(value = "退款申请记录实体类")
public class VideoRefundRecord implements Serializable{

    @ApiModelProperty(name = "id",value = "id",dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "orderId",value = "订单id",dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "planId",value = "计划id",dataType = "String")
    private String planId;

    @ApiModelProperty(name = "tenantId",value = "计划id",dataType = "Long")
    private Long tenantId;

    @ApiModelProperty(name = "refundApplyId",value = "退款操作员ID",dataType = "Long")
    private Long refundApplyId;

    @ApiModelProperty(name = "refundApplyName",value = "退款操作员名称",dataType = "String")
    private String refundApplyName;

    @ApiModelProperty(name = "refundReason",value = "退款原因",dataType = "String")
    private String refundReason;

    @ApiModelProperty(name = "refundPrice",value = "退款金额",dataType = "BigDecimal")
    private BigDecimal refundPrice;

    @ApiModelProperty(name = "refundApplyTime",value = "退款申请时间",dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date refundApplyTime;

    @ApiModelProperty(name = "auditId",value = "审批人ID",dataType = "Long")
    private Long auditId;

    @ApiModelProperty(name = "auditName",value = "审批人名称",dataType = "String")
    private String auditName;

    @ApiModelProperty(name = "auditMessage",value = "审批留言",dataType = "String")
    private String auditMessage;

    @ApiModelProperty(name = "auditTime",value = "审批时间",dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date auditTime;

    @ApiModelProperty(name = "auditStatus",value = "审批状态(0：不通过；1、通过)",dataType = "Integer")
    private Integer auditStatus;

    @ApiModelProperty(name = "refundStatus",value = "退款状态(0-取消,1-申请退款中，2-退款成功,3-退款失败)",dataType = "Integer")
    private Integer refundStatus;

    @ApiModelProperty(name = "userId",value = "申请退款用户uuid",dataType = "String")
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
