package com.iot.building.allocation.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("空间接口")
@FeignClient(value = "building-control-service")
@RequestMapping("/environment")
public interface EnvironmentPropertyApi {

	@ApiOperation("获取配置文件的属性")
    @RequestMapping(value = "/getPropertyValue", method = RequestMethod.GET)
    String getPropertyValue(@RequestParam("propertyName") String propertyName);
}
