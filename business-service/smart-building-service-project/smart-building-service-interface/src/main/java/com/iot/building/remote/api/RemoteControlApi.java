package com.iot.building.remote.api;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.remote.api.fallback.RemoteControlApiFallbackFactory;

import io.swagger.annotations.Api;

@Api("OTA接口")
@FeignClient(value = "building-control-service", fallbackFactory = RemoteControlApiFallbackFactory.class)
@RequestMapping("/remote")
public interface RemoteControlApi {

	@RequestMapping(value = "/synchronousRemoteControl", method = RequestMethod.POST)
    void synchronousRemoteControl(@RequestParam("tenantId") Long tenantId, @RequestParam("spaceId") Long spaceId);
}
