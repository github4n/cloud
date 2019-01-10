package com.iot.device.controller;

import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.common.exception.BusinessException;
import com.iot.device.api.TechnicalRelateApi;
import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.service.ITechnicalRelateService;
import com.iot.device.vo.NetworkTypeVo;

import java.util.List;

@RestController
@RequestMapping("/technicalRelate")
public class TechnicalRelateController implements TechnicalRelateApi {

	@Autowired
	ITechnicalRelateService technicalRelateService;


	/**
	 * 描述：查询UUID申请订单
	 * @author shenxiang
	 * @created 2018年6月29日 下午1:59:27
	 * @param uuidOrderReq
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/getNetworkInfo", method = RequestMethod.GET)
	public NetworkTypeDto getNetworkInfo(@RequestParam("networkTypeId") Long networkTypeId) throws BusinessException {
		return technicalRelateService.getNetworkInfo(networkTypeId);
	}
	
	/**
	 * 
	 * 描述：更新配网模式信息
	 * @author 李帅
	 * @created 2018年10月12日 下午5:22:29
	 * @since 
	 * @param networkTypeVo
	 * @throws BusinessException
	 */
	@Override
	@RequestMapping(value = "/updateNetworkInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateNetworkInfo(@RequestBody NetworkTypeVo networkTypeVo) throws BusinessException{
		technicalRelateService.updateNetworkInfo(networkTypeVo);
	}

	/**
	 * @despriction：保存设备支持的技术方案
	 * @author  yeshiyuan
	 * @created 2018/10/15 19:18
	 * @return
	 */
	@Override
	public void saveDeviceTechnical(@RequestBody SaveDeviceTechnicalReq saveDeviceTechnicalReq) {
		technicalRelateService.saveDeviceTechnical(saveDeviceTechnicalReq);
	}

	/**
	 * @despriction：查询设备类型支持的技术方案id
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:04
	 * @return
	 */
	@Override
	public List<Long> queryDeviceTechnicalIds(@RequestParam("deviceTypeId") Long deviceTypeId) {
		return technicalRelateService.queryDeviceTechnical(deviceTypeId);
	}

	/**
	 * @despriction：设备支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:31
	 * @return
	 */
	@Override
	public List<NetworkTypeResp> deviceSupportNetworkType(@RequestParam("deviceTypeId") Long deviceTypeId) {
		return technicalRelateService.deviceSupportNetworkType(deviceTypeId);
	}

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:31
	 * @return
	 */
	@Override
	public List<NetworkTypeResp> technicalSupportNetworkType(Long technicalId) {
		return technicalRelateService.technicalSupportNetworkType(technicalId);
	}

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/16 17:03
	 * @return
	 */
	@Override
	public List<NetworkTypeResp> findNetworkByTechnicalIds(@RequestParam("technicalIds") List<Long> technicalIds) {
		return technicalRelateService.findNetworkByTechnicalIds(technicalIds);
	}

	/**
	 * @despriction：查看设备类型某种方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/12/11 15:39
	 */
	@Override
	public List<NetworkTypeResp> deviceSupportNetwrokByTechnicalId(Long deviceTypeId, Long technicalId) {
		return technicalRelateService.deviceSupportNetwrokByTechnicalId(deviceTypeId, technicalId);
	}
}
