package com.iot.boss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.boss.service.user.UserService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.device.api.DataPointApi;
import com.iot.device.vo.req.DataPointReq;
import com.iot.device.vo.rsp.DataPointResp;
import com.iot.device.vo.rsp.DeviceFunResp;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年5月11日 上午9:36:06
 * 修改人： chenxiaolin
 * 修改时间：2018年5月11日 上午9:36:06
 */
@RestController
@Api(description = "大类接口",value = "大类接口")
@RequestMapping("/api/point")
public class DatePointController {

	private Logger log = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private DataPointApi dataPointApi;

	@Autowired
	private UserService userService;
	
	@ApiOperation("新增功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public CommonResponse<Boolean> save(@RequestBody DataPointReq req) {
		log.debug("新增功能点:{}", JSONObject.toJSON(req));
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
//		req.setTenantId(SaaSContextHolder.currentTenantId());
        req.setCreateBy(userId);
		if (!dataPointApi.addDataPoint(req)) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}
	
	@ApiOperation("批量删除功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public CommonResponse<Boolean> delect(@RequestBody ArrayList<Long> ids) {
		log.debug("批量删除功能点:{}", JSONArray.toJSON(ids));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.deleteByIds(ids));
		return result;
	}

	@ApiOperation("批量删除功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delectByPost", method = RequestMethod.POST)
	public CommonResponse<Boolean> delectByPost(@RequestBody ArrayList<Long> ids) {
		log.debug("批量删除功能点:{}", JSONArray.toJSON(ids));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.deleteByIds(ids));
		return result;
	}
	
	@ApiOperation("更新功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public CommonResponse<Boolean> update(@RequestBody DataPointReq req) {
		log.debug("更新功能点:{}", JSONArray.toJSON(req));
		Long userId = SaaSContextHolder.getCurrentUserId();
//		req.setTenantId(SaaSContextHolder.currentTenantId());
        req.setCreateBy(userId);
		userService.checkUserHadRight(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.updateDataPoint(req));
		return result;
	}
	
	
	@ApiOperation("查询所有非自定义功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getDataPointByCondition", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public CommonResponse getDataPointByCondition(@RequestBody DataPointReq req) {
		CommonResponse result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.findExceptCustom(req));
		return result;
	}
	
	@ApiOperation("根据设备Id查询功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getByDeviceId/{deviceId}", method = RequestMethod.GET)
	public CommonResponse<List<DeviceFunResp>> getByDeviceId(@PathVariable(value = "deviceId", required = true) String deviceId) {
		log.debug("根据设备Id查询功能点:{}", deviceId);
		CommonResponse<List<DeviceFunResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.findDataPointListByDeviceId(deviceId));
		return result;
	}
	
	@ApiOperation("根据设备Id查询功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getByDeviceTypeId/{deviceTypeId}", method = RequestMethod.GET)
	public CommonResponse<List<DeviceFunResp>> getByDeviceTypeId(@PathVariable(value = "deviceTypeId", required = true) Long deviceTypeId) {
		log.debug("根据设备Id查询功能点:{}", deviceTypeId);
		CommonResponse<List<DeviceFunResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.findDataPointListByDeviceTypeId(deviceTypeId));
		return result;
	}
	
	@ApiOperation("查询smart")
    @RequestMapping(value = "/getSmart/{dataPointId}", method = RequestMethod.GET)
    public CommonResponse<List<SmartDataPointResp>> getSmartByDataPointId(@PathVariable("dataPointId") Long dataPointId) {
		log.debug("查询smart:{}", dataPointId);
		CommonResponse<List<SmartDataPointResp>> result = new CommonResponse<>(ResultMsg.SUCCESS, dataPointApi.getSmartByDataPointId(dataPointId));
		return result;
	}
	    
}
