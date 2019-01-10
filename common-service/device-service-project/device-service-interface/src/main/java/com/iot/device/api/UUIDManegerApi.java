package com.iot.device.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.dto.UUIDInfoDto;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.uuid.GenerateUUID;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.GetUUIDReq;
import com.iot.device.vo.req.uuid.UUIDRefundOperateReq;
import com.iot.device.vo.req.uuid.UUIDRefundReq;
import com.iot.device.vo.req.uuid.UUidApplyRecordInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：UUID管理
 * 功能描述：UUID管理
 * 创建人： 李帅
 * 创建时间：2018年4月11日 上午9:33:45
 * 修改人：李帅
 * 修改时间：2018年4月11日 上午9:33:45
 */
@Api(tags = "UUID管理")
@FeignClient(value = "device-service")
@RequestMapping("/certGenerate")
public interface UUIDManegerApi {

    /**
	 * 
	 * 描述：UUID生成
	 * @author 李帅
	 * @created 2018年4月12日 下午2:48:43
	 * @since 
	 * @param generateUUID
	 * @throws BusinessException
	 */
	@ApiOperation(value = "UUID生成", notes = "UUID生成")
	@ApiImplicitParams({@ApiImplicitParam(name = "batchNum", value = "批次号", required = true, paramType = "query", dataType = "Integer")})
	@RequestMapping(value = "/generateUUID", method = RequestMethod.POST)
	Long generateUUID(@RequestParam("batchNum")  Long batchNum) throws BusinessException;

	/**
     * 
     * 描述：通过批次号获取证书下载URL
     * @author 李帅
     * @created 2018年6月8日 下午1:57:06
     * @since 
     * @param tenantId
     * @param userId
     * @param batchNum
     * @return
     * @throws BusinessException
     */
	@ApiOperation(value = "通过批次号获取证书下载URL", notes = "通过批次号获取证书下载URL")
	@ApiImplicitParams({@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, paramType = "query", dataType = "Integer"),
			@ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "batchNum", value = "批次号", required = true, paramType = "query", dataType = "Long")})
	@RequestMapping(value = "/getDownloadUUID", method = RequestMethod.GET)
	String getDownloadUUID(@RequestParam("tenantId") Long tenantId,@RequestParam("userId")  String userId,
						   @RequestParam("batchNum")  Long batchNum) throws BusinessException;

	/**
	 * 描述：上报总装测试数据
	 *
	 * @param licenseUsageReq
	 * @throws BusinessException
	 * @author chq
	 * @created 2018年6月27日 下午2:48:43
	 * @since
	 */
	@ApiOperation(value = "上报总装测试数据", notes = "上报总装测试数据")
	@RequestMapping(value = "/licenseUsage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void licenseUsage(@RequestBody LicenseUsageReq licenseUsageReq) throws BusinessException;

	/**
	 * 描述：查询UUID申请订单
	 * @author shenxiang
	 * @created 2018年6月29日 下午1:59:27
	 * @param uuidOrderReq
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "查询UUID申请订单", notes = "查询UUID申请订单")
	@RequestMapping(value = "/getUUIDOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Page<UUIDOrderDto> getUUIDOrder(@RequestBody GetUUIDOrderReq uuidOrderReq) throws BusinessException;

	/**
	 * 描述：查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询
	 * @author shenxiang
	 * @created 2018年6月29日 下午1:59:27
	 * @param uuidReq
	 * @return
	 * @throws BusinessException
	 */
	@ApiOperation(value = "查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询", notes = "查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询")
	@RequestMapping(value = "/getUUIDInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Page<UUIDInfoDto> getUUIDInfo(@RequestBody GetUUIDReq uuidReq) throws BusinessException;

	/**
	 * 
	 * 描述：证书生成补偿
	 * @author 李帅
	 * @created 2018年8月24日 上午9:51:08
	 * @since 
	 * @throws BusinessException
	 */
	@ApiOperation(value = "证书生成补偿", notes = "证书生成补偿")
	@RequestMapping(value = "/generateCompensate", method = RequestMethod.GET)
	void generateCompensate() throws BusinessException;

	@ApiOperation(value = "获取uuid详细信息", notes = "获取uuid详细信息")
	@RequestMapping(value = "/getGenerateUUIDInfo", method = RequestMethod.GET)
	GenerateUUID getGenerateUUIDInfo(@RequestParam("batchNum") Long batchNum);

	/**
	 * 
	 * 描述：UUID订单退款前操作
	 * @author 李帅
	 * @created 2018年11月14日 下午4:47:45
	 * @since 
	 * @param uuidRefundReq
	 */
	@ApiOperation(value = "UUID订单退款前操作", notes = "UUID订单退款前操作")
	@RequestMapping(value = "/beforeUUIDRefund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	UUidApplyRecordInfo beforeUUIDRefund(@RequestBody UUIDRefundReq uuidRefundReq);
	
	/**
	 * 
	 * 描述：UUID订单退款成功操作
	 * @author 李帅
	 * @created 2018年11月15日 下午4:00:57
	 * @since 
	 * @param refundOperateReq
	 */
	@ApiOperation(value = "UUID订单退款成功操作", notes = "UUID订单退款成功操作")
	@RequestMapping(value = "/refundSuccess", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void refundSuccess(@RequestBody UUIDRefundOperateReq refundOperateReq);
	
	/**
	 * 
	 * 描述：UUID订单退款失败操作
	 * @author 李帅
	 * @created 2018年11月15日 下午4:00:06
	 * @since 
	 * @param refundOperateReq
	 */
	@ApiOperation(value = "UUID订单退款失败操作", notes = "UUID订单退款失败操作")
	@RequestMapping(value = "/refundFail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void refundFail(@RequestBody UUIDRefundOperateReq refundOperateReq);
}
