package com.iot.device.dto;

import java.io.Serializable;
import java.util.Date;

public class UUIDInfoDto implements Serializable {

    private static final long serialVersionUID =-1L;
    /** UUID所属批次 */
    private Long batchNumId;
    /** UUID */
    private String uuid;
    /** 产品ID */
    private Long productId;
    /** 产品名称 */
    private String productName;
    /** 产品型号 */
    private String productModel;
    /** 商品ID，可查询产品方案 */
    private Long goodsId;
    /** 激活状态 */
    private Integer activeStatus;
    /** 首次激活时间 */
    private Date activeTime;
    /** 最近上线时间 */
    private Date lastLoginTime;

    public Long getBatchNumId() {
        return batchNumId;
    }

    public void setBatchNumId(Long batchNumId) {
        this.batchNumId = batchNumId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
