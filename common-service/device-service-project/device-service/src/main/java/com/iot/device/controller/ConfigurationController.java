package com.iot.device.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.device.api.ConfigurationApi;
import com.iot.device.service.IConfigurationService;
import com.iot.device.service.impl.ConfigurationServiceImpl;
import com.iot.device.vo.rsp.ConfigurationRsp;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author CHQ
 * @since 2018-05-10
 */
@RestController
public class ConfigurationController implements ConfigurationApi{

	@Autowired
	private IConfigurationService configurationService;
	
	@Override
	public List<ConfigurationRsp> selectConfigByTenantId(@RequestParam("tenantId") Long tenantId) {
	
		return configurationService.selectConfigByTenantId(tenantId);
	}

}

