package com.iot.device.api;

import com.iot.common.helper.Page;
import com.iot.device.vo.req.NetworkTypePageReq;
import com.iot.device.vo.rsp.NetworkTypeResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 项目名称：cloud
 * 功能描述：配网类型管理
 * 创建人： yeshiyuan
 * 创建时间：2018/6/29 14:33
 * 修改人： yeshiyuan
 * 修改时间：2018/6/29 14:33
 * 修改描述：
 */
@Api(tags = "配网类型管理")
@FeignClient(value = "device-service")
@RequestMapping("/networkType")
public interface NetworkTypeApi {

	/**
	  * @despriction：查询配网类型信息
	  * @author  yeshiyuan
	  * @created 2018/10/15 16:15
	  * @return
	  */
	@ApiOperation(value = "查询配网类型信息", notes = "查询配网类型信息")
	@ApiImplicitParam(name = "networkTypeId", value = "配网类型id", dataType = "Long", paramType = "query")
	@RequestMapping(value = "/getNetworkType", method = RequestMethod.GET)
	NetworkTypeResp getNetworkType(@RequestParam("networkTypeId") Long networkTypeId);

	/**
	 * @despriction：查询配网类型信息
	 * @author  yeshiyuan
	 * @created 2018/10/15 16:15
	 * @return
	 */
	@ApiOperation(value = "查询配网类型信息", notes = "查询配网类型信息")
	@ApiImplicitParam(name = "networkTypeIds", value = "配网类型ids", dataType = "List", paramType = "query")
	@RequestMapping(value = "/getNetworkTypes", method = RequestMethod.GET)
	Map<Long, NetworkTypeResp>  getNetworkTypes(@RequestParam("networkTypeIds") List<Long> networkTypeIds);

	/**
	  * @despriction：分页查询配网类型
	  * @author  yeshiyuan
	  * @created 2018/10/15 17:16
	  * @return
	  */
	@ApiOperation(value = "分页查询配网类型", notes = "分页查询配网类型")
	@RequestMapping(value = "/page", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Page<NetworkTypeResp> page(@RequestBody NetworkTypePageReq pageReq);

	/**
	  * @despriction：通过配网编码查找配网方式
	  * @author  yeshiyuan
	  * @created 2018/12/11 16:34
	  */
	@ApiOperation(value = "通过配网编码查找配网方式", notes = "通过配网编码查找配网方式")
	@RequestMapping(value = "/findByTypeCode", method = RequestMethod.GET)
	List<NetworkTypeResp> findByTypeCode(@RequestParam("typeCodes") List<String> typeCodes);
}
