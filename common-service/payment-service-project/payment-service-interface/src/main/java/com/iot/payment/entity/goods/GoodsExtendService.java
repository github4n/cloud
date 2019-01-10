package com.iot.payment.entity.goods;

import io.swagger.annotations.ApiModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：商品信息
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 11:32
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 11:32
 * 修改描述：
 */
@ApiModel(value = "商品附加服务信息",description = "商品附加服务信息")
public class GoodsExtendService {

    /**商品附加服务id*/
    private Long id;

    /**所属商品id*/
    private Long goodsId;

    /**商品附加服务名称*/
    private String goodsExName;

    /**商品附加服务描述*/
    private String goodsExDesc;

    /**商品价格*/
    private BigDecimal price;

    /**货币单位*/
    private String currency;

    /**排序字段*/
    private short sort;

    /**创建时间*/
    private Date createTime;

    /**修改时间*/
    private Date updateTime;

    /**上线状态（0：下线；1、上线；）*/
    private Byte onlineStatus;

    /**数据有效性（0：无效；1：有效；）*/
    private Byte dataStatus;

    public GoodsExtendService() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsExName() {
        return goodsExName;
    }

    public void setGoodsExName(String goodsExName) {
        this.goodsExName = goodsExName;
    }

    public String getGoodsExDesc() {
        return goodsExDesc;
    }

    public void setGoodsExDesc(String goodsExDesc) {
        this.goodsExDesc = goodsExDesc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public short getSort() {
        return sort;
    }

    public void setSort(short sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Byte onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Byte getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Byte dataStatus) {
        this.dataStatus = dataStatus;
    }

}
