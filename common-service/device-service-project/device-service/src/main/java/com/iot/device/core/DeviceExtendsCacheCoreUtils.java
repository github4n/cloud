package com.iot.device.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.DeviceExtend;
import com.iot.redis.RedisCacheUtil;
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
public class DeviceExtendsCacheCoreUtils {

    public static List<DeviceExtend> getCacheDataList(Long tenantId, List<String> deviceIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_extend").append(":").append(version.toString()).append(":").append(deviceId);
            redisKeys.add(sb.toString());
        });
        List<DeviceExtend> resultDataList = RedisCacheUtil.mget(redisKeys, DeviceExtend.class);
        return resultDataList;
    }

    public static DeviceExtend getCacheData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_extend").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        DeviceExtend resultData = RedisCacheUtil.valueObjGet(redisKey, DeviceExtend.class);
        return resultData;
    }

    public static void cacheData(Long tenantId, String deviceId, DeviceExtend target, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_extend").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, target);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataList(Long tenantId, List<DeviceExtend> targetList, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        Map<String, DeviceExtend> dataMap = targetList.stream().collect(Collectors.toMap(DeviceExtend::getDeviceId, Function.identity()));
        Set<String> deviceIds = dataMap.keySet();
        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            if (tenantId != null) {
                sb.append(tenantId).append(":");
            }
            sb.append("device_extend").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            RedisCacheUtil.valueObjSet(redisKey, dataMap.get(deviceId));
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        });
    }

    public static void removeData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_extend").append(":").append(version.toString()).append(":").append(deviceId);
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
            sb.append("device_extend").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }
}
