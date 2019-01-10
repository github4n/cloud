package com.iot.device.api;

import com.iot.device.api.fallback.DeviceApiFallbackFactory;
import com.iot.device.vo.rsp.DeviceResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton: yun设备服务、中控设备服务
 * @Date: 11:12 2018/4/17
 * @Modify by:
 */
@Api(tags = "云端设备接口")
@FeignClient(value = "device-service", fallbackFactory = DeviceApiFallbackFactory.class)
@RequestMapping("/device")
public interface DeviceApi {


    @ApiOperation("查询产品Id")
    @RequestMapping(value = "/findUuidProductIdMap", method = RequestMethod.GET)
    Map<String,Long> findUuidProductIdMap(@RequestParam("uuIdList") List<String> uuIdList);

    @ApiOperation("查询租户Id")
    @RequestMapping(value = "/findUuidTenantIdMap", method = RequestMethod.GET)
    Map<String,Long> findUuidTenantIdMap(@RequestParam("uuIdList") List<String> uuIdList);

    @ApiOperation("查询设备版本")
    @RequestMapping(value = "/getVersionByDeviceIdList", method = RequestMethod.GET)
    List<DeviceResp> getVersionByDeviceIdList( @RequestParam("deviceIdList")List<String> deviceIdList);



}
