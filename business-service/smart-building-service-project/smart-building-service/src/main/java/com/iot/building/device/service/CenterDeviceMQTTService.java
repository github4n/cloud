package com.iot.building.device.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.callback.impl.DeviceRemoteControlCallBack;
import com.iot.building.device.service.impl.DeviceService;
import com.iot.building.device.util.RedisKeyUtil;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.group.service.IGroupService;
import com.iot.building.group.vo.GroupResp;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.helper.Constants;
import com.iot.building.helper.MapCallBack;
import com.iot.building.helper.OnlineStatusEnum;
import com.iot.building.ifttt.api.AutoTobApi;
import com.iot.building.mqtt.BusinessDispatchMqttHelper;
import com.iot.building.ota.service.OtaControlService;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.utils.MQTTUtils;
import com.iot.common.beans.BeanUtil;
import com.iot.common.enums.APIType;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStatusCoreApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import com.iot.device.vo.req.ota.OtaFileInfoReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.device.vo.rsp.ota.OtaFileInfoResp;
import com.iot.ifttt.api.IftttApi;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;

@Service("centerDevice")
public class CenterDeviceMQTTService {

	public static final int QOS = 1;

	private final static Logger LOGGER = LoggerFactory.getLogger(CenterDeviceMQTTService.class);
	/**
	 * 线程池
	 */
	private static ExecutorService executorService = Executors.newCachedThreadPool();
	@Autowired
	private DeviceTobService deviceTobService;
	@Autowired
	private IBuildingSpaceService buildSpaceService;
	@Autowired
	private SceneApi sceneApi;
	@Autowired
	private DeviceCoreApi deviceCoreApi;
	@Autowired
	private DeviceStatusCoreApi deviceStatusCoreApi;
	@Autowired
	private ProductCoreApi productApi;
	@Autowired
	private OtaControlService otaControlService;
	@Autowired
	private com.iot.control.space.api.SpaceDeviceApi commonSpaceDeviceServce;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private SpaceDeviceApi spaceDeviceApi;
	@Autowired
	private SpaceApi spaceApi;
	@Autowired
	private AutoTobApi autoTobApi;
	@Autowired
	private IftttApi iftttApi;
	@Autowired
	private Environment dnvironment;
	@Autowired
	private IGroupService groupService;

	private void commonDevAttrResp(MqttMsg getDevAttrRespMsg, String getDevAttrRespTopic, String deviceIdKey,
			String paramKey) {
		LOGGER.info("getDevAttrResp({}, {})", getDevAttrRespMsg, getDevAttrRespTopic);
		if (getDevAttrRespMsg.getAck().getCode() == 200) {
			Map<String, Object> payload = (Map<String, Object>) getDevAttrRespMsg.getPayload();
			String devId = (String) payload.get(deviceIdKey);
			Map<String, Object> attr = (Map<String, Object>) payload.get(paramKey);
			// 保存内存中
			CenterControlDeviceStatus.putDeviceStatus(devId, attr);
		}
	}

