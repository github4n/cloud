package com.iot.payment.service.impl;

import com.iot.payment.dao.OrderGoodsExtendServiceMapper;
import com.iot.payment.entity.order.OrderGoodsExtendService;
import com.iot.payment.service.OrderGoodsExServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单商品附加服务
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:32
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:32
 * 修改描述：
 */
@Service
public class OrderGoodsExServerServiceImpl implements OrderGoodsExServerService {

    @Autowired
    private OrderGoodsExtendServiceMapper orderGoodsExtendServiceMapper;

    @Override
    public int add(OrderGoodsExtendService orderGoodsExtendService) {
        return orderGoodsExtendServiceMapper.add(orderGoodsExtendService);
    }

    @Override
    public int add(List<OrderGoodsExtendService> list) {
        return orderGoodsExtendServiceMapper.batchAdd(list);
    }

    @Override
    public List<OrderGoodsExtendService> findByGoodsIds(Long tenantId, List<Long> orderGoodsIds) {
        return orderGoodsExtendServiceMapper.findByGoodsIds(tenantId, orderGoodsIds);
    }
}
