package com.iot.payment.restful;

import com.iot.payment.api.PaymentApi;
import com.iot.payment.contants.ModuleConstants;
import com.iot.payment.dto.RefundDto;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.PayRes;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.manager.PaymentServiceManager;
import com.iot.payment.vo.pay.req.CreatePayReq;
import com.iot.payment.vo.pay.resp.CreatePayResp;
import com.iot.redis.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PaymentRestful implements PaymentApi {

   /* @Autowired
    private PaymentService paymentService;*/

    @Autowired
	private PaymentServiceManager paymentServiceManager;

    /**
     * 
     * 描述：创建交易
     * @author wujianlong
     * @created 2018年3月31日 上午11:26:09
     * @since 
     * @param transation
     * @return
     * @throws Exception
     */
	@Override
	public String createTransation(@RequestBody TransationDto transation) throws Exception{
		return paymentServiceManager.createTransation(transation);
	}

	/**
	 * 
	 * 描述：付款
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:26:26
	 * @since 
	 * @param payerId
	 * @param paymentId
	 */
	@Override
	public void pay(@RequestParam("payerId") String payerId,@RequestParam("paymentId") String paymentId){
		paymentServiceManager.pay(payerId, paymentId);
	}

	/**
	 * 
	 * 描述：退款
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:27:54
	 * @since 
	 * @param refund
	 * @return
	 * @throws Exception
	 */
	@Override
	public PayRes refund(@RequestBody RefundDto refund) throws Exception{
		return paymentServiceManager.refund(refund);
	}

	/**
	 * 
	 * 描述：app校验
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:28:03
	 * @since 
	 * @param currency
	 * @param payId
	 * @param payPrice
	 * @return
	 * @throws Exception
	 */
	@Override
	public String appVerify(@RequestParam("currency") String currency, @RequestParam("payId") String payId,
			@RequestParam("payPrice") Double payPrice) throws Exception {
		return paymentServiceManager.appVerify(currency, payId, payPrice);
	}

	/**
	 * 
	 * 描述：获取支付状态
	 * @author wujianlong
	 * @created 2018年3月31日 上午11:28:17
	 * @since 
	 * @param userId
	 * @param payId
	 * @return
	 */
	@Override
	public Integer getAppPayStatus(@RequestParam("userId") String userId,@RequestParam("payId") String payId) {
		return paymentServiceManager.getAppPayStatus(userId,payId);
	}

	/**
	 * 
	 * 描述：获取支付交易信息
	 * @author 李帅
	 * @created 2018年5月22日 下午4:19:44
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	@Override
	public List<PayTransation> getPayTransation(@RequestBody List<String> orderIDList){
		return paymentServiceManager.getPayTransation(orderIDList);
	}

	@Override
	public TransationDto getTransationDtoFromRedis(String paymentId) {
		return RedisCacheUtil.valueObjGet(ModuleConstants.REDIS_PRE_PAY_PAYID + paymentId, TransationDto.class);
	}

	/**
	 * @despriction：创建支付
	 * @author  yeshiyuan
	 * @created 2018/7/17 19:30
	 * @param createPayReq
	 * @return
	 */
	@Override
	public CreatePayResp createPay(@RequestBody CreatePayReq createPayReq) {
		return paymentServiceManager.createPay(createPayReq);
	}
}
