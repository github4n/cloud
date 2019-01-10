package com.iot.building.ifttt.calculatro;

import com.iot.building.ifttt.vo.SensorVo;
import com.iot.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class TimingTriggerStateCalculator implements IStateCalculator {

	@Override
	public boolean calcStatus(SensorVo sensor, Map<String, Object> sensorProperties) throws BusinessException {
		// 定时触发条件判断，到时间点才进来，所以可以直接返回true
		return true;
	}

}
