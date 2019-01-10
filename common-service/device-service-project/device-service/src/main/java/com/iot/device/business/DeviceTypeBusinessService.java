package com.iot.device.business;

import com.google.common.collect.Lists;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.comm.utils.HitCacheUtils;
import com.iot.device.core.DeviceTypeCacheCoreUtils;
import com.iot.device.model.DeviceType;
import com.iot.device.service.IDeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:03 2018/10/10
 * @Modify by:
 */
@Component
public class DeviceTypeBusinessService {

    @Autowired
    private IDeviceTypeService deviceTypeService;


    public List<DeviceType> listDeviceTypes(List<Long> deviceTypeIds) {
        List<DeviceType> resultDataList;
        //1.获取缓存数据
        List<DeviceType> cacheDataList = DeviceTypeCacheCoreUtils.getCacheDataList(deviceTypeIds, VersionEnum.V1);
        List<Long> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(device -> {
                cacheIds.add(device.getId());
            });
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<Long> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheIds, deviceTypeIds);
        // 3.db获取
        List<DeviceType> dbDataList = this.deviceTypeService.findListByDeviceTypeIds(noHitDeviceIds);
        // 4.缓存db数据
        DeviceTypeCacheCoreUtils.cacheDataList(dbDataList, VersionEnum.V1);
        // 5.组转返回
        resultDataList = HitCacheUtils.installList(cacheDataList, dbDataList);
        return resultDataList;
    }

    public List<DeviceType> listBatchDeviceTypes(List<Long> deviceTypeIds) {
        return listBatchDeviceTypes(deviceTypeIds, 15);
    }


    public List<DeviceType> listBatchDeviceTypes(List<Long> deviceTypeIds, int batchSize) {
        if (CollectionUtils.isEmpty(deviceTypeIds)) {
            return Lists.newArrayList();
        }
        List<DeviceType> resultDataList = Lists.newArrayList();

        List<Long> batchIds = Lists.newArrayList();
        for (int i = 0; i < deviceTypeIds.size(); i++) {
            batchIds.add(deviceTypeIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                //获取一次 并清空batchIds
                List<DeviceType> tempResultDataList = this.listDeviceTypes(batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<DeviceType> tempResultDataList = this.listDeviceTypes(batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;
    }

    public DeviceType getDeviceType(Long deviceTypeId) {

        // 1.获取缓存数据
        DeviceType cacheData = DeviceTypeCacheCoreUtils.getCacheData(deviceTypeId, VersionEnum.V1);
        if (null != cacheData) {
            return cacheData;
        }
        // 1.1防止穿透db处理
        // 2.db获取
        DeviceType dbData = this.deviceTypeService.getDeviceTypeById(deviceTypeId);
        // 3.缓存db数据
        if (null != dbData) {
            DeviceTypeCacheCoreUtils.cacheData(deviceTypeId, dbData, VersionEnum.V1);
            DeviceTypeCacheCoreUtils.cacheData(dbData.getType(), dbData.getId(), VersionEnum.V1);
        }
        return dbData;
    }

    public DeviceType getDeviceType(String typeModel) {

        // 1.获取缓存数据
        Long cacheData = DeviceTypeCacheCoreUtils.getCacheData(typeModel, VersionEnum.V1);
        if (null != cacheData) {
            return getDeviceType(cacheData);
        }
        // 1.1防止穿透db处理
        // 2.db获取
        DeviceType dbData = this.deviceTypeService.getDeviceTypeByTypeModel(typeModel);
        // 3.缓存db数据
        if (null != dbData) {
            DeviceTypeCacheCoreUtils.cacheData(dbData.getId(), dbData, VersionEnum.V1);
            DeviceTypeCacheCoreUtils.cacheData(typeModel, dbData.getId(), VersionEnum.V1);
        }
        return dbData;
    }

    public void deleteByDeviceTypeId(Long deviceTypeId) {
        DeviceType deviceType = this.getDeviceType(deviceTypeId);
        if (deviceType != null) {
            this.deviceTypeService.deleteById(deviceTypeId);
            DeviceTypeCacheCoreUtils.removeData(deviceTypeId, VersionEnum.V1);
            DeviceTypeCacheCoreUtils.removeData(deviceType.getType(), VersionEnum.V1);

        }
    }
}
