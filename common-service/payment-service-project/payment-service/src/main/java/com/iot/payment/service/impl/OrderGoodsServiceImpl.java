package com.iot.payment.service.impl;

import com.iot.payment.dao.OrderGoodsMapper;
import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.service.OrderGoodsService;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单商品service
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:31
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:31
 * 修改描述：
 */
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Override
    public int add(OrderGoods orderGoods) {
        return orderGoodsMapper.add(orderGoods);
    }

    @Override
    public int add(List<OrderGoods> orderGoodsList) {
        return orderGoodsMapper.batchAdd(orderGoodsList);
    }

    /**
     * 描述：修改订单UUID数量
     * @author maochengyuan
     * @created 2018/7/3 19:34
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    @Override
    public void editOrderCreateNum(String orderId, Integer createNum){
        this.orderGoodsMapper.editOrderCreateNum(orderId, createNum);
    }

    /**
     * 描述：依据订单id获取订单商品详情
     * @author maochengyuan
     * @created 2018/7/3 19:56
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.payment.entity.order.OrderGoods
     */
    @Override
    public List<OrderGoods> getOrderGoodsByOrderId(Long tenantId, String orderId){
        return this.orderGoodsMapper.getOrderGoodsByOrderId(tenantId, orderId);
    }

    /**
     * @despriction：获取订单下的商品id
     * @author  yeshiyuan
     * @created 2018/7/4 16:09
     * @param orderId 订单id
     * @param 租户id 租户id
     * @return
     */
    @Override
    public List<Long> getOrderGoodsIds(String orderId, Long tenantId) {
        return this.orderGoodsMapper.getOrderGoodsIds(orderId, tenantId);
    }

    /**
     * @despriction：查询视频计划类型、容量
     * @author  yeshiyuan
     * @created 2018/7/17 15:39
     * @param orderGoodsId 订单商品id
     * @return
     */
    @Override
    public VideoPlanTypeResp getVideoPlanType(String orderId, Long orderGoodsId) {
        return this.orderGoodsMapper.getVideoPlanType(orderId, orderGoodsId);
    }
}
