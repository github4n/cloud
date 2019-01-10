package com.iot.payment.service;

import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单商品service
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:28
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:28
 * 修改描述：
 */
public interface OrderGoodsService {

    /**
      * @despriction：添加订单的商品
      * @author  yeshiyuan
      * @created 2018/7/3 17:03
      * @param orderGoods
      * @return
      */
    int add(OrderGoods orderGoods);

    /**
     * @despriction：添加订单的商品
     * @author  yeshiyuan
     * @created 2018/7/3 17:03
     * @param orderGoods
     * @return
     */
    int add(List<OrderGoods> orderGoodsList);

    /**
     * 描述：修改订单UUID数量
     * @author maochengyuan
     * @created 2018/7/3 19:34
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    void editOrderCreateNum(String orderId, Integer createNum);

    /**
     * 描述：依据订单id获取订单商品详情
     * @author maochengyuan
     * @created 2018/7/3 19:56
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.payment.entity.order.OrderGoods
     */
    List<OrderGoods> getOrderGoodsByOrderId(Long tenantId, String orderId);

    /**
     * @despriction：获取订单下的商品id
     * @author  yeshiyuan
     * @created 2018/7/4 16:09
     * @param orderId 订单id
     * @param 租户id 租户id
     * @return
     */
    List<Long> getOrderGoodsIds(String orderId, Long tenantId);

    /**
     * @despriction：查询视频计划类型、容量
     * @author  yeshiyuan
     * @created 2018/7/17 15:39
     * @param orderGoodsId 订单商品id
     * @return
     */
    VideoPlanTypeResp getVideoPlanType(String orderId, Long orderGoodsId);
}
