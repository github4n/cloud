package com.iot.device.core.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.DeviceStatusCacheCoreUtils;
import com.iot.device.model.DeviceStatus;
import com.iot.device.service.IDeviceStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:08 2018/6/21
 * @Modify by:
 */
@Slf4j
public class DeviceStatusServiceCoreUtils {

    /**
     * 获取设备状态
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/21 10:11
     */
    public static DeviceStatus getDeviceStatusByDeviceId(Long tenantId, String deviceId) {
        if (StringUtils.isEmpty(deviceId)) {
            return null;
        }
        DeviceStatus deviceStatus = DeviceStatusCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);
        if (deviceStatus == null) {

            IDeviceStatusService deviceStatusService = ApplicationContextHelper.getBean(IDeviceStatusService.class);
            EntityWrapper<DeviceStatus> wrapper = new EntityWrapper<>();
            wrapper.eq("device_id", deviceId);
            deviceStatus = deviceStatusService.selectOne(wrapper);
            //add cache
            DeviceStatusCacheCoreUtils.cacheData(tenantId, deviceId, deviceStatus, VersionEnum.V1);
        }
        return deviceStatus;
    }

    public static List<DeviceStatus> findDeviceStatusListByDeviceIds(Long tenantId, List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return null;
        }
        List<DeviceStatus> deviceStatusList = DeviceStatusCacheCoreUtils.getCacheDataList(tenantId, deviceIds, VersionEnum.V1);
        //检查部分缓存过期的 ---捞出未命中的deviceIds
        List<String> noHitCacheDeviceIds = getNoHitCacheDeviceIds(deviceStatusList, deviceIds);
        //@防止缓存穿透
        List<String> needGetDBDeviceIds = needGetDBDeviceIds(tenantId, noHitCacheDeviceIds);
        //db获取
        List<DeviceStatus> dbDeviceList = findDBDeviceListByDeviceIds(tenantId, needGetDBDeviceIds);
        //组转返回
        return installList(deviceStatusList, dbDeviceList);
    }

    //防止缓存穿透
    public static List<String> needGetDBDeviceIds(Long tenantId, List<String> noHitCacheDeviceIds) {
        List<String> needGetDBDeviceIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(noHitCacheDeviceIds)) {
            noHitCacheDeviceIds.forEach(deviceId -> {
                boolean hasKey = DeviceStatusCacheCoreUtils.exist(tenantId, deviceId, VersionEnum.V1);
                if (!hasKey) {
                    needGetDBDeviceIds.add(deviceId);
                }
            });
        }
        return needGetDBDeviceIds;
    }

    public static void cacheDeviceStatusList(Long tenantId, List<DeviceStatus> deviceStatusList) {
        if (CollectionUtils.isEmpty(deviceStatusList)) {
            return;
        }
        for (DeviceStatus deviceStatus : deviceStatusList) {
            String deviceId = deviceStatus.getDeviceId();
            //add cache
            DeviceStatusCacheCoreUtils.cacheData(tenantId, deviceId, deviceStatus, VersionEnum.V1);
        }
    }

    public static void cacheDeviceStatusList(Long tenantId, List<String> deviceIds, List<DeviceStatus> deviceStatusList) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        if (!CollectionUtils.isEmpty(deviceStatusList)) {
            deviceStatusList.forEach(deviceStatus -> {
                DeviceStatusCacheCoreUtils.cacheData(tenantId, deviceStatus.getDeviceId(), deviceStatus, VersionEnum.V1);
            });
            return;
        }
    }

    /**
     * 获取db数据
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/27 18:02
     */
    public static DeviceStatus getDBDeviceStatusByDeviceId(String deviceId) {
        DeviceStatus deviceStatus;
        IDeviceStatusService deviceStatusService = ApplicationContextHelper.getBean(IDeviceStatusService.class);
        EntityWrapper<DeviceStatus> wrapper = new EntityWrapper<>();
        wrapper.eq("device_id", deviceId);
        deviceStatus = deviceStatusService.selectOne(wrapper);
        return deviceStatus;
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
    public static List<String> getNoHitCacheDeviceIds(List<DeviceStatus> resList, List<String> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(resList)) {
            //结果内容为空都未命中 返回请求的集合 去db获取
            return targetList;
        }
        if (resList.contains(null)) {
            resList.remove(null);
        }
        if (resList.size() == targetList.size()) {
            log.debug("hit-cache-all-------end");
            return null;
        }
        log.debug("部分缓存失效----》》》");

        Map<String, DeviceStatus> tempMap = Maps.newHashMap();
        List<String> noHitCacheList = Lists.newArrayList();
        for (DeviceStatus res : resList) {
            if (res == null) {
                continue;
            }
            tempMap.put(res.getDeviceId(), res);
        }
        for (String targetId : targetList) {
            DeviceStatus temp = tempMap.get(targetId);
            if (temp == null) {
                //过滤出未命中的设备
                noHitCacheList.add(targetId);
            }
        }

        return noHitCacheList;
    }


    /**
     * 获取db 缓存的数据
     *
     * @param deviceIds
     * @return
     * @author lucky
     * @date 2018/6/27 17:57
     */
    public static List<DeviceStatus> findDBDeviceListByDeviceIds(Long tenantId, List<String> deviceIds) {
        List<DeviceStatus> dbList = null;
        if (!CollectionUtils.isEmpty(deviceIds)) {
            IDeviceStatusService deviceStatusService = ApplicationContextHelper.getBean(IDeviceStatusService.class);
            EntityWrapper<DeviceStatus> wrapper = new EntityWrapper<>();
            if (deviceIds.size() == 1) {
                wrapper.eq("device_id", deviceIds.get(0));
            } else {
                wrapper.in("device_id", deviceIds);
            }
            dbList = deviceStatusService.selectList(wrapper);

            cacheDeviceStatusList(tenantId, deviceIds, dbList);
        }
        return dbList;
    }

    /**
     * 组装 缓存命中和db命中的数据
     *
     * @param cacheList 缓存list
     * @param dbList    db list
     * @return
     * @author lucky
     * @date 2018/6/27 18:37
     */
    public static List<DeviceStatus> installList(List<DeviceStatus> cacheList, List<DeviceStatus> dbList) {
        List<DeviceStatus> returnInstallList = null;
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (cacheList.contains(null)) {
                cacheList.remove(null);
            }
            returnInstallList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(dbList)) {
                cacheList.addAll(dbList);
            }
            returnInstallList.addAll(cacheList);
        } else {
            if (!CollectionUtils.isEmpty(dbList)) {
                returnInstallList = Lists.newArrayList();
                returnInstallList.addAll(dbList);
            }
        }
        return returnInstallList;
    }
}
