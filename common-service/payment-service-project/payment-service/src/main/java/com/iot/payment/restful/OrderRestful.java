package com.iot.payment.restful;

import com.iot.payment.api.OrderApi;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.manager.OrderServiceManager;
import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：订单管理
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:38
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:38
 * 修改描述：
 */
@RestController
public class OrderRestful implements OrderApi{

    @Autowired
    private OrderServiceManager orderServiceManager;

    /**
     * @despriction：创建订单记录，
     * @author  yeshiyuan
     * @created 2018/7/3 14:10
     * @return 订单id
     */
    @Override
    public String createOrderRecord(@RequestBody CreateOrderRecordReq createOrderRecordReq) {
        return orderServiceManager.createOrderRecord(createOrderRecordReq);
    }

    /**
     * 描述：编辑UUID订单
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    @Override
    public void editOrderCreateNum(@RequestParam("orderId") String orderId, @RequestParam("createNum") Integer createNum) {
        this.orderServiceManager.editOrderCreateNum(orderId, createNum);
    }

    /**
     * @despriction：获取订单记录信息
     * @author  yeshiyuan
     * @created 2018/7/4 15:43
     * @param orderId 订单id
     * @param 租户id 租户id
     * @return
     */
    @Override
    public OrderRecord getOrderRecord(@RequestParam("orderId") String orderId,@RequestParam("tenantId") Long tenantId) {
        return this.orderServiceManager.getOrderRecord(orderId, tenantId);
    }
    
    /**
     * 
     * 描述：依据订单id获取订单信息
     * @author 李帅
     * @created 2018年11月14日 上午9:51:53
     * @since 
     * @param orderIds
     * @return
     */
    @Override
    public Map<String, OrderRecord> getOrderRecordByOrderIds(@RequestBody List<String> orderIds) {
        return this.orderServiceManager.getOrderRecordByOrderIds(orderIds);
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
    public List<Long> getOrderGoodsIds(@RequestParam("orderId") String orderId,@RequestParam("tenantId") Long tenantId) {
        return this.orderServiceManager.getOrderGoodsIds(orderId, tenantId);
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
    public int updateOrderRecordStatus(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId, @RequestParam("orderStatus") Integer orderStatus,@RequestParam("oldOrderStatus")Integer oldOrderStatus) {
        return this.orderServiceManager.updateOrderRecordStatus(orderId, tenantId, orderStatus,oldOrderStatus);
    }

    /**
     * @despriction：查询订单详情
     * @author  yeshiyuan
     * @created 2018/7/5 11:07
     * @param orderId 订单id
     * @param tenantId 租户id
     * @return
     */
    @Override
    public OrderDetailInfoResp getOrderDetailInfo(@RequestParam("orderId") String orderId, @RequestParam("tenantId") Long tenantId) {
        return this.orderServiceManager.getOrderDetailInfo(orderId, tenantId);
    }

    /**
     * @despriction：查询视频计划类型、容量
     * @author  yeshiyuan
     * @created 2018/7/17 15:39
     * @param oriGoodsId 订单下原商品id
     * @return
     */
    @Override
    public VideoPlanTypeResp getVideoPlanType(@RequestParam("orderId") String orderId, @RequestParam("oriGoodsId") Long oriGoodsId) {
        return this.orderServiceManager.getVideoPlanType(orderId, oriGoodsId);
    }
}
