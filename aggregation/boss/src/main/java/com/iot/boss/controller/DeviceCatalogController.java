package com.iot.boss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.boss.service.user.UserService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import com.iot.common.helper.Page;
import com.iot.device.api.DeviceCatalogApi;
import com.iot.device.vo.req.DeviceCatalogReq;
import com.iot.device.vo.rsp.DeviceCatalogRes;
import com.iot.saas.SaaSContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人：chenxiaolin
 * 创建时间：2018年5月11日 上午9:36:06
 * 修改人： chenxiaolin
 * 修改时间：2018年5月11日 上午9:36:06
 */
@RestController
@Api(description = "大类接口",value = "大类接口")
@RequestMapping("/api/catalog")
public class DeviceCatalogController {

	private Logger log = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private DeviceCatalogApi deviceCatalogApi;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		List<Object> aa = new ArrayList<>();
		aa.add(1111l);
		DeviceCatalogRes res = new DeviceCatalogRes();
		res.setId(12312l);
		Map<String, Object> test = new HashMap<>();
		test.put("test", 99L);
		System.out.println(JSONObject.toJSON(res));
	}
	
	@ApiOperation("新增一级类目")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public CommonResponse<Boolean> save(@RequestBody DeviceCatalogReq req) {
		log.debug("新增一级类目:{}", JSONObject.toJSON(req));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		req.setTenantId(SaaSContextHolder.currentTenantId());
		req.setUpdateBy(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS);
		if (!deviceCatalogApi.addDeviceCatalog(req)) {
			result = new CommonResponse<>(ResultMsg.FAIL);
		}
		return result;
	}
	
	@ApiOperation("批量删除一级类目")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delect", method = RequestMethod.DELETE)
	public CommonResponse<Integer> delect(@RequestBody ArrayList<Long> ids) {
		log.debug("批量删除一级类目:{}", JSONArray.toJSON(ids));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		CommonResponse<Integer> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceCatalogApi.deleteCatalogs(ids));
		return result;
	}

	@ApiOperation("批量删除一级类目")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "delectByPost", method = RequestMethod.POST)
	public CommonResponse<Integer> delectByPost(@RequestBody ArrayList<Long> ids) {
		log.debug("批量删除一级类目:{}", JSONArray.toJSON(ids));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		CommonResponse<Integer> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceCatalogApi.deleteCatalogs(ids));
		return result;
	}
	
	@ApiOperation("更新一级类目")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public CommonResponse<Boolean> update(@RequestBody DeviceCatalogReq req) {
		log.debug("更新一级类目:{}", JSONArray.toJSON(req));
		Long userId = SaaSContextHolder.getCurrentUserId();
		userService.checkUserHadRight(userId);
		req.setTenantId(SaaSContextHolder.currentTenantId());
		req.setUpdateBy(userId);
		CommonResponse<Boolean> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceCatalogApi.updateDeviceCatalog(req));
		return result;
	}

	@ApiOperation("获取所有一级分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getAll", method = RequestMethod.GET)
	public CommonResponse<List<DeviceCatalogRes>> getAll() {
		CommonResponse<List<DeviceCatalogRes>> result = new CommonResponse<>(ResultMsg.SUCCESS, deviceCatalogApi.getDeviceCatalog());
		return result;
	}

	@ApiOperation("分页获取设备分类")
	@LoginRequired(value = Action.Normal)
	@RequestMapping(value = "getDevCatalogPageByCondition", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public CommonResponse<Page<DeviceCatalogRes>> findDevCatalogPageByCondition(@RequestBody DeviceCatalogReq req) {
		log.debug("分页获取设备分类:{}", JSONArray.toJSON(req));
		return new CommonResponse<>(ResultMsg.SUCCESS, deviceCatalogApi.getDevCatalogPageByCondition(req));

	}
}
