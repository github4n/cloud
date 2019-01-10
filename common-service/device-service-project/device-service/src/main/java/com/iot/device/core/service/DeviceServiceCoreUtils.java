package com.iot.device.core.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.DeviceCacheCoreUtils;
import com.iot.device.model.Device;
import com.iot.device.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xfz @Descrpiton: @Date: 16:44 2018/6/20 @Modify by:
 */
@Slf4j
public class DeviceServiceCoreUtils {


    /**
     * 获取设备详细信息
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/21 10:11
     */
    public static Device getDeviceInfoByDeviceId(String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        Device device = DeviceCacheCoreUtils.getCacheData(null, deviceId, VersionEnum.V1);
        if (device == null) {

            device = getDBDeviceInfoByDeviceId(deviceId);
            // add cache
            cacheDevice(deviceId, device != null ? device.getParentId() : null, device);
        }
        return device;
    }

    public static List<Device> findDeviceListByDeviceIds(List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return null;
        }
        List<Device> deviceList = DeviceCacheCoreUtils.getCacheDataList(null, deviceIds, VersionEnum.V1);
        // 检查部分缓存过期的 ---捞出未命中的deviceIds
        List<String> noHitCacheDeviceIds = getNoHitCacheDeviceIds(deviceList, deviceIds);
        // @防止缓存穿透
        List<String> needGetDBDeviceIds = needGetDBDeviceIds(noHitCacheDeviceIds);
        // db获取
        List<Device> dbDeviceList = findDBDeviceListByDeviceIds(needGetDBDeviceIds);
        // 组转返回
        return installDeviceList(deviceList, dbDeviceList);
    }

    public static List<Device> findChildDeviceListByParentDeviceId(String parentDeviceId) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return null;
        }
        Set<String> childDeviceIds =
                DeviceCacheCoreUtils.getCacheDataByParentId(null, parentDeviceId, VersionEnum.V1);
        List<Device> childDeviceList;
        if (CollectionUtils.isEmpty(childDeviceIds)) {
            // get  db  data
            childDeviceList = findDBDeviceListByParentId(parentDeviceId);
        } else {
            // get cache
            childDeviceList = DeviceCacheCoreUtils.getCacheDataList(null, Lists.newArrayList(childDeviceIds), VersionEnum.V1);
        }
        return childDeviceList;

    }

    public static void cacheDevices(List<String> deviceIds, List<Device> deviceList) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        if (!CollectionUtils.isEmpty(deviceList)) {
            deviceList.forEach(
                    device -> {
                        cacheDevice(device.getUuid(), device.getParentId(), device);
                    });
        } else {
            deviceIds.forEach(
                    deviceId -> {
                        cacheDevice(deviceId, null, null);
                    });
        }
    }

    public static void cacheDevices(List<Device> deviceList) {
        if (CollectionUtils.isEmpty(deviceList)) {
            return;
        }
        for (Device device : deviceList) {
            cacheDevice(device);
        }
    }

    public static void cacheDevices(String parentDeviceId, List<Device> deviceList) {
        if (StringUtils.isEmpty(parentDeviceId)) {
            return;
        }
        if (!CollectionUtils.isEmpty(deviceList)) {
            deviceList.forEach(
                    device -> {
                        cacheDevice(device.getUuid(), parentDeviceId, device);
                    });
        } else {
            cacheChildDeviceByParentDeviceId(parentDeviceId, null);
        }
    }

    public static void cacheDevice(Device device) {
        if (device == null) {
            return;
        }
        String deviceId = device.getUuid();
        String parentDeviceId = device.getParentId();
        // add cache
        DeviceCacheCoreUtils.cacheData(null, deviceId, device, VersionEnum.V1);
        // remove cache by parentDeviceId
        removeCacheParentDeviceId(parentDeviceId);
    }

    public static void cacheDevice(String deviceId, String parentDeviceId, Device device) {
        // add cache
        DeviceCacheCoreUtils.updateCacheDeviceInfo(deviceId, device);
        // remove cache by parentDeviceId
        removeCacheParentDeviceId(parentDeviceId);
    }

    public static void cacheChildDeviceByParentDeviceId(String parentDeviceId, String childDeviceId) {
        DeviceCacheCoreUtils.addCacheChildDevice(parentDeviceId, childDeviceId);
    }

    /**
     * 删除device 缓存
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/7/18 15:42
     */
    public static void removeDeviceByDeviceId(String deviceId) {
        DeviceCacheCoreUtils.removeCacheDeviceInfo(deviceId);
    }

    public static void removeCacheChildDeviceByParentDeviceId(
            String parentDeviceId, String childDeviceId) {
        DeviceCacheCoreUtils.removeDataByParentId(null, parentDeviceId, childDeviceId, VersionEnum.V1);
    }

    public static void removeCacheParentDeviceId(String parentDeviceId) {
        DeviceCacheCoreUtils.removeData(null, parentDeviceId, VersionEnum.V1);
    }


    /**
     * 获取db数据
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/27 18:02
     */
    public static Device getDBDeviceInfoByDeviceId(String deviceId) {
        IDeviceService deviceService = ApplicationContextHelper.getBean(IDeviceService.class);
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", deviceId);
        Device device = deviceService.selectOne(wrapper);
        return device;
    }

    /**
     * 匹配命中缓存的数据 捞出过时导致未命中的集合 方便从db获取
     *
     * @param resList    命中缓存的结果
     * @param targetList 传入查询的集合
     * @return
     * @author lucky
     * @date 2018/6/27 17:55
     */
    public static List<String> getNoHitCacheDeviceIds(List<Device> resList, List<String> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(resList)) {
            // 结果内容为空都未命中 返回请求的集合 去db获取
            return targetList;
        }
        if (resList.contains(null)) {
            resList.remove(null);
        }
        if (resList.size() == targetList.size()) {
            log.debug("getNoHitCacheDeviceIds-hit-cache-all-------end");
            return null;
        }
        log.debug("getNoHitCacheDeviceIds-部分缓存失效----》》》");

        Map<String, Device> tempMap = Maps.newHashMap();
        List<String> noHitCacheList = Lists.newArrayList();
        for (Device res : resList) {
            if (res == null) {
                continue;
            }
            tempMap.put(res.getUuid(), res);
        }
        for (String targetId : targetList) {
            Device temp = tempMap.get(targetId);
            if (temp == null) {
                // 过滤出未命中的设备
                noHitCacheList.add(targetId);
            }
        }

        return noHitCacheList;
    }

    // 防止缓存穿透
    public static List<String> needGetDBDeviceIds(List<String> noHitCacheDeviceIds) {
        List<String> needGetDBDeviceIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(noHitCacheDeviceIds)) {
            noHitCacheDeviceIds.forEach(
                    deviceId -> {
                        boolean hasKey = DeviceCacheCoreUtils.exist(null, deviceId, VersionEnum.V1);
                        if (!hasKey) {
                            needGetDBDeviceIds.add(deviceId);
                        }
                    });
        }
        return needGetDBDeviceIds;
    }

    public static List<Device> findDBDeviceListByParentId(String parentDeviceId) {
        IDeviceService deviceService = ApplicationContextHelper.getBean(IDeviceService.class);
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.in("parent_id", parentDeviceId);
        List<Device> childDeviceList = deviceService.selectList(wrapper);
        // add cache
        cacheDevices(parentDeviceId, childDeviceList);
        //        cacheDevices(childDeviceList);
        return childDeviceList;
    }

    /**
     * 获取db 缓存的数据
     *
     * @param deviceIds
     * @return
     * @author lucky
     * @date 2018/6/27 17:57
     */
    public static List<Device> findDBDeviceListByDeviceIds(List<String> deviceIds) {
        List<Device> deviceList = null;
        if (!CollectionUtils.isEmpty(deviceIds)) {

            IDeviceService deviceService = ApplicationContextHelper.getBean(IDeviceService.class);
            EntityWrapper<Device> wrapper = new EntityWrapper<>();
            if (deviceIds.size() == 1) {

                wrapper.eq("uuid", deviceIds.get(0));
            } else {
                wrapper.in("uuid", deviceIds);
            }
            deviceList = deviceService.selectList(wrapper);
            // cache device
            cacheDevices(deviceIds, deviceList);
        }
        return deviceList;
    }

    /**
     * 组装 缓存命中和db命中的数据
     *
     * @param cacheDeviceList 缓存list
     * @param dbDeviceList    db list
     * @return
     * @author lucky
     * @date 2018/6/27 18:37
     */
    public static List<Device> installDeviceList(
            List<Device> cacheDeviceList, List<Device> dbDeviceList) {
        List<Device> returnInstallDeviceList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDeviceList)) {
            if (cacheDeviceList.contains(null)) {
                cacheDeviceList.remove(null);
            }
            if (!CollectionUtils.isEmpty(dbDeviceList)) {
                cacheDeviceList.addAll(dbDeviceList);
            }
            returnInstallDeviceList.addAll(cacheDeviceList);
        } else {
            if (!CollectionUtils.isEmpty(dbDeviceList)) {
                returnInstallDeviceList.addAll(dbDeviceList);
            }
        }
        return returnInstallDeviceList;
    }
}
