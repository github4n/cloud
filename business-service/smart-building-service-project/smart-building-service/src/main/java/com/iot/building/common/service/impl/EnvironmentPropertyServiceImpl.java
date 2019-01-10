package com.iot.building.common.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.building.common.service.EnvironmentPropertyService;

@Service
@Transactional
public class EnvironmentPropertyServiceImpl implements EnvironmentPropertyService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(EnvironmentPropertyServiceImpl.class);

	@Autowired
	private Environment environment;
	
	@Override
	public String getPropertyValue(String propertyName) {
		return environment.getProperty(propertyName);
	}

}
