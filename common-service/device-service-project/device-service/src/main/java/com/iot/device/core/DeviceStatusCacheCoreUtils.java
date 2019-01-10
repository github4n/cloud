package com.iot.device.core;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.comm.cache.MultiCacheKeyEnum;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.DeviceStatus;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 13:56 2018/6/20
 * @Modify by:
 */
public class DeviceStatusCacheCoreUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DeviceStatusCacheCoreUtils.class);

    /**
     * 获取缓存设备状态信息
     *
     * @param deviceId 设备id
     * @return
     * @author lucky
     * @date 2018/6/20 13:59
     */
    public static DeviceStatus getCacheDeviceStatusByDeviceId(Long tenantId, String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        try {
            String redisKey = CacheKeyUtils.getDeviceStatusKey(tenantId, VersionEnum.V1, deviceId);
            DeviceStatus deviceStatus = RedisCacheUtil.valueObjGet(redisKey, DeviceStatus.class);
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
            return deviceStatus;
        } catch (Exception e) {
            LOGGER.info("DeviceStatusCacheCoreUtils-getCacheDeviceStatusByDeviceId-error", e);
        }
        return null;
    }

    /**
     * 批量获取设备状态
     *
     * @param deviceIds
     * @return
     * @author lucky
     * @date 2018/6/20 14:08
     */
    public static List<DeviceStatus> getCacheDeviceStatusListByDeviceIds(List<String> deviceIds) {
        try {
            if (CollectionUtils.isEmpty(deviceIds)) {
                return null;
            }
            Long tenantId = null;

            List<String> redisKeys = CacheKeyUtils.getMultiKey(tenantId, VersionEnum.V1, deviceIds, MultiCacheKeyEnum.DEVICE_STATUS);
            return RedisCacheUtil.mget(redisKeys, DeviceStatus.class);
        } catch (Exception e) {
            LOGGER.info("DeviceStatusCacheCoreUtils-getCacheDeviceStatusListByDeviceIds-error", e);
        }
        return null;
    }


    /**
     * 更新设备状态详细信息
     *
     * @param deviceId
     * @param targetDeviceStatus
     * @return
     * @author lucky
     * @date 2018/6/20 14:05
     */
    public static void updateCacheDeviceStatus(Long tenantId, String deviceId, DeviceStatus targetDeviceStatus) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }

            String redisKey = CacheKeyUtils.getDeviceStatusKey(tenantId, VersionEnum.V1, deviceId);
            RedisCacheUtil.valueObjSet(redisKey, targetDeviceStatus);
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceStatusCacheCoreUtils-updateCacheDeviceStatus-error", e);
        }
    }

    public static void removeCacheDeviceStatus(Long tenantId, String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }

            String redisKey = CacheKeyUtils.getDeviceStatusKey(tenantId, VersionEnum.V1, deviceId);
            RedisCacheUtil.delete(redisKey);
        } catch (Exception e) {
            LOGGER.info("DeviceStatusCacheCoreUtils-removeCacheDeviceStatus-error", e);
        }
    }

    public static boolean checkExistKeyDeviceId(Long tenantId, String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return false;
            }

            String redisKey = CacheKeyUtils.getDeviceStatusKey(tenantId, VersionEnum.V1, deviceId);
            LOGGER.info("DeviceStatusCacheCoreUtils-checkExistKeyDeviceId-key:{}", redisKey);
            return RedisCacheUtil.hasKey(redisKey);
        } catch (Exception e) {
            LOGGER.info("DeviceStatusCacheCoreUtils-checkExistKeyDeviceId-error", e);
        }
        return false;
    }
    public static void removeCacheAll() {
        try {
            String redisKey = "device_status:" + "*";
            Set<String> keys = RedisCacheUtil.keys(redisKey);
            if (!CollectionUtils.isEmpty(keys)) {
                RedisCacheUtil.delete(keys);
            }
        } catch (Exception e) {
            LOGGER.info("DeviceStatusCacheCoreUtils-removeCacheAll-error", e);
        }
    }


    /*******************************######2018-9-28#######新的缓存修改#############*****************************************/

    public static List<DeviceStatus> getCacheDataList(Long tenantId, List<String> deviceIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
            redisKeys.add(sb.toString());
        });
        List<DeviceStatus> resultDataList = RedisCacheUtil.mget(redisKeys, DeviceStatus.class);
        return resultDataList;
    }

    public static List<DeviceStatus> getCacheDataMap(Map<String, Long> deviceIdToTenantMap, VersionEnum version) {
        if (CollectionUtils.isEmpty(deviceIdToTenantMap)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        deviceIdToTenantMap.keySet().forEach(deviceId ->{
            StringBuilder sb = new StringBuilder();
            Long tenantId = deviceIdToTenantMap.get(deviceId);
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
            redisKeys.add(sb.toString());
        });

        List<DeviceStatus> resultDataList = RedisCacheUtil.mget(redisKeys, DeviceStatus.class);
        return resultDataList;
    }

    public static DeviceStatus getCacheData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        DeviceStatus resultData = RedisCacheUtil.valueObjGet(redisKey, DeviceStatus.class);
        return resultData;
    }

    public static void cacheData(Long tenantId, String deviceId, DeviceStatus target, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, target);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheBatchData(Long tenantId, List<DeviceStatus> targetList, VersionEnum version) {
        Map<String, String> stringValuesMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(targetList)) {
            targetList.forEach(target ->{
                StringBuilder sb = new StringBuilder();
                if (tenantId != null) {
                    sb.append(tenantId).append(":");
                }
                sb.append("device_status").append(":").append(version.toString()).append(":").append(target.getDeviceId());
                String redisKey = sb.toString();
                String valueJson = JSON.toJSONString(target);
                stringValuesMap.put(redisKey, valueJson);
            });
        }
        RedisCacheUtil.valueSetBatch(stringValuesMap);
        stringValuesMap.keySet().forEach(redisKey ->{
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        });
    }

    public static void cacheDataList(Long tenantId, List<DeviceStatus> targetList, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        Map<String, DeviceStatus> dataMap = targetList.stream().collect(Collectors.toMap(DeviceStatus::getDeviceId, Function.identity()));
        Set<String> deviceIds = dataMap.keySet();
        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            RedisCacheUtil.valueObjSet(redisKey, dataMap.get(deviceId));
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        });
    }

    public static void removeCacheData(Long tenantId, String deviceId, VersionEnum version) {

        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);
    }


    public static boolean exist(Long tenantId, String deviceId, VersionEnum version) {
        if (StringUtils.isEmpty(deviceId)) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_status").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();

        return RedisCacheUtil.hasKey(redisKey);
    }
}
