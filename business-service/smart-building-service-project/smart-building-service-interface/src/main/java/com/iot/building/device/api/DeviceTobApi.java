package com.iot.building.device.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Api(tags = "遥控器相关接口")
@FeignClient(value = "building-control-service")
@RequestMapping(value = "/DeviceTobApi")
public interface DeviceTobApi {

    @ApiOperation("删除子设备和删除对应的情景,空间,ifttt")
    @RequestMapping(value = "/deleteDeviceRelation", method = RequestMethod.GET)
    boolean deleteDeviceRelation(@RequestParam("orgId")Long orgId, @RequestParam("deviceId")String deviceId, @RequestParam("tenantId")Long tenantId,@RequestParam("check")boolean check,@RequestParam("clientId")String clientId);

    @ApiOperation("重启网关(网关重置)")
    @RequestMapping(value = "/resetReq", method = RequestMethod.GET)
    void resetReq(@RequestParam("clientId")String clientId);
}
