package com.iot.building.common.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface EnvironmentPropertyService {

	String getPropertyValue(@RequestParam("propertyName") String propertyName);
	
}
