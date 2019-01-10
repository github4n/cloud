package com.iot.building.group.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.excepiton.BusinessExceptionEnum;
import com.iot.building.group.service.IGroupService;
import com.iot.building.group.vo.GroupReq;
import com.iot.building.group.vo.GroupResp;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.Constants;
import com.iot.building.helper.DispatcherRouteHelper;
import com.iot.building.helper.MountDataUtil;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceDeviceVo;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;

/**
 * 项目名称: IOT云平台 模块名称： 功能描述： 创建人: linjihuang 创建时间: 2018/6/30 10:28 修改人: 修改时间：
 */
@Service("spaceMQTTService")
public class GroupMQTTService implements CallBackProcessor {

	private static final Logger logger = LoggerFactory.getLogger(GroupMQTTService.class);

	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private SpaceDeviceApi spaceDeviceApi;

	@Autowired
	private IBuildingSpaceService spaceService;
	
	@Autowired
	private IGroupService groupService;

	@Autowired
	private Environment dnvironment;
	
	private static final int QOS = 1;

	public static final int MULTI_STATUS = 207;

	public static final String CREATE_GROUP = "createResp";
	public static final String CREATE_RESP = "create_resp";

	public static final String MOUNT_DEVICE = "addResp";
	public static final String ADD_RESP = "add_resp";

	public static final String REMOVE_MOUNT_DEVICE = "removeResp";

	@Override
	public void onMessage(MqttMsg mqttMsg) {

		if (mqttMsg == null) {
			return;
		}
		DispatcherRouteHelper.dispatch(this, mqttMsg);
	}

	public void createReq(MqttMsg msg, String topic) {
	}

	public void createResp(MqttMsg msg, String topic) {
		// 分组创建 回调处理
//		deviceMountSpaceResponse(msg);
	}

	public void deleteReq(MqttMsg msg, String topic) {
	}

	public void deleteResp(MqttMsg msg, String topic) {
	}

	public void addReq(MqttMsg msg, String topic) {
	}

	public void addResp(MqttMsg msg, String topic) {
		// 添加分组成员 回调处理
		deviceMountSpaceResponse(msg);
	}

	public void removeReq(MqttMsg msg, String topic) {
	}

	public void removeResp(MqttMsg msg, String topic) {
		// 移除分组成员 回调处理
//		deviceMountSpaceResponse(msg);
	}

	public void listReq(MqttMsg msg, String topic) {
	}

	/**
	 * 5.1.8 查询分组列表回调
	 * @param msg
	 * @param topic
	 */
	public void listResp(MqttMsg msg, String topic) {
		logger.info("listResp({}, {})", msg, topic);
		Map<String,Object> payload=(Map<String, Object>) msg.getPayload();
		List<Map<String, Object>> listMap=(List<Map<String, Object>>)payload.get("list");
		List<GroupReq>  reqList=new ArrayList<>();
		String gatewayId=msg.getSrcAddr().split("\\.")[1];
		groupService.delGroupListByGatewayId(gatewayId);
		if(CollectionUtils.isNotEmpty(listMap)) {
 			listMap.forEach(map->{
 				GroupReq groupReq =new GroupReq();
 				groupReq.setGatewayId(gatewayId);
 				groupReq.setGroupId(map.get("uuid").toString());
 				//groupReq.setModel(map.get("model")==null?null:map.get("model").toString());
				//groupReq.setName(map.get("name")==null?null:map.get("name").toString());
				//groupReq.setRemoteId(map.get("name").toString().split("\\|")[0]);

				String name = map.get("name").toString();
				String[] mapProperties = name.split("|");
				groupReq.setName(mapProperties[0]);
				groupReq.setModel(mapProperties[1]);
				if(mapProperties.length == 3){
					//等于3为remote，等于2为device
					groupReq.setRemoteId(mapProperties[2]);
				}
 				List<String> deviceIds=(List<String>)map.get("members");//分组下边的成员
 				groupReq.setDeviceIds(deviceIds);
 				reqList.add(groupReq);
 			});
 		}
		groupService.saveOrUpdate(reqList);
		List<GroupReq> groupListStr = Lists.newArrayList();
		for(GroupReq groupReq:reqList){
			if(StringUtils.isBlank(groupReq.getRemoteId())){
				//为device
				//通过remoteId去查找group_id
				List<GroupResp> groupList = groupService.getGroupListByRemoteId(groupReq.getRemoteId());
				RedisCacheUtil.valueSet(Constants.GROUP_REMOTE_KEY+groupReq.getRemoteId(),groupReq.getName());
 				RedisCacheUtil.listSet(Constants.GROUP_NAME_KEY+groupReq.getName(),groupList);
			}
		}
	}

