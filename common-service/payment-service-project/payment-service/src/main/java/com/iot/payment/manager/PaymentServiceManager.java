package com.iot.payment.manager;

import com.iot.common.beans.BeanUtil;
import com.iot.payment.dto.RefundDto;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.PayRes;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.entity.order.OrderRecord;
import com.iot.payment.enums.PayRespCodeEnum;
import com.iot.payment.enums.order.OrderRecordStatusEnum;
import com.iot.payment.service.OrderGoodsService;
import com.iot.payment.service.PaymentService;
import com.iot.payment.vo.pay.req.CreatePayReq;
import com.iot.payment.vo.pay.resp.CreatePayResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("paymentServiceManager")
public class PaymentServiceManager {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceManager.class);


	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderServiceManager orderServiceManager;

	@Autowired
	private OrderGoodsService orderGoodsService;

	/**
	 * 
	 * 描述：创建交易
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:33:50
	 * @since 
	 * @param transation
	 * @return
	 */
	public String createTransation(TransationDto transation) {
		return paymentService.createTransation(transation);
	}



	/**
	 * 
	 * 描述：扣款
	 * 
	 * @author wujianlong
	 * @created 2018年3月13日 下午3:55:51
	 * @since
	 * @param payerId
	 * @param paymentId
	 * @return
	 */
	public void pay(String payerId, String paymentId) {
		paymentService.pay(payerId, paymentId);
	}




	/**
	 * 
	 * 描述：退款
	 * 
	 * @author wujianlong
	 * @created 2018年3月13日 下午3:56:02
	 * @since
	 * @param refund
	 * @return
	 */
	@Transactional
	public PayRes refund(RefundDto refund) {
		OrderRecord orderRecord = orderServiceManager.getOrderRecord(refund.getOrderId(), refund.getTenantId());
		if (orderRecord == null) {
			return new PayRes(PayRespCodeEnum.FAIL.getCode(), "order not exist");
		}
		if (!OrderRecordStatusEnum.checkRefundPremise(orderRecord.getOrderStatus())) {
			//throw new BusinessException(BusinessExceptionEnum.PAY_TRANSATION_HAVE_REFUND);
			return new PayRes(PayRespCodeEnum.FAIL.getCode(), "this order is refunding or had refund success, please refresh");
		}
		if (orderRecord.getTotalPrice().compareTo(refund.getRefundSum()) == -1) {
			orderServiceManager.updateOrderRecordStatus(refund.getOrderId(), orderRecord.getTenantId(), OrderRecordStatusEnum.REFUND_FAIL.getCode(), orderRecord.getOrderStatus());
			//throw new BusinessException(BusinessExceptionEnum.REFUND_AMOUNT_ERROR);
			return new PayRes(PayRespCodeEnum.FAIL.getCode(), "refund amount error, more than order total price");
		}
		int i = orderServiceManager.updateOrderRecordStatus(refund.getOrderId(), orderRecord.getTenantId(), OrderRecordStatusEnum.REFUNDING.getCode(), orderRecord.getOrderStatus());
		if (i<1) {
			return new PayRes(PayRespCodeEnum.FAIL.getCode(), "update order status error");
		}
		PayRes payRes = paymentService.refund(refund);
		return payRes;
	}



	/**
	 * 
	 * 描述：app数据校验
	 * 
	 * @author wujianlong
	 * @created 2018年3月13日 下午3:56:17
	 * @since
	 * @param currency
	 * @param payId
	 * @param payPrice
	 * @return
	 */
	public String appVerify(String currency, String payId, Double payPrice) {
		return paymentService.appVerify(currency, payId, payPrice);
	}



	/**
	 * 
	 * 描述：获取app支付状态
	 * @author wujianlong
	 * @created 2018年3月28日 下午7:37:03
	 * @since 
	 * @param userId
	 * @param payId
	 * @return
	 */
	public Integer getAppPayStatus(String userId, String payId) {
		return this.paymentService.getAppPayStatus(userId,payId);
	}

	/**
	 * 
	 * 描述：获取支付交易信息
	 * @author 李帅
	 * @created 2018年5月22日 下午4:24:18
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	public List<PayTransation> getPayTransation(List<String> orderIDList) {
		return paymentService.getPayTransation(orderIDList);
	}


	/**
	 * @despriction：创建支付
	 * @author  yeshiyuan
	 * @created 2018/7/17 19:30
	 * @param createPayReq
	 * @return
	 */
	@Transactional
	public CreatePayResp createPay(CreatePayReq createPayReq) {
		//创建订单
		String orderId = orderServiceManager.createOrderRecord(createPayReq.getCreateOrderRecordReq());
		//保存支付相关信息，提交至paypal生成支付url
		TransationDto transationDto = new TransationDto();
		BeanUtil.copyProperties(createPayReq,transationDto);
		/*List<Long> goodsIds = orderServiceManager.getOrderGoodsIds(orderId, createPayReq.getTenantId());
		if (goodsIds != null && goodsIds.size()>0){
			transationDto.setGoodsId(goodsIds.get(0));
		}*/
		String returnUrl = transationDto.getReturnUrl() + "?orderId=" + orderId;
		transationDto.setReturnUrl(returnUrl);
		transationDto.setOrderId(orderId);
		String payUrl = paymentService.createTransation(transationDto);
		CreatePayResp resp = new CreatePayResp(orderId, payUrl);
		return resp;
	}

}
