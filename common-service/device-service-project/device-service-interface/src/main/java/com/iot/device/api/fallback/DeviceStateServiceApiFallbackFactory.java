//package com.iot.device.api.fallback;
//
//import com.iot.device.api.DeviceStateServiceApi;
//import com.iot.device.vo.req.DeviceStateReq;
//import feign.hystrix.FallbackFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * @Author: xfz @Descrpiton: @Date: 14:18 2018/5/2 @Modify by:
// */
//@Component
//public class DeviceStateServiceApiFallbackFactory implements FallbackFactory<DeviceStateServiceApi> {
//    /** */
//    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStateServiceApiFallbackFactory.class);
//
//    @Override
//    public DeviceStateServiceApi create(Throwable cause) {
//        return new DeviceStateServiceApi() {
//            @Override
//            public void addDeviceStateListByDeviceId(DeviceStateReq deviceStateReq) {
//
//            }
//
//            @Override
//            public Map<String, Object> findDeviceStateListByDeviceId(String deviceId) {
//                return null;
//            }
//
//            @Override
//            public void recoveryDefaultState(String deviceId) {
//
//            }
//        };
//    }
//}
