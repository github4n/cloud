package com.iot.control.device.core.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.control.device.core.utils.CacheKeyUtils;
import com.iot.control.device.entity.UserDevice;
import com.iot.control.space.enums.VersionEnum;
import com.iot.redis.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xfz
 * @Descrpiton: user_device缓存设计, 一个用户有多个的设备，获取设备要批次获取
 * @Date: 11:48 2018/6/20
 * @Modify by:
 */

@Slf4j
public class UserDeviceCacheCoreUtils {

    public static List<String> getCacheDataKeys(Long tenantId, Long orgId, Long userId, VersionEnum versionEnum) {
        List<String> resultDataList = Lists.newArrayList();
        Map<String, UserDevice> returnDataMap = UserDeviceCacheCoreUtils.getCacheDataMap(tenantId, orgId, userId, versionEnum);
        if (!CollectionUtils.isEmpty(returnDataMap)) {
            // 存放的是 List<String>   ---json  deviceIds
            Set<String> keys = returnDataMap.keySet();
            resultDataList.addAll(keys);
            return resultDataList;
        }
        return resultDataList;
    }


    public static List<UserDevice> getCacheDataList(Long tenantId, Long orgId, Long userId, VersionEnum versionEnum) {
        List<UserDevice> resultDataList = Lists.newArrayList();
        Map<String, UserDevice> returnDataMap = UserDeviceCacheCoreUtils.getCacheDataMap(tenantId, orgId, userId, versionEnum);
        if (!CollectionUtils.isEmpty(returnDataMap)) {
            resultDataList.addAll(returnDataMap.values());
            return resultDataList;
        }
        return resultDataList;
    }

    public static List<UserDevice> getMultiCacheDataMap(Long tenantId, Long orgId, Long userId, List<String> deviceIds, VersionEnum versionEnum) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        List<UserDevice> resultCacheDataList = RedisCacheUtil.hashMultiGetArray(redisKey, deviceIds, UserDevice.class);

        return resultCacheDataList;
    }
    public static Map<String, UserDevice> getCacheDataMap(Long tenantId, Long orgId, Long userId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        Map<String, UserDevice> returnDataMap = RedisCacheUtil.hashGetAll(redisKey, UserDevice.class, true);
        return returnDataMap;
    }

    public static UserDevice getCacheData(Long tenantId, Long orgId, Long userId, String deviceId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        UserDevice resultData = RedisCacheUtil.hashGet(redisKey, deviceId, UserDevice.class);
        return resultData;
    }

    public static void removeCacheData(Long tenantId, Long orgId, Long userId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeCacheData(Long tenantId, Long orgId, Long userId, String deviceId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        if (StringUtils.isEmpty(deviceId)) {
            RedisCacheUtil.delete(redisKey);
            return;
        }
        RedisCacheUtil.hashRemove(redisKey, deviceId);
    }

    public static void removeCacheData(Long tenantId, Long orgId, Long userId, List<String> deviceIds, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        if (!CollectionUtils.isEmpty(deviceIds)) {
            RedisCacheUtil.hashRemove(redisKey, deviceIds);
        }
    }


    public static void cacheData(Long tenantId, Long orgId, Long userId, Map<String, UserDevice> targetDataMap, VersionEnum versionEnum) {
        if (CollectionUtils.isEmpty(targetDataMap)) {
            return;
        }
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        RedisCacheUtil.hashPutAll(redisKey, targetDataMap, true);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataList(Long tenantId, Long orgId, Long userId, List<UserDevice> targetDataList, VersionEnum versionEnum) {
        if (CollectionUtils.isEmpty(targetDataList)) {
            return;
        }
        Map<String, UserDevice> targetDataMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(targetDataList)) {
            targetDataList.forEach(targetData -> {
                String deviceId = targetData.getDeviceId();
                targetDataMap.put(deviceId, targetData);
            });
        }
        String redisKey = CacheKeyUtils.getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, null);
        RedisCacheUtil.hashPutAll(redisKey, targetDataMap, true);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static Long getCacheDataUserIdByDeviceId(Long tenantId, String deviceId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getDeviceIdKey(tenantId, deviceId, versionEnum);
        String redisValue = RedisCacheUtil.valueGet(redisKey);
        if (!StringUtils.isEmpty(redisValue)) {
            return Long.parseLong(redisValue);
        }
        return null;
    }

    public static void cacheDataUserIdByDeviceId(Long tenantId, String deviceId, Long userId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getDeviceIdKey(tenantId, deviceId, versionEnum);
        RedisCacheUtil.valueSet(redisKey, userId.toString(), CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void removeDataUserIdByDeviceId(Long tenantId, String deviceId, VersionEnum versionEnum) {
        String redisKey = CacheKeyUtils.getDeviceIdKey(tenantId, deviceId, versionEnum);
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeDataUserIdByBatchDeviceId(Long tenantId, List<String> deviceIds, VersionEnum versionEnum) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        List<String> redisKeys = Lists.newArrayList();
        deviceIds.forEach(deviceId ->{
            String redisKey = CacheKeyUtils.getDeviceIdKey(tenantId, deviceId, versionEnum);
            redisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(redisKeys);
    }
}
