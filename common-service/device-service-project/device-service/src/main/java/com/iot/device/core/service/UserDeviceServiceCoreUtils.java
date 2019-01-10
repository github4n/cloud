//package com.iot.device.core.service;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.iot.common.helper.ApplicationContextHelper;
//import com.iot.device.core.UserDeviceCacheCoreUtils;
//import com.iot.device.model.UserDevice;
//import com.iot.device.service.IUserDeviceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 10:25 2018/6/21
// * @Modify by:
// */
//public class UserDeviceServiceCoreUtils {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeviceServiceCoreUtils.class);
//    /**
//     * 获取用户对应设备的 用户设备信息明细
//     *
//     * @param userId
//     * @param deviceId
//     * @return
//     * @author lucky
//     * @date 2018/6/21 10:28
//     */
//    public static UserDevice getUserDeviceByUserId(Long userId, String deviceId) {
//        UserDevice userDevice;
//        userDevice = UserDeviceCacheCoreUtils.getCacheUserDeviceByUserId(userId, deviceId);
//        if (userDevice == null) {
//            //@防止缓存穿透
////            if (UserDeviceCacheCoreUtils.checkExistKeyUserId(userId)) {
////                LOGGER.info("UserDeviceServiceCoreUtils-getUserDeviceByUserId-防止缓存穿透");
////                return null;
////            }
//            IUserDeviceService userDeviceService = ApplicationContextHelper.getBean(IUserDeviceService.class);
//            EntityWrapper<UserDevice> wrapper = new EntityWrapper<>();
////            wrapper.eq("device_id", deviceId);
//            wrapper.eq("user_id", userId);
//            List<UserDevice> userDeviceList = userDeviceService.selectList(wrapper);
//
//            Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
//            if (!CollectionUtils.isEmpty(userDeviceList)) {
//                for (UserDevice targetUserDevice : userDeviceList) {
//                    if (targetUserDevice.getDeviceId().equals(deviceId)) {
//                        userDevice = targetUserDevice;
//                        break;
//                    }
//                }
//                userDeviceList.forEach(tempUserDevice -> {
//                    userDeviceMap.put(tempUserDevice.getDeviceId(), tempUserDevice);
//                });
//            }
//            cacheAllUserDevice(userId, userDeviceList);
////            UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap, false);
//            // add cache deviceId --> userId
//            UserDeviceCacheCoreUtils.addCacheUserIdByDeviceId(userId, deviceId, true);
//        }
//        return userDevice;
//    }
//
//
//    /**
//     * 获取用户所有的用户设备信息列表
//     *
//     * @param userId
//     * @return
//     * @author lucky
//     * @date 2018/6/21 10:29
//     */
//    public static List<UserDevice> findUserDevicesByUserId(Long userId) {
//        List<UserDevice> userDeviceList;
//        userDeviceList = UserDeviceCacheCoreUtils.getCacheUserDeviceListByUserId(userId);
//        if (CollectionUtils.isEmpty(userDeviceList)) {
//            //@防止缓存穿透
////            if (UserDeviceCacheCoreUtils.checkExistKeyUserId(userId)) {
////                LOGGER.info("findUserDevicesByUserId-防止缓存穿透");
////                return null;
////            }
//            IUserDeviceService userDeviceService = ApplicationContextHelper.getBean(IUserDeviceService.class);
//            EntityWrapper<UserDevice> wrapper = new EntityWrapper<>();
//            wrapper.eq("user_id", userId);
//            userDeviceList = userDeviceService.selectList(wrapper);
//            //cache user device list
//            cacheAllUserDevice(userId, userDeviceList);
//        }
//        return userDeviceList;
//    }
//
//    public static List<String> findDeviceIdsByUserId(Long userId) {
//        List<UserDevice> userDeviceList = findUserDevicesByUserId(userId);
//        if (!CollectionUtils.isEmpty(userDeviceList)) {
//            List<String> deviceIds = Lists.newArrayList();
//            for (UserDevice userDevice : userDeviceList) {
//                deviceIds.add(userDevice.getDeviceId());
//            }
//            return deviceIds;
//        }
//        return null;
//    }
//
//    public static List<UserDevice> findUserDeviceListByDeviceId(String deviceId) {
//        List<UserDevice> userDeviceList = Lists.newArrayList();
//        Long cacheUserId = UserDeviceCacheCoreUtils.getCacheUserIdByDeviceId(deviceId);
//        if (cacheUserId == null || cacheUserId <= 0) {
//            //目前 设备跟用户的关系也只会有一条数据
////            //@防止缓存穿透
////            if (UserDeviceCacheCoreUtils.checkExistKeyDeviceId(deviceId)) {
////                LOGGER.info("findUserDeviceListByDeviceId-防止缓存穿透");
////                return null;
////            }
//            IUserDeviceService userDeviceService = ApplicationContextHelper.getBean(IUserDeviceService.class);
//            EntityWrapper<UserDevice> wrapper = new EntityWrapper<>();
//            wrapper.eq("device_id", deviceId);
//            userDeviceList = userDeviceService.selectList(wrapper);
//            //cache user device list
//
//            cacheUserDeviceList(deviceId, userDeviceList);
//        } else {
//            UserDevice userDevice = getUserDeviceByUserId(cacheUserId, deviceId);
//            if (userDevice != null) {
//                userDeviceList.add(userDevice);
//            }
//        }
//        return userDeviceList;
//    }
//
//    public static List<Long> findUserIdsByDeviceIds(List<String> deviceIds) {
//        if (CollectionUtils.isEmpty(deviceIds)) {
//            return null;
//        }
//        List<Long> userIds = Lists.newArrayList();
//        Map<String, String> deviceIdMap = Maps.newHashMap();
//        for (String deviceId : deviceIds) {
//            deviceIdMap.put(deviceId, deviceId);
//        }
//        for (String deviceId : deviceIds) {
//            List<UserDevice> userDeviceList = findUserDeviceListByDeviceId(deviceId);
//            if (!CollectionUtils.isEmpty(userDeviceList)) {
//                for (UserDevice userDevice : userDeviceList) {
//                    Long userId = userDevice.getUserId();
//                    userIds.add(userId);
//                }
//            }
//        }
//        return userIds;
//    }
//
//    public static void cacheAllUserDevice(Long userId, List<UserDevice> userDeviceList) {
//        if (userId == null) {
//            return;
//        }
//        Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
//
//        if (CollectionUtils.isEmpty(userDeviceList)) {
//            //存空 防止穿透
//            UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap);
//            return;
//        }
//
//        for (UserDevice userDevice : userDeviceList) {
//            String deviceId = userDevice.getDeviceId();
//            userDeviceMap.put(deviceId, userDevice);
//        }
//        cacheAllUserDeviceMap(userDeviceMap, userId);
//
//    }
//
//    private static void cacheAllUserDeviceMap(Map<String, UserDevice> userDeviceMap, Long userId) {
//
//        if (userDeviceMap == null) {
//            userDeviceMap = Maps.newHashMap();
//        }
//        //add cache userId -->deviceId/userDevice
//        UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap);
//        //add cache deviceId --> userId
//        for (String deviceId : userDeviceMap.keySet()) {
//            UserDeviceCacheCoreUtils.addCacheUserIdByDeviceId(userId, deviceId);
//        }
//
//    }
//
//    public static void cacheUserDeviceList(String deviceId, List<UserDevice> userDeviceList) {
//        if (StringUtils.isEmpty(deviceId)) {
//            return;
//        }
//        List<Long> userIds = Lists.newArrayList();
//        if (CollectionUtils.isEmpty(userDeviceList)) {
//            //防止缓存穿透--->设备对应的用户.
//            //UserDeviceCacheCoreUtils.addCacheUserIdsByDeviceId(deviceId, userIds);
//            return;
//        }
//        userDeviceList.forEach(userDevice -> {
//            if (!userIds.contains(userDevice.getUserId())) {
//                userIds.add(userDevice.getUserId());
//            }
//        });
//        UserDeviceCacheCoreUtils.addCacheUserIdsByDeviceId(deviceId, userIds);
//    }
//
//    public static void cacheUserDeviceList(List<UserDevice> userDeviceList) {
//        if (CollectionUtils.isEmpty(userDeviceList)) {
//            return;
//        }
//        for (UserDevice userDevice : userDeviceList) {
//            cacheUserDevice(userDevice);
//        }
//    }
//    public static void cacheUserDevice(UserDevice targetUserDevice) {
//        if (targetUserDevice == null) {
//            return;
//        }
//        String deviceId = targetUserDevice.getDeviceId();
//        Long userId = targetUserDevice.getUserId();
//        Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
//        userDeviceMap.put(deviceId, targetUserDevice);
//        UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap, false);
//        UserDeviceCacheCoreUtils.addCacheUserIdByDeviceId(userId, deviceId, true);
//    }
//
//    public static void removeCacheUserDevice(Long userId, String deviceId) {
//
//        UserDeviceCacheCoreUtils.removeCacheUserIdByDeviceId(userId, deviceId);
//        UserDeviceCacheCoreUtils.removeCacheDeviceByUserId(userId, deviceId);
//    }
//
//    public static void removeAllCacheUserDeviceByUserId(Long userId) {
//        UserDeviceCacheCoreUtils.removeCacheDeviceAllByUserId(userId);
//    }
//
//    public static void removeAllCacheUserDeviceByDeviceId(String deviceId) {
//        UserDeviceCacheCoreUtils.removeCacheUserDeviceByDeviceId(deviceId);
//    }
//
//    private static void cacheUserDeviceList(List<UserDevice> userDeviceList, Long userId) {
//        Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
//        if (CollectionUtils.isEmpty(userDeviceList)) {
//            //防止缓存穿透
//            UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap);
//            return;
//        }
//        if (!CollectionUtils.isEmpty(userDeviceList)) {
//
//            for (UserDevice userDevice : userDeviceList) {
//                String deviceId = userDevice.getDeviceId();
//                userDeviceMap.put(deviceId, userDevice);
//                UserDeviceCacheCoreUtils.addCacheUserIdByDeviceId(userId, deviceId, true);
//            }
//            UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap, false);
//        }
//    }
//
//    private static void cacheAllUserDeviceList(List<UserDevice> userDeviceList, Long userId) {
//        if (userDeviceList == null) {
//            userDeviceList = Lists.newArrayList();
//        }
//        Map<String, UserDevice> userDeviceMap = Maps.newConcurrentMap();
//        //add cache
//        for (UserDevice userDevice : userDeviceList) {
//            String deviceId = userDevice.getDeviceId();
//            userDeviceMap.put(deviceId, userDevice);
//            //add cache deviceId --> userId
//            UserDeviceCacheCoreUtils.addCacheUserIdByDeviceId(userId, deviceId, true);
//        }
//        //add cache userId -->deviceId/userDevice
//        UserDeviceCacheCoreUtils.addCacheDevicesByUserId(userId, userDeviceMap, false);
//    }
//
//}
