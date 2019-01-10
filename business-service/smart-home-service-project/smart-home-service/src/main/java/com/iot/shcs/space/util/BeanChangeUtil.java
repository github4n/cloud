package com.iot.shcs.space.util;

import com.iot.common.util.StringUtil;
import com.iot.device.vo.rsp.DeviceResp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：bean转换工具类 创建人： chq 创建时间： 2018/6/21 16:53
 */
public class BeanChangeUtil {

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
		map.put("lastUpdateTime", device.getLastUpdateDate() != null ? device.getLastUpdateDate().getTime() : device.getCreateTime() != null ? device.getCreateTime().getTime() : new Date().getTime());
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
}
