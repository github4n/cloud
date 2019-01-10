package com.iot.building.group.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.group.api.GroupApi;
import com.iot.building.group.service.IGroupService;
import com.iot.building.group.vo.GroupReq;
import com.iot.building.group.vo.GroupResp;
import com.iot.control.space.vo.SpaceReq;

@RestController()
public class GroupController implements GroupApi {

	@Autowired
	private IGroupService groupService;

	@Override
	public Long saveOrUpdate(@RequestBody List<GroupReq> groupReqList) {
		return groupService.saveOrUpdate(groupReqList);
	}

	@Override
	public List<GroupResp> getGroupListByRemoteId(Long tenantId,@RequestParam("remoteId") String remoteId) {
		return groupService.getGroupListByRemoteId(remoteId);
	}

	@Override
	public int delGroupListByGatewayId(Long tenantId,@RequestParam("gatewayId") String gatewayId) {
		return groupService.delGroupListByGatewayId(gatewayId);
	}

	@Override
	public void initGroupBySpaceId(SpaceReq spaceReq) {
		groupService.initGroupBySpaceId(spaceReq);
	}
}
