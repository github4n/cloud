package com.iot.payment.vo.goods.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Set;

/**
 * 项目名称：cloud
 * 模块名称：
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:17
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:17
 * 修改描述：
 */
@ApiModel(value = "商品的附加服务", description = "商品的附加服务")
public class GoodsExtendServiceReq {

    @ApiModelProperty(name = "goodsId", value = "商品Id", dataType = "Long")
    private Long goodsId;

    @ApiModelProperty(name = "num", value = "购买数量", dataType = "int")
    private Integer num;

    @ApiModelProperty(name = "goodsExIds", value = "商品的附加服务Id集合", dataType = "List")
    private List<Long> goodsExIds;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public List<Long> getGoodsExIds() {
        return goodsExIds;
    }

    public void setGoodsExIds(List<Long> goodsExIds) {
        this.goodsExIds = goodsExIds;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public GoodsExtendServiceReq() {
    }

    public GoodsExtendServiceReq(Long goodsId, Integer num) {
        this.goodsId = goodsId;
        this.num = num;
    }
}
