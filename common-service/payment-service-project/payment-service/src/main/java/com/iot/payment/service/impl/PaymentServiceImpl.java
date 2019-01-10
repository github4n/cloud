package com.iot.payment.service.impl;


import com.iot.common.exception.BusinessException;
import com.iot.common.util.CommonUtil;
import com.iot.payment.contants.ModuleConstants;
import com.iot.payment.dao.OrderGoodsMapper;
import com.iot.payment.dao.PaymentMapper;
import com.iot.payment.dto.RefundDto;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.PayRes;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.entity.order.OrderGoods;
import com.iot.payment.enums.*;
import com.iot.payment.enums.order.OrderRecordStatusEnum;
import com.iot.payment.exception.BusinessExceptionEnum;
import com.iot.payment.exception.OrderExceptionEnum;
import com.iot.payment.rabbitmq.RabbitMqUtil;
import com.iot.payment.service.OrderRecordService;
import com.iot.payment.service.PaymentService;
import com.iot.redis.RedisCacheUtil;
import com.iot.system.api.LangApi;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


	private final int DeleteNum = 100;

	@Value("${paypal.clientID}")
	private String clientID;

	@Value("${paypal.clientSecret}")
	private String clientSecret;

	@Value("${paypal.mode}")
	private String mode;

	@Value("${paypal.tokenUrl}")
	private String tokenUrl;

	@Value("${paypal.paymentUrl}")
	private String paymentUrl;

	@Autowired
	private PaymentMapper paymentMapper;

    @Autowired
	private OrderRecordService orderRecordService;

    @Autowired
	private OrderGoodsMapper orderGoodsMapper;

    @Autowired
	private LangApi langApi;

    /**
     * 
     * 描述：创建临时交易
     * @author wujianlong
     * @created 2018年3月13日 下午3:55:35
     * @since 
     * @param transation
     * @return
     * @throws Exception 
     */
	@Override
	public String createTransation(TransationDto transation) {
		verifyTransationParam(transation);

		APIContext apiContext = new APIContext(clientID, clientSecret, mode);

		Details details = new Details();
		details.setShipping("0");
		details.setSubtotal(transation.getPayPrice().toString());
		details.setTax("0");

		Amount amount = new Amount();
		amount.setCurrency(transation.getCurrency());
		amount.setTotal(transation.getPayPrice().toString());
		amount.setDetails(details);

		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("This is the payment transaction description.");
		//添加商品信息
		List<OrderGoods> orderGoodsList = orderGoodsMapper.getOrderGoodsByOrderId(transation.getTenantId(), transation.getOrderId());
		List<Item> items = new ArrayList<Item>();
		if (orderGoodsList != null && orderGoodsList.size()>0){
			transation.setGoodsId(orderGoodsList.get(0).getId());
			Set<String> goodsNameKeys = orderGoodsList.stream().map(OrderGoods::getGoodsName).collect(Collectors.toSet());
			Map<String, String> nameMap =  this.langApi.getLangValueByKey(goodsNameKeys, LocaleContextHolder.getLocale().toString());
			orderGoodsList.forEach(orderGoods -> {
				String name = nameMap.containsKey(orderGoods.getGoodsName()) ? nameMap.get(orderGoods.getGoodsName()) : orderGoods.getGoodsName();
				Item item = new Item();
				item.setName(name).setQuantity(orderGoods.getNum().toString()).setCurrency(orderGoods.getGoodsCurrency())
						.setPrice(orderGoods.getGoodsPrice().toString());
				items.add(item);
			});
		}
		ItemList itemList = new ItemList();
		itemList.setItems(items);
		transaction.setItemList(itemList);  //paypal记录我们的商品信息

		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		Payment payment = new Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(transation.getCancelUrl());
		redirectUrls.setReturnUrl(transation.getReturnUrl());
		payment.setRedirectUrls(redirectUrls);

		Payment createdPayment = null;
		try {
			createdPayment = payment.create(apiContext);
		} catch (PayPalRESTException e1) {
			logger.error("", e1);
			throw new BusinessException(BusinessExceptionEnum.CREATE_PAYMENT_FAIL,e1);
		}
		logger.info(
				"Created payment with id = " + createdPayment.getId() + " and status = " + createdPayment.getState());

		Iterator<Links> links = createdPayment.getLinks().iterator();

		String redirectURL = null;
		while (links.hasNext()) {
			Links link = links.next();
			if (link.getRel().equalsIgnoreCase("approval_url")) {
				redirectURL = link.getHref();
				break;
			}
		}

		// 将交易数据传入redis中，回调的时候可以从中获取,paypal可以保证交易唯一
		try {
			RedisCacheUtil.valueObjSet(ModuleConstants.REDIS_PRE_PAY_PAYID + createdPayment.getId(), transation,48*60*60L);
		} catch (Exception e) {
			logger.error("createTransation create redis id = " + createdPayment.getId(), e);
			throw new BusinessException(BusinessExceptionEnum.SET_REDIS_FAIL,e);

		}

		return redirectURL;
	}

	/**
	 * 
	 * 描述：扣款
	 * @author wujianlong
	 * @created 2018年3月13日 下午3:55:51
	 * @since 
	 * @param payerId
	 * @param paymentId
	 * @return
	 */
	@Override
	public void pay(String payerId, String paymentId) {
		verifyPayParam(payerId, paymentId);

		TransationDto transation = null;
		try {
			transation = RedisCacheUtil.valueObjGet(ModuleConstants.REDIS_PRE_PAY_PAYID + paymentId, TransationDto.class);
		} catch (BusinessException e) {
			this.logger.error("", e);
			throw e;
		}
		if (transation == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TRANSATION);
		}

		// 创建之前做去重校验
		PayTransation payTransation = this.paymentMapper.getPayTransationByOrderIdAndUserId(transation.getOrderId(), transation.getUserId(),transation.getTenantId());
		if(payTransation != null) {
			updatePayTransationInfo(transation, payTransation, paymentId);
		}else{
			// 创建交易记录
			savePayTransation(transation, paymentId);
		}
		try {
			//修改订单支付状态为成功
			int i = orderRecordService.updateOrderRecordStatus(transation.getOrderId(),transation.getTenantId(), OrderRecordStatusEnum.PAY_SUCCESS.getCode(), OrderRecordStatusEnum.WAIT_PAY.getCode());
			if (i < 0){
				throw new BusinessException(OrderExceptionEnum.ORDER_UPDATE_ERROR, "this order status isn't wait pay");
			}
			// 申请扣款
			APIContext apiContext = new APIContext(clientID, clientSecret, mode);
			Payment payment = new Payment();
			payment.setId(paymentId);
			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(payerId);
			Payment pay = payment.execute(apiContext, paymentExecution);

			// 交易号
			String tradeId = pay.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
			String payState = pay.getTransactions().get(0).getRelatedResources().get(0).getSale().getState();

			// 扣款完成, 更新交易记录
			payTransation = this.paymentMapper.getPayTransationById(paymentId, null,null);
			payTransation.setTradeId(tradeId);
			if (payState.equals("completed")) {
				payTransation.setPayStatus(PayStatus.ALREADY_PAY.getCode());
			}
			payTransation.setRefundStatus(RefundStatus.NO_REFUND.getCode());
			payTransation.setPayId(pay.getId());
			payTransation.setPayTime(new Date());
			this.paymentMapper.updatePayTransation(payTransation);
		} catch (Exception e) {
			//修改订单支付状态为失败
			orderRecordService.updateOrderRecordStatus(transation.getOrderId(),transation.getTenantId(), OrderRecordStatusEnum.PAY_FAIL.getCode(),null);
			// 记录失败交易记录
			payTransation = this.paymentMapper.getPayTransationById(paymentId, null,null);
			payTransation.setPayStatus(PayStatus.PAY_FAIL.getCode());
			payTransation.setPayFailReason(e.getMessage().substring(199));
			payTransation.setPayTime(new Date());
			this.paymentMapper.updatePayTransation(payTransation);
			logger.error("web pay", e);
			throw new BusinessException(BusinessExceptionEnum.WEB_PAY_FAIL);
		} finally {
			try {
				RedisCacheUtil.delete(ModuleConstants.REDIS_PRE_PAY_PAYID + paymentId);
			} catch (BusinessException e) {
				logger.error("delete redis id = " + paymentId, e);
				throw e;
			}
		}
	}

	/**
	 * 
	 * 描述：退款
	 * @author wujianlong
	 * @created 2018年3月13日 下午3:56:02
	 * @since 
	 * @param refund
	 * @return
	 */
	@Override
	public PayRes refund(RefundDto refund) {
		verifyRefundParam(refund);

		logger.info("refund,refundSum={},userId={},orderId={}", refund.getRefundSum(),
				refund.getUserId(), refund.getOrderId());

		// 校验交易订单是否存在
		PayTransation payTransation = this.paymentMapper.getPayTransationByOrderIdAndUserId(refund.getOrderId(), refund.getUserId(),refund.getTenantId());
		if (payTransation == null){
			throw new BusinessException(BusinessExceptionEnum.PAY_TRANSATION_EMPTY);
		}
		//校验订单是否未退款\退款失败
		if (payTransation.getRefundStatus() != RefundStatus.NO_REFUND.getCode() && payTransation.getRefundStatus() != RefundStatus.REFUND_FAIL.getCode() ){
			throw new BusinessException(BusinessExceptionEnum.PAY_TRANSATION_HAVE_REFUND);
		}
		//修改为退款中
		orderRecordService.updateOrderRecordStatus(refund.getOrderId(),refund.getTenantId(), OrderRecordStatusEnum.REFUNDING.getCode(),null);

		DetailedRefund detailedRefund = null;
		APIContext apiContext = new APIContext(clientID, clientSecret, mode);
		Amount amount = new Amount();
		amount.setCurrency(payTransation.getCurrency());
		amount.setTotal(refund.getRefundSum().setScale(2).toString());
		String tradeId = payTransation.getTradeId();
		Sale sale = new Sale();
		sale.setId(tradeId);// tradeId

		RefundRequest refundReq = new RefundRequest();
		refundReq.setAmount(amount);

		PayRes res = new PayRes();
		try {
			detailedRefund = sale.refund(apiContext, refundReq);
			String state = detailedRefund.getState();
			if (state.equals("completed")) {
				payTransation.setRefundStatus(RefundStatus.REFUND_SUCCESS.getCode());
			}else {
				payTransation.setRefundStatus(RefundStatus.REFUND_FAIL.getCode());
			}
			payTransation.setRefundSum(refund.getRefundSum());
			payTransation.setRefundTime(new Date());
			payTransation.setRefundReason(refund.getRefundReason());
			this.paymentMapper.updatePayTransation(payTransation);
			//修改订单状态为退款成功
			orderRecordService.updateOrderRecordStatus(refund.getOrderId(),refund.getTenantId(), OrderRecordStatusEnum.REFUND_SUCCESS.getCode(),null);
			logger.info("patId:{},tradeId:{} refund success",payTransation.getPayId(),payTransation.getTradeId());
			res.setCode(PayRespCodeEnum.SUCCESS.getCode());
		} catch (PayPalRESTException e) {
			payTransation.setRefundStatus(RefundStatus.REFUND_FAIL.getCode());
			payTransation.setRefundFailReason(e.getMessage());
			payTransation.setRefundReason(refund.getRefundReason());
			payTransation.setRefundSum(refund.getRefundSum());
			this.paymentMapper.updatePayTransation(payTransation);
			//修改订单状态为退款是啊比
			orderRecordService.updateOrderRecordStatus(refund.getOrderId(), refund.getTenantId(), OrderRecordStatusEnum.REFUND_FAIL.getCode(),null);
			logger.error("refund fail", e);
			res.setCode(PayRespCodeEnum.FAIL.getCode());
			res.setDesc(e.getMessage());
			//throw new BusinessException(BusinessExceptionEnum.REFUND_FAIL);
		}
		return res;
	}

	/**
	 * 
	 * 描述：app数据校验
	 * @author wujianlong
	 * @created 2018年3月13日 下午3:56:17
	 * @since 
	 * @param currency
	 * @param payId
	 * @param payPrice
	 * @return
	 */
	@Override
	public String appVerify(String currency, String payId, Double payPrice) {
		verifyAppPayParam(currency, payId, payPrice);

		PayTransation payTran =  this.paymentMapper.getPayTransationByPayId(payId);
		if(payTran == null) {
			// 先创建交易记录，然后把参数传递到rabbitmq中，直接返回校验中
			payTran = new PayTransation();
//			payTran.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_IOT_DB_PAYMENT, 0L));
			payTran.setPayId(payId);
			payTran.setPayPrice(payPrice);
			payTran.setCurrency(currency);
			payTran.setPaySource(PaySource.FROM_APP.getCode());
			payTran.setPayStatus(PayStatus.IN_PAYMENT.getCode());
			payTran.setPayType(PayType.PAY.getCode());
			payTran.setCreateTime(new Date());
			this.paymentMapper.savePayTransation(payTran);
		}

		StringBuffer sb = new StringBuffer();
		try {
			RabbitMqUtil.sendQueue("appVerify", "payment", "appPay", sb.append(payTran.getId()).append(",").append(currency)
					.append(",").append(payId).append(",").append(payPrice).toString());
		} catch (IOException e) {
			logger.error("rabbitmq send queue", e);
			throw new BusinessException(BusinessExceptionEnum.RABBITMQ_SEND_FAIL);
		}

		return "支付正在校验中...";
	}

	/**
	 * 
	 * 描述：获取app支付状态
	 * @author wujianlong
	 * @created 2018年3月28日 下午7:36:23
	 * @since 
	 * @param userId
	 * @param payId
	 * @return
	 */
	@Override
	public Integer getAppPayStatus(String userId, String payId) {
		return this.paymentMapper.getAppPayStatus(userId,payId);
	}

	/**
	 * 
	 * 描述：获取支付交易信息
	 * @author 李帅
	 * @created 2018年5月22日 下午4:22:59
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	@Override
	public List<PayTransation> getPayTransation(List<String> orderIDList) {
		List<PayTransation> payTransations = new ArrayList<PayTransation>();
		List<List<String>> idsListList = null;
		if(orderIDList.size() > DeleteNum) {
			idsListList = CommonUtil.dealBySubList(orderIDList, DeleteNum);
			for(List<String> ids : idsListList) {
				payTransations.addAll(this.paymentMapper.getPayTransation(ids));
			}
		}else {
			payTransations = this.paymentMapper.getPayTransation(orderIDList);
		}
		return payTransations;
	}

	/**
	 *
	 * 描述：参数校验
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:34:06
	 * @since
	 * @param transation
	 */
	private void verifyTransationParam(TransationDto transation) {
		if (transation == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TRANSATION);
		}

		if (transation.getPayPrice() == null || !(transation.getPayPrice().compareTo(new BigDecimal(0)) == 1)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PAYPRICE);
		}

		if (StringUtils.isBlank(transation.getCurrency())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_CURRENCY);
		}

		if (StringUtils.isBlank(transation.getCancelUrl())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_CANCELURL);
		}

		if (StringUtils.isBlank(transation.getReturnUrl())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_RETURNURL);
		}

		if (transation.getGoodsId()==null || transation.getGoodsId().longValue()==0) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_GOODSID);
		}

		if (StringUtils.isBlank(transation.getUserId())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_USERID);
		}

		if (StringUtils.isBlank(transation.getOrderId())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_ORDERID);
		}
	}

	/**
	 *
	 * 描述：参数校验
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:53:07
	 * @since
	 * @param payerId
	 * @param paymentId
	 */
	private void verifyPayParam(String payerId, String paymentId) {
		if (StringUtils.isBlank(payerId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PAYERID);
		}

		if (StringUtils.isBlank(paymentId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PAYMENTID);
		}
	}

	/**
	 * @despriction：修改交易信息
	 * @author  yeshiyuan
	 * @created 2018/7/10 11:33
	 * @param null
	 * @return
	 */
	private void updatePayTransationInfo(TransationDto transation, PayTransation payTransation,String paymentId) {
		payTransation.setGoodsId(transation.getGoodsId());
		payTransation.setPayPrice(transation.getPayPrice().doubleValue());
		payTransation.setPayType(PayType.PAY.getCode());
		payTransation.setPayStatus(PayStatus.TO_BE_PAID.getCode());
		payTransation.setCurrency(transation.getCurrency());
		payTransation.setPaySource(PaySource.FROM_WEB.getCode());
		payTransation.setPaymentId(paymentId);
		this.paymentMapper.updatePayTransationInfo(payTransation);
	}

	/**
	 *
	 * 描述：保存交易记录
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:52:50
	 * @since
	 * @param transation
	 * @param paymentId
	 */
	private void savePayTransation(TransationDto transation, String paymentId) {
		PayTransation payTran = new PayTransation();
//		payTran.setId(RedisCacheUtil.incr(ModuleConstants.DB_TABLE_IOT_DB_PAYMENT, 0L));
		payTran.setUserId(transation.getUserId());
		payTran.setGoodsId(transation.getGoodsId());
		payTran.setPayPrice(transation.getPayPrice().doubleValue());
		payTran.setOrderId(transation.getOrderId());
		payTran.setPayType(PayType.PAY.getCode());
		payTran.setCreateTime(new Date());
		payTran.setPayStatus(PayStatus.TO_BE_PAID.getCode());
		payTran.setCurrency(transation.getCurrency());
		payTran.setPaySource(PaySource.FROM_WEB.getCode());
		payTran.setPaymentId(paymentId);
		payTran.setTenantId(transation.getTenantId());
		this.paymentMapper.savePayTransation(payTran);
	}

	/**
	 *
	 * 描述：参数校验
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:53:21
	 * @since
	 * @param refund
	 */
	private void verifyRefundParam(RefundDto refund) {
		if (refund == null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_REFUND);
		}

		if (refund.getRefundSum() == null || refund.getRefundSum().compareTo(new BigDecimal("0"))==0) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_REFUNDSUM);
		}
		if (StringUtils.isBlank(refund.getUserId())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_USERID);
		}

		if (StringUtils.isBlank(refund.getOrderId())) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_ORDERID);
		}
		if (refund.getTenantId()==null) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_TENANTID);
		}
	}

	private void verifyAppPayParam(String currency, String payId, Double payPrice) {
		if (StringUtils.isBlank(currency)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_CURRENCY);
		}

		if (StringUtils.isBlank(payId)) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PAYID);
		}

		if (payPrice == null || payPrice == 0) {
			throw new BusinessException(BusinessExceptionEnum.EMPTY_OF_PAYPRICE);
		}

	}
}
