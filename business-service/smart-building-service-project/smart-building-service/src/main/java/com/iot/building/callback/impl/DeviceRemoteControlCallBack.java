package com.iot.building.callback.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.building.callback.ListenerCallback;
import com.iot.building.device.api.DeviceRemoteApi;
import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.building.device.vo.DeviceRemoteControlResp;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.group.service.IGroupService;
import com.iot.building.group.vo.GroupResp;
import com.iot.building.helper.Constants;
import com.iot.building.scene.service.SceneService;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.enums.APIType;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

public class DeviceRemoteControlCallBack implements ListenerCallback {

	private static final Logger log = LoggerFactory.getLogger(DeviceRemoteControlCallBack.class);

	private IDeviceRemoteService deviceRemoteService = ApplicationContextHelper.getBean(IDeviceRemoteService.class);

	private IBuildingSpaceService spaceService = (IBuildingSpaceService) ApplicationContextHelper.getBean(IBuildingSpaceService.class);

	private SceneService sceneService = (SceneService) ApplicationContextHelper.getBean(SceneService.class);

	private IGroupService iGroupService = (IGroupService) ApplicationContextHelper.getBean(IGroupService.class);

	@Override
	public void callback(GetDeviceInfoRespVo device, Map<String, Object> map, APIType apiType) {
		long start = System.currentTimeMillis();
		log.info("............进入遥控器回调。。。。。。。。。。。。。");
		try {
			// 根据传进来的DeviceTypeId 判断是否是遥控器 不是就跳过 是就响应时间
			if (filter(device, map)) {
				String event = map.get("event") == null ? "" : map.get("event").toString();
				if (StringUtils.isNotBlank(event)) {
					excuteKeyFunction(device, event);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
        log.info("++++++++++遥控器回调处理耗时（ms）=" + (end - start));
		log.info("............遥控器回调end end。。。。。。。。。。。。。");
	}

	/**
	 * 过滤条件
	 *
	 * @param device
	 * @param map
	 */
	private Boolean filter(GetDeviceInfoRespVo device, Map<String, Object> map) {
		if (device.getDeviceTypeId().longValue() == -1018L ||
				device.getDeviceTypeId().longValue() == -1022L ||
				device.getDeviceTypeId().longValue() == -1027L ||
				device.getDeviceTypeId().longValue() == -1028L ||
				device.getDeviceTypeId().longValue() == -1029L) {// 遥控类型
			return true;
		}
		return false;
	}

	/**
	 * 获取遥控器按键对应的功能点
	 *
	 * @param deviceTypeId
	 * @param event
	 * @return
	 * @throws Exception
	 */
	private DeviceRemoteControlResp getKeyTypeValue(GetDeviceInfoRespVo device, String event) throws Exception {
		// 查询缓存是否存在该遥控器的类型
		if (Constants.DEVICE_REMOTE_CONTROL.get(device.getDeviceTypeId()) == null) {
			List<DeviceRemoteControlResp> remoteControlList = deviceRemoteService.findRemoteControlByDeviceType(device.getTenantId(), device.getDeviceTypeId());
			// 不存在查询数据库在放入缓存
			Constants.DEVICE_REMOTE_CONTROL.put(device.getDeviceTypeId(), remoteControlList);
		}
		return getRemoteControlByKey(event, Constants.DEVICE_REMOTE_CONTROL.get(device.getDeviceTypeId()));
	}

	/**
	 * 获取遥控器的功能键
	 *
	 * @param event
	 * @param remoteControlList
	 * @return
	 */
	private DeviceRemoteControlResp getRemoteControlByKey(String event, List<DeviceRemoteControlResp> remoteControlList) {
		DeviceRemoteControlResp deviceRemoteControl = null;
		if (CollectionUtils.isNotEmpty(remoteControlList)) {
			for (DeviceRemoteControlResp remoteControl : remoteControlList) {
				if (remoteControl.getEvent().equals(event)) {
					deviceRemoteControl = remoteControl;
					break;
				}
			}
		}
		return deviceRemoteControl;
	}

	/**
	 * 执行遥控器按键的功能
	 *
	 * @param device
	 * @param keyCode
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void excuteKeyFunction(GetDeviceInfoRespVo device, String keyCode) throws Exception {
		// 根据设备ID查询该遥控器的归属房间
		SpaceDeviceResp spaceDevice = spaceService.findSpaceIdByDeviceId(device.getUuid(),device.getOrgId(),device.getTenantId());
		if (spaceDevice != null) {
		    //获取遥控器按键对应的功能点
			DeviceRemoteControlResp deviceRemoteControl = getKeyTypeValue(device, keyCode);
			if (deviceRemoteControl != null) {
				Map<String, Object> propertyMap = new HashMap<>();
				if (StringUtils.isNotBlank(deviceRemoteControl.getDefaultValue())) {
					propertyMap = JSON.parseObject(deviceRemoteControl.getDefaultValue(), Map.class);
				}
				dispatchExcute(spaceDevice.getSpaceId(), deviceRemoteControl, device, propertyMap);
			}
		}
	}

	/**
	 * 根据控制器定义的类型走不同业务
	 *
	 * @param spaceId
	 * @param deviceRemoteControl
	 * @param propertyMap
	 */
	private void dispatchExcute(Long spaceId, DeviceRemoteControlResp deviceRemoteControl, GetDeviceInfoRespVo deviceResp,
			Map<String, Object> propertyMap) {
		List<GroupResp> groupRespList = Lists.newArrayList();
		log.info("遥控器发送的业务(deviceRemoteControl-type):"+deviceRemoteControl.getType());
		switch (deviceRemoteControl.getType()) {
		case "GROUP_ONOFF":
//			List<String> deviceIds = spaceService.getDeviceIdBySpaceId(spaceId);
//			int flag = spaceService.judgeSpaceSwitchStatus(deviceIds) == 0 ? 1 : 0;
			propertyMap = getOnOff(deviceResp, propertyMap);
			groupRespList = iGroupService.getGroupListByRemoteId(deviceResp.getUuid());
			groupControl(deviceResp, propertyMap, groupRespList);
			break;
		case "GROUP_DIMMING_ADD":
			propertyMap = addDimming(spaceId, propertyMap);
			groupRespList = iGroupService.getGroupListByRemoteId(deviceResp.getUuid());
			groupControl(deviceResp, propertyMap, groupRespList);
			break;
		case "GROUP_DIMMING_SUB":
			propertyMap = subDimming(spaceId, propertyMap);
			groupRespList = iGroupService.getGroupListByRemoteId(deviceResp.getUuid());
			groupControl(deviceResp, propertyMap, groupRespList);
			break;
		case "SCENE":
			sceneService.sceneExecuteNext(deviceResp.getTenantId(),spaceId);
			break;
		case "ONOFF":
		    //如果房间遥控器灯缓存不存在 默认是0 也就是开的状态
			int onOff = Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid()) == null
			?0: Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid());

			String name=(onOff==1?"CLOSE":"OPEN");
			//如果之前是open 改成 CLOSE 之前是CLOSE改成OPEN
			int flag=(onOff==1?0:1);
			sceneService.excuteSceneBySpaceAndSceneName(deviceResp.getTenantId(),spaceId, name);
			//执行成功之后更新该遥控器的缓存状态
            Constants.REMOTE_ONOFF_STATUS.put(deviceResp.getUuid(), flag);
			break;
		default:
			sceneService.excuteSceneBySpaceAndSceneName(deviceResp.getTenantId(),spaceId, deviceRemoteControl.getType());
			break;
		}
	}

	private Map<String, Object> getOnOff(GetDeviceInfoRespVo deviceResp, Map<String, Object> propertyMap) {
		int OnOff = Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid()) == null
				? Integer.parseInt(propertyMap.get("OnOff").toString())
				: Constants.REMOTE_ONOFF_STATUS.get(deviceResp.getUuid());
		if (OnOff == 1) {
			OnOff = 0;
		}else {
			OnOff = 1;
		}
		Constants.REMOTE_ONOFF_STATUS.put(deviceResp.getUuid(), OnOff);
		propertyMap.put("OnOff", OnOff);
		return propertyMap;
	}

