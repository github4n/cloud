package com.iot.payment.vo.order.resp;

import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.vo.order.OrderGoodsAndExtendServiceVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单详情
 * 创建人： yeshiyuan
 * 创建时间：2018/7/5 10:46
 * 修改人： yeshiyuan
 * 修改时间：2018/7/5 10:46
 * 修改描述：
 */
@ApiModel(value = "订单详情", description = "订单详情")
public class OrderDetailInfoResp extends OrderRecord{

    @ApiModelProperty(name = "goodsList", value = "商品列表", dataType = "list")
    private List<OrderGoodsAndExtendServiceVo> goodsList;

    public List<OrderGoodsAndExtendServiceVo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoodsAndExtendServiceVo> goodsList) {
        this.goodsList = goodsList;
    }
}
