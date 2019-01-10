package com.iot.building.ifttt.calculatro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.utils.ValueUtils;
import com.iot.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PeopleStateCalculator implements IStateCalculator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PeopleStateCalculator.class);

	@Override
	public boolean calcStatus(SensorVo sensor, Map<String, Object> callbackMap) throws BusinessException {
		//return true;
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

		Boolean flag = true;
		for (JSONObject jsonObject : jsonObjects) {
			Map<String, Object> sensorConfig = jsonObject;
			String propertyName = (String) sensorConfig.get("propertyName");
			Integer propertyType = (Integer) sensorConfig.get("propertyType");
			String triggerSign = (String) sensorConfig.get("triggerSign");
			switch (propertyType) {
			case 0:// 整型
				Integer intValue = ValueUtils.getIntegerValue(callbackMap.get("usedAmount"));
				Integer intTriggerValue = ValueUtils.getIntegerValue(sensorConfig.get("triggerValue"));
				if (intValue == null || intTriggerValue == null) {
					return false;
				}
				if (!integerJudgment(triggerSign, intValue, intTriggerValue)) {
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
		
		return result;
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

}
