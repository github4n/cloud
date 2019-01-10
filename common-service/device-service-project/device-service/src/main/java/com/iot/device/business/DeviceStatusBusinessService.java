package com.iot.device.business;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.comm.utils.HitCacheUtils;
import com.iot.device.core.DeviceStatusCacheCoreUtils;
import com.iot.device.model.DeviceStatus;
import com.iot.device.queue.bean.DeviceStatusMessage;
import com.iot.device.queue.sender.DeviceStatusSender;
import com.iot.device.service.IDeviceStatusService;
import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:06 2018/9/28
 * @Modify by:
 */
@Slf4j
@Component
public class DeviceStatusBusinessService {

    @Autowired
    private IDeviceStatusService deviceStatusService;


    public List<DeviceStatus> listDeviceStatus(Long tenantId, List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }

        List<DeviceStatus> resultDataList;
        // 1.获取缓存
        List<DeviceStatus> cacheDataList = DeviceStatusCacheCoreUtils.getCacheDataList(tenantId, deviceIds, VersionEnum.V1);
        List<String> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(device -> {
                cacheIds.add(device.getDeviceId());
            });
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<String> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheIds, deviceIds);

        // 3.db获取
        List<DeviceStatus> dbDeviceList = this.deviceStatusService.findListByDeviceIds(tenantId, noHitDeviceIds);
        // 4.组转返回
        resultDataList = HitCacheUtils.installList(cacheDataList, dbDeviceList);
        return resultDataList;
    }

    public List<DeviceStatus> listBatchDeviceStatus(Long tenantId, List<String> deviceIds) {
        return listBatchDeviceStatus(tenantId, deviceIds, 15);
    }

    /**
     * 获取设备状态列表[批量获取个数]
     *
     * @param tenantId
     * @param deviceIds
     * @param batchSize 一次获取多少个
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public List<DeviceStatus> listBatchDeviceStatus(Long tenantId, List<String> deviceIds, int batchSize) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        List<DeviceStatus> resultDataList = Lists.newArrayList();

        List<String> batchIds = Lists.newArrayList();
        for (int i = 0; i < deviceIds.size(); i++) {
            batchIds.add(deviceIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                //获取一次 并清空batchIds
                List<DeviceStatus> tempResultDataList = this.listDeviceStatus(tenantId, batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<DeviceStatus> tempResultDataList = this.listDeviceStatus(tenantId, batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;

    }

    /**
     * 获取设备状态详情
     *
     * @param tenantId
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public DeviceStatus getDevice(Long tenantId, String deviceId) {
        // 1.获取缓存数据
        DeviceStatus cacheData = DeviceStatusCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);
        if (null != cacheData) {
            return cacheData;
        }
        // 1.1防止穿透db处理
        // 2.db获取
        DeviceStatus dbData = this.deviceStatusService.getByDeviceId(tenantId, deviceId);
        // 3.缓存db数据
        if (null != dbData) {
            DeviceStatusCacheCoreUtils.cacheData(tenantId, deviceId, dbData, VersionEnum.V1);
        }
        return dbData;
    }

    public void saveOrUpdate(UpdateDeviceStatusReq params) {
        log.debug("deviceStatus saveOrUpdate :{}", params);

        DeviceStatus sourceData = this.getDevice(params.getTenantId(), params.getDeviceId());
        if (sourceData == null) {
            //为空直接做保存
            sourceData = new DeviceStatus();
            sourceData.setDeviceId(params.getDeviceId());
            sourceData.setTenantId(params.getTenantId());
            if (params.getActiveStatus() != null) {
                sourceData.setActiveStatus(params.getActiveStatus());
            }
            if (params.getActiveTime() != null) {
                sourceData.setActiveTime(params.getActiveTime());
            }
            if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                if (params.getOnlineStatus().equals(DeviceStatus.CONNECTED)) {
                    if ( params.getLastLoginTime() != null) {
                        sourceData.setLastLoginTime(params.getLastLoginTime());
                    } else {
                        sourceData.setLastLoginTime(new Date());
                    }
                }
            }
            if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                sourceData.setOnlineStatus(params.getOnlineStatus());
            }
            if (params.getOnOff() != null) {
                sourceData.setOnOff(params.getOnOff());
            }
            if (params.getToken() != null) {
                sourceData.setToken(params.getToken());
            }
            this.deviceStatusService.insert(sourceData);
        } else {
            if (sourceData.getId() == null) {
                //避免缓存未清空 遗留的历史问题
                sourceData = deviceStatusService.getByDeviceId(params.getTenantId(), params.getDeviceId());
            }
            if (params.getActiveStatus() != null) {
                sourceData.setActiveStatus(params.getActiveStatus());
            }
            if (params.getActiveTime() != null) {
                sourceData.setActiveTime(params.getActiveTime());
            }
            if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                if (params.getOnlineStatus().equals(DeviceStatus.CONNECTED)) {
                    if ( params.getLastLoginTime() != null) {
                        sourceData.setLastLoginTime(params.getLastLoginTime());
                    } else {
                        sourceData.setLastLoginTime(new Date());
                    }                }
            }
            if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                sourceData.setOnlineStatus(params.getOnlineStatus());
            }
            if (params.getOnOff() != null) {
                sourceData.setOnOff(params.getOnOff());
            }
            if (params.getToken() != null) {
                sourceData.setToken(params.getToken());
            }
            //更新 ---发送异步更新db操作
            ApplicationContextHelper.getBean(DeviceStatusSender.class).send(DeviceStatusMessage.builder()
                    .deviceStatuses(Lists.newArrayList(sourceData)).build());
        }

        //更新cache
        DeviceStatusCacheCoreUtils.cacheData(params.getTenantId(), params.getDeviceId(), sourceData, VersionEnum.V1);


    }

    public void saveOrUpdateBatch(List<UpdateDeviceStatusReq> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }
        List<DeviceStatus> insertDeviceStatusList = Lists.newArrayList();
        List<DeviceStatus> updateCacheDeviceStatusList = Lists.newArrayList();
        //1.原所有的deviceIds
        List<String> sourceDeviceIdList = Lists.newArrayList();
        Long tenantId = null;
        Map<String, UpdateDeviceStatusReq> deviceStatusReqMap = Maps.newHashMap();
        for (UpdateDeviceStatusReq params : paramsList) {
            if (tenantId == null) {
                tenantId = params.getTenantId();
            }
            deviceStatusReqMap.put(params.getDeviceId(), params);
            sourceDeviceIdList.add(params.getDeviceId());
        }
        //2.获取原所有的deviceIds 是否已经存在 数据库存在
        List<DeviceStatus> existSourceDeviceList = this.listBatchDeviceStatus(tenantId, sourceDeviceIdList);
        List<String> existDeviceIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(existSourceDeviceList)) {
            existSourceDeviceList.forEach(source ->{
                existDeviceIds.add(source.getDeviceId());
            });
        }
        // 2.1.检查部分不存在的deviceId(数据库) ---捞出未命中的deviceIds
        List<String> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(existDeviceIds, sourceDeviceIdList);

        //3.存在的 设备做批量更新 不存在的设备做批量保存
        if (!CollectionUtils.isEmpty(existSourceDeviceList)) {
            existSourceDeviceList.forEach(sourceData ->{
                UpdateDeviceStatusReq params = deviceStatusReqMap.get(sourceData.getDeviceId());
                if (sourceData.getId() == null) {
                    //避免缓存未清空 遗留的历史问题
                    sourceData = deviceStatusService.getByDeviceId(params.getTenantId(), params.getDeviceId());
                }
                if (params.getActiveStatus() != null) {
                    sourceData.setActiveStatus(params.getActiveStatus());
                }
                if (params.getActiveTime() != null) {
                    sourceData.setActiveTime(params.getActiveTime());
                }
                if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                    if (params.getOnlineStatus().equals(DeviceStatus.CONNECTED)) {
                        if ( params.getLastLoginTime() != null) {
                            sourceData.setLastLoginTime(params.getLastLoginTime());
                        } else {
                            sourceData.setLastLoginTime(new Date());
                        }                }
                }
                if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                    sourceData.setOnlineStatus(params.getOnlineStatus());
                }
                if (params.getOnOff() != null) {
                    sourceData.setOnOff(params.getOnOff());
                }
                if (params.getToken() != null) {
                    sourceData.setToken(params.getToken());
                }
                updateCacheDeviceStatusList.add(sourceData);

            });
            //批量异步更新数据库
            ApplicationContextHelper.getBean(DeviceStatusSender.class).send(DeviceStatusMessage.builder()
                    .deviceStatuses(updateCacheDeviceStatusList).build());
        }
        //3.1 批量保存到数据库
        if (!CollectionUtils.isEmpty(noHitDeviceIds)) {
            noHitDeviceIds.forEach(deviceId ->{
                UpdateDeviceStatusReq params = deviceStatusReqMap.get(deviceId);
                DeviceStatus  sourceData = new DeviceStatus();
                sourceData.setDeviceId(params.getDeviceId());
                sourceData.setTenantId(params.getTenantId());
                if (params.getActiveStatus() != null) {
                    sourceData.setActiveStatus(params.getActiveStatus());
                }
                if (params.getActiveTime() != null) {
                    sourceData.setActiveTime(params.getActiveTime());
                }
                if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                    if (params.getOnlineStatus().equals(DeviceStatus.CONNECTED)) {
                        if ( params.getLastLoginTime() != null) {
                            sourceData.setLastLoginTime(params.getLastLoginTime());
                        } else {
                            sourceData.setLastLoginTime(new Date());
                        }
                    }
                }
                if (!StringUtils.isEmpty(params.getOnlineStatus())) {
                    sourceData.setOnlineStatus(params.getOnlineStatus());
                }
                if (params.getOnOff() != null) {
                    sourceData.setOnOff(params.getOnOff());
                }
                if (params.getToken() != null) {
                    sourceData.setToken(params.getToken());
                }
                insertDeviceStatusList.add(sourceData);
            });

            deviceStatusService.insertBatch(insertDeviceStatusList);
        }

        if (!CollectionUtils.isEmpty(updateCacheDeviceStatusList)) {
            //批量更新cache
            DeviceStatusCacheCoreUtils.cacheBatchData(tenantId, updateCacheDeviceStatusList, VersionEnum.V1);
        }
    }

    public void delByDeviceId(Long tenantId, String deviceId) {
        DeviceStatus deviceStatus = getDevice(tenantId, deviceId);
        if (deviceStatus != null) {
            this.deviceStatusService.deleteById(deviceStatus.getId());
            DeviceStatusCacheCoreUtils.removeCacheData(tenantId, deviceId, VersionEnum.V1);
        }
    }
}
