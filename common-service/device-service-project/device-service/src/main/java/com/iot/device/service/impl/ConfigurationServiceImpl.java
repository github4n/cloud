package com.iot.device.service.impl;

import com.iot.device.model.Configuration;
import com.iot.device.mapper.ConfigurationMapper;
import com.iot.device.service.IConfigurationService;
import com.iot.device.vo.rsp.ConfigurationRsp;
import com.netflix.config.ConfigurationManager;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-10
 */
@Service
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, Configuration> implements IConfigurationService {

	@Autowired
	private ConfigurationMapper configurationMapper;
	
	@Override
	public List<ConfigurationRsp> selectConfigByTenantId(Long tenantId) {
		
		return configurationMapper.selectConfigByTenantId(tenantId);
	}

}
