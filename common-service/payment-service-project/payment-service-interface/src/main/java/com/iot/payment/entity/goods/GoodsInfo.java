package com.iot.payment.entity.goods;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
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
@ApiModel(value = "商品信息",description = "商品信息")
public class GoodsInfo implements Serializable{

    /**商品id*/
    private Long id;

    /**商品类型*/
    private Integer typeId;

    /**商品图片*/
    private String icon;

    /**商品编码*/
    private String goodsCode;

    /**商品名称*/
    private String goodsName;

    /**商品描述*/
    private String description;

    /**商品规格（描述商品某种属性）*/
    private String standard;

    /**商品规格单位*/
    private Integer standardUnit;

    /**商品价格*/
    private BigDecimal price;

    /**货币单位*/
    private String currency;

    /**选择附加服务(0：可不选；1：必选附加规格)*/
    private Byte selectExtendService;

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

    public GoodsInfo() {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Integer getStandardUnit() {
        return standardUnit;
    }

    public void setStandardUnit(Integer standardUnit) {
        this.standardUnit = standardUnit;
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

    public Byte getSelectExtendService() {
        return selectExtendService;
    }

    public void setSelectExtendService(Byte selectExtendService) {
        this.selectExtendService = selectExtendService;
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
