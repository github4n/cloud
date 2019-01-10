package com.iot.device.business;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.comm.utils.HitCacheUtils;
import com.iot.device.core.DeviceExtendsCacheCoreUtils;
import com.iot.device.model.DeviceExtend;
import com.iot.device.service.IDeviceExtendService;
import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:06 2018/9/28
 * @Modify by:
 */
@Slf4j
@Component
public class DeviceExtendBusinessService {

    @Autowired
    private IDeviceExtendService deviceExtendService;

    /**
     * 获取设备扩展列表
     *
     * @param tenantId
     * @param deviceIds
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public List<DeviceExtend> listDeviceExtends(Long tenantId, List<String> deviceIds) {
        List<DeviceExtend> resultDataList;
        //1.获取缓存数据
        List<DeviceExtend> cacheDataList = DeviceExtendsCacheCoreUtils.getCacheDataList(tenantId, deviceIds, VersionEnum.V1);
        List<String> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(device -> {
                cacheIds.add(device.getDeviceId());
            });
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<String> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheIds, deviceIds);
        // 3.db获取
        List<DeviceExtend> dbDataList = this.deviceExtendService.findListByDeviceIds(tenantId, noHitDeviceIds);
        // 4.缓存db数据
        DeviceExtendsCacheCoreUtils.cacheDataList(tenantId, dbDataList, VersionEnum.V1);
        // 5.组转返回
        resultDataList = HitCacheUtils.installList(cacheDataList, dbDataList);
        return resultDataList;
    }

    public List<DeviceExtend> listBatchDeviceExtends(Long tenantId, List<String> deviceIds) {
        return listBatchDeviceExtends(tenantId, deviceIds, 15);
    }

    /**
     * 获取设备扩展列表[批量获取个数]
     *
     * @param tenantId
     * @param deviceIds
     * @param batchSize 一次获取多少个
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public List<DeviceExtend> listBatchDeviceExtends(Long tenantId, List<String> deviceIds, int batchSize) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        List<DeviceExtend> resultDataList = Lists.newArrayList();

        List<String> batchIds = Lists.newArrayList();
        for (int i = 0; i < deviceIds.size(); i++) {
            batchIds.add(deviceIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                //获取一次 并清空batchIds
                List<DeviceExtend> tempResultDataList = this.listDeviceExtends(tenantId, batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<DeviceExtend> tempResultDataList = this.listDeviceExtends(tenantId, batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;
    }

    /**
     * 获取设备扩展详情
     *
     * @param tenantId
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public DeviceExtend getDeviceExtend(Long tenantId, String deviceId) {

        // 1.获取缓存数据
        DeviceExtend cacheData = DeviceExtendsCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);
        if (null != cacheData) {
            return cacheData;
        }
        // 1.1防止穿透db处理
        // 2.db获取
        DeviceExtend dbData = this.deviceExtendService.getByDeviceId(tenantId, deviceId);
        // 3.缓存db数据
        if (null != dbData) {
            DeviceExtendsCacheCoreUtils.cacheData(tenantId, deviceId, dbData, VersionEnum.V1);
        }
        return dbData;
    }


    public void saveOrUpdate(UpdateDeviceExtendReq params) {
        DeviceExtend deviceExtend = install(params);
        log.debug("device extend info = " + JSON.toJSONString(deviceExtend));
        this.deviceExtendService.insertOrUpdate(deviceExtend);
        DeviceExtendsCacheCoreUtils.removeData(params.getTenantId(), params.getDeviceId(), VersionEnum.V1);

    }

    public void saveOrUpdateBatch(List<UpdateDeviceExtendReq> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }
        paramsList.forEach(params->{
            saveOrUpdate(params);
        });

    }

    public void delByDeviceId(Long tenantId, String deviceId) {
        DeviceExtend deviceExtend = getDeviceExtend(tenantId, deviceId);
        if (deviceExtend != null) {
            this.deviceExtendService.deleteById(deviceExtend.getId());
            DeviceExtendsCacheCoreUtils.removeData(tenantId, deviceId, VersionEnum.V1);
        }
    }


    private DeviceExtend install(UpdateDeviceExtendReq params) {
        DeviceExtend sourceData = new DeviceExtend();
        DeviceExtend targetData = this.getDeviceExtend(params.getTenantId(), params.getDeviceId());
        if (null == targetData) {
            sourceData.setCreateTime(new Date());
            sourceData.setDeviceId(params.getDeviceId());
            sourceData.setTenantId(params.getTenantId());
        } else {
            sourceData.setId(targetData.getId());
        }
        if (!StringUtils.isEmpty(params.getBatchNumId())) {

            sourceData.setBatchNumId(params.getBatchNumId());
        }
        if (!StringUtils.isEmpty(params.getP2pId())) {

            sourceData.setP2pId(params.getP2pId());
        }
        if (!StringUtils.isEmpty(params.getDeviceCipher())) {
            sourceData.setDeviceCipher(params.getDeviceCipher());
        }
        if (params.getUuidValidityDays() != null) {
            sourceData.setUuidValidityDays(params.getUuidValidityDays());
        }
        if (params.getUnbindFlag() != null) {
            sourceData.setUnbindFlag(params.getUnbindFlag());
        }
        if (params.getResetFlag() != null) {
            sourceData.setResetFlag(params.getResetFlag());
        }
        if (params.getFirstUploadSubDev() != null) {
            sourceData.setFirstUploadSubDev(params.getFirstUploadSubDev());
        }
        if (params.getArea() != null) {
            if (params.getArea().equals("")) {
                sourceData.setArea("");
            } else {
                sourceData.setArea(params.getArea());
            }
        }
        if (params.getBatchNum() != null) {
            sourceData.setBatchNum(params.getBatchNum());
        }

        if (params.getAddress() != null){
            sourceData.setAddress(params.getAddress());
        }

        if (!Strings.isNullOrEmpty(params.getCommType())) {
            sourceData.setCommType(params.getCommType());
        }

        if (!Strings.isNullOrEmpty(params.getTimezone())) {
            sourceData.setTimezone(params.getTimezone());
        }

        if (!Strings.isNullOrEmpty(params.getServerIp())) {
            sourceData.setServerIp(params.getServerIp());
        }

        if (params.getServerPort() != null) {
            sourceData.setServerPort(params.getServerPort());
        }

        if (params.getReportInterval() != null) {
            sourceData.setReportInterval(params.getReportInterval());
        }
        return sourceData;
    }
}