	/**
	 * 子设备上下线状态或名称改变上报
	 *
	 * @param mqttMsg
	 *            消息
	 * @param statusNotifyTopic
	 *            主题
	 */
	public void statusNotify(MqttMsg mqttMsg, String statusNotifyTopic) {
		LOGGER.info("statusNotify({}, {})", mqttMsg, statusNotifyTopic);
		try {
			Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
			Integer status = payload.get("status") == null ? 0 : Integer.valueOf(payload.get("status").toString());// 0
			// 表示离线,
			// 1
			// 表示在线
			String deviceId = payload.get("did") == null ? null : payload.get("did").toString();
			if (StringUtils.isNotBlank(deviceId)) {
				GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
				UpdateDeviceStatusReq req = new UpdateDeviceStatusReq();
				req.setDeviceId(deviceId);
				req.setTenantId(vo.getTenantId());
				if (status == 0) {
					req.setOnlineStatus(OnlineStatusEnum.DISCONNECTED.getCode());
				} else if (status == 1) {
					req.setOnlineStatus(OnlineStatusEnum.CONNECTED.getCode());
				}
				deviceStatusCoreApi.saveOrUpdate(req);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("statusNotify-error.{}", e);
		}
	}

	/**
	 * 设备上线
	 *
	 * @param connectMsg
	 *            消息
	 * @param connectTopic
	 *            主题
	 */
	public void connect(MqttMsg connectMsg, String connectTopic) {
		LOGGER.info("connect({}, {})", connectMsg, connectTopic);
		try {
			String deviceId = MQTTUtils.parseReqTopic(connectTopic);
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						UpdateDeviceStatusReq req = new UpdateDeviceStatusReq();
						Map<String, Object> payload = (Map<String, Object>) connectMsg.getPayload();
						String token = (String) payload.get("token");
						String tokenOld = null;
						LOGGER.info("============tokenNew====" + token);
						req.setDeviceId(deviceId);
						req.setOnlineStatus(OnlineStatusEnum.CONNECTED.getCode());
						req.setToken(token);
						GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
						LOGGER.info("============connectd====vo:" + vo);
						if (vo != null) {
							LOGGER.info("============connect===上线=====");
							req.setTenantId(vo.getTenantId());
							UpdateDeviceInfoReq deviceInfoReq = new UpdateDeviceInfoReq();
							deviceInfoReq.setUuid(deviceId);
							deviceInfoReq.setDevModel((String) payload.get("model"));
							deviceInfoReq.setMac((String) payload.get("mac"));
							deviceInfoReq.setVersion((String) payload.get("fw_ver"));
							Map<String, Object> netif = (Map<String, Object>) payload.get("netif");
							deviceInfoReq.setIp((String) netif.get("localIp"));
							deviceInfoReq.setTenantId(vo.getTenantId());
							deviceInfoReq.setDeviceTypeId(1L);
							// 判断网关是否复位
							LOGGER.info("============哪个网关====" + deviceId);
							GetDeviceStatusInfoRespVo getDeviceStatusInfoRespVo = deviceStatusCoreApi
									.get(vo.getTenantId(), deviceId);
							LOGGER.info(
									"============connectd====getDeviceStatusInfoRespVo:" + getDeviceStatusInfoRespVo);
							if (getDeviceStatusInfoRespVo != null) {
								tokenOld = getDeviceStatusInfoRespVo.getToken();
							}
							LOGGER.info("============tokenOld====" + tokenOld);
							if (StringUtil.isBlank(tokenOld)) {// 网关不是复位，直接做上线操作
								LOGGER.info("============网关不是复位======直接做上线操作");
								deviceCoreApi.saveOrUpdate(deviceInfoReq);
								deviceStatusCoreApi.saveOrUpdate(req);
								LOGGER.info("============网关不是复位======直接做上线操作=====success");
							} else {
								if (token.equals(tokenOld)) {// 新旧两个token相同，直接做上线操作
									LOGGER.info("============新旧两个token相同的======直接做上线操作");
									deviceCoreApi.saveOrUpdate(deviceInfoReq);
									deviceStatusCoreApi.saveOrUpdate(req);
									LOGGER.info("============新旧两个token相同的======success");
								} else {
									// 新旧两个token不相同，网关复位了，先删除ifttt,子设备，device-space,scene
									LOGGER.info("============新旧两个token不是相同的======直接网关复位操作====start");
									List<String> deviceIds = Lists.newArrayList();
									LOGGER.info("============新旧两个token不是相同的======直接网关复位操作====获取子设备id==start");
									// 获取子设备id
									List<ListDeviceInfoRespVo> listDeviceInfoRespVos = deviceCoreApi
											.listDevicesByParentId(deviceId);
									LOGGER.info(
											"============新旧两个token不是相同的======直接网关复位操作====获取子设备id==listDeviceInfoRespVos.size:"
													+ listDeviceInfoRespVos.size());
									for (ListDeviceInfoRespVo listDeviceInfoRespVo : listDeviceInfoRespVos) {
										deviceIds.add(listDeviceInfoRespVo.getUuid());
									}
									LOGGER.info("============新旧两个token不是相同的======直接网关复位操作====获取子设备id==deviceIds:"
											+ deviceIds.size());
									// 删除设备对应的空间关系
									LOGGER.info(
											"============新旧两个token不是相同的======直接网关复位操作====deleteMountByDeviceIds==start");
									buildSpaceService.deleteMountByDeviceIds(
											org.apache.commons.lang.StringUtils.join(deviceIds, ","), vo.getTenantId(),vo.getOrgId());
									LOGGER.info(
											"============新旧两个token不是相同的======直接网关复位操作====deleteMountByDeviceIds==end");
									// 子设备删除后删除对应的情景,ifttt
									for (String deviceIdNew : deviceIds) {
										LOGGER.info(
												"============新旧两个token不是相同的======直接网关复位操作====deleteDeviceRelation==start");
										boolean flag = deviceTobService.deleteDeviceRelation(vo.getOrgId(), deviceIdNew,
												vo.getTenantId(), true, deviceId);
										LOGGER.info(
												"============新旧两个token不是相同的======直接网关复位操作====deleteDeviceRelation==end");
									}
									LOGGER.info("============新旧两个token不是相同的======直接网关复位操作====deleteByDeviceId==start");
									deviceCoreApi.deleteByDeviceId(deviceId);
									LOGGER.info("============新旧两个token不是相同的======直接网关复位操作====deleteByDeviceId==end");
									LOGGER.info("============新旧两个token不是相同的======直接网关复位操作=====success");
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.info("connect-device-error1", e);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("connect-device-error2", e);
		}
	}

	/**
	 * 设备下线
	 *
	 * @param disConnectMsg
	 *            消息
	 * @param disConnectTopic
	 *            主题
	 */
	public void disconnect(MqttMsg disConnectMsg, String disConnectTopic) {
		LOGGER.info("disconnect({}, {})", disConnectMsg, disConnectTopic);
		try {
			String deviceId = MQTTUtils.parseReqTopic(disConnectTopic);
			executorService.submit(new Runnable() {
				@Override
				public void run() {
					try {
						String token = null;
						GetDeviceInfoRespVo vo = deviceCoreApi.get(deviceId);
						LOGGER.info("============disconnectd====vo:" + vo);
						if (vo != null) {
							LOGGER.info("============disconnect===下线=====");
							GetDeviceStatusInfoRespVo getDeviceStatusInfoRespVo = deviceStatusCoreApi
									.get(vo.getTenantId(), deviceId);
							if (getDeviceStatusInfoRespVo != null) {
								LOGGER.info("============disconnectd====getDeviceStatusInfoRespVo:"
										+ getDeviceStatusInfoRespVo);
								token = getDeviceStatusInfoRespVo.getToken();
							}
							LOGGER.info("============网关id====" + deviceId);
							LOGGER.info("============下线前的token====" + token);
							UpdateDeviceStatusReq req = new UpdateDeviceStatusReq();
							req.setTenantId(vo.getTenantId());
							req.setDeviceId(deviceId);
							req.setOnlineStatus(OnlineStatusEnum.DISCONNECTED.getCode());
							req.setToken(token);
							LOGGER.info("============disconnect===下线=====saveOrUpdate====start");
							deviceStatusCoreApi.saveOrUpdate(req);
							LOGGER.info("============disconnect===下线=====saveOrUpdate====success");
						}
					} catch (Exception e) {
						e.printStackTrace();
						LOGGER.info("disconnect-device-error.{}", e);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("disconnect-device-error", e);
		}
	}

	/***********************************
	 * 以下为2b新的逻辑subDevice回调
	 ***************************************/

	/**
	 * 4.46设备状态通知
	 *
	 * @param msg
	 * @param reqTopic
	 * @return
	 * @author lucky
	 * @date 2018/5/25 8:32
	 */
	public void devStautsNotif(MqttMsg msg, String reqTopic) {
	}

	public void setDevAttrResp(MqttMsg msg, String resqTopic) {

	}

	/**
	 * 获取网关基本信息
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void getInfoResp(MqttMsg msg, String resqTopic) {
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		Map<String, Object> netif = (Map<String, Object>) payload.get("netif");
		String directDeviceId = msg.getSrcAddr().split("\\.")[1];
		GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreApi.get(directDeviceId);
		UpdateDeviceInfoReq deviceInfoReq = new UpdateDeviceInfoReq();
		deviceInfoReq.setUuid(directDeviceId);
		deviceInfoReq.setMac((String) payload.get("mac"));
		deviceInfoReq.setIp((String) netif.get("localIp"));
		deviceInfoReq.setName((String) payload.get("name"));
		deviceInfoReq.setVersion((String) payload.get("fw_ver"));
		deviceInfoReq.setDevModel((String) payload.get("model"));
		deviceInfoReq.setTenantId(deviceInfoRespVo.getTenantId());
		deviceCoreApi.saveOrUpdate(deviceInfoReq);
	}

	/**
	 * 4.1 子设备属性上报
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void propsNotify(MqttMsg msg, String resqTopic) {
		commonNotify(msg, resqTopic, "did", "params");
	}

	/**
	 * 4.8 子设备实时属性上报
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void updatePropResp(MqttMsg msg, String resqTopic) {
		// 1.更新在线
		if (msg.getAck().getCode() == 200) {
			Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
			payload.put("status", 1);// 1表示在线
			statusNotify(msg, resqTopic);
		}
		// 2.更新状态
		commonDevAttrResp(msg, resqTopic, "did", "params");
	}

	/**
	 * 4.2 子设备事件上报
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void eventNotify(MqttMsg msg, String resqTopic) {
		LOGGER.info("eventNotify==========eventType=========1");

		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String eventType = String.valueOf(payload.get("event"));
		LOGGER.info("eventNotify==========eventType=========" + eventType);
		if (StringUtils.isNotBlank(eventType) && eventType.equals("button")) {
			// 为13键墙控
			LOGGER.info("eventNotify==========eventType=========2");
			commonNotify(msg, resqTopic, "did", "params");
		} else {
			commonNotifyEvent(msg, resqTopic, "did", null);
		}
	}

	/**
	 * 4.4 设置子设备属性回调
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void setPropResp(MqttMsg msg, String resqTopic) {

	}

	/**
	 * 4.5 获取子设备属性回调
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void getPropResp(MqttMsg msg, String resqTopic) {
		commonDevAttrResp(msg, resqTopic, "did", "params");
	}

	/**
	 * 4.8 查询子设备列表回调
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void queryResp(MqttMsg msg, String resqTopic) {
		commonInitChildDevice(msg);
	}

	/**
	 * 4.9 子设备列表变更上报回调
	 *
	 * @param msg
	 * @param resqTopic
	 */
	public void listNotify(MqttMsg msg, String resqTopic) {
		commonInitChildDevice(msg);
	}

	private void commonInitChildDevice(MqttMsg msg) {
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String directDeviceId = msg.getSrcAddr().split("\\.")[1];
		// 查询现有的子设备
		List<String> oldDeviceIds = getOldDevieIds(directDeviceId);
		List<Map> childList = JSON.parseArray(payload.get("list").toString(), Map.class);// (List<Map<String, Object>>)
		List<SpaceDeviceReq> listReq = new ArrayList<>();// payload.get("list");
		if (CollectionUtils.isNotEmpty(childList)) {
			GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(directDeviceId);
			if (deviceResp != null) {
				List<SpaceDeviceReq> spaceDeviceReqs = Lists.newArrayList();
				for (Map<String, Object> map : childList) {
					// 解析并保存数据--先删除子设备在添加做全量5--解析还要注意名称规则
					// String name = (String) map.get("name");
					// map.put("name", name);
					try {
						SpaceDeviceReq req = saveNewDevice(map, deviceResp);
						if (req == null) {
							continue;
						}
						listReq.add(req);
						// 移除旧的存在设备
						oldDeviceIds.remove(req.getDeviceId());
						// 删除空间和设备关系
						// commonSpaceDeviceServce.deleteSpaceDeviceByDeviceId(deviceResp.getTenantId(),
						// req.getDeviceId());
						// 添加空间和设备关系
						// spaceDeviceReqs.add(req);
						// 获取设备版本号
						MultiProtocolGatewayHepler.otaQueryReq(directDeviceId, req.getDeviceId());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				// if (CollectionUtils.isNotEmpty(spaceDeviceReqs)) {
				// commonSpaceDeviceServce.saveSpaceDeviceList(spaceDeviceReqs);
				// }
			}
		}
		if (CollectionUtils.isNotEmpty(oldDeviceIds)) {
			GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(directDeviceId);
			if (getDeviceInfoRespVo != null) {
				SpaceAndSpaceDeviceVo vo = new SpaceAndSpaceDeviceVo();
				vo.setDeviceIds(oldDeviceIds);
				vo.setTenantId(getDeviceInfoRespVo.getTenantId());
				commonSpaceDeviceServce.deleteSpaceDeviceBySpaceIdsOrDeviceIds(vo);
				deviceCoreApi.deleteBatchByDeviceIds(oldDeviceIds);
			}
		}

		// 查询直连设备 相关产品ID 是否存在OTA包列表数据 否：新增数据
		saveDirectDeviceOtaFileInfo(directDeviceId);
		try {
			// 获取分组信息
			MultiProtocolGatewayHepler.queryGroupList(directDeviceId);
			// 获取网关信息
			MultiProtocolGatewayHepler.getInfoReq(directDeviceId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 发送mqtt消息到前段页面
		Map<String, Object> map = Maps.newHashMap();
		map.put("result", "success");
		sendMqttMsg("center", "synchronization", map);
	}

	private void sendMqttMsg(String method, String title, Map<String, Object> map) {
		String uuid = dnvironment.getProperty(Constants.AGENT_MQTT_USERNAME);
		String topic = "iot/v1/c/" + uuid + "/" + method + "/" + title;
		BusinessDispatchMqttHelper.sendSynDeviceTopic(map, topic);
	}

	private void saveDirectDeviceOtaFileInfo(String directDeviceId) {
		GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(directDeviceId);
		if (deviceResp != null) {
			GetProductInfoRespVo productResp = productApi.getByProductId(deviceResp.getProductId());
			saveOtaFileInfo(deviceResp.getLocationId(), deviceResp.getTenantId(), productResp);
		}
	}

	private void saveOtaFileInfo(Long locationId, Long tenantId, GetProductInfoRespVo productResp) {
		if (productResp != null) {
			OtaFileInfoReq otaFileInfoReq = new OtaFileInfoReq();
			otaFileInfoReq.setTenantId(tenantId);
			otaFileInfoReq.setLocationId(locationId);
			otaFileInfoReq.setProductId(productResp.getId());
			OtaFileInfoResp otaFileInfoResp = otaControlService.findOtaFileInfoByProductId(otaFileInfoReq);
			if (otaFileInfoResp == null) {
				otaFileInfoReq.setCreateTime(new Date());
				otaFileInfoReq.setUpdateTime(new Date());
				otaControlService.saveOtaFileInfo(otaFileInfoReq);
			}
		}
	}

	private SpaceDeviceReq saveNewDevice(Map<String, Object> map, GetDeviceInfoRespVo deviceResp) {
		String deviceId = map.get("did").toString();
		String model = map.get("model").toString();
		String mac = map.get("mac").toString();
		String status = map.get("status").toString();
		SpaceDeviceReq req = null;
		req = new SpaceDeviceReq();
		Long locationId = deviceResp.getLocationId();
		Long tenantId = deviceResp.getTenantId();
		req.setDeviceId(deviceId);
		req.setLocationId(locationId);
		req.setTenantId(tenantId);
		GetProductInfoRespVo productResp = productApi.getByProductModel(model);
		req.setProductId(productResp == null ? null : productResp.getId());
		req.setDeviceTypeId(productResp == null ? null : productResp.getDeviceTypeId());
		GetDeviceInfoRespVo resp = deviceCoreApi.get(deviceId);
		UpdateDeviceInfoReq deviceReq = setDeviceReqInfo(deviceId, model, mac, resp, productResp, locationId, tenantId,
				deviceResp.getUuid(), status);
		deviceReq.setTenantId(tenantId);
		// 保存新设备,已存在则更新
		deviceCoreApi.saveOrUpdate(deviceReq);
		saveOtaFileInfo(locationId, tenantId, productResp);
		return req;
	}

	/**
	 *
	 * @param deviceId
	 * @param model
	 * @param mac
	 * @param locationId
	 * @param tenantId
	 * @return
	 */
	private UpdateDeviceInfoReq setDeviceReqInfo(String deviceId, String model, String mac, GetDeviceInfoRespVo resp,
			GetProductInfoRespVo productResp, Long locationId, Long tenantId, String directDeviceId, String status) {
		String deviceName = "";
		if (resp != null && StringUtils.isNotBlank(resp.getName())) {
			deviceName = resp.getName();
		} else {
			Integer number = RedisCacheUtil.valueObjGet(RedisKeyUtil.getDeviceNumberKey(tenantId), Integer.class);
			if (number == null) {
				number = 0;
			}
			deviceName = mac + "_" + (number + 1);
			RedisCacheUtil.valueObjSet(RedisKeyUtil.getDeviceNumberKey(tenantId), (number + 1));
		}
		UpdateDeviceInfoReq info = new UpdateDeviceInfoReq();
		info.setUuid(deviceId);
		info.setName(deviceName);
		info.setDevModel(model);
		info.setMac(mac);
		info.setParentId(directDeviceId);
		info.setIsDirectDevice(Constants.IS_NOT_DIRECT_DEVICE);
		info.setLocationId(locationId);
		info.setTenantId(tenantId);
		info.setProductId(productResp == null ? null : productResp.getId());
		info.setDeviceTypeId(productResp == null ? null : productResp.getDeviceTypeId());
		// 获取ota列表的最新版本
		if (productResp != null && productResp.getId() != null) {
			OtaFileInfoReq otaFileInfoReq = new OtaFileInfoReq();
			otaFileInfoReq.setTenantId(tenantId);
			otaFileInfoReq.setLocationId(locationId);
			otaFileInfoReq.setProductId(productResp.getId());
			OtaFileInfoResp otaFileInfoResp = otaControlService.findOtaFileInfoByProductId(otaFileInfoReq);
			if (otaFileInfoResp != null && StringUtils.isNotBlank(otaFileInfoResp.getVersion())) {
				info.setHwVersion(otaFileInfoResp.getVersion());
			}
		}
		if (StringUtils.isNotBlank(status)) {
			UpdateDeviceStatusReq updateDeviceStatusReq = new UpdateDeviceStatusReq();
			updateDeviceStatusReq.setDeviceId(deviceId);
			updateDeviceStatusReq.setTenantId(tenantId);
			if (status.equals("1")) {
				updateDeviceStatusReq.setOnlineStatus("connected");
			} else if (status.equals("0")) {
				updateDeviceStatusReq.setOnlineStatus("disconnected");
			} else if (status.equals("2")) {
				updateDeviceStatusReq.setOnlineStatus("disconnected");
			}
			deviceStatusCoreApi.saveOrUpdate(updateDeviceStatusReq);
		}
		return info;
	}

	/**
	 * 获取旧的子设备设备ID集合
	 *
	 * @param directDeviceId
	 * @return
	 */
	private List<String> getOldDevieIds(String directDeviceId) {
		List<String> oldDeviceIds = new ArrayList<>();
		List<ListDeviceInfoRespVo> oldChildList = deviceCoreApi.listDevicesByParentId(directDeviceId);
		if (CollectionUtils.isNotEmpty(oldChildList)) {
			for (ListDeviceInfoRespVo resp : oldChildList) {
				oldDeviceIds.add(resp.getUuid());
			}
		}
		return oldDeviceIds;
	}

	/**
	 * 截取需要的内容
	 *
	 * @param name
	 * @param index
	 * @return
	 */
	private String getParamFromName(String name, int index) {
		try {
			return name.split("\\|")[index];// name格式 deviceName|businessTypeId|locationId|tenantId|spaceId|position
		} catch (Exception e) {
			return name;
		}
	}

	private void commonNotify(MqttMsg msg, String resqTopic, String deviceIdKey, String paramKey) {
		LOGGER.info("setDevAttrNotif({}, {})", msg, resqTopic);
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String devId = (String) payload.get(deviceIdKey);
		String eventType = (String) payload.get("event");
		GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(devId);
		Map<String, Object> attr = Maps.newHashMap();
		if (paramKey != null) {
			LOGGER.info("==================button==============start=======");
			if (StringUtils.isNotBlank(eventType) && eventType.equals("button")) {
				LOGGER.info("==================button==============process1=======");
				List<Integer> params = (List<Integer>) payload.get("params");
				if (CollectionUtils.isNotEmpty(params) && params.size() == 3) {
					// 组控回调
					if (params.get(0) == 12 && params.get(2) != null) {
						attr.put("OnOff", params.get(2));
						GetDeviceInfoRespVo deviceInfoRespVo = deviceCoreApi.get(devId);
						// 组控处理
						groupCallbackProcessor(devId, attr, deviceInfoRespVo);
					}
				} else {
					// 黄旭用的
					String paramsStr = payload.get(paramKey).toString();
					String field = "{\"button\":\"" + params + "\"}";
					LOGGER.info("==================button==============field=======");
					attr = (Map<String, Object>) JSON.parse(field);
					// 更新内存中
					CenterControlDeviceStatus.putDeviceStatus(devId, attr);
					if (attr != null && MapUtils.isNotEmpty(attr)) {
						MapCallBack.mapCallBack(deviceResp, attr, APIType.MultiProtocolGateway);
					}
				}
			} else {
				attr = (Map<String, Object>) payload.get(paramKey);
				// 更新内存中
				CenterControlDeviceStatus.putDeviceStatus(devId, attr);
				if (attr != null && MapUtils.isNotEmpty(attr)) {
					MapCallBack.mapCallBack(deviceResp, attr, APIType.MultiProtocolGateway);
				}
			}
		} else {
			DeviceRemoteControlCallBack callBack = new DeviceRemoteControlCallBack();
			callBack.callback(deviceResp, payload, APIType.MultiProtocolGateway);
		}
	}

	private void groupCallbackProcessor(String devId, Map<String, Object> attr, GetDeviceInfoRespVo deviceInfoRespVo) {
		if (deviceInfoRespVo != null) {
			List<String> deviceIds = Lists.newArrayList();
			List<GroupResp> groupResps = groupService.getGroupVoListByRemoteId(devId);
			if (CollectionUtils.isNotEmpty(groupResps)) {
				for (GroupResp groupResp : groupResps) {
					deviceIds.add(groupResp.getDeviceId());								
				}
				if (CollectionUtils.isNotEmpty(deviceIds)) {
					ListDeviceInfoReq param = new ListDeviceInfoReq();
					param.setDeviceIds(deviceIds);
					List<ListDeviceInfoRespVo> Lists = deviceCoreApi.listDevices(param);
					for (ListDeviceInfoRespVo listDeviceInfoRespVo : Lists) {
						if (listDeviceInfoRespVo != null) {
							GetDeviceInfoRespVo device = new GetDeviceInfoRespVo();
							BeanUtil.copyProperties(listDeviceInfoRespVo, device);
							// 更新内存中
							CenterControlDeviceStatus.putDeviceStatus(device.getUuid(), attr);
							if (attr != null && MapUtils.isNotEmpty(attr)) {
								MapCallBack.mapCallBack(device, attr, APIType.MultiProtocolGateway);
							}
						}
					}
				}
			}
		}
	}

	private void commonNotifyEvent(MqttMsg msg, String resqTopic, String deviceIdKey, String paramKey) {
		LOGGER.info("commonNotifyEvent({}, {})", msg, resqTopic);
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String devId = (String) payload.get(deviceIdKey);
		GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(devId);
		DeviceRemoteControlCallBack callBack = new DeviceRemoteControlCallBack();
		callBack.callback(deviceResp, payload, APIType.MultiProtocolGateway);
	}

	private void common13RemoteNotify(MqttMsg msg, String resqTopic) {
		LOGGER.info("common13RemoteNotify({}, {})", msg, resqTopic);
		Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
		String devId = (String) payload.get("did");
		String eventType = (String) payload.get("event");
		Map<String, Object> attr = Maps.newHashMap();
		if (StringUtils.isNotBlank(eventType) && eventType.equals("button")) {
			List<Integer> params = (List<Integer>) payload.get("params");
			if (CollectionUtils.isNotEmpty(params) && params.size() == 3) {
				if (params.get(0) == 12 && params.get(2) != null) {
					attr.put("OnOff", params.get(2));
					// 更新内存中
					CenterControlDeviceStatus.putDeviceStatus(devId, attr);
				}
			}
		}
		if (attr != null && MapUtils.isNotEmpty(attr)) {
			GetDeviceInfoRespVo deviceResp = deviceCoreApi.get(devId);
			MapCallBack.mapCallBack(deviceResp, attr, APIType.MultiProtocolGateway);
		}
	}
}