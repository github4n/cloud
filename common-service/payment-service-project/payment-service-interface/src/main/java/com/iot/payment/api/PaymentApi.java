package com.iot.payment.api;

import com.iot.payment.dto.RefundDto;
import com.iot.payment.dto.TransationDto;
import com.iot.payment.entity.PayRes;
import com.iot.payment.entity.PayTransation;
import com.iot.payment.vo.pay.req.CreatePayReq;
import com.iot.payment.vo.pay.resp.CreatePayResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(value = "payment service接口")
@FeignClient("payment-service")
public interface PaymentApi {
    

    /**
     * 
     * 描述：web创建临时交易
     * @author wujianlong
     * @created 2018年3月13日 下午2:46:06
     * @since 
     * @param transation
     * @return
     */
	@ApiOperation(value = "web创建临时交易", notes = "web创建临时交易")
    @RequestMapping(value = "/api/payment/service/createTransation", method = {RequestMethod.POST},consumes = MediaType.APPLICATION_JSON_VALUE)
    String createTransation(@RequestBody TransationDto transation) throws Exception;
    
    /**
     * 
     * 描述：扣款
     * @author wujianlong
     * @created 2018年3月13日 下午3:15:12
     * @since 
     * @param payerId 唯一paypal客户账户识别码
     * @param paymentId 预支付id
     * @return
     */
	@ApiOperation(value = "web扣款", notes = "web扣款")
	@ApiImplicitParams({@ApiImplicitParam(name = "payerId", value = "唯一paypal客户账户识别码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "paymentId", value = "预支付id", required = false, paramType = "query", dataType = "String") })
    @RequestMapping(value = "/api/payment/service/pay", method = {RequestMethod.POST})
    void pay(@RequestParam("payerId") String payerId, @RequestParam("paymentId") String paymentId);
//	void pay(HttpServletRequest req);


    /**
     *
     * 描述：退款
     * @author wujianlong
     * @created 2018年3月13日 下午3:36:05
     * @since
     * @param refund
     * @return
     */
	@ApiOperation(value = "扣款", notes = "扣款")
    //@ApiImplicitParam(name = "refundDTo", value = "退款dto", required = true, dataType = "RefundDto", paramType = "query")
    @RequestMapping(value = "/api/payment/service/refund", method = {RequestMethod.POST},consumes = MediaType.APPLICATION_JSON_VALUE)
    PayRes refund(@RequestBody RefundDto refund) throws Exception;


    /**
     *
     * 描述：app数据校验
     * @author wujianlong
     * @created 2018年3月13日 下午3:37:12
     * @since
     * @param currency 货币代码
     * @param payId 支付id
     * @param payPrice 支付金额
     * @return
     */
	@ApiOperation(value = "app数据校验", notes = "app数据校验")
	@ApiImplicitParams({@ApiImplicitParam(name = "currency", value = "货币代码", required = true, paramType = "query", dataType = "String"),
        @ApiImplicitParam(name = "payId", value = "支付id", required = true, paramType = "query", dataType = "String") ,
        @ApiImplicitParam(name = "payPrice", value = "付款金额", required = true, paramType = "query", dataType = "double")})
    @RequestMapping(value = "/api/payment/service/appVerify", method = {RequestMethod.POST})
	String appVerify(@RequestParam("currency") String currency, @RequestParam("payId") String payId,
                     @RequestParam("payPrice") Double payPrice) throws Exception;


	@RequestMapping(value = "/api/payment/service/getAppPayStatus", method = {RequestMethod.POST})
	Integer getAppPayStatus(@RequestParam("userId") String userId, @RequestParam("payId") String payId);

	/**
	 * 
	 * 描述：获取支付交易信息
	 * @author 李帅
	 * @created 2018年5月22日 下午4:16:56
	 * @since 
	 * @param orderIDList
	 * @return
	 */
	@ApiOperation(value = "获取支付交易信息", notes = "获取支付交易信息")
	@RequestMapping(value = "/api/payment/service/getPayTransation", method = {RequestMethod.POST},consumes = MediaType.APPLICATION_JSON_VALUE)
   	List<PayTransation> getPayTransation(@RequestBody List<String> orderIDList);

	@ApiOperation(value = "从Redis中获取交易信息", notes = "从Redis中获取交易信息")
	@ApiImplicitParam(name = "paymentId", value = "支付id", dataType = "string")
	@RequestMapping(value = "/api/payment/service/getTransationDtoFromRedis", method = {RequestMethod.GET})
	TransationDto getTransationDtoFromRedis(@RequestParam("paymentId") String paymentId);

	/**
	  * @despriction：创建支付
	  * @author  yeshiyuan
	  * @created 2018/7/17 19:30
	  * @param null
	  * @return
	  */
	@ApiOperation(value = "创建支付", notes = "创建支付")
	@RequestMapping(value = "/api/payment/service/createPay", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	CreatePayResp createPay(@RequestBody CreatePayReq createPayReq);
}
