//package com.iot.device.controller;
//
//import com.iot.device.comm.cache.CacheKeyUtils;
//import com.iot.device.comm.handler.CacheInitHandlerRunner;
//import com.iot.device.core.service.DeviceServiceCoreUtils;
//import com.iot.device.model.Device;
//import com.iot.redis.RedisCacheUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 17:35 2018/6/22
// * @Modify by:
// */
//@RestController
//@RequestMapping("/cache/init")
//public class CacheController {
//
//    @Autowired
//    private CacheInitHandlerRunner cacheInitHandlerRunner;
//
//
//    @RequestMapping(value = "/initCache", method = RequestMethod.GET)
//    public String initCache() {
//        RedisCacheUtil.delete(CacheKeyUtils.INIT_CACHE_KEY);
//        cacheInitHandlerRunner.initCache();
//        return "SUCCESS";
//    }
//
//    @RequestMapping(value = "/removeCache", method = RequestMethod.GET)
//    public String removeCache() {
//        cacheInitHandlerRunner.removeCacheAll();
//        return "SUCCESS";
//    }
//
//
//    /**
//     * remove cache by deviceIds
//     *
//     * @param deviceIds
//     * @return
//     * @author lucky
//     * @date 2018/7/25 13:45
//     */
//    @RequestMapping(value = "/removeCacheByDeviceIds", method = RequestMethod.POST)
//    public String removeCacheByDeviceIds(@RequestBody List<String> deviceIds) {
//        if (!CollectionUtils.isEmpty(deviceIds)) {
//            List<Device> deviceList = DeviceServiceCoreUtils.findDeviceListByDeviceIds(deviceIds);
//            if (!CollectionUtils.isEmpty(deviceList)) {
//                deviceList.forEach(device -> {
//                    String deviceId = device.getUuid();
//                    String parentDeviceId = device.getParentId();
//                    DeviceServiceCoreUtils.removeDeviceByDeviceId(deviceId);
//                    DeviceServiceCoreUtils.removeCacheChildDeviceByParentDeviceId(parentDeviceId, deviceId);
//                });
//            }
//
//        }
//        return "SUCCESS";
//    }
//}
