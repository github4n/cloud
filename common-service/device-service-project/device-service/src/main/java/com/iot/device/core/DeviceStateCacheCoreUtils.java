package com.iot.device.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:56 2018/6/20
 * @Modify by:
 */
public class DeviceStateCacheCoreUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DeviceStateCacheCoreUtils.class);

    /**
     * 获取缓存设备属性信息
     *
     * @param deviceId 设备id
     * @return
     * @author lucky
     * @date 2018/6/20 13:59
     */
    public static Map<String, Object> getCacheDeviceStateByDeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        try {
            String redisKey = CacheKeyUtils.getDeviceStateKey(VersionEnum.V1, deviceId);
            LOGGER.debug("getCacheDeviceStateByDeviceId key: {}", redisKey);
            return RedisCacheUtil.hashGetAll(redisKey, Object.class, false);
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-getCacheDeviceStateByDeviceId-error", e);
        }
        return null;
    }


    /**
     * 更新设备属性信息
     *
     * @param deviceId
     * @param propertyName
     * @param propertyValue
     * @return
     * @author lucky
     * @date 2018/6/20 14:05
     */
    public static void updateCacheDeviceState(String deviceId, String propertyName, String propertyValue) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }
            if (StringUtils.isEmpty(propertyName)) {
                return;
            }
            String redisKey = CacheKeyUtils.getDeviceStateKey(VersionEnum.V1, deviceId);
            RedisCacheUtil.hashPut(redisKey, propertyName, propertyValue, false);
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-updateCacheDeviceState-error", e);
        }
    }

    public static void addCacheDeviceState(String deviceId, Map<String, String> propertyMap) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }
            if (propertyMap == null) {
                propertyMap = Maps.newHashMap();
            }
            String redisKey = CacheKeyUtils.getDeviceStateKey(VersionEnum.V1, deviceId);

            RedisCacheUtil.hashPutAll(redisKey, propertyMap, false);
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-updateCacheDeviceState-error", e);
        }
    }

    public static void updateCacheDeviceState(String deviceId, final Map<String, String> propertyMap) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }
            String redisKey = CacheKeyUtils.getDeviceStateKey(VersionEnum.V1, deviceId);
            if (CollectionUtils.isEmpty(propertyMap)) {
                //防止穿透
                RedisCacheUtil.hashPutAll(redisKey, propertyMap, false);
//                RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
                return;
            }
            RedisCacheUtil.hashPutAll(redisKey, propertyMap, false);
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-updateCacheDeviceState-error", e);
        }
    }
    /**
     * 恢复默认属性值
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/20 14:26
     */
    public static void recoveryCacheDeviceState(String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }
            String redisKey = CacheKeyUtils.getDeviceStateKey(VersionEnum.V1, deviceId);
            Map<String, Object> propertyMap = RedisCacheUtil.hashGetAll(redisKey, Object.class, false);
            if (propertyMap != null && propertyMap.size() > 0) {
                Set<String> keys = propertyMap.keySet();
                for (String key : keys) {
                    if (key.equals("Dimming")) {
                        propertyMap.put("Dimming", "100");
                    } else if (key.equals("RGBW")) {
                        propertyMap.put("RGBW", "-1375666432");
                    } else if (key.equals("CCT")) {
                        propertyMap.put("CCT", "4000");
                    } else if (key.equals("OnOff")) {
                        propertyMap.put("OnOff", "1");//默认开
                    }
                }
                RedisCacheUtil.hashPutAll(redisKey, propertyMap, false);
            }
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("recoveryDefaultState-error", e);
        }
    }

    public static boolean checkExistKeyDeviceId(String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return false;
            }
            Long tenantId = null;

            String redisKey = CacheKeyUtils.getDeviceStateKey(tenantId, VersionEnum.V1, deviceId);
            LOGGER.info("DeviceStateCacheCoreUtils-checkExistKeyDeviceId-key:{}", redisKey);
            return RedisCacheUtil.hasKey(redisKey);
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-checkExistKeyDeviceId-error", e);
        }
        return false;
    }
    public static void removeCacheAll() {
        try {
            String redisKey = "device_state:" + "*";
            Set<String> keys = RedisCacheUtil.keys(redisKey);
            if (!CollectionUtils.isEmpty(keys)) {
                RedisCacheUtil.delete(keys);
            }
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-removeCacheAll-error", e);
        }
    }


    /*******************************######2018-9-28#######新的缓存修改#############*****************************************/

    public static Map<String, Map<String, Object>> getCacheDataList(Long tenantId, List<String> deviceIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Maps.newHashMap();
        }

        Map<String, Map<String, Object>> resultDataMap = Maps.newHashMap();

        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            Map<String, Object> resultMap = RedisCacheUtil.hashGetAll(redisKey, Object.class, false);
            resultDataMap.put(deviceId, resultMap);
        });
        return resultDataMap;
    }

    public static Map<String, Object> getCacheData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        Map<String, Object> resultDataMap = RedisCacheUtil.hashGetAll(redisKey, Object.class, false);
        return resultDataMap;
    }

    public static void cacheData(Long tenantId, String deviceId, Map<String, Object> target, VersionEnum version) {
        if (CollectionUtils.isEmpty(target)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.hashPutAll(redisKey, target, false);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataMap(Long tenantId, Map<String, Map<String, Object>> targetMap, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetMap)) {
            return;
        }
        Set<String> deviceIds = targetMap.keySet();
        deviceIds.forEach(deviceId -> {
            Map<String, Object> dataMap = targetMap.get(deviceId);
            if (!CollectionUtils.isEmpty(dataMap)) {
                cacheData(tenantId, deviceId, dataMap, version);
            }
        });

    }

    public static boolean checkExistKeyDeviceId(Long tenantId, String deviceId, VersionEnum version) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            return RedisCacheUtil.hasKey(redisKey);
        } catch (Exception e) {
            LOGGER.info("DeviceStateCacheCoreUtils-checkExistKeyDeviceId-error", e);
        }
        return false;
    }

    public static void removeData(Long tenantId, String deviceId, String hashKey, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.hashRemove(redisKey, hashKey);
    }

    public static void removeData(Long tenantId, String deviceId, List<String> hashKeys, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.hashRemove(redisKey, hashKeys);
    }

    public static void removeData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeData(Long tenantId, List<String> deviceIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        Set<String> targetRedisKeys = Sets.newLinkedHashSet();
        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_state").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }

}
