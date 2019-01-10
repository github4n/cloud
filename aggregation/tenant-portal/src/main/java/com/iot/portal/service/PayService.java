package com.iot.portal.service;

import com.iot.common.exception.BusinessException;
import com.iot.payment.api.OrderApi;
import com.iot.payment.api.PaymentApi;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.vo.order.req.OrderPayReq;
import com.iot.portal.exception.BusinessExceptionEnum;
import com.iot.portal.exception.UuidBusinessExceptionEnum;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：支付
 * 创建人： yeshiyuan
 * 创建时间：2018/9/14 11:04
 * 修改人： yeshiyuan
 * 修改时间：2018/9/14 11:04
 * 修改描述：
 */
@Service
public class PayService {

    private static Logger logger = LoggerFactory.getLogger(PayService.class);

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private PaymentApi paymentApi;

    @Value("${paypal.callBackUrl.service}")
    private String serviceCallBackUrl;

    @Value("${paypal.callBackUrl.uuid}")
    private String uuidCallBackUrl;

    @Value("${paypal.callBackPort}")
    private String callBackPort;

    @Value("${paypal.serverIp}")
    private String serverIp;

    /**
      * @despriction：生成支付url
      * @author  yeshiyuan
      * @created 2018/9/14 11:06
      * @return
      */
    public String payAndGetUrl(OrderPayReq orderPayReq, String orderType) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        String orderId = orderPayReq.getOrderId();
        //2.获取订单相关信息（价格）
        OrderRecord orderRecord = orderApi.getOrderRecord(orderPayReq.getOrderId(),tenantId);
        if (orderRecord == null){
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "order not exist");
        }
        String callBackUrl = "";
        if ("service".equals(orderType)) {
            //虚拟服务
            callBackUrl = serviceCallBackUrl;
        }else if ("uuid".equals(orderType)) {
            //uuid订单
            callBackUrl = uuidCallBackUrl;
        }else {
            throw new BusinessException(BusinessExceptionEnum.PARAM_IS_ERROR, "callback url is null");
        }
        // 创建交易
        TransationDto transation = new TransationDto();
        BeanUtils.copyProperties(orderPayReq, transation);
        transation.setCurrency(orderRecord.getCurrency());
        transation.setPayPrice(orderRecord.getTotalPrice());
        transation.setTenantId(tenantId);
        transation.setUserId(SaaSContextHolder.getCurrentUserUuid());
        //获取订单下的商品（这里暂时只有一个商品）
        List<Long> goodsIds = orderApi.getOrderGoodsIds(orderId,tenantId);
        if (goodsIds != null && goodsIds.size()>0){
            transation.setGoodsId(goodsIds.get(0));
        }else{
            throw new BusinessException(UuidBusinessExceptionEnum.UUID_ORDER_GOODS_NULL);
        }
        String returnUrl = serverIp + ":" + callBackPort + callBackUrl + "?orderId=" + orderId;
        transation.setReturnUrl(returnUrl);
        try {
            //3.调用paypal生成支付url
            return this.paymentApi.createTransation(transation);
        } catch (Exception e) {
            logger.error("payAndGetUrl error", e);
            throw new BusinessException(BusinessExceptionEnum.PAY_GET_URL_ERROR, e.getMessage());
        }
    }

}
