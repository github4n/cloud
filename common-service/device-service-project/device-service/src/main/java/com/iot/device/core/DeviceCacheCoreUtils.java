package com.iot.device.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.comm.cache.MultiCacheKeyEnum;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.Device;
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
public class DeviceCacheCoreUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DeviceCacheCoreUtils.class);

    /**
     * 获取缓存设备信息
     *
     * @param deviceId 设备id
     * @return
     * @author lucky
     * @date 2018/6/20 13:59
     */
    public static Device getCacheDeviceInfoByDeviceId(String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return null;
            }
            Long tenantId = null;

            String redisKey = CacheKeyUtils.getDeviceInfoKey(tenantId, VersionEnum.V1, deviceId);
            Device device = RedisCacheUtil.valueObjGet(redisKey, Device.class);
            return device;
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return null;
    }


    /**
     * 获取直连设备下所有的子设备列表
     *
     * @param parentDeviceId
     * @return
     * @author lucky
     * @date 2018/6/21 19:42
     */
    public static List<String> getCacheDeviceIdsListByParentDeviceId(String parentDeviceId) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return null;
        }
        try {
            String redisKey = CacheKeyUtils.getDeviceInfoKey(VersionEnum.V1, parentDeviceId);
            List<String> childDeviceId = RedisCacheUtil.listGetAll(redisKey, String.class);
            return childDeviceId;
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-getCacheDeviceInfoListByParentDeviceId-error", e);
        }
        return null;
    }

    /**
     * 获取直连设备下所有的子设备列表
     *
     * @param parentDeviceId
     * @return
     * @author lucky
     * @date 2018/6/21 19:42
     */
    public static List<Device> getCacheDeviceInfoListByParentDeviceId(String parentDeviceId) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return null;
        }
        try {
            String redisKey = CacheKeyUtils.getDeviceInfoKey(VersionEnum.V1, parentDeviceId);
            List<String> childDeviceId = RedisCacheUtil.listGetAll(redisKey, String.class);
            if (!CollectionUtils.isEmpty(childDeviceId)) {
                return getCacheDeviceInfoListByDeviceIds(childDeviceId);
            }
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-getCacheDeviceInfoListByParentDeviceId-error", e);
        }
        return null;
    }

    /**
     * 批量获取设备
     *
     * @param deviceIds
     * @return
     * @author lucky
     * @date 2018/6/20 14:08
     */
    public static List<Device> getCacheDeviceInfoListByDeviceIds(List<String> deviceIds) {
        List<Device> deviceList = null;
        try {
            if (CollectionUtils.isEmpty(deviceIds)) {
                return null;
            }
            Long tenantId = null;
            List<String> redisKeys = CacheKeyUtils.getMultiKey(tenantId, VersionEnum.V1, deviceIds, MultiCacheKeyEnum.DEVICE_INFO);
            deviceList = RedisCacheUtil.mget(redisKeys, Device.class);
            LOGGER.info("DeviceCacheCoreUtils-getCacheDeviceInfoListByDeviceIds:{}", deviceList);
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return deviceList;
    }


    /**
     * 更新设备详细信息
     *
     * @param deviceId
     * @param targetDevice
     * @return
     * @author lucky
     * @date 2018/6/20 14:05
     */
    public static void updateCacheDeviceInfo(String deviceId, Device targetDevice) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }
            Long tenantId = null;
            String redisKey = CacheKeyUtils.getDeviceInfoKey(tenantId, VersionEnum.V1, deviceId);
            RedisCacheUtil.valueObjSet(redisKey, targetDevice);
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-updateCacheDeviceInfo-error", e);
        }
    }


    /**
     * 添加 父级设备 子设备
     *
     * @param parentDeviceId
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/21 19:47
     */
    public static void addCacheChildDevice(String parentDeviceId, String deviceId) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return;
        }
        String redisKey = CacheKeyUtils.getDeviceInfoKey(VersionEnum.V1, parentDeviceId);
        if (StringUtils.isEmpty(deviceId)) {
            if (!checkExistKeyDeviceId(redisKey)) {
                //添加一个空的.
                RedisCacheUtil.listLeftPush(redisKey, deviceId);
                RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
            }
            return;
        }

        try {

            List<String> childDeviceIds = RedisCacheUtil.listGetAll(redisKey, String.class);
            if (!CollectionUtils.isEmpty(childDeviceIds)) {
                if (childDeviceIds.contains(deviceId)) {
                    return; // 已经存在 无需添加
                }
                RedisCacheUtil.listLeftPush(redisKey, deviceId);
            } else {
                RedisCacheUtil.listLeftPush(redisKey, deviceId);
            }
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-updateCacheDeviceInfo-error", e);
        }
    }

    /**
     * 删除缓存
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/21 15:10
     */
    public static void removeCacheDeviceInfo(String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return;
            }
            Long tenantId = null;
            String redisKey = CacheKeyUtils.getDeviceInfoKey(tenantId, VersionEnum.V1, deviceId);
            RedisCacheUtil.delete(redisKey);
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-removeCacheDeviceInfo-error", e);
        }
    }


    /**
     * 清除 父级设备 子设备
     *
     * @param parentDeviceId
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/21 19:47
     */
    public static void removeCacheChildDevice(String parentDeviceId, String deviceId) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return;
        }
        if (StringUtils.isEmpty(deviceId)) {
            return;
        }
        try {
            String redisKey = CacheKeyUtils.getDeviceInfoKey(VersionEnum.V1, parentDeviceId);
            List<String> childDeviceIds = RedisCacheUtil.listGetAll(redisKey, String.class);
            if (!CollectionUtils.isEmpty(childDeviceIds)) {
                if (childDeviceIds.contains(deviceId)) {
                    //delete child deviceId
                    RedisCacheUtil.removeListValue(redisKey, 1L, deviceId);
                }
            }
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-updateCacheDeviceInfo-error", e);
        }
    }

    public static boolean checkExistKeyDeviceId(String deviceId) {
        try {
            if (StringUtils.isEmpty(deviceId)) {
                return false;
            }
            Long tenantId = null;

            String redisKey = CacheKeyUtils.getDeviceInfoKey(tenantId, VersionEnum.V1, deviceId);
            LOGGER.info("DeviceCacheCoreUtils-checkExistKeyDeviceId-key:{}", redisKey);
            return RedisCacheUtil.hasKey(redisKey);
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-checkExistKeyDeviceId-error", e);
        }
        return false;
    }

    public static void removeCacheAll() {
        try {
            String redisKey = "device:" + "*";
            Set<String> keys = RedisCacheUtil.keys(redisKey);
            if (!CollectionUtils.isEmpty(keys)) {
                RedisCacheUtil.delete(keys);
            }
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-removeCacheAll-error", e);
        }
    }

    public static void removeCacheParentDeviceId(String parentDeviceId) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return;
        }
        try {
            String redisKey = CacheKeyUtils.getDeviceInfoKey(VersionEnum.V1, parentDeviceId);
            if (RedisCacheUtil.hasKey(redisKey)) {
                RedisCacheUtil.delete(redisKey);
            }
        } catch (Exception e) {
            LOGGER.info("DeviceCacheCoreUtils-removeCacheParentDeviceId-error", e);
        }
    }

    /*******************************######2018-9-28#######新的缓存修改#############*****************************************/

    public static List<Device> getCacheDataList(Long tenantId, List<String> deviceIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        deviceIds.forEach(
                deviceId -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
                    redisKeys.add(sb.toString());
                });
        List<Device> resultDataList = RedisCacheUtil.mget(redisKeys, Device.class);
        return resultDataList;
    }

    public static Device getCacheData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        Device resultData = RedisCacheUtil.valueObjGet(redisKey, Device.class);
        return resultData;
    }

    public static void cacheData(Long tenantId, String deviceId, Device target, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, target);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataList(Long tenantId, List<Device> targetList, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        Map<String, Device> dataMap = targetList.stream().collect(Collectors.toMap(Device::getUuid, Function.identity()));
        Set<String> deviceIds = dataMap.keySet();
        deviceIds.forEach(deviceId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            RedisCacheUtil.valueObjSet(redisKey, dataMap.get(deviceId));
            RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
        });
    }

    public static void removeData(Long tenantId, String deviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
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
            sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }

    public static void removeDataBlurry(Long tenantId, VersionEnum version) {
        if (tenantId == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("device").append(":").append(version.toString()).append(":");
        String redisKey = sb.toString();
        RedisCacheUtil.deleteBlurry(redisKey);
    }

    public static void cacheDataByParentId(Long tenantId, String parentDeviceId, String childDeviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_parent").append(":").append(version.toString()).append(":").append(parentDeviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.setPush(redisKey, childDeviceId, false);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataByParentId(Long tenantId, String parentDeviceId, List<String> childDeviceIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(childDeviceIds)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("device_parent").append(":").append(version.toString()).append(":").append(parentDeviceId);
        String redisKey = sb.toString();
        childDeviceIds.forEach(childDeviceId -> {
            RedisCacheUtil.setPush(redisKey, childDeviceId, false);
        });
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void removeDataByParentId(Long tenantId, String parentDeviceId, String childDeviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_parent").append(":").append(version.toString()).append(":").append(parentDeviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.setRemove(redisKey, childDeviceId, false);
    }

    public static void removeDataByParentId(Long tenantId, String parentDeviceId, VersionEnum version) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("device_parent").append(":").append(version.toString()).append(":").append(parentDeviceId);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);

    }

    public static Set<String> getCacheDataByParentId(Long tenantId, String parentDeviceId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_parent").append(":").append(version.toString()).append(":").append(parentDeviceId);
        String redisKey = sb.toString();
        return RedisCacheUtil.setGetAll(redisKey, String.class, false);
    }


    public static boolean exist(Long tenantId, String deviceId, VersionEnum version) {
        if (StringUtils.isEmpty(deviceId)) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("device").append(":").append(version.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();

        return RedisCacheUtil.hasKey(redisKey);
    }

}
