package com.iot.device.api;

import com.iot.common.exception.BusinessException;
import com.iot.device.vo.req.servicereview.GetServiceReviewReq;
import com.iot.device.vo.req.servicereview.SetServiceReviewReq;
import com.iot.device.vo.rsp.servicereview.ServiceReviewRecordResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：第三方接入审核管理
 * 功能描述：第三方接入审核管理
 * 创建人： 李帅
 * 创建时间：2018年10月25日 上午10:02:38
 * 修改人：李帅
 * 修改时间：2018年10月25日 上午10:02:38
 */
@Api(tags = "第三方接入审核管理")
@FeignClient(value = "device-service")
@RequestMapping("/serviceReviewApi")
public interface ServiceReviewApi {

	/**
	 * 
	 * 描述：提交语音服务审核
	 * @author 李帅
	 * @created 2018年10月25日 上午11:26:23
	 * @since 
	 * @param setServiceReviewReq
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "提交语音服务审核", notes = "提交语音服务审核")
	@RequestMapping(value = "/submitServiceReview", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void submitServiceReview(@RequestBody SetServiceReviewReq setServiceReviewReq) throws BusinessException;
	
	/**
	 * 
	 * 描述：语音服务审核操作
	 * @author 李帅
	 * @created 2018年10月25日 上午11:26:23
	 * @since 
	 * @param setServiceReviewReq
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "语音服务审核操作", notes = "语音服务审核操作")
	@RequestMapping(value = "/setServiceReview", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void setServiceReview(@RequestBody SetServiceReviewReq setServiceReviewReq) throws BusinessException;

	/**
	 * 
	 * 描述：语音服务重开操作
	 * @author 李帅
	 * @created 2018年10月25日 上午11:26:23
	 * @since 
	 * @param setServiceReviewReq
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "语音服务重开操作", notes = "语音服务重开操作")
	@RequestMapping(value = "/reSetServiceReview", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void reSetServiceReview(@RequestBody SetServiceReviewReq setServiceReviewReq) throws BusinessException;
	
	/**
	 * 
	 * 描述：获取语音服务审核记录
	 * @author 李帅
	 * @created 2018年10月25日 下午5:30:31
	 * @since 
	 * @param getServiceReviewReq
	 * @return
	 */
	@ApiOperation("获取语音服务审核记录")
    @RequestMapping(value = "/getServiceReviewRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ServiceReviewRecordResp> getServiceReviewRecord(@RequestBody GetServiceReviewReq getServiceReviewReq);

	/**
	  * @despriction：获取租户id
	  * @author  yeshiyuan
	  * @created 2018/11/3 14:18
	  * @return
	  */
	@ApiOperation(value = "获取租户id", notes = "获取租户id")
	@RequestMapping(value = "/getTenantIdById", method = RequestMethod.GET)
	Long getTenantIdById(@RequestParam("id") Long id);

	/**
	  * @despriction：添加记录
	  * @author  yeshiyuan
	  * @created 2018/11/3 14:49
	  * @return
	  */
	@ApiOperation(value = "添加记录", notes = "添加记录")
	@RequestMapping(value = "/addRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Long addRecord(@RequestBody SetServiceReviewReq setServiceReviewReq);

	/**
	 * @author wucheng
	 * @param productId
	 * @param tenantId
	 */
	@ApiOperation(value = "退款时将审核记录转为历史记录", notes = "退款时将审核记录转为历史记录")
	@RequestMapping(value = "/invalidRecord", method = RequestMethod.POST)
	void invalidRecord(@RequestParam("productId") Long productId, @RequestParam("tenantId") Long tenantId, @RequestParam("serviceId") Long serviceId);
}
