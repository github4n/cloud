package com.iot.device.vo.req.uuid;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：UUID申请入参
 * 创建人： yeshiyuan
 * 创建时间：2018年06月28日 16:58
 */
@ApiModel(value = "UUID申请入参", description = "UUID申请入参")
public class UuidApplyRecordReq {

    @ApiModelProperty(name = "tenantId", value = "租户id", dataType = "long")
    private Long tenantId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "long")
    private Long userId;

    @ApiModelProperty(name = "orderId", value = "订单id", dataType = "String")
    private String orderId;

    @ApiModelProperty(name = "createNum", value = "uuid生成数量", dataType = "int")
    private Integer createNum;

    @ApiModelProperty(name = "goodsId", value = "商品id（对应goods_info主键（主要用于做方案条件搜索））", dataType = "long")
    private Long goodsId;

    @ApiModelProperty(name = "productId", value = "产品id", dataType = "Long")
    private Long productId;

   /* @ApiModelProperty(name = "uuidValidityYear", value = "有效时长，单位：年", dataType = "Integer")
    private Integer uuidValidityYear;*/


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

    public Integer getCreateNum() {
        return createNum;
    }

    public void setCreateNum(Integer createNum) {
        this.createNum = createNum;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public UuidApplyRecordReq() {
    }

    public UuidApplyRecordReq(Long tenantId, Long userId, String orderId, Integer createNum, Long goodsId, Long productId) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.orderId = orderId;
        this.createNum = createNum;
        this.goodsId = goodsId;
        this.productId = productId;
    }
}