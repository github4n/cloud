package com.iot.payment.service;

import com.iot.payment.entity.order.OrderGoodsExtendService;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单商品附加服务
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:29
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:29
 * 修改描述：
 */
public interface OrderGoodsExServerService {

    /**
      * @despriction：记录订单中商品的附加服务
      * @author  yeshiyuan
      * @created 2018/7/3 17:15
      * @param null
      * @return
      */
    int add(OrderGoodsExtendService orderGoodsExtendService);

    /**
     * @despriction：记录订单中商品的附加服务
     * @author  yeshiyuan
     * @created 2018/7/3 17:15
     * @param null
     * @return
     */
    int add(List<OrderGoodsExtendService> list);

    /**
     * @despriction：查找商品的附加服务
     * @author  yeshiyuan
     * @created 2018/7/5 11:28
     * @param tenantId 租户id
     * @param orderGoodsIds 商品id
     * @return
     */
    List<OrderGoodsExtendService> findByGoodsIds(Long tenantId, List<Long> orderGoodsIds);
}
