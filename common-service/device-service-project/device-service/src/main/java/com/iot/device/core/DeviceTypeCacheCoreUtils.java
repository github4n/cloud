package com.iot.device.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.DeviceType;
import com.iot.redis.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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
@Slf4j
public class DeviceTypeCacheCoreUtils {

    public static void removeCacheAll() {
        try {
            String redisKey = "device_type:" + "*";
            Set<String> keys = RedisCacheUtil.keys(redisKey);
            if (!CollectionUtils.isEmpty(keys)) {
                RedisCacheUtil.delete(keys);
            }
        } catch (Exception e) {
            log.error("DeviceTypeCacheCoreUtils-removeCacheAll-error", e);
        }
    }


    /*******************************######2018-9-28#######新的缓存修改#############*****************************************/

    public static List<DeviceType> getCacheDataList(List<Long> targetIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        targetIds.forEach(targetId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("device_type").append(":").append(version.toString()).append(":").append(targetId);
            redisKeys.add(sb.toString());
        });
        List<DeviceType> resultDataList = RedisCacheUtil.mget(redisKeys, DeviceType.class);
        return resultDataList;
    }

    public static DeviceType getCacheData(Long targetId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();

        sb.append("device_type").append(":").append(version.toString()).append(":").append(targetId);
        String redisKey = sb.toString();
        DeviceType resultData = RedisCacheUtil.valueObjGet(redisKey, DeviceType.class);
        return resultData;
    }

    public static Long getCacheData(String targetId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(version.toString()).append(":").append("model:").append(targetId);
        String redisKey = sb.toString();
        Long resultData = RedisCacheUtil.valueObjGet(redisKey, Long.class);
        return resultData;
    }

    public static void cacheData(Long deviceTypeId, DeviceType target, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(version.toString()).append(":").append(deviceTypeId);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, target);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheData(String targetDeviceTypeModelKey, Long targetDeviceTypeIdValue, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(version.toString()).append(":").append("model:").append(targetDeviceTypeModelKey);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, targetDeviceTypeIdValue);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataList(List<DeviceType> targetList, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        Map<Long, DeviceType> dataMap = targetList.stream().collect(Collectors.toMap(DeviceType::getId, Function.identity()));
        Set<Long> targetIds = dataMap.keySet();
        targetIds.forEach(targetId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("device_type").append(":").append(version.toString()).append(":").append(targetId);
            String redisKey = sb.toString();
            RedisCacheUtil.valueObjSet(redisKey, dataMap.get(targetId));
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        });
    }

    public static void removeData(String targetTypeKey, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(version.toString()).append(":").append("model:").append(targetTypeKey);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeData(Long targetId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(version.toString()).append(":").append(targetId);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeDataList(List<Long> targetIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return;
        }
        Set<String> targetRedisKeys = Sets.newLinkedHashSet();
        targetIds.forEach(targetId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("device_type").append(":").append(version.toString()).append(":").append(targetId);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }

    public static void removeDataTypes(List<String> typeKeys, VersionEnum version) {
        if (CollectionUtils.isEmpty(typeKeys)) {
            return;
        }
        Set<String> targetRedisKeys = Sets.newLinkedHashSet();
        typeKeys.forEach(typeModel -> {
            StringBuilder sb = new StringBuilder();
            sb.append("device_type").append(":").append(version.toString()).append(":").append(typeModel);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }


    public static boolean exist(Long targetId, VersionEnum version) {
        if (targetId == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(version.toString()).append(":").append(targetId);
        String redisKey = sb.toString();
        return RedisCacheUtil.hasKey(redisKey);
    }

}
