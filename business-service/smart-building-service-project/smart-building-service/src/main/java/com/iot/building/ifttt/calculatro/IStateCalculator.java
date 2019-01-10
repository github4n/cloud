package com.iot.building.ifttt.calculatro;

import com.iot.building.ifttt.vo.SensorVo;
import com.iot.common.exception.BusinessException;
import java.util.Map;

public interface IStateCalculator {

	boolean calcStatus(SensorVo sensor, Map<String, Object> callbackMap) throws BusinessException;
}
