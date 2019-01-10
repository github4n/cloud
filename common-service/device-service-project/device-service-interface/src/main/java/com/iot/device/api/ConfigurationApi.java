package com.iot.device.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.device.api.fallback.ConfigurationApiFallbackFactory;
import com.iot.device.vo.rsp.ConfigurationRsp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "设备配置接口")
@FeignClient(value = "device-service", fallbackFactory = ConfigurationApiFallbackFactory.class)
@RequestMapping("/Configuration")
public interface ConfigurationApi {
	
	@ApiOperation("根据租户查找配置")
    @RequestMapping(value = "/selectConfigByTenantId", method = RequestMethod.GET)
	public List<ConfigurationRsp> selectConfigByTenantId(@RequestParam("tenantId") Long tenantId);
	
}
