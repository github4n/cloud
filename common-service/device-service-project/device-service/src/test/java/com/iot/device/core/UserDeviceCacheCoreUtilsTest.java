//package com.iot.device.core;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.iot.device.BaseTest;
//import com.iot.device.model.UserDevice;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 11:03 2018/6/22
// * @Modify by:
// */
//public class UserDeviceCacheCoreUtilsTest extends BaseTest {
//
//    @Test
//    public void removeCacheAll() {
//        UserDeviceCacheCoreUtils.removeCacheAll();
//    }
//
//    @Override
//    public String getBaseUrl() {
//        return null;
//    }
//
//    @Test
//    public void getCacheDeviceIdsByUserId() {
//
//        List<String> deviceIds = UserDeviceCacheCoreUtils.getCacheDeviceIdsByUserId(1001L);
//        System.out.println(deviceIds);
//    }
//
//    @Test
//    public void getCacheUserDeviceByUserId() {
//        UserDevice userDevice = UserDeviceCacheCoreUtils.getCacheUserDeviceByUserId(1001L, "1001");
//        System.out.println(userDevice);
//    }
//
//    @Test
//    public void getCacheUserDeviceListByUserId() {
//        List<UserDevice> userDeviceList = UserDeviceCacheCoreUtils.getCacheUserDeviceListByUserId(1001L);
//        System.out.println(userDeviceList);
//    }
//
//    @Test
//    public void removeCacheDeviceAllByUserId() {
//        UserDeviceCacheCoreUtils.removeCacheDeviceAllByUserId(1001L);
//    }
//
//    @Test
//    public void removeCacheDeviceByUserId() {
//        UserDeviceCacheCoreUtils.removeCacheDeviceByUserId(1001L, "1001");
//    }
//
//    @Test
//    public void removeCacheDevicesByUserId() {
//        List<String> deviceIds = Lists.newArrayList();
//        deviceIds.add("1001");
//        UserDeviceCacheCoreUtils.removeCacheDeviceByUserId(1001L, deviceIds);
//    }
//
//    @Test
//    public void addCacheDevicesByUserId() {
//        Map<String, UserDevice> userDeviceMap = Maps.newConcurrentMap();
//        UserDevice userDevice = new UserDevice();
//        userDevice.setUserId(1001L);
//        userDevice.setDeviceId("1001");
//        userDevice.setTenantId(0L);
//        userDevice.setOrgId(1001L);
//        userDeviceMap.put("1001", userDevice);
//
//        UserDeviceCacheCoreUtils.addCacheDevicesByUserId(1001L, userDeviceMap, false);
//    }
//
//    @Test
//    public void removeCacheUserIdsByDeviceId() {
//        List<Long> userIds = Lists.newArrayList();
//        userIds.add(100L);
//        UserDeviceCacheCoreUtils.removeCacheUserIdsByDeviceId(userIds, "1001");
//    }
//
//    @Test
//    public void getCacheUserIdByDeviceId() {
//        Long userId = UserDeviceCacheCoreUtils.getCacheUserIdByDeviceId("1001");
//        System.out.println(userId);
//    }
//
//    @Test
//    public void addCacheUserIdsByDeviceId() {
//        Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
//        UserDeviceCacheCoreUtils.addCacheDevicesByUserId(10000000001L, userDeviceMap);
////
//        boolean checkUser = UserDeviceCacheCoreUtils.checkExistKeyUserId(10000000001L);
//        System.out.println(checkUser);
////        List<UserDevice> userDeviceList = UserDeviceServiceCoreUtils.findUserDeviceListByDeviceId("000001");
////        boolean check = UserDeviceCacheCoreUtils.checkExistKeyDeviceId("000001");
////        List<Long> userIds = Lists.newArrayList();
////        userIds.add(100L);
////        UserDeviceCacheCoreUtils.addCacheUserIdsByDeviceId("000001", userIds);
//    }
//
//    @Test
//    public void removeCacheUserIdByDeviceId() {
//        UserDeviceCacheCoreUtils.removeCacheUserIdByDeviceId(1001L, "1001");
//    }
//
//    @Test
//    public void addCacheUserIdByDeviceId() {
//        UserDeviceCacheCoreUtils.addCacheUserIdByDeviceId(1001L, "1001", false);
//    }
//}