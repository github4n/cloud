//package com.iot.device.core;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.iot.device.comm.cache.CacheKeyUtils;
//import com.iot.device.comm.cache.VersionEnum;
//import com.iot.device.model.UserDevice;
//import com.iot.redis.RedisCacheUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 11:48 2018/6/20
// * @Modify by:
// */
//public class UserDeviceCacheCoreUtils {
//
//    public static final Logger LOGGER = LoggerFactory.getLogger(UserDeviceCacheCoreUtils.class);
//
//
//    /***************************************通过用户找设备的关系begin*******************************************************************/
//
//    /**
//     * 通过用户id 获取设备ids
//     *
//     * @param userId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 11:52
//     */
//    public static List<String> getCacheDeviceIdsByUserId(Long userId) {
//        if (userId == null || userId <= 0) {
//            return null;
//        }
//        Map<String, UserDevice> returnDataMap = getCacheUserDevicesByUserId(userId);
//        if (!CollectionUtils.isEmpty(returnDataMap)) {
//            // 存放的是 List<String>   ---json  deviceIds
//            Set<String> keys = returnDataMap.keySet();
//            List<String> deviceIds = Lists.newArrayList();
//            deviceIds.addAll(keys);
//            return deviceIds;
//        }
//        return null;
//    }
//
//    public static UserDevice getCacheUserDeviceByUserId(Long userId, String deviceId) {
//        if (userId == null || userId <= 0) {
//            return null;
//        }
//        if (StringUtils.isEmpty(deviceId)) {
//            return null;
//        }
//        try {
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//            return RedisCacheUtil.hashGet(redisKey, deviceId, UserDevice.class);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-getCacheUserDeviceByUserId-error", e);
//        }
//        return null;
//    }
//
//    /**
//     * 通过用户id 获取user_device 集合
//     *
//     * @param userId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 11:52
//     */
//    public static List<UserDevice> getCacheUserDeviceListByUserId(Long userId) {
//        if (userId == null || userId <= 0) {
//            return null;
//        }
//        Map<String, UserDevice> returnDataMap = getCacheUserDevicesByUserId(userId);
//        if (!CollectionUtils.isEmpty(returnDataMap)) {
//            List<UserDevice> userDeviceList = Lists.newArrayList();
//            userDeviceList.addAll(returnDataMap.values());
//            return userDeviceList;
//        }
//        return null;
//    }
//
//
//    /**
//     * 获取用户对应的 user_device 信息列表
//     *
//     * @param userId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 19:46
//     */
//    private static Map<String, UserDevice> getCacheUserDevicesByUserId(Long userId) {
//        try {
//            if (userId == null || userId <= 0) {
//                return null;
//            }
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//            Map<String, UserDevice> returnDataMap = RedisCacheUtil.hashGetAll(redisKey, UserDevice.class, true);
//            return returnDataMap;
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-getCacheUserDevicesByUserId-error", e);
//        }
//        return null;
//    }
//
//    /**
//     * 删除用户所有的设备
//     *
//     * @param userId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 11:52
//     */
//    public static void removeCacheDeviceAllByUserId(Long userId) {
//        try {
//            if (userId == null || userId <= 0) {
//                return;
//            }
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//            RedisCacheUtil.delete(redisKey);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-removeCacheDeviceAllByUserId-error", e);
//        }
//    }
//
//    public static void removeCacheDeviceByUserId(Long userId, String deviceId) {
//
//        if (userId == null || userId <= 0) {
//            return;
//        }
//        if (StringUtils.isEmpty(deviceId)) {
//            return;
//        }
//        List<String> deviceIds = Lists.newArrayList();
//        deviceIds.add(deviceId);
//        removeCacheDeviceByUserId(userId, deviceIds);
//    }
//    /**
//     * 通过用户id 删除具体 设备ids
//     *
//     * @param userId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 11:52
//     */
//    public static void removeCacheDeviceByUserId(Long userId, List<String> deviceIds) {
//        try {
//            if (userId == null || userId <= 0) {
//                return;
//            }
//            if (CollectionUtils.isEmpty(deviceIds)) {
//                return;
//            }
//            List<String> cacheUserDeviceIds = getCacheDeviceIdsByUserId(userId);
//            if (!CollectionUtils.isEmpty(cacheUserDeviceIds)) {
//                Long tenantId = null;
//                Long orgId = null;
//                String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//
//                for (String deviceId : deviceIds) {
//                    if (cacheUserDeviceIds.contains(deviceId)) {
//                        //删除设备
//                        RedisCacheUtil.hashRemove(redisKey, deviceId);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-removeCacheDeviceByUserId-error", e);
//        }
//    }
//
//    public static void addCacheDevicesByUserId(Long userId, Map<String, UserDevice> userDeviceMap) {
//        try {
//            if (userId == null || userId <= 0) {
//                return;
//            }
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//            RedisCacheUtil.hashPutAll(redisKey, userDeviceMap, true);
//
//            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-addCacheDevicesByUserId-error", e);
//        }
//    }
//
//    /**
//     * 通过用户id 缓存 设备ids
//     *
//     * @param userId
//     * @param userDeviceMap 设备id 对应的详细
//     * @param removeOldAll  是否往旧的里面添加
//     * @return
//     * @author lucky
//     * @date 2018/6/20 11:52
//     */
//    public static void addCacheDevicesByUserId(Long userId, final Map<String, UserDevice> userDeviceMap, boolean removeOldAll) {
//        try {
//            if (userId == null || userId <= 0) {
//                return;
//            }
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//
//            if (removeOldAll) {
//                RedisCacheUtil.delete(redisKey);
//                RedisCacheUtil.hashPutAll(redisKey, userDeviceMap, true);
//            } else {
//                //put all
//                RedisCacheUtil.hashPutAll(redisKey, userDeviceMap, true);
//            }
//            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-addCacheDevicesByUserId-error", e);
//        }
//    }
//
//    public static void addCacheDevicesByUserId(Long userId, UserDevice userDevice, boolean removeOldAll) {
//        try {
//            if (userId == null || userId <= 0) {
//                return;
//            }
//            if (userDevice == null) {
//                return;
//            }
//            if (StringUtils.isEmpty(userDevice.getDeviceId())) {
//                return;
//            }
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//
//            String deviceId = userDevice.getDeviceId();
//            if (removeOldAll) {
//                RedisCacheUtil.delete(redisKey);
//                Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
//                userDeviceMap.put(deviceId, userDevice);
//                RedisCacheUtil.hashPutAll(redisKey, userDeviceMap, true);
//            } else {
//                Map<String, UserDevice> userDeviceMap = RedisCacheUtil.hashGetAll(redisKey, UserDevice.class, true);
//                if (!CollectionUtils.isEmpty(userDeviceMap)) {
//                    userDeviceMap.put(deviceId, userDevice);
//                } else {
//                    userDeviceMap = Maps.newHashMap();
//                    userDeviceMap.put(deviceId, userDevice);
//                }
//                RedisCacheUtil.hashPutAll(redisKey, userDeviceMap, true);
//            }
//            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-addCacheDevicesByUserId-error", e);
//        }
//    }
//
//    /***************************************通过设备找用户的关系 begin**********************************************************/
//
//    /**
//     * 根据设备id 获取用户跟设备的关系 对应的用户id
//     *
//     * @param deviceId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 19:46
//     */
//    private static List<Long> getCacheUsersByDeviceId(String deviceId) {
//        try {
//            if (StringUtils.isEmpty(deviceId)) {
//                return null;
//            }
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            List<Long> userIds = RedisCacheUtil.listGetAll(redisKey, Long.class);
//            return userIds;
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-getCacheUsersByDeviceId-error", e);
//        }
//        return null;
//    }
//
//    /**
//     * 删除设备与用户的关系
//     *
//     * @param userIds  用户id
//     * @param deviceId key
//     * @return
//     * @author lucky
//     * @date 2018/6/21 11:14
//     */
//    public static void removeCacheUserIdsByDeviceId(List<Long> userIds, String deviceId) {
//        try {
//            if (CollectionUtils.isEmpty(userIds)) {
//                return;
//            }
//            if (StringUtils.isEmpty(deviceId)) {
//                return;
//            }
//            for (Long userId : userIds) {
//                String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//                RedisCacheUtil.removeListValue(redisKey, 1L, userId);
//            }
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-removeCacheDeviceByUserId-error", e);
//        }
//    }
//
//    public static void removeCacheUserDeviceByDeviceId(String deviceId) {
//        try {
//
//            if (StringUtils.isEmpty(deviceId)) {
//                return;
//            }
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            RedisCacheUtil.delete(redisKey);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-removeCacheUserDeviceByDeviceId-error", e);
//        }
//    }
//
//    /**
//     * 根据设备id 获取用户跟设备的关系 对应的用户id
//     *
//     * @param deviceId
//     * @return
//     * @author lucky
//     * @date 2018/6/20 19:46
//     */
//    public static Long getCacheUserIdByDeviceId(String deviceId) {
//        try {
//            if (StringUtils.isEmpty(deviceId)) {
//                return null;
//            }
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            List<Long> userIds = RedisCacheUtil.listGetAll(redisKey, Long.class);
//            if (!CollectionUtils.isEmpty(userIds)) {
//                return userIds.get(0);
//            }
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-getCacheUsersByDeviceId-error", e);
//        }
//        return null;
//    }
//
//
//    /**
//     * 根据设备 删除 用户跟设备的关系
//     *
//     * @param userId
//     * @param deviceId
//     * @return
//     * @author lucky
//     * @date 2018/6/21 11:15
//     */
//    public static void removeCacheUserIdByDeviceId(Long userId, String deviceId) {
//        if (StringUtils.isEmpty(deviceId)) {
//            return;
//        }
//        if (userId == null || userId <= 0) {
//            return;
//        }
//        List<Long> userIds = Lists.newArrayList();
//        userIds.add(userId);
//        removeCacheUserIdsByDeviceId(userIds, deviceId);
//    }
//
//    public static void addCacheUserIdByDeviceId(Long userId, String deviceId) {
//        try {
//            if (StringUtils.isEmpty(deviceId)) {
//                return;
//            }
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            RedisCacheUtil.delete(redisKey);
//            RedisCacheUtil.listLeftPush(redisKey, userId);
//            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-addCacheDevicesByUserId-error", e);
//        }
//    }
//    /**
//     * 添加 设备id 对应的用户id
//     *
//     * @param userId
//     * @param deviceId
//     * @param removeOldAll
//     * @return
//     * @author lucky
//     * @date 2018/6/21 11:21
//     */
//    public static void addCacheUserIdByDeviceId(Long userId, String deviceId, boolean removeOldAll) {
//        try {
//            if (StringUtils.isEmpty(deviceId)) {
//                return;
//            }
//            if (userId == null || userId <= 0) {
//                return;
//            }
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            if (removeOldAll) {
//                RedisCacheUtil.delete(redisKey);
//                RedisCacheUtil.listLeftPush(redisKey, userId);
//            } else {
//                List<Long> userIds = getCacheUsersByDeviceId(deviceId);
//                if (!CollectionUtils.isEmpty(userIds)) {
//                    if (userIds.contains(userId)) {
//                        //判断是否已经重复value 存在忽略
//                        return;
//                    }
//                }
//                RedisCacheUtil.listLeftPush(redisKey, userId);
//            }
//            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-addCacheDevicesByUserId-error", e);
//        }
//    }
//
//    public static boolean checkExistKeyUserId(Long userId) {
//        try {
//            if (userId == null || userId <= 0) {
//                return false;
//            }
//            Long tenantId = null;
//            Long orgId = null;
//            String redisKey = CacheKeyUtils.getUserDeviceKey(tenantId, VersionEnum.V1, orgId, userId);
//            LOGGER.info("UserDeviceCacheCoreUtils-checkExistKeyUserId:{}", redisKey);
//            return RedisCacheUtil.hasKey(redisKey);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-checkExistKeyUserId-error", e);
//        }
//        return false;
//    }
//
//    public static boolean checkExistKeyDeviceId(String deviceId) {
//        try {
//            if (StringUtils.isEmpty(deviceId)) {
//                return false;
//            }
//
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            LOGGER.info("UserDeviceCacheCoreUtils-checkExistKeyDeviceId:{}", redisKey);
//            return RedisCacheUtil.hasKey(redisKey);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-checkExistKeyDeviceId-error", e);
//        }
//        return false;
//    }
//    public static void removeCacheAll() {
//        try {
//            String redisKey = "user_device:" + "*";
//            Set<String> keys = RedisCacheUtil.keys(redisKey);
//            if (!CollectionUtils.isEmpty(keys)) {
//                RedisCacheUtil.delete(keys);
//            }
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-removeCacheAll-error", e);
//        }
//    }
//
//    public static void addCacheUserIdsByDeviceId(String deviceId, List<Long> userIds) {
//        try {
//            if (StringUtils.isEmpty(deviceId)) {
//                return;
//            }
//            if (CollectionUtils.isEmpty(userIds)) {
//                return;
//            }
//            String redisKey = CacheKeyUtils.getUserDeviceKey(VersionEnum.V1, deviceId);
//            RedisCacheUtil.listLeftPushAll(redisKey, userIds);
//            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
//        } catch (Exception e) {
//            LOGGER.info("UserDeviceCacheCoreUtils-addCacheUserIdsByDeviceId-error", e);
//        }
//    }
//}
