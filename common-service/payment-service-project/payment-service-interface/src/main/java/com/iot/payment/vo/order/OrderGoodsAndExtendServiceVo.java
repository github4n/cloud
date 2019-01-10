package com.iot.payment.vo.order;

import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.entity.order.OrderGoodsExtendService;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单商品与附加服务关系
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 16:27
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 16:27
 * 修改描述：
 */
public class OrderGoodsAndExtendServiceVo {

    private OrderGoods orderGoods;

    private List<OrderGoodsExtendService> extendServices;

    public OrderGoods getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(OrderGoods orderGoods) {
        this.orderGoods = orderGoods;
    }

    public List<OrderGoodsExtendService> getExtendServices() {
        return extendServices;
    }

    public void setExtendServices(List<OrderGoodsExtendService> extendServices) {
        this.extendServices = extendServices;
    }

    public OrderGoodsAndExtendServiceVo() {
    }

    public OrderGoodsAndExtendServiceVo(OrderGoods orderGoods, List<OrderGoodsExtendService> extendServices) {
        this.orderGoods = orderGoods;
        this.extendServices = extendServices;
    }
}
