package com.iot.boss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.boss.service.file.FileService;
import com.iot.boss.service.user.UserService;
import com.iot.boss.vo.FileResp;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.vo.req.DeviceType2PointsReq;
import com.iot.device.vo.req.DeviceTypeReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Api(description = "设备分类接口",value = "设备分类接口")
@RequestMapping("/api/deviceType")
public class DeviceTypeController {

	private Logger log = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private DeviceTypeApi deviceTypeApi;

	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;

	@ApiOperation("新增设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public CommonResponse<Boolean> save(@RequestBody @Valid DeviceTypeReq req) {
		log.debug("新增一级类目:{}", JSONObject.toJSON(req));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		req.setTenantId(SaaSContextHolder.currentTenantId());
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		if (!deviceTypeApi.addDeviceType(req)) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}
	
	@ApiOperation("设备类型增加功能点")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "addDataPoint", method = RequestMethod.POST)
	public CommonResponse<Boolean> addDataPoint(@RequestBody DeviceType2PointsReq req) {
		log.debug("新增一级类目:{}", JSONObject.toJSON(req));
		req.setTenantId(SaaSContextHolder.currentTenantId());
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		if (!deviceTypeApi.addDataPoint(req)) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}
	
	@ApiOperation("批量删除设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delete", method = RequestMethod.DELETE)
	public CommonResponse<Boolean> delect(@RequestBody ArrayList<Long> ids) {
		log.debug("批量删除设备分类:{}", JSONArray.toJSON(ids));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceTypeApi.deleteByDeviceTypeIds(ids));
		return result;
	}

	@ApiOperation("批量删除设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delectByPost", method = RequestMethod.POST)
	public CommonResponse<Boolean> delectByPost(@RequestBody ArrayList<Long> ids) {
		log.debug("批量删除设备分类:{}", JSONArray.toJSON(ids));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceTypeApi.deleteByDeviceTypeIds(ids));
		return result;
	}
	
	@ApiOperation("更新设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public CommonResponse<Boolean> update(@RequestBody @Valid DeviceTypeReq req) {
		log.debug("更新设备分类:{}", JSONArray.toJSON(req));
		Long userId = SaaSContextHolder.getCurrentUserId();
		req.setTenantId(SaaSContextHolder.currentTenantId());
		userService.checkUserHadRight(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceTypeApi.updateDeviceType(req));
		return result;
	}
	
	@ApiOperation("获取所有设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getAll", method = RequestMethod.GET)
	public CommonResponse<List<DeviceTypeResp>> getAll() {
		List<DeviceTypeResp> list = deviceTypeApi.findDeviceTypeList();
		List<String> imgIds = new ArrayList<>();
		list.forEach(m->{
			if (StringUtils.isNotEmpty(m.getImg())){
				imgIds.add(m.getImg());
			}
		});
		Map map = fileService.getUrl(imgIds);
		if (map!=null){
			list.forEach(n->{
				if (map.get(n.getImg())!=null){
					n.setImgFileId(n.getImg());
					n.setImg(map.get(n.getImg()).toString());
				}
			});
		}

		return new CommonResponse<>(ResultMsg.SUCCESS, list);
	}

	@ApiOperation("根据一级类目id获取设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getByCatalogId/{catalogId}", method = RequestMethod.GET)
	public CommonResponse<List<DeviceTypeResp>> getByCatalogId(@PathVariable(value = "catalogId", required = true) Long catalogId) {
		log.debug("更新设备分类:{}", catalogId);
		List<DeviceTypeResp> list = deviceTypeApi.getDeviceTypeByCataLogId(catalogId);
		List<String> imgIds = new ArrayList<>();
		list.forEach(m->{
			if (StringUtils.isNotEmpty(m.getImg())){
				imgIds.add(m.getImg());
			}
		});
		Map map = fileService.getUrl(imgIds);
		if (map!=null){
			list.forEach(n->{
				if (map.get(n.getImg())!=null){
					n.setImgFileId(n.getImg());
					n.setImg(map.get(n.getImg()).toString());
				}
			});
		}

		return new CommonResponse<>(ResultMsg.SUCCESS, list);
	}
	
	@ApiOperation("根据条件获取设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getDeviceTypeByCondition", method = RequestMethod.POST)
	public CommonResponse<Page<DeviceTypeResp>> getDeviceTypeByCondition(@RequestBody DeviceTypeReq req) {
		log.debug("获取设备分类:{}", JSONObject.toJSONString(req));
		Page<DeviceTypeResp> respPage = deviceTypeApi.getDeviceTypeByCondition(req);
		List<DeviceTypeResp> list = respPage.getResult();
		List<String> imgIds = new ArrayList<>();
		list.forEach(m->{
			if (StringUtils.isNotEmpty(m.getImg())){
				imgIds.add(m.getImg());
			}
		});
		Map map = fileService.getUrl(imgIds);
		if (map!=null){
			list.forEach(n->{
				if (map.get(n.getImg())!=null){
					n.setImgFileId(n.getImg());
					n.setImg(map.get(n.getImg()).toString());
				}
			});
		}

		respPage.setResult(list);
		return new CommonResponse<>(ResultMsg.SUCCESS, respPage);
	}
	
	@ApiOperation("根据一级类目id获取设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getById/{deviceTypeId}", method = RequestMethod.GET)
	public CommonResponse<DeviceTypeResp> getById(@PathVariable(value = "deviceTypeId", required = true) Long deviceTypeId) {
		log.debug("根据一级类目id获取设备分类:{}", deviceTypeId);
		DeviceTypeResp deviceTypeResp = deviceTypeApi.getDeviceTypeById(deviceTypeId);
		FileResp fileResp = new FileResp();
		if (StringUtil.isNotBlank(deviceTypeResp.getImg())){
			try {
				fileResp = fileService.getUrl(deviceTypeResp.getImg());
				deviceTypeResp.setImgFileId(deviceTypeResp.getImg());
				deviceTypeResp.setImg(fileResp.getUrl());
			} catch (Exception e) {
				log.warn("getById getFileUrl error.", e);
			}
		}
		return new CommonResponse<>(ResultMsg.SUCCESS, deviceTypeResp);
	}

	@ApiOperation("根据id删除")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delete/{id}" , method = RequestMethod.GET)
	public CommonResponse delete(@PathVariable(value = "id",required = true) Long id){
		log.debug("删除",id);
		deviceTypeApi.delete(id,SaaSContextHolder.currentTenantId());
		return CommonResponse.success();
	}

}