	public void queryMembersReq(MqttMsg msg, String topic) {
	}

	public void queryMembersResp(MqttMsg msg, String topic) {
	}

	public void operateReq(MqttMsg msg, String topic) {
	}

	public void operateResp(MqttMsg msg, String topic) {
		logger.info("operateResp({}, {})", msg, topic);
		Map<String,Object> payload=(Map<String, Object>) msg.getPayload();
		String uuid=payload.get("uuid").toString();
		Map<String,Object> params=(Map<String,Object>)payload.get("params");//控制属性
		List<String> deviceIds=RedisCacheUtil.listGetAll(Constants.GROUP_UUID_KEY+uuid, String.class);
		// 更新内存中并回调前段
		if(CollectionUtils.isNotEmpty(deviceIds)) {
			deviceIds.forEach(devId->{
//				GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(devId);
				CenterControlDeviceStatus.putDeviceStatus(devId, params);
//				if (MapUtils.isNotEmpty(params)) {
//					MapCallBack.mapCallBack(deviceResp, params, APIType.MultiProtocolGateway);
//				}
			});
		}
//		logger.info("operateResp({}, {})", msg, topic);
//		Map<String,Object> payload=(Map<String, Object>) msg.getPayload();
//		String uuid=payload.get("uuid").toString();
//		Map<String,Object> params=(Map<String,Object>)payload.get("params");//控制属性
//		List<String> deviceIds=RedisCacheUtil.listGetAll(Constants.GROUP_UUID_KEY+uuid, String.class);
//		// 更新内存中并回调前段
//		if(CollectionUtils.isNotEmpty(deviceIds)) {
//			deviceIds.forEach(devId->{
//				DeviceResp deviceResp = deviceApi.getDeviceByDeviceUUID(devId);
//				CenterControlDeviceStatus.putDeviceStatus(devId, params);
//				if (MapUtils.isNotEmpty(params)) {
//					MapCallBack.mapCallBack(deviceResp, params, APIType.MultiProtocolGateway);
//				}
//			});
//		}
	}

	/**
	 * 分组信息 回调处理
	 *
	 *            msg
	 * @return
	 */
	private void deviceMountSpaceResponse(MqttMsg msg) {
		Map<String, Object> paramMap = (Map<String, Object>) msg.getPayload();
		// Map<String, Object> ackMap = (Map<String, Object>) msg.getAck();
		MqttMsgAck ack = msg.getAck();
		Map<String, Object> msgMap = Maps.newHashMap();
		String method = msg.getMethod();
		String userId = dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		String centerTopic = "iot/v1/c/" + userId + "/center/deviceMount";
		List<String> members = (List<String>) paramMap.get("members");
		List<String> failIds = Lists.newArrayList();
		if (ack.getData() != null) {
			members.clear();
			ack = setDeviceMsg(ack, members, failIds);
		}
		msgMap.put("successIds", members);
		msgMap.put("failIds", failIds);
		// 分组数据处理
		sendMountMsg(paramMap, ack, centerTopic, msgMap, members, method);
	}

