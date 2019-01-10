package com.iot.user.api;

import com.iot.user.api.fallback.OnlineDebugApiFallbackFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api("线上Debug接口")
@FeignClient(value = "user-service", fallbackFactory = OnlineDebugApiFallbackFactory.class)
@RequestMapping("/debug")
public interface OnlineDebugApi {

    @ApiOperation("验证用户线上Debug权限")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    Boolean checkOnlineDebug(@RequestParam("uuid") String uuid);


    @ApiOperation("修改用户线上Debug权限")
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    String updateOnlineDebug(@RequestParam("userName") String userName,@RequestParam("state") int state,@RequestParam("tenantId") int tenantId);


}