	private void groupControl(GetDeviceInfoRespVo deviceResp, Map<String, Object> propertyMap, List<GroupResp> groupRespList) {
		for (GroupResp groupResp : groupRespList) {
			try {
				MultiProtocolGatewayHepler.groupControl(deviceResp.getParentId(), groupResp.getGroupId().toString(),
						propertyMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 累加dimming
	 *
	 * @param spaceId
	 * @param porperty
	 * @return
	 */
	private Map<String, Object> addDimming(Long spaceId, Map<String, Object> porperty) {
		int dimming = Constants.SPACE_DIMMING_STATUS.get(spaceId) == null
				? Integer.parseInt(porperty.get("Dimming").toString())
				: Constants.SPACE_DIMMING_STATUS.get(spaceId);
		dimming += 30;
		if (dimming > 100) {
			dimming = 100;
		}
		Constants.SPACE_DIMMING_STATUS.put(spaceId, dimming);
		porperty.put("Dimming", dimming);
		return porperty;
	}

	/**
	 * 递减dimming
	 *
	 * @param spaceId
	 * @param porperty
	 * @return
	 */
	private Map<String, Object> subDimming(Long spaceId, Map<String, Object> porperty) {
		int dimming = Constants.SPACE_DIMMING_STATUS.get(spaceId) == null
				? Integer.parseInt(porperty.get("Dimming").toString())
				: Constants.SPACE_DIMMING_STATUS.get(spaceId);
		dimming -= 30;
		if (dimming < 10) {
			dimming = 10;
		}
		Constants.SPACE_DIMMING_STATUS.put(spaceId, dimming);
		porperty.put("Dimming", dimming);
		return porperty;
	}

	public static void main(String[] args) {
		Integer m=null;
		int onOff = m == null ? 0: m;
		String name=onOff==1?"CLOSE":"OPEN"; int flag=onOff==1?0:1;
		System.out.println(onOff+"====="+name+"===="+flag);
	}
}
