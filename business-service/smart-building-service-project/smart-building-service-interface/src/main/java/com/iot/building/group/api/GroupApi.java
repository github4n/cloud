package com.iot.building.group.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.group.vo.GroupReq;
import com.iot.building.group.vo.GroupResp;
import com.iot.control.space.vo.SpaceReq;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("分组接口")
@FeignClient(value = "building-control-service")
@RequestMapping("/group")
public interface GroupApi {

	/**
	 * 新建空间
	 *
	 * @return
	 * @author wanglei
	 */
	@ApiOperation("空间保存")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Long saveOrUpdate(@RequestBody List<GroupReq> groupReqList);

	@ApiOperation("根据遥控器查询实际的分组")
	@RequestMapping(value = "/getGroupListByRemoteId", method = RequestMethod.GET)
	List<GroupResp> getGroupListByRemoteId(@RequestParam("tenantId") Long tenantId, @RequestParam("remoteId") String remoteId);
	
	@ApiOperation("根据网关ID删除相关分组信息")
	@RequestMapping(value = "/delGroupListByGatewayId", method = RequestMethod.GET)
	int delGroupListByGatewayId(@RequestParam("tenantId") Long tenantId, @RequestParam("gatewayId") String gatewayId);
	
	@ApiOperation("根据SpaceId初始化Group")
	@RequestMapping(value = "/initGroupBySpaceId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	void initGroupBySpaceId(@RequestBody SpaceReq spaceReq);
}
