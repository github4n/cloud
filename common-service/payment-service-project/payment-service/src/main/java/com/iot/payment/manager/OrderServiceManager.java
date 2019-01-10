package com.iot.payment.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.common.beans.BeanUtil;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.payment.entity.goods.GoodsExtendService;
import com.iot.payment.entity.goods.GoodsInfo;
import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.entity.order.OrderGoodsExtendService;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.enums.order.OrderRecordStatusEnum;
import com.iot.payment.enums.order.OrderTypeEnum;
import com.iot.payment.exception.BusinessExceptionEnum;
import com.iot.payment.exception.OrderExceptionEnum;
import com.iot.payment.service.GoodsService;
import com.iot.payment.service.OrderGoodsExServerService;
import com.iot.payment.service.OrderGoodsService;
import com.iot.payment.service.OrderRecordService;
import com.iot.payment.util.UUIDUtil;
import com.iot.payment.vo.goods.req.GoodsExtendServiceReq;
import com.iot.payment.vo.order.OrderGoodsAndExtendServiceVo;
import com.iot.payment.vo.order.req.CreateOrderRecordReq;
import com.iot.payment.vo.order.resp.OrderDetailInfoResp;
import com.iot.payment.vo.order.resp.VideoPlanTypeResp;
import com.iot.saas.SaaSContextHolder;

/**
 * 项目名称：cloud
 * 功能描述：订单管理
 * 创建人： yeshiyuan
 * 创建时间：2018/7/3 14:33
 * 修改人： yeshiyuan
 * 修改时间：2018/7/3 14:33
 * 修改描述：
 */
