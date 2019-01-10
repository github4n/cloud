package com.iot.payment.service;

import com.iot.payment.dto.RefundDto;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.PayRes;
import com.iot.payment.entity.PayTransation;

import java.util.List;

public interface PaymentService {

	/**
	 * 
	 * 描述：创建交易
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:28:32
	 * @since 
	 * @param transation
	 * @return
	 */
	String createTransation(TransationDto transation) ;
	
	/**
	 * 
	 * 描述：支付
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:28:42
	 * @since 
	 * @param payerId
	 * @param paymentId
	 */
	void pay(String payerId, String paymentId) ;

	/**
	 *
	 * 描述：退款
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:33:07
	 * @since
	 * @param refund
	 * @return
	 */
	PayRes refund(RefundDto refund) ;

	/**
	 *
	 * 描述：app支付校验
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:33:17
	 * @since
	 * @param currency
	 * @param payId
	 * @param payPrice
	 * @return
	 */
	String appVerify(String currency, String payId, Double payPrice) ;

	/**
	 * 
	 * 描述：获取支付状态
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:33:29
	 * @since 
	 * @param userId
	 * @param payId
	 * @return
	 */
	Integer getAppPayStatus(String userId, String payId);

	/**
	 * 
	 * 描述：获取支付交易信息
	 * @author 李帅
	 * @created 2018年5月22日 下午4:20:24
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	List<PayTransation> getPayTransation(List<String> orderIDList);


}
