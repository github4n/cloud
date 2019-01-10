package com.iot.device.core.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.core.DeviceTypeCacheCoreUtils;
import com.iot.device.model.DeviceType;
import com.iot.device.service.IDeviceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:14 2018/6/20
 * @Modify by:
 */
@Slf4j
public class DeviceTypeServiceCoreUtils {

    /**
     * 组装获取deviceType 信息
     *
     * @param deviceTypeId
     * @return
     * @author lucky
     * @date 2018/6/20 17:17
     */
    public static DeviceType getDeviceTypeByDeviceTypeId(Long deviceTypeId) {
        if (deviceTypeId == null) {
            return null;
        }
        DeviceType deviceType = DeviceTypeCacheCoreUtils.getCacheData(deviceTypeId, VersionEnum.V1);
        if (deviceType == null) {
            deviceType = getDBDeviceTypeById(deviceTypeId);
        }
        return deviceType;
    }

    /**
     * 批量获取设备类型
     *
     * @param ids
     * @return
     * @author lucky
     * @date 2018/7/12 9:47
     */
    public static List<DeviceType> getDeviceTypeListByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<DeviceType> cacheList = DeviceTypeCacheCoreUtils.getCacheDataList(ids, VersionEnum.V1);
        //检查部分缓存过期的 ---捞出未命中的deviceIds
        List<Long> noHitCacheIds = getNoHitCacheDeviceTypeIds(cacheList, ids);
        //@防止缓存穿透
        List<Long> needGetDBIds = needGetDBIds(noHitCacheIds);
        //db获取
        List<DeviceType> dbList = findDBDeviceTypeListByIds(needGetDBIds);
        //组转返回
        return installList(cacheList, dbList);

    }

    //防止缓存穿透
    public static List<Long> needGetDBIds(List<Long> noHitCacheIds) {
        List<Long> needGetDBIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(noHitCacheIds)) {
            noHitCacheIds.forEach(id -> {
                boolean hasKey = DeviceTypeCacheCoreUtils.exist(id, VersionEnum.V1);
                if (!hasKey) {
                    needGetDBIds.add(id);
                }
            });
        }
        return needGetDBIds;
    }
    /**
     * 缓存 设备类型信息
     *
     * @param deviceTypeList
     * @return
     * @author lucky
     * @date 2018/6/22 10:25
     */
    public static void cacheDeviceTypeList(List<DeviceType> deviceTypeList) {
        if (CollectionUtils.isEmpty(deviceTypeList)) {
            return;
        }
        for (DeviceType deviceType : deviceTypeList) {
            cacheDeviceType(deviceType);
        }
    }

    public static void cacheDeviceTypeList(List<Long> ids, List<DeviceType> deviceTypeList) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        if (!CollectionUtils.isEmpty(deviceTypeList)) {
            deviceTypeList.forEach(deviceType -> {
                cacheDeviceType(deviceType);
            });
            return;
        }
        ids.forEach(deviceTypeId -> {
            cacheDeviceType(deviceTypeId, null);
        });
    }

    public static void cacheDeviceType(Long deviceTypeId, DeviceType deviceType) {
        if (deviceTypeId == null) {
            return;
        }
        //add cache
        DeviceTypeCacheCoreUtils.cacheData(deviceTypeId, deviceType, VersionEnum.V1);
    }
    public static void cacheDeviceType(DeviceType deviceType) {
        if (deviceType == null) {
            return;
        }
        Long deviceTypeId = deviceType.getId();
        cacheDeviceType(deviceTypeId, deviceType);
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
    public static List<Long> getNoHitCacheDeviceTypeIds(List<DeviceType> resList, List<Long> targetList) {
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

        Map<Long, DeviceType> tempMap = Maps.newHashMap();
        List<Long> noHitCacheList = Lists.newArrayList();
        for (DeviceType res : resList) {
            if (res == null) {
                continue;
            }
            tempMap.put(res.getId(), res);
        }
        for (Long targetId : targetList) {
            DeviceType temp = tempMap.get(targetId);
            if (temp == null) {
                //过滤出未命中的设备
                noHitCacheList.add(targetId);
            }
        }
        return noHitCacheList;
    }

    public static DeviceType getDBDeviceTypeById(Long id) {
        if (id == null) {
            return null;
        }
        IDeviceTypeService deviceTypeService = ApplicationContextHelper.getBean(IDeviceTypeService.class);
        DeviceType deviceType = deviceTypeService.selectById(id);
        cacheDeviceType(id, deviceType);
        return deviceType;
    }

    /**
     * 获取db 缓存的数据
     *
     * @param ids
     * @return
     * @author lucky
     * @date 2018/6/27 17:57
     */
    public static List<DeviceType> findDBDeviceTypeListByIds(List<Long> ids) {
        List<DeviceType> resultList = null;
        if (!CollectionUtils.isEmpty(ids)) {
            IDeviceTypeService deviceTypeService = ApplicationContextHelper.getBean(IDeviceTypeService.class);
            EntityWrapper<DeviceType> wrapper = new EntityWrapper<>();
            if (ids.size() == 1) {
                wrapper.eq("id", ids.get(0));
            } else {
                wrapper.in("id", ids);
            }
            resultList = deviceTypeService.selectList(wrapper);
            cacheDeviceTypeList(ids, resultList);
        }
        return resultList;
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
    public static List<DeviceType> installList(List<DeviceType> cacheList, List<DeviceType> dbList) {
        List<DeviceType> returnInstallDeviceList = null;
        if (!CollectionUtils.isEmpty(cacheList)) {
            if (cacheList.contains(null)) {
                cacheList.remove(null);
            }
            returnInstallDeviceList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(dbList)) {
                cacheList.addAll(dbList);
            }
            returnInstallDeviceList.addAll(cacheList);
        } else {
            if (!CollectionUtils.isEmpty(dbList)) {
                returnInstallDeviceList = Lists.newArrayList();
                returnInstallDeviceList.addAll(dbList);
            }
        }
        return returnInstallDeviceList;
    }

}
