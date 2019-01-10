package com.iot.building.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

public class MountDataUtil {

	private static final Logger log = LoggerFactory.getLogger(MountDataUtil.class);

	private static DeviceCoreApi deviceService = ApplicationContextHelper.getBean(DeviceCoreApi.class);
	private static SpaceDeviceApi spaceDeviceApi = ApplicationContextHelper.getBean(SpaceDeviceApi.class);

    private static IBuildingSpaceService spaceService = ApplicationContextHelper.getBean(IBuildingSpaceService.class);

	/**
	 * 描述:挂载灯类设备挂载数据
     *
	 * @param spaceId
	 * @param deviceId
	 */
	public static void addLightMountData(String uuid, String deviceId) {
		GetDeviceInfoRespVo device = deviceService.get(deviceId);
		String[] ids = uuid.split("and");
		Set<String> gatewaySet = Constants.SPACE_GATEWAY_MOUNT.get("device-" + ids[0] + "-" + ids[2]);
		Set<String> uuidSet = Constants.SPACE_GROUP_UUID.get("group-" + ids[0] + "-" + ids[2]);
		if (gatewaySet == null) {
			gatewaySet = Sets.newHashSet();
		}
		if (uuidSet == null) {
			uuidSet = Sets.newHashSet();
		}
		gatewaySet.add(device.getParentId());
		uuidSet.add(uuid);
		Constants.SPACE_GATEWAY_MOUNT.put("device-" + ids[0] + "-" + ids[2], gatewaySet);
		Constants.SPACE_GROUP_UUID.put("group-" + ids[0] + "-" + ids[2], uuidSet);
	}

	/**
	 * 描述:移除灯类设备挂载数据
     *
	 * @param spaceId
	 * @param deviceId
	 */
	public static void removeLightMountData(String uuid, String deviceId) {
		GetDeviceInfoRespVo device = deviceService.get(deviceId);
		String[] ids = uuid.split("and");
		Set<String> gatewaySet = Constants.SPACE_GATEWAY_MOUNT.get("device-" + ids[0] + "-" + ids[2]);
		SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
		spaceDeviceReq.setSpaceId(Long.valueOf(ids[0]));
		spaceDeviceReq.setLocationId(device.getLocationId());
		spaceDeviceReq.setTenantId(device.getTenantId());
		spaceDeviceReq.setProductId(device.getProductId());
		spaceDeviceReq.setBusinessTypeId(device.getBusinessTypeId());
		List<SpaceDeviceResp> spaceDevices = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
		if (gatewaySet != null && spaceDevices == null) {
			gatewaySet.remove(device.getParentId());
			Constants.SPACE_GATEWAY_MOUNT.put("group-" + ids[0] + "-" + ids[2], gatewaySet);
		}
	}

	/**
	 * 描述:添加Plug设备挂载数据
     *
	 * @param spaceId
	 * @param deviceId
	 */
	public static void addPlugMountData(long spaceId, String deviceId) {
		GetDeviceInfoRespVo device = deviceService.get(deviceId);
		Set<String> plugSet = Constants.SPACE_PLUG_MOUNT.get(spaceId);
		if (device != null && Constants.getPlugTypeMap().containsKey(device.getDeviceTypeId().toString())) {
			if (plugSet == null) {
				plugSet = new HashSet<>();
			}
			plugSet.add(deviceId);
			Constants.SPACE_PLUG_MOUNT.put(spaceId, plugSet);
		}
	}

	/**
	 * 描述:移除Plug设备挂载数据
     *
	 * @param spaceId
	 * @param deviceId
	 */
	public static void removePlugMountData(long spaceId, String deviceId) {
		GetDeviceInfoRespVo device = deviceService.get(deviceId);
		Set<String> plugSet = Constants.SPACE_PLUG_MOUNT.get(spaceId);
		if (device != null && Constants.getPlugTypeMap().containsKey(device.getDeviceTypeId().toString())) {
			if (plugSet != null) {
				plugSet.remove(deviceId);
				Constants.SPACE_PLUG_MOUNT.put(spaceId, plugSet);
			}
		}
	}

	/**
	 * 描述:添加Hue设备挂载数据
     *
	 * @param spaceId
	 * @param deviceId
	 */
	public static void addExternalDeviceMountData(long spaceId, String deviceId) {
		GetDeviceInfoRespVo device = deviceService.get(deviceId);
		Set<String> externalDeviceSet = Constants.SPACE_EXTERNAL_DEVICE_MOUNT.get(spaceId+"and"+device.getBusinessTypeId());
		if (device != null && Constants.getExternalDeviceType().containsKey(device.getDeviceTypeId().toString())) {
			if (externalDeviceSet == null) {
				externalDeviceSet = new HashSet<>();
			}
			externalDeviceSet.add(deviceId);
			Constants.SPACE_EXTERNAL_DEVICE_MOUNT.put(spaceId+"and"+device.getBusinessTypeId(), externalDeviceSet);
		}
	}

	/**
	 * 描述:移除Hue设备挂载数据
     *
	 * @param spaceId
	 * @param deviceId
	 */
	public static void removeExternalDeviceMountData(long spaceId, String deviceId) {
		GetDeviceInfoRespVo device = deviceService.get(deviceId);
		Set<String> externalDeviceSet = Constants.SPACE_EXTERNAL_DEVICE_MOUNT.get(spaceId+"and"+device.getBusinessTypeId());
		if (device != null && Constants.getExternalDeviceType().containsKey(device.getDeviceTypeId().toString())) {
			if (externalDeviceSet != null) {
				externalDeviceSet.remove(deviceId);
				Constants.SPACE_EXTERNAL_DEVICE_MOUNT.put(spaceId+"and"+device.getBusinessTypeId(), externalDeviceSet);
			}
		}
	}

}
