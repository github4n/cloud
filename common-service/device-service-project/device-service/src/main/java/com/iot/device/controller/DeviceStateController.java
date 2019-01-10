//package com.iot.device.controller;
//
//import com.iot.device.api.DeviceStateServiceApi;
//import com.iot.device.service.IDeviceStateMongoService;
//import com.iot.device.vo.req.DeviceStateReq;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 9:21 2018/5/2
// * @Modify by:
// */
//@RestController
//public class DeviceStateController implements DeviceStateServiceApi {
//
//    @Autowired
//    private IDeviceStateMongoService deviceStateMongoService;
//
//    @Override
//    public void addDeviceStateListByDeviceId(@RequestBody DeviceStateReq deviceStateReq) {
//        deviceStateMongoService.addDeviceStatusListByDeviceId(deviceStateReq.getDeviceId(), deviceStateReq.getDeviceStateInfoReqList(), deviceStateReq.isRemoveCache());
//    }
//
//    @Override
//    public Map<String, Object> findDeviceStateListByDeviceId(@RequestParam("deviceId") String deviceId) {
//        return deviceStateMongoService.findDeviceStateListByDeviceId(deviceId);
//    }
//
//    @Override
//    public void recoveryDefaultState(@RequestParam("deviceId") String deviceId) {
//        deviceStateMongoService.recoveryDefaultState(deviceId);
//    }
//}
