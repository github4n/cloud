package com.iot.device.api;

import com.iot.device.vo.req.technical.SaveDeviceTechnicalReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.common.exception.BusinessException;
import com.iot.device.dto.NetworkTypeDto;
import com.iot.device.vo.NetworkTypeVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：技术方案相关功能
 * 功能描述：技术方案相关功能
 * 创建人： 李帅
 * 创建时间：2018年10月12日 上午11:29:21
 * 修改人：李帅
 * 修改时间：2018年10月12日 上午11:29:21
 */
@Api(tags = "技术方案相关功能")
@FeignClient(value = "device-service")
@RequestMapping("/technicalRelate")
public interface TechnicalRelateApi {

	/**
	 * 
	 * 描述：查询配网模式信息
	 * @author 李帅
	 * @created 2018年10月12日 上午11:44:03
	 * @since 
	 * @param networkTypeId
	 * @throws BusinessException
	 */
	@ApiOperation(value = "查询配网模式信息", notes = "查询配网模式信息")
	@ApiImplicitParams({@ApiImplicitParam(name = "networkTypeId", value = "配网模式id", required = true, paramType = "query", dataType = "Integer")})
	@RequestMapping(value = "/getNetworkInfo", method = RequestMethod.GET)
	NetworkTypeDto getNetworkInfo(@RequestParam("networkTypeId") Long networkTypeId) throws BusinessException;

	/**
	 * 
	 * 描述：更新配网模式信息
	 * @author 李帅
	 * @created 2018年10月12日 下午5:20:47
	 * @since 
	 * @param networkTypeVo
	 * @throws BusinessException
	 */
	@ApiOperation(value = "更新配网模式信息", notes = "更新配网模式信息")
	@RequestMapping(value = "/updateNetworkInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void updateNetworkInfo(@RequestBody NetworkTypeVo networkTypeVo) throws BusinessException;

	/**
	  * @despriction：保存设备支持的技术方案
	  * @author  yeshiyuan
	  * @created 2018/10/15 19:18
	  * @return
	  */
	@ApiOperation(value = "保存设备支持的技术方案", notes = "保存设备支持的技术方案")
	@RequestMapping(value = "/saveDeviceTechnical", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveDeviceTechnical(@RequestBody SaveDeviceTechnicalReq saveDeviceTechnicalReq);

	/**
	 * @despriction：查询设备类型支持的技术方案id
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:04
	 * @return
	 */
	@ApiOperation(value = "查询设备类型支持的技术方案id", notes = "查询设备类型支持的技术方案id")
	@ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query")
	@RequestMapping(value = "queryDeviceTechnicalIds", method = RequestMethod.GET)
	List<Long> queryDeviceTechnicalIds(@RequestParam("deviceTypeId") Long deviceTypeId);

	/**
	 * @despriction：设备支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:31
	 * @return
	 */
	@ApiOperation(value = "设备支持的配网类型", notes = "设备支持的配网类型")
	@ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query")
	@RequestMapping(value = "/deviceSupportNetworkType", method = RequestMethod.GET)
	List<NetworkTypeResp> deviceSupportNetworkType(@RequestParam("deviceTypeId") Long deviceTypeId);

	/**
	 * @despriction：方案支持的配网类型
	 * @author  yeshiyuan
	 * @created 2018/10/15 20:31
	 * @return
	 */
	@ApiOperation(value = "方案支持的配网类型", notes = "方案支持的配网类型")
	@ApiImplicitParam(name = "technicalId", value = "技术方案id", dataType = "Long", paramType = "query")
	@RequestMapping(value = "/technicalSupportNetworkType", method = RequestMethod.GET)
	List<NetworkTypeResp> technicalSupportNetworkType(@RequestParam("technicalId") Long technicalId);

	/**
	  * @despriction：方案支持的配网类型
	  * @author  yeshiyuan
	  * @created 2018/10/16 17:03
	  * @return
	  */
	@ApiOperation(value = "方案支持的配网类型", notes = "方案支持的配网类型")
	@ApiImplicitParam(name = "technicalIds", value = "技术方案ids", dataType = "List", paramType = "query")
	@RequestMapping(value = "/findNetworkByTechnicalIds", method = RequestMethod.GET)
	List<NetworkTypeResp> findNetworkByTechnicalIds(@RequestParam("technicalIds") List<Long> technicalIds);

	/**
	  * @despriction：查看设备类型某种方案支持的配网类型
	  * @author  yeshiyuan
	  * @created 2018/12/11 15:39
	  */
	@ApiOperation(value = "查看设备类型某种方案支持的配网类型", notes = "查看设备类型某种方案支持的配网类型")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "deviceTypeId", value = "设备类型id", dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "technicalId", value = "技术方案id", dataType = "Long", paramType = "query")
	})
	@RequestMapping(value = "/deviceSupportNetwrokByTechnicalId", method = RequestMethod.GET)
	List<NetworkTypeResp> deviceSupportNetwrokByTechnicalId(@RequestParam("deviceTypeId") Long deviceTypeId, @RequestParam("technicalId") Long technicalId);
}
