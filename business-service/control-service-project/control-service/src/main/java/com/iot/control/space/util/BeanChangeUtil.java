package com.iot.control.space.util;

import com.iot.common.util.StringUtil;
import com.iot.control.scene.domain.Scene;
import com.iot.control.space.domain.Space;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.vo.rsp.DeviceResp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：bean转换工具类 创建人： chq 创建时间： 2018/6/21 16:53
 */
public class BeanChangeUtil {


	public static Map<String, Object> sceneToMap(Scene scene) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", scene.getId());
		map.put("name", scene.getSceneName());
		map.put("type", "SCENE");
		return map;
	}


	/**
	 * 对象转换为Map
	 *
	 * @param device
	 * @return
	 */
	public static Map<String, Object> deviceToMap(DeviceResp device) {
		Map<String, Object> map = new HashMap<>();
		String onlineStatus = device.getOnlineStatus();
		boolean online = true;
		if (onlineStatus != null && "disconnected".equals(onlineStatus)) {
			online = false;
		}
		map.put("online", online);
		map.put("devId", StringUtil.isBlank(device.getDeviceId()) ? StringUtil.EMPTY : device.getDeviceId());
		map.put("parentId", StringUtil.isBlank(device.getParentId()) ? StringUtil.EMPTY : device.getParentId());
		map.put("name", StringUtil.isBlank(device.getName()) ? StringUtil.EMPTY : device.getName());
		map.put("productId", device.getProductId() == null ? StringUtil.EMPTY : device.getProductId().toString());
		map.put("icon", StringUtil.isBlank(device.getIcon()) ? StringUtil.EMPTY : device.getIcon());
		map.put("password", StringUtil.isBlank(device.getPassword()) ? StringUtil.EMPTY : device.getPassword());
		return map;
	}

	/**
	 * Map 转换 List<Long>
	 *
	 * @param groupMapList
	 * @return
	 */
	private List<Long> getGroupIds(List<Map<String, Object>> groupMapList) {
		List<Long> groupIds = new ArrayList<>();
		if (!groupMapList.isEmpty()) {
			groupMapList.forEach(map -> groupIds.add((Long) map.get("id")));
		}
		return groupIds;
	}
	
	/**
	 * List对象转List<Map>
	 *
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> spaceToMapList(List<SpaceResp> list) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		if (!list.isEmpty()) {
			Map<String, Object> mapSpace = null;
			for (SpaceResp spaceResp : list) {
				mapSpace = spaceToMap(spaceResp,null);
				mapList.add(mapSpace);
			}
		}
		return mapList;
	}
	
	/**
	 * 对象转换为Map
	 *
	 * @param space
	 * @return
	 */
	public static Map<String, Object> spaceToMap(SpaceResp space,String deployName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", space.getId());
		map.put("icon", space.getIcon());
		map.put("position", space.getPosition());
		map.put("name", space.getName());
		map.put("parentId", space.getParentId());
		map.put("type", space.getType());
		map.put("locationId", space.getLocationId());
		map.put("sort", space.getSort());
		map.put("userId", space.getCreateBy());
		if (space.getDeployId() != null) {
			map.put("deployId", space.getDeployId());
		}
		map.put("deployName", deployName);
		return map;
	}
}
