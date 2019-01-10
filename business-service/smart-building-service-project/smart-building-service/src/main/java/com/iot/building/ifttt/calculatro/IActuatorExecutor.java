package com.iot.building.ifttt.calculatro;

import java.util.Map;

public interface IActuatorExecutor {
	void execute(Map<String, Object> params);
}