@Service
public class OrderServiceManager {

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceManager.class);

    @Autowired
    private OrderRecordService orderRecordService;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private OrderGoodsExServerService orderGoodsExServerService;

    @Autowired
    private GoodsService goodsService;

    /**
     * @despriction：创建订单记录，
     * @author  yeshiyuan
     * @created 2018/7/3 14:10
     * @return 订单id
     */
    @Transactional
    public String createOrderRecord(CreateOrderRecordReq createOrderRecordReq) {
        String orderId = UUIDUtil.getUUID();
        //1.参数校验
        checkCreateOrderRecordReq(createOrderRecordReq);
        //2.校验商品是否存在
        List<GoodsExtendServiceReq> goodsExtendServiceReqs = createOrderRecordReq.getGoodsExtendServiceReq();
        List<OrderGoodsAndExtendServiceVo> orderGoodsList = new ArrayList<>(); //商品与附加服务集合
        for (GoodsExtendServiceReq goodsExtendServiceReq : goodsExtendServiceReqs ) {
            if (goodsExtendServiceReq.getNum()==null || goodsExtendServiceReq.getNum().compareTo(1)==-1){
                throw new BusinessException(OrderExceptionEnum.ORDER_GOODS_ERROR,"goods buy num less than one");
            }
            GoodsInfo goodsInfo = goodsService.getGoodsInfoByGoodsId(goodsExtendServiceReq.getGoodsId());
            if (goodsInfo == null){
                throw new BusinessException(OrderExceptionEnum.ORDER_GOODS_ERROR,"goods not exist");
            }
            OrderGoods orderGoods = new OrderGoods(orderId,goodsExtendServiceReq.getNum(),goodsInfo.getId(),goodsInfo.getGoodsName(),goodsInfo.getStandard()
                    ,goodsInfo.getStandardUnit(),goodsInfo.getPrice(),goodsInfo.getCurrency(),createOrderRecordReq.getTenantId());
            List<OrderGoodsExtendService> orderGoodsExtendServiceList = null;
            //校验商品的附加服务
            if (goodsExtendServiceReq.getGoodsExIds() != null && !goodsExtendServiceReq.getGoodsExIds().isEmpty()){
                orderGoodsExtendServiceList = new ArrayList<>();
                for (Long goodsExId : goodsExtendServiceReq.getGoodsExIds()) {
                    GoodsExtendService goodsExtendService = goodsService.getGoodsExServiceById(goodsExId);
                    if (goodsExtendService == null) {
                        throw new BusinessException(OrderExceptionEnum.ORDER_GOODS_ERROR, "goods extend service not exist");
                    }
                    //校验附加服务是否属于当前商品
                    if (goodsExtendService.getGoodsId().compareTo(goodsInfo.getId())!=0){
                        throw new BusinessException(OrderExceptionEnum.ORDER_GOODS_ERROR,"the extend service does not belong current goods");
                    }
                    OrderGoodsExtendService orderGoodsExtendService = new OrderGoodsExtendService(orderId,goodsExtendService.getId(),
                            goodsExtendService.getGoodsExName(),goodsExtendService.getPrice(),goodsExtendService.getCurrency(),createOrderRecordReq.getTenantId());
                    orderGoodsExtendServiceList.add(orderGoodsExtendService);
                }
            }
            OrderGoodsAndExtendServiceVo vo = new OrderGoodsAndExtendServiceVo(orderGoods,orderGoodsExtendServiceList);
            orderGoodsList.add(vo);
        }
        //3.校验金额
        checkOrderPrice(createOrderRecordReq.getTotalPrice(),orderGoodsList);
        //4.创建order_record
        OrderRecord orderRecord = new OrderRecord();
        BeanUtil.copyProperties(createOrderRecordReq,orderRecord);
        orderRecord.setCreateTime(new Date());
        orderRecord.setUpdateTime(new Date());
        orderRecord.setId(orderId);
        orderRecord.setOrderStatus(OrderRecordStatusEnum.WAIT_PAY.getCode());
        orderRecordService.add(orderRecord);
        //5.创建订单对应的商品
        recordOrderRelateGoodsAndService(orderGoodsList);
        return orderId;
    }

    /**
      * @despriction：创建订单记录的参数校验
      * @author  yeshiyuan
      * @created 2018/7/3 15:17
      * @param null
      * @return
      */
    private void checkCreateOrderRecordReq(CreateOrderRecordReq createOrderRecordReq){
        if (createOrderRecordReq.getTenantId() == null) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TENANTID);
        }
        if (createOrderRecordReq.getUserId() == null) {
            throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_USERID);
        }
        if (createOrderRecordReq.getTotalPrice() == null) {
            throw new BusinessException(OrderExceptionEnum.ORDER_PRICE_EMPTY);
        }
        if (!OrderTypeEnum.checkOrderType(createOrderRecordReq.getOrderType())){
            throw new BusinessException(OrderExceptionEnum.ORDER_TYPE_ERROR);
        }
        if (createOrderRecordReq.getGoodsExtendServiceReq() == null || createOrderRecordReq.getGoodsExtendServiceReq().isEmpty()){
            throw new BusinessException(OrderExceptionEnum.ORDER_GOODS_EMPTY);
        }
    }

    /**
      * @despriction：校验订单价格
      * @author  yeshiyuan
      * @created 2018/7/3 15:38
      * @param null
      * @return
      */
    private void checkOrderPrice(BigDecimal orderPrice, List<OrderGoodsAndExtendServiceVo> orderGoodsList){
        BigDecimal totalPrice = new BigDecimal("0");
        if (orderGoodsList!=null && !orderGoodsList.isEmpty()) {
            for (OrderGoodsAndExtendServiceVo vo : orderGoodsList) {
                OrderGoods orderGoods = vo.getOrderGoods();
                BigDecimal unitPrice = orderGoods.getGoodsPrice(); //商品单价
                if (vo.getExtendServices() != null && !vo.getExtendServices().isEmpty()){
                    for (OrderGoodsExtendService orderGoodsExtendService : vo.getExtendServices()) {
                        unitPrice = unitPrice.add(orderGoodsExtendService.getGoodsExPrice());
                    }
                }
                totalPrice = totalPrice.add(unitPrice.multiply(new BigDecimal(orderGoods.getNum())));
            }
        }
        if (orderPrice.compareTo(totalPrice) != 0){
            throw new BusinessException(OrderExceptionEnum.ORDER_PRICE_ERROR);
        }
    }

    /**
      * @despriction：记录订单相关的商品、以及商品附加服务信息
      * @author  yeshiyuan
      * @created 2018/7/3 17:19
      * @param vos
      * @return
      */
    private void recordOrderRelateGoodsAndService(List<OrderGoodsAndExtendServiceVo> vos){
        if (vos!=null && !vos.isEmpty()) {
            List<OrderGoods> orderGoodsList = new ArrayList<>();
            List<OrderGoodsExtendService> orderGoodsExtendServiceList = new ArrayList<>();
            for (OrderGoodsAndExtendServiceVo vo : vos) {
                OrderGoods orderGoods = vo.getOrderGoods();
//                orderGoods.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_ORDER_GOODS,0L));
                orderGoodsList.add(orderGoods);
                if (vo.getExtendServices() != null && !vo.getExtendServices().isEmpty()){
                    for (OrderGoodsExtendService orderGoodsExtendService : vo.getExtendServices()) {
                        orderGoodsExtendService.setOrderGoodsId(orderGoods.getId());
//                        orderGoodsExtendService.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_ORDER_GOODS_EX_SER,0L));
                        orderGoodsExtendServiceList.add(orderGoodsExtendService);
                    }
                }
            }
            if (!orderGoodsList.isEmpty()){
                orderGoodsService.add(orderGoodsList);
            }
            if (!orderGoodsExtendServiceList.isEmpty()){
                orderGoodsExServerService.add(orderGoodsExtendServiceList);
            }
        }
    }

    /**
     * 描述：修改订单UUID数量
     * @author maochengyuan
     * @created 2018/7/3 19:32
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    public void editOrderCreateNum(String orderId, Integer createNum) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        if (createNum == null || createNum< 1){
            throw new BusinessException(OrderExceptionEnum.ORDER_CREATE_NUM_ERROR);
        }
        /**查询订单详情*/

        OrderRecord orderRecord = this.orderRecordService.getOrderRecordByOrderId(SaaSContextHolder.currentTenantId(), orderId);
        /**查询订单商品详情*/
        List<OrderGoods> orderGoodsList = this.orderGoodsService.getOrderGoodsByOrderId(SaaSContextHolder.currentTenantId(), orderId);
        if (orderRecord == null || orderGoodsList == null || orderGoodsList.isEmpty()){
            throw new BusinessException(OrderExceptionEnum.ORDER_NOT_EXIST);
        }
        /**只有待支付或者支付失败的订单才可以修改*/
        if (!(OrderRecordStatusEnum.WAIT_PAY.getCode().equals(orderRecord.getOrderStatus()) || OrderRecordStatusEnum.PAY_FAIL.getCode().equals(orderRecord.getOrderStatus()))){
            throw new BusinessException(OrderExceptionEnum.ORDER_STATUS_ERROR);
        }
        /**数量不一致才修改*/
        OrderGoods orderGoods = orderGoodsList.get(0);
        if(!createNum.equals(orderGoods.getNum())){
            BigDecimal totalPrice = new BigDecimal(createNum).multiply(orderGoods.getGoodsPrice());
            this.orderRecordService.editOrderTotalPrice(orderId, totalPrice);
            this.orderGoodsService.editOrderCreateNum(orderId, createNum);
        }
    }

    /**
     * @despriction：获取订单记录信息
     * @author  yeshiyuan
     * @created 2018/7/4 15:43
     * @param orderId 订单id
     * @param 租户id 租户id
     * @return
     */
    public OrderRecord getOrderRecord(String orderId, Long tenantId) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        return this.orderRecordService.getOrderRecordByOrderId(tenantId, orderId);
    }
    
    /**
     * 
     * 描述：依据订单id获取订单信息
     * @author 李帅
     * @created 2018年11月14日 上午9:52:03
     * @since 
     * @param orderIds
     * @return
     */
    public Map<String, OrderRecord> getOrderRecordByOrderIds(List<String> orderIds) {
        if (orderIds == null){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        List<OrderRecord> orderRecords = this.orderRecordService.getOrderRecordByOrderIds(orderIds);
        Map<String, OrderRecord> orderMap = new HashMap<String, OrderRecord>();
        if(orderRecords != null) {
        	for(OrderRecord orderRecord : orderRecords) {
        		orderMap.put(orderRecord.getId(), orderRecord);
        	}
        }
        return orderMap;
    }

    /**
     * @despriction：获取订单下的商品
     * @author  yeshiyuan
     * @created 2018/7/4 16:09
     * @param orderId 订单id
     * @param 租户id 租户id
     * @return
     */
    public List<Long> getOrderGoodsIds(String orderId, Long tenantId) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        return this.orderGoodsService.getOrderGoodsIds(orderId, tenantId);
    }

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
    public int updateOrderRecordStatus(String orderId, Long tenantId, Integer orderStatus, Integer oldOrderStatus) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        if (!OrderRecordStatusEnum.checkOrderStatus(orderStatus) || !OrderRecordStatusEnum.checkOrderStatus(oldOrderStatus) ){
            throw new BusinessException(OrderExceptionEnum.ORDER_STATUS_ERROR);
        }
        return orderRecordService.updateOrderRecordStatus(orderId, tenantId, orderStatus, oldOrderStatus);
    }

    /**
     * @despriction：查询订单详情
     * @author  yeshiyuan
     * @created 2018/7/5 11:07
     * @param orderId 订单id
     * @param tenantId 租户id
     * @return
     */
    public OrderDetailInfoResp getOrderDetailInfo(String orderId, Long tenantId) {
        if (StringUtil.isEmpty(orderId)){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        OrderDetailInfoResp orderDetailInfoResp = new OrderDetailInfoResp();
        //查询订单信息
        OrderRecord orderRecord = orderRecordService.getOrderRecordByOrderId(tenantId, orderId);
        if (orderRecord == null) {
            throw new BusinessException(OrderExceptionEnum.ORDER_NOT_EXIST);
        }
        BeanUtil.copyProperties(orderRecord,orderDetailInfoResp);
        //查询订单商品
        List<OrderGoods> orderGoodsList = orderGoodsService.getOrderGoodsByOrderId(tenantId, orderId);
        if(orderGoodsList != null && !orderGoodsList.isEmpty()){
            List<OrderGoodsAndExtendServiceVo> voList = new ArrayList<>();
            List<Long> orderGoodsIds = orderGoodsList.stream().map(OrderGoods::getId).collect(Collectors.toList());
            //查找商品的附加服务
            List<OrderGoodsExtendService> extendServiceList = orderGoodsExServerService.findByGoodsIds(tenantId, orderGoodsIds);
            orderGoodsList.forEach(orderGoods->{
                List<OrderGoodsExtendService> list = null;
                if (extendServiceList!=null && !extendServiceList.isEmpty()){
                    list = extendServiceList.stream().filter(o -> o.getOrderGoodsId().compareTo(orderGoods.getId())==0).collect(Collectors.toList());
                }
                OrderGoodsAndExtendServiceVo vo = new OrderGoodsAndExtendServiceVo(orderGoods,list);
                voList.add(vo);
            });
            orderDetailInfoResp.setGoodsList(voList);
        }
        return orderDetailInfoResp;
    }

    /**
     * @despriction：查询视频计划类型、容量
     * @author  yeshiyuan
     * @created 2018/7/17 15:39
     * @param orderGoodsId 订单商品id
     * @return
     */
    public VideoPlanTypeResp getVideoPlanType(String orderId, Long orderGoodsId) {
        if (orderId == null){
            throw new BusinessException(OrderExceptionEnum.ORDER_ID_ISNULL);
        }
        VideoPlanTypeResp resp = orderGoodsService.getVideoPlanType(orderId, orderGoodsId);
        return resp;
    }
}
