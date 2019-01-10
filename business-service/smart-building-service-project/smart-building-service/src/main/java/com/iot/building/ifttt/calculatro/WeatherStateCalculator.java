package com.iot.building.ifttt.calculatro;

import com.iot.building.ifttt.vo.SensorVo;
import com.iot.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class WeatherStateCalculator implements IStateCalculator {

	@Override
	public boolean calcStatus(SensorVo sensor, Map<String, Object> callbackMap) throws BusinessException {
		return true;
	}

}
