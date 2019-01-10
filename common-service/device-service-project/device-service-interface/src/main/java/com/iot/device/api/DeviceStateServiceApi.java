//package com.iot.device.api;
//
//import com.iot.device.api.fallback.DeviceStateServiceApiFallbackFactory;
//import com.iot.device.vo.req.DeviceStateReq;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.cloud.netflix.feign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Map;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 14:17 2018/5/2
// * @Modify by:
// */
//@Api(tags = "设备属性接口")
//@FeignClient(value = "device-service", fallbackFactory = DeviceStateServiceApiFallbackFactory.class)
//@RequestMapping(value = "/deviceState")
//public interface DeviceStateServiceApi {
//
//    @ApiOperation("上传设备属性值")
//    @RequestMapping(value = "/addDeviceStateListByDeviceId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    void addDeviceStateListByDeviceId(@RequestBody DeviceStateReq deviceStateReq);
//
//    @ApiOperation("获取设备属性值")
//    @RequestMapping(value = "/findDeviceStateListByDeviceId", method = RequestMethod.GET)
//    Map<String, Object> findDeviceStateListByDeviceId(@RequestParam("deviceId") String deviceId);
//
//    @ApiOperation("恢复设备默认属性值")
//    @RequestMapping(value = "/recoveryDefaultState", method = RequestMethod.GET)
//    void recoveryDefaultState(@RequestParam("deviceId") String deviceId);
//
//}
