//package com.iot.device.api;
//
//import com.iot.device.api.fallback.DeviceStatusApiFallbackFactory;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * @Author: xfz
// * @Descrpiton: 设备上下线、是否激活
// * @Date: 17:23 2018/9/18
// * @Modify by:
// */
//@Api(tags = "设备状态接口")
//@FeignClient(value = "device-service", fallbackFactory = DeviceStatusApiFallbackFactory.class)
//@RequestMapping("/deviceStatus")
//public interface DeviceStatusApi {
//
//    @ApiOperation(value = "更新设备激活状态")
//    @RequestMapping(value = "/updateDeviceActiveStatus", method = RequestMethod.GET)
//    void updateDeviceActiveStatus(@RequestParam("deviceId") String deviceId
//            , @RequestParam("activeStatus") Integer activeStatus);
//
//    @ApiOperation(value = "更新设备上下线状态")
//    @RequestMapping(value = "/updateDeviceActiveStatus", method = RequestMethod.GET)
//    void updateDeviceOnlineStatus(@RequestParam("deviceId") String deviceId
//            , @RequestParam("onlineStatus") String onlineStatus);
//
//}
