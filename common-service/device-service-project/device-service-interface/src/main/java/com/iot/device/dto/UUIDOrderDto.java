package com.iot.device.dto;

import java.io.Serializable;
import java.util.Date;

public class UUIDOrderDto implements Serializable {

    private static final long serialVersionUID =-1L;

    /** 订单Id,批次号 */
    private Long batchNumId;
    /** 订单id，对应order_record表主键 */
    private String orderId;
    /** 产品ID */
    private Integer productId;
    /** UUID所属产品名称 */
    private String productName;
    /** 产品型号 */
    private String productModel;

   /** UUID商品ID，可查询到对应的方案名称 */
    private Long goodsId;

    /** 订单状态 */
    private Integer applyStatus;

    /** 支付状态 */
    private Integer payStatus;

    /** 用户ID */
    private Long userId;

    /** 租户ID */
    private Long tenantId;

    /** 本批次UUID生成数量 */
    private Integer uuidNum;

    /** 申请时间 */
    private Date createTime;

    public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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

    public Integer getUuidNum() {
        return uuidNum;
    }

    public void setUuidNum(Integer uuidNum) {
        this.uuidNum = uuidNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
