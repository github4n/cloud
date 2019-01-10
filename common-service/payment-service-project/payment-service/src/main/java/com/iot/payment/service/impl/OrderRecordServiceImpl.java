package com.iot.payment.service.impl;

import com.iot.payment.dao.OrderRecordMapper;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.service.OrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单记录
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:30
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:30
 * 修改描述：
 */
@Service
public class OrderRecordServiceImpl implements OrderRecordService {

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Override
    public int add(OrderRecord orderRecord) {
        return orderRecordMapper.add(orderRecord);
    }

    /**
     * 描述：修改订单UUID数量
     * @author maochengyuan
     * @created 2018/7/3 19:34
     * @param orderId 订单id
     * @param totalPrice 商品总价
     * @return void
     */
    @Override
    public void editOrderTotalPrice(String orderId, BigDecimal totalPrice){
        this.orderRecordMapper.editOrderTotalPrice(orderId, totalPrice, new Date());
    }

    /**
     * 描述：依据订单id获取订单信息
     * @author maochengyuan
     * @created 2018/7/3 19:44
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.payment.entity.order.OrderRecord
     */
    @Override
    public OrderRecord getOrderRecordByOrderId(Long tenantId, String orderId){
        return orderRecordMapper.getOrderRecordByOrderId(tenantId, orderId);
    }

    /**
     * 
     * 描述：依据订单id获取订单信息
     * @author 李帅
     * @created 2018年11月14日 上午9:52:20
     * @since 
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderRecord> getOrderRecordByOrderIds(List<String> orderIds){
        return orderRecordMapper.getOrderRecordByOrderIds(orderIds);
    }
    
    /**
     * @despriction：修改订单状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param orderStatus 订单状态
     * @return
     */
    @Override
    public int updateOrderRecordStatus(String orderId, Long tenantId, Integer orderStatus, Integer oldOrderStatus) {
        return orderRecordMapper.updateOrderRecordStatus(orderId, tenantId, orderStatus, new Date(),oldOrderStatus);
    }
}
