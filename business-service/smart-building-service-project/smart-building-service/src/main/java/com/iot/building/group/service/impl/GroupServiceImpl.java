package com.iot.building.group.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.group.mapper.GroupMapper;
import com.iot.building.group.service.IGroupService;
import com.iot.building.group.vo.GroupReq;
import com.iot.building.group.vo.GroupResp;
import com.iot.building.helper.Constants;
import com.iot.control.space.api.SpaceApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceReq;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceTypeCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.redis.RedisCacheUtil;

@Service
public class GroupServiceImpl implements IGroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

	@Autowired
	private GroupMapper groupMapper;

	@Autowired
	private SpaceDeviceApi spaceDeviceApi;

	@Autowired
	private SpaceApi spaceApi;

	@Autowired
	private DeviceCoreApi deviceCoreApi;

	@Autowired
	private DeviceTypeCoreApi deviceTypeCoreApi;

	@Override
	public Long saveOrUpdate(List<GroupReq> groupReqList) {
		if (CollectionUtils.isNotEmpty(groupReqList)) {
			for (GroupReq groupReq : groupReqList) {
				if (StringUtils.isBlank(groupReq.getRemoteId())) {
					continue;
				}
				String remoteId = groupReq.getRemoteId();
				groupReq.setRemoteId(remoteId);
				GroupResp group = groupMapper.findGroupByGrouId(groupReq.getGroupId());
				if (group == null) {
					groupMapper.save(groupReq);
				} else {
					groupMapper.updateByGroupId(groupReq);
				}
				List<GroupResp> groupList = groupMapper.findGroupListByRemoteId(remoteId);
				if (groupList == null)
					groupList = new ArrayList<>();
				RedisCacheUtil.listSet(Constants.GROUP_KEY + remoteId, groupList);
				RedisCacheUtil.listSet(Constants.GROUP_UUID_KEY + groupReq.getGroupId(), groupReq.getDeviceIds());// 成员列表
			}
		}

		return null;
	}

	@Override
	public List<GroupResp> getGroupListByRemoteId(String remoteId) {
		// 通过remoteId 获取 name
		String name = groupMapper.getNameByRemoteId(remoteId);
		// 先从redis里获取
		List<GroupResp> groupList = RedisCacheUtil.listGetAll(Constants.GROUP_NAME_KEY + name, GroupResp.class);
		List<GroupResp> respList = new ArrayList<>();
		if (CollectionUtils.isEmpty(groupList)) {
			// 若为空，则在数据库里查找
			respList = groupMapper.getGroupListByName(name);
			return respList;
		} else {
			return groupList;
		}
	}

	@Override
	public int delGroupListByGatewayId(String gatewayId) {
		return groupMapper.delGroupListByGatewayId(gatewayId);
	}

	@Override
	public void initGroupBySpaceId(SpaceReq spaceReq) {
		if (spaceReq != null && spaceReq.getId() != null) {
			// 获取空间底下的设备
			SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
			spaceDeviceReq.setSpaceId(spaceReq.getId());
			spaceDeviceReq.setTenantId(spaceReq.getTenantId());
			spaceDeviceReq.setLocationId(spaceReq.getLocationId());
			List<String> deviceIds = Lists.newArrayList();
			Map<String, List<String>> wallControlMap = Maps.newHashMap();
			List<String> wallControls = Lists.newArrayList();
			List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
			// Map<String, List<String>> params = Maps.newHashMap();
			Set<String> gatewayIdSet = Sets.newHashSet();
			if (CollectionUtils.isNotEmpty(spaceDeviceResps)) {
				for (SpaceDeviceResp spaceDeviceResp : spaceDeviceResps) {
					GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreApi.get(spaceDeviceResp.getDeviceId());
					if (deviceInfoRespVo != null) {
						if (!gatewayIdSet.contains(deviceInfoRespVo.getParentId())) {
							// 下发网关删除分组
							try {
								MultiProtocolGatewayHepler.deleteGroup(deviceInfoRespVo.getParentId(),
										spaceReq.getId().toString());
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
							// 删除数据库
							delGroupListByGatewayId(deviceInfoRespVo.getParentId());
							gatewayIdSet.add(deviceInfoRespVo.getParentId());
							RedisCacheUtil.delete(Constants.GROUP_UUID_KEY + spaceReq.getId());
						}
						// 判断该类型是否为灯类
						if (Constants.getLightTypeMap().containsKey(deviceInfoRespVo.getDeviceTypeId().toString())) {
							List<String> mountDevice = Lists.newArrayList();
							mountDevice.add(deviceInfoRespVo.getUuid());
							deviceIds.add(deviceInfoRespVo.getUuid());
							try {
								MultiProtocolGatewayHepler.mount(deviceInfoRespVo.getParentId(),
										spaceReq.getId().toString(), mountDevice);
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
						}
						// 墙控类型绑定分组
						if (Constants.WallSwitchTypeMap().containsKey(deviceInfoRespVo.getDeviceTypeId().toString())) {
							wallControls = wallControlMap.get(deviceInfoRespVo.getParentId());
							if (CollectionUtils.isEmpty(wallControls)) {
								wallControls = Lists.newArrayList();
							}
							wallControls.add(deviceInfoRespVo.getUuid());
							wallControlMap.put(deviceInfoRespVo.getParentId(), wallControls);
						}
					}
				}
				for (String gatewayId : wallControlMap.keySet()) {
					List<String> liStrings = wallControlMap.get(gatewayId);
					for (String deviceId : liStrings) {
						try {
							MultiProtocolGatewayHepler.setPropReq(gatewayId, deviceId, spaceReq.getId());
							for (String uuid : deviceIds) {
								GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(uuid);
								if (getDeviceInfoRespVo != null && gatewayId.equals(getDeviceInfoRespVo.getParentId())) {
									GroupReq groupReq = new GroupReq();
									groupReq.setGroupId(spaceReq.getId().toString());
									groupReq.setName(spaceReq.getName());
									groupReq.setGatewayId(getDeviceInfoRespVo.getParentId());
									groupReq.setRemoteId(deviceId);
									groupReq.setDeviceId(uuid);
									groupMapper.save(groupReq);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
				// group-device-uuid : group-uuid 缓存
				if (CollectionUtils.isNotEmpty(deviceIds)) {
					RedisCacheUtil.listSet(Constants.GROUP_UUID_KEY + spaceReq.getId(), deviceIds);
				}
			}
		}
	}

	@Override
	public List<GroupResp> getGroupVoListByRemoteId(String remoteId) {
		return groupMapper.getGroupListByRemoteId(remoteId);
	}

}