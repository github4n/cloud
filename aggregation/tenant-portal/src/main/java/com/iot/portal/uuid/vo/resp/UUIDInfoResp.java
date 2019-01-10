package com.iot.portal.uuid.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "UUID信息",description = "UUID信息")
public class UUIDInfoResp {

    @ApiModelProperty(value="批次号",name="batchNumId")
    private Long batchNumId;

    @ApiModelProperty(value="UUID",name="uuid")
    private String uuid;

    @ApiModelProperty(value="UUID所属产品",name="productModel")
    private String productModel;

    @ApiModelProperty(value="商品ID",name="goodsId")
    private Long goodsId;

    @ApiModelProperty(value="UUID所属产品方案",name="productSchema")
    private String productSchema;

    @ApiModelProperty(value="激活状态",name="activeStatus")
    private Integer activeStatus;

    @ApiModelProperty(value="激活状态描述",name="activeStatusDesc")
    private String activeStatusDesc;

    @ApiModelProperty(value="首次激活时间",name="activeTime")
    private Date activeTime;

    @ApiModelProperty(value="最近上线时间",name="lastLoginTime")
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

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public String getProductSchema() {
        return productSchema;
    }

    public void setProductSchema(String productSchema) {
        this.productSchema = productSchema;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getActiveStatusDesc() {
        return activeStatusDesc;
    }

    public void setActiveStatusDesc(String activeStatusDesc) {
        this.activeStatusDesc = activeStatusDesc;
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
}
