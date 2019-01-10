package com.iot.boss.entity.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：退款操作日志实体类
 * 创建人： yeshiyuan
 * 创建时间：2018/5/21 10:45
 * 修改人： yeshiyuan
 * 修改时间：2018/5/21 10:45
 * 修改描述：
 */
@ApiModel(value = "退款操作日志实体类")
public class VideoRefundLog {
    @ApiModelProperty(name = "id",value = "id",dataType = "Long")
    private Long id;

    @ApiModelProperty(name = "refundId",value = "退款订单ID",dataType = "Long")
    private Long refundId;

    @ApiModelProperty(name = "operatorId",value = "操作者id",dataType = "Long")
    private Long operatorId;

    @ApiModelProperty(name = "refundPrice",value = "退款金额",dataType = "BigDecimal")
    private BigDecimal refundPrice;

    @ApiModelProperty(name = "createTime",value = "创建时间",dataType = "Date")
    private Date createTime;

    @ApiModelProperty(name = "refundRemark",value = "退款备注",dataType = "String")
    private String refundRemark;

    @ApiModelProperty(name = "refundStatus",value = "退款状态(0-取消,1-申请退款中，2-退款成功,3-退款失败)",dataType = "Integer")
    private Integer refundStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public BigDecimal getRefundPrice() {
        return refundPrice;
    }

    public void setRefundPrice(BigDecimal refundPrice) {
        this.refundPrice = refundPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRefundRemark() {
        return refundRemark;
    }

    public void setRefundRemark(String refundRemark) {
        this.refundRemark = refundRemark;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public VideoRefundLog(Long refundId, Long operatorId, BigDecimal refundPrice,Integer refundStatus,String refundRemark) {
        this.refundId = refundId;
        this.operatorId = operatorId;
        this.refundPrice = refundPrice;
        this.refundStatus = refundStatus;
        this.refundRemark = refundRemark;
    }
}
