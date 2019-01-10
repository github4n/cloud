package com.iot.payment.service;

import com.iot.payment.entity.order.OrderRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：订单记录service
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:28
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:28
 * 修改描述：
 */
public interface OrderRecordService {

    /**
      * @despriction：插入订单记录
      * @author  yeshiyuan
      * @created 2018/7/3 15:33
      * @param orderRecord
      * @return
      */
    int add(OrderRecord orderRecord);

    /**
     * 描述：修改订单总价
     * @author maochengyuan
     * @created 2018/7/3 19:34
     * @param orderId 订单id
     * @param totalPrice 订单总价
     * @return void
     */
    void editOrderTotalPrice(String orderId, BigDecimal totalPrice);

    /**
     * 描述：依据订单id获取订单信息
     * @author maochengyuan
     * @created 2018/7/3 19:44
     * @param tenantId 租户id
     * @param orderId 订单id
     * @return com.iot.payment.entity.order.OrderRecord
     */
    OrderRecord getOrderRecordByOrderId(Long tenantId, String orderId);

    /**
     * 
     * 描述：依据订单id获取订单信息
     * @author 李帅
     * @created 2018年11月14日 上午9:52:13
     * @since 
     * @param orderIds
     * @return
     */
    List<OrderRecord> getOrderRecordByOrderIds(List<String> orderIds);
    
    /**
     * @despriction：修改订单状态
     * @author  yeshiyuan
     * @created 2018/7/4 16:59
     * @param orderId 订单id
     * @param 租户id 租户id
     * @param orderStatus 订单状态
     * @param oldOrderStatus 旧订单状态
     * @return
     */
    int updateOrderRecordStatus(String orderId, Long tenantId, Integer orderStatus, Integer oldOrderStatus);
}
