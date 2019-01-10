package com.iot.building.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.allocation.api.EnvironmentPropertyApi;
import com.iot.building.common.service.EnvironmentPropertyService;

@RestController
public class EnvironmentPropertController implements EnvironmentPropertyApi{
	
	@Autowired
	private EnvironmentPropertyService environment;

	@Override
	public String getPropertyValue(@RequestParam("propertyName") String propertyName) {
		return environment.getPropertyValue(propertyName);
	}

}