	/**
	 * 组装实体参数
	 *
	 * @param deviceResp
	 * @param uuid
	 * @return
	 */
	private SpaceDeviceReq createTempSpaceDevice(GetDeviceInfoRespVo deviceResp, String uuid) {
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
//		String[] ids = uuid.split("and");
		spaceDeviceReq.setDeviceId(deviceResp.getUuid());
		spaceDeviceReq.setDeviceCategoryId(deviceResp.getDeviceTypeId());
		spaceDeviceReq.setDeviceTypeId(deviceResp.getDeviceTypeId());
		spaceDeviceReq.setLocationId(deviceResp.getLocationId());
		spaceDeviceReq.setTenantId(deviceResp.getTenantId());
		spaceDeviceReq.setBusinessTypeId(deviceResp.getBusinessTypeId());
		spaceDeviceReq.setProductId(deviceResp.getProductId());
		spaceDeviceReq.setSpaceId(Long.valueOf(uuid));
		return spaceDeviceReq;
	}

	/**
	 * 分组信息 保存数据和发送消息
	 *
	 * @param paramMap
	 * @param ack
	 * @param centerTopic
	 * @param msgMap
	 * @param members
	 * @return
	 */
	//TODO 上层未使用，可删除
	private void sendMountMsg(Map<String, Object> paramMap, MqttMsgAck ack, String centerTopic,
			Map<String, Object> msgMap, List<String> members, String method) {
		String uuid = (String) paramMap.get("uuid");
		if ((ack.getCode() == MqttMsgAck.SUCCESS || ack.getCode() == MULTI_STATUS) && StringUtils.isNotBlank(uuid)) {
			for (String deviceId : members) {
				GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(deviceId);
				if (deviceResp != null) {
					if (method.equals(CREATE_GROUP) || method.equals(MOUNT_DEVICE) 
							|| method.equals(CREATE_RESP) || method.equals(ADD_RESP)) {
						SpaceDeviceReq spaceDeviceReq = createTempSpaceDevice(deviceResp, uuid);
						List<SpaceDeviceResp> spaceDeviceResps = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
						if (CollectionUtils.isEmpty(spaceDeviceResps)) {
							spaceDeviceReq.setCreateTime(new Date());
							spaceDeviceReq.setUpdateTime(new Date());
							try {
								spaceDeviceApi.inserSpaceDevice(spaceDeviceReq);
							} catch (Exception e) {
								logger.error(method+"-system-error", e);
					            ack.setCode(MqttMsgAck.ERROR);
					            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
							}
//							MountDataUtil.addLightMountData(uuid, deviceId);
						}
					} 
//					else {
//						String[] ids = uuid.split("and");
//						SpaceDeviceVo spaceDeviceVO = new SpaceDeviceVo();
//						spaceDeviceVO.setSpaceId(Long.valueOf(ids[0]));
//						spaceDeviceVO.setDeviceId(deviceId);
//						spaceDeviceApi.deleteSpaceDeviceByDeviceId(deviceResp.getTenantId(),deviceId);
////						MountDataUtil.removeLightMountData(uuid, deviceId);
//					}
				}
				BusinessDispatchMqttHelper.sendDeviceMountTopic(msgMap, centerTopic);
			}
		} else {
			// 设备挂载、移除失败 或者 分组创建失败
			msgMap.put("successIds", new ArrayList<>());
			msgMap.put("failIds", members);
			BusinessDispatchMqttHelper.sendDeviceMountTopic(msgMap, centerTopic);
		}
	}

	/**
	 * 挂载设备回调 保存数据和发送消息
	 *
	 * @param ack
	 * @param members
	 * @return
	 */
	//TODO 上层未使用，可删除
	private MqttMsgAck setDeviceMsg(MqttMsgAck ack, List<String> members, List<String> failIds) {
		// MqttMsgAck ack = MqttMsgAck.successAck(ackMap);
		List<Map<String, Object>> data = (List<Map<String, Object>>) ack.getData();
		for (Map<String, Object> map : data) {
			if ((int) map.get("code") == 0) {
				members.add(map.get("did").toString());
			} else {
				failIds.add(map.get("did").toString());
			}
		}
		return ack;
	}

	/**
	 * 5.1.3 编辑分组信息回调
	 * @param msg
	 * @param topic
	 */
	public void updateBaseResp(MqttMsg msg, String topic) {
	}
	
	/**
	 * 5.1.7 查询分组列表回调
	 * @param msg
	 * @param topic
	 */
	public void queryBaseResp(MqttMsg msg, String topic) {
	}
	
}
