package com.iot.building.ifttt.calculatro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.building.helper.CenterControlDeviceStatus;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.utils.ValueUtils;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DeviceStateCalculator implements IStateCalculator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStateCalculator.class);

	private IBuildingSpaceService service;
	@Autowired
	private DeviceCoreApi devieApi;

	@SuppressWarnings("unchecked")
	@Override
	public boolean calcStatus(SensorVo sensor, Map<String, Object> callbackMap) throws BusinessException {
		service= ApplicationContextHelper.getBean(IBuildingSpaceService.class);
		boolean result = true;
		JSONObject[] jsonObjects = null;
		try {
			String properties = sensor.getProperties();
			JSONArray jsonArray = JSONObject.parseArray(properties);
			JSONObject[] objs = new JSONObject[jsonArray.size()];
			jsonObjects = jsonArray.toArray(objs);
		} catch (Exception e) {
		    LOGGER.error("ifttt callback returned, parse sensor's properties error.");
			return false;
		}
		if (jsonObjects == null) {
		    LOGGER.error("ifttt callback returned, sensor's properties is null.");
			return false;
		}
		
//		Map<String, Object> sensorStatesMap = (Map<String, Object>) CenterControlConstants.deviceStates.get(sensor.getDeviceId());
//		if (MapUtils.isEmpty(sensorStatesMap)) {
			Map<String, Object> deviceStatesMap = (Map<String, Object>) CenterControlDeviceStatus.getDeviceStatus(sensor.getDeviceId());
			if (MapUtils.isNotEmpty(deviceStatesMap)) {
				callbackMap.putAll(deviceStatesMap);
			}
//		} else {
//			callbackMap.putAll(sensorStatesMap);
//		}

		if (MapUtils.isEmpty(callbackMap)) {
		    LOGGER.error("ifttt callback returned, device cache properties is empty.");
			return false;
		}

		Boolean flag = true;
		int occupancyStatus=0;
		for (JSONObject jsonObject : jsonObjects) {
			Map<String, Object> sensorConfig = jsonObject;
			String propertyName = (String) sensorConfig.get("propertyName");//属性名，如：门
			Integer propertyType = (Integer) sensorConfig.get("propertyType");//整型，浮点型，字符串
			String triggerSign = (String) sensorConfig.get("triggerSign");//条件判断（大于等于的那些）
			//判断Occupancy 特殊需求
			if(propertyName.equals("Occupancy")){//Occupancy
				occupancyStatus++;
			}
			switch (propertyType) {
			case 0:// 整型
				Integer intValue = ValueUtils.getIntegerValue(callbackMap.get(propertyName));//设备属性上报后得到的触发条件
				Integer intTriggerValue = ValueUtils.getIntegerValue(sensorConfig.get("triggerValue"));//redis或库中设备的触发条件
				if (intValue == null || intTriggerValue == null) {
					flag = false;
				}
				if (!integerJudgment(triggerSign, intValue, intTriggerValue)) {
					flag = false;
				}
				break;
			case 1:// 浮点型
				Float floatValue = ValueUtils.getFloatValue(callbackMap.get(propertyName));
				Float floatTriggerValue = ValueUtils.getFloatValue(sensorConfig.get("triggerValue"));
				if (floatValue == null || floatTriggerValue == null) {
					return false;
				}
				if (!floatJudgment(triggerSign, floatValue, floatTriggerValue)) {
					flag = false;
				}
				break;
			case 2:// 字符串
				String strValue = ValueUtils.getStringValue(callbackMap.get(propertyName));
				String strTriggerValue = ValueUtils.getStringValue(sensorConfig.get("triggerValue"));
				if (StringUtils.isBlank(strValue) || StringUtils.isBlank(strTriggerValue)) {
					return false;
				}
				if (!stringJudgment(triggerSign, strValue, strTriggerValue)) {
					flag = false;
				}
				break;
			default:
				break;
			}
			if (!flag) {
				result = false;
				break;
			}
		}
		//Occupancy 触发需要判断房间的状态
		if (result && occupancyStatus>0){
			LOGGER.info("Occupancy========status============result="+result+"**********occupancyStatus="+occupancyStatus);
			//occupancyStatus>0 且 当房间状态为0时 满足occupancy触发
			int nowOccupancyStutus = (int)deviceStatesMap.get("Occupancy");
			return isSceneClose(sensor.getDeviceId(),nowOccupancyStutus)?true:false;
		}
		return result;
	}

	private Boolean isSceneClose(String deviceId,int nowOccupancyStutus){
		boolean flag = false;
		GetDeviceInfoRespVo device=devieApi.get(deviceId);
		LOGGER.info("=====获取房间的状态=====================");
		//获取房间的状态
		//先写死spaceStatus 为0 做测试
		//int spaceStatus = 0;
		//先通过deviceId 获取spaceId，再去查询房间的状态
		Long spaceId = service.findSpaceIdByDeviceId(deviceId,device.getOrgId(),device.getTenantId()).getSpaceId();
		int spaceStatus = service.getSpaceStatus(spaceId,device.getOrgId(),device.getTenantId());
		if(spaceStatus==0){//当房间状态为0时  返回true
			if(nowOccupancyStutus == 1){//当前Occupancy 状态为1
				flag = true;
				return flag;
			}else {
				return false;
			}
		}else if(spaceStatus==1){
			if(nowOccupancyStutus == 0){//当前Occupancy 状态为0
				flag = true;
				return flag;
			}else {
				return false;
			}
		}
		return flag;
		//return spaceStatus==0?true:false;//当房间状态为0时  返回true
	}
	
	private boolean integerJudgment(String triggerSign, Integer intValue, Integer intTriggerValue) {
		if (">".equals(triggerSign) || "0".equals(triggerSign)) {
			if (!(intValue > intTriggerValue)) {
				return false;
			}
		} else if (">=".equals(triggerSign) || "1".equals(triggerSign)) {
			if (!(intValue >= intTriggerValue)) {
				return false;
			}
		} else if ("<".equals(triggerSign) || "2".equals(triggerSign)) {
			if (!(intValue < intTriggerValue)) {
				return false;
			}
		} else if ("<=".equals(triggerSign) || "3".equals(triggerSign)) {
			if (!(intValue <= intTriggerValue)) {
				return false;
			}
		} else if ("=".equals(triggerSign) || "4".equals(triggerSign)) {
			if (!(intValue == intTriggerValue)) {
				return false;
			}
		} else if ("!=".equals(triggerSign) || "5".equals(triggerSign)) {
			if (!(intValue != intTriggerValue)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean floatJudgment(String triggerSign, Float floatValue, Float floatTriggerValue) {
		if (">".equals(triggerSign) || "0".equals(triggerSign)) {
			if (!(floatValue > floatTriggerValue)) {
				return false;
			}
		} else if (">=".equals(triggerSign) || "1".equals(triggerSign)) {
			if (!(floatValue >= floatTriggerValue)) {
				return false;
			}
		} else if ("<".equals(triggerSign) || "2".equals(triggerSign)) {
			if (!(floatValue < floatTriggerValue)) {
				return false;
			}
		} else if ("<=".equals(triggerSign) || "3".equals(triggerSign)) {
			if (!(floatValue <= floatTriggerValue)) {
				return false;
			}
		} else if ("=".equals(triggerSign) || "4".equals(triggerSign)) {
			if (!(floatValue == floatTriggerValue)) {
				return false;
			}
		} else if ("!=".equals(triggerSign) || "5".equals(triggerSign)) {
			if (!(floatValue != floatTriggerValue)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean stringJudgment(String triggerSign, String strValue, String strTriggerValue) {
		if ("=".equals(triggerSign) || "4".equals(triggerSign)) {
			if (!(strValue.equals(strTriggerValue))) {
				return false;
			}
		} else if ("!=".equals(triggerSign) || "5".equals(triggerSign)) {
			if (!(!strValue.equals(strTriggerValue))) {
				return false;
			}
		}
		return true;
	}

}
