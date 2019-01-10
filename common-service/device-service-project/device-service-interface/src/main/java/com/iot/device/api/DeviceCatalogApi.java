package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.api.fallback.DeviceCatalogApiFallbackFactory;
import com.iot.device.vo.req.DeviceCatalogReq;
import com.iot.device.vo.rsp.DeviceCatalogListResp;
import com.iot.device.vo.rsp.DeviceCatalogRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


@Api(tags = "设备分类接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceCatalogApiFallbackFactory.class)
@RequestMapping(value = "/devCatalog")
public interface DeviceCatalogApi {
	
	@ApiOperation("新增分类")
    @RequestMapping(value = "/addCatalog", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	boolean addDeviceCatalog(@RequestBody DeviceCatalogReq req);
	
	@ApiOperation("修改分类")
	@RequestMapping(value = "/updateCatalog", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	boolean updateDeviceCatalog(@RequestBody DeviceCatalogReq req);
	
	@ApiOperation("删除分类")
	@RequestMapping(value = "/deleteCatalogs", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	Integer deleteCatalogs(@RequestBody ArrayList<Long> ids);
	
	@ApiOperation("分类查询")
	@RequestMapping(value = "/getDeviceCatalog", method = RequestMethod.GET)
	List<DeviceCatalogRes> getDeviceCatalog();

	@ApiOperation("分页获取设备分类")
	@RequestMapping(value = "/getDevCatalogPageByCondition", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	Page<DeviceCatalogRes> getDevCatalogPageByCondition(@RequestBody DeviceCatalogReq req);
	/**
	 * 查看所有目录列表
	 *
	 * @param
	 * @return
	 * @author lucky
	 * @date 2018/6/29 14:28
	 */
	@ApiOperation("查看所有目录列表")
	@RequestMapping(value = "/findAllCatalogList", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	List<DeviceCatalogListResp> findAllCatalogList();
	
}
