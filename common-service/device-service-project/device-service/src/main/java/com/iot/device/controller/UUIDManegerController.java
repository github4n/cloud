package com.iot.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.device.api.UUIDManegerApi;
import com.iot.device.dto.UUIDInfoDto;
import com.iot.device.dto.UUIDOrderDto;
import com.iot.device.service.IUUIDManegerService;
import com.iot.device.vo.req.LicenseUsageReq;
import com.iot.device.vo.req.uuid.GenerateUUID;
import com.iot.device.vo.req.uuid.GetUUIDOrderReq;
import com.iot.device.vo.req.uuid.GetUUIDReq;
import com.iot.device.vo.req.uuid.UUIDRefundOperateReq;
import com.iot.device.vo.req.uuid.UUIDRefundReq;
import com.iot.device.vo.req.uuid.UUidApplyRecordInfo;

@RestController
@RequestMapping("/certGenerate")
public class UUIDManegerController implements UUIDManegerApi {

	@Autowired
	IUUIDManegerService uuidManegerService;


	/**
	 *
	 * 描述：UUID生成
	 * @author 李帅
	 * @created 2018年4月12日 下午2:48:07
     * @since
	 * @param generateUUID
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/generateUUID", method = RequestMethod.POST)
	public Long generateUUID(@RequestParam("batchNum")  Long batchNum) throws BusinessException {
		return uuidManegerService.generateUUID(batchNum);
	}

	/**
	 *
	 * 描述：通过批次号获取证书下载URL
	 * @author 李帅
	 * @created 2018年6月8日 下午1:58:38
	 * @since
	 * @param tenantId
	 * @param userId
	 * @param batchNum
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/getDownloadUUID", method = RequestMethod.GET)
	public String getDownloadUUID(@RequestParam("tenantId") Long tenantId,@RequestParam("userId")  String userId,
								  @RequestParam("batchNum")  Long batchNum) throws BusinessException {
		return uuidManegerService.getDownloadUUID(tenantId, userId, batchNum);
	}

	/**
	 * 描述：查询UUID申请订单
	 * @author shenxiang
	 * @created 2018年6月29日 下午1:59:27
	 * @param uuidOrderReq
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/getUUIDOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Page<UUIDOrderDto> getUUIDOrder(@RequestBody GetUUIDOrderReq uuidOrderReq) throws BusinessException {
		return uuidManegerService.getUUIDOrder(uuidOrderReq);
	}
	/**
	 * 描述：查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询，
	 * @author shenxiang
	 * @created 2018年6月29日 下午1:59:27
	 * @param uuidReq
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/getUUIDInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Page<UUIDInfoDto> getUUIDInfo(@RequestBody GetUUIDReq uuidReq) throws BusinessException {
		return uuidManegerService.getUUIDInfo(uuidReq);
	}

    /**
     * 描述：上报总装测试数据
     *
     * @param licenseUsageReq
     * @throws BusinessException
     * @author chq
     * @created 2018年6月27日 下午2:48:43
     * @since
     */
    @Override
    @RequestMapping(value = "/licenseUsage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void licenseUsage(@RequestBody LicenseUsageReq licenseUsageReq) throws BusinessException {
        uuidManegerService.licenseUsage(licenseUsageReq);
    }

    /**
     * 
     * 描述：证书生成补偿
     * @author 李帅
     * @created 2018年8月24日 上午9:53:15
     * @since 
     * @throws BusinessException
     */
    @Override
    @RequestMapping(value = "/generateCompensate", method = RequestMethod.GET)
    public void generateCompensate() throws BusinessException {
        uuidManegerService.generateCompensate();
    }

	public GenerateUUID getGenerateUUIDInfo(@RequestParam("batchNum") Long batchNum){
		return uuidManegerService.getGenerateUUIDInfo(batchNum);
	}

	/**
	 * 
	 * 描述：UUID订单退款前操作
	 * @author 李帅
	 * @created 2018年11月14日 下午4:50:30
	 * @since 
	 * @param uuidRefundReq
	 */
	@Override
    @RequestMapping(value = "/beforeUUIDRefund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UUidApplyRecordInfo beforeUUIDRefund(@RequestBody UUIDRefundReq uuidRefundReq) {
		return uuidManegerService.beforeUUIDRefund(uuidRefundReq);
	}
	
	/**
	 * 
	 * 描述：UUID订单退款成功操作
	 * @author 李帅
	 * @created 2018年11月15日 下午4:00:50
	 * @since 
	 * @param refundOperateReq
	 */
	@Override
    @RequestMapping(value = "/refundSuccess", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void refundSuccess(@RequestBody UUIDRefundOperateReq refundOperateReq) {
		uuidManegerService.refundSuccess(refundOperateReq);
	}
	
	/**
	 * 
	 * 描述：UUID订单退款失败操作
	 * @author 李帅
	 * @created 2018年11月15日 下午3:59:59
	 * @since 
	 * @param refundOperateReq
	 */
	@Override
    @RequestMapping(value = "/refundFail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void refundFail(@RequestBody UUIDRefundOperateReq refundOperateReq) {
		uuidManegerService.refundFail(refundOperateReq);
	}
}
