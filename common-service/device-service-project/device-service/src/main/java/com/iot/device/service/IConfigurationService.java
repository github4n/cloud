package com.iot.device.service;

import com.iot.device.model.Configuration;
import com.iot.device.vo.rsp.ConfigurationRsp;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CHQ
 * @since 2018-05-10
 */
public interface IConfigurationService extends IService<Configuration> {

	List<ConfigurationRsp> selectConfigByTenantId(Long tenantId);

}
