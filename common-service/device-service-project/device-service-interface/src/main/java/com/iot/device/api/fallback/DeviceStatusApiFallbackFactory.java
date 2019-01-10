//package com.iot.device.api.fallback;
//
//import com.iot.device.api.DeviceStatusApi;
//import feign.hystrix.FallbackFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DeviceStatusApiFallbackFactory implements FallbackFactory<DeviceStatusApi> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStatusApiFallbackFactory.class);
//
//    @Override
//    public DeviceStatusApi create(Throwable cause) {
//        return new DeviceStatusApi() {
//            @Override
//            public void updateDeviceActiveStatus(String deviceId, Integer activeStatus) {
//
//            }
//
//            @Override
//            public void updateDeviceOnlineStatus(String deviceId, String onlineStatus) {
//
//            }
//        };
//    }
//}
