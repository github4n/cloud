package com.iot.device.business;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.comm.utils.HitCacheUtils;
import com.iot.device.core.DeviceCacheCoreUtils;
import com.iot.device.model.Device;
import com.iot.device.service.IDeviceService;
import com.iot.device.vo.req.device.UpdateDeviceConditionReq;
import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateSetDeviceInfoReq;
import com.iot.device.vo.req.device.UpdateWrapperDeviceReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.web.utils.DeviceCoreCopyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/** @Author: lucky @Descrpiton: @Date: 15:06 2018/9/28 @Modify by: */
@Slf4j
@Component
public class DeviceBusinessService {

    @Autowired
    private IDeviceService deviceService;

    public List<Device> listDevices(Long tenantId, List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }

        List<Device> resultDataList;
        // 1.获取缓存
        List<Device> cacheDataList =
                DeviceCacheCoreUtils.getCacheDataList(tenantId, deviceIds, VersionEnum.V1);
        List<String> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(
                    device -> {
                        cacheIds.add(device.getUuid());
                    });
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<String> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheIds, deviceIds);

        // 3.db获取
        List<Device> dbDataList = this.deviceService.findListByDeviceIds(tenantId, noHitDeviceIds);
        // 4.cache
        DeviceCacheCoreUtils.cacheDataList(tenantId, dbDataList, VersionEnum.V1);
        // 4.组转返回
        resultDataList = HitCacheUtils.installList(cacheDataList, dbDataList);
        return resultDataList;
    }

    public List<Device> listBatchDevices(Long tenantId, List<String> deviceIds) {
        return listBatchDevices(tenantId, deviceIds, 15);
    }

    /**
     * 获取设备列表[批量获取个数]
     *
     * @param tenantId
     * @param deviceIds
     * @param batchSize 一次获取多少个
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public List<Device> listBatchDevices(Long tenantId, List<String> deviceIds, int batchSize) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return Lists.newArrayList();
        }
        List<Device> resultDataList = Lists.newArrayList();

        List<String> batchIds = Lists.newArrayList();
        for (int i = 0; i < deviceIds.size(); i++) {
            batchIds.add(deviceIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                // 获取一次 并清空batchIds
                List<Device> tempResultDataList = this.listDevices(tenantId, batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<Device> tempResultDataList = this.listDevices(tenantId, batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;
    }

    /**
     * 获取设备详情
     *
     * @param tenantId
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/9/28 15:50
     */
    public Device getDevice(Long tenantId, String deviceId) {
        // 1.获取缓存数据
        Device cacheData = DeviceCacheCoreUtils.getCacheData(tenantId, deviceId, VersionEnum.V1);
        if (null != cacheData) {
            return cacheData;
        }
        // 1.1防止穿透db处理
        // 2.db获取
        Device dbData = this.deviceService.getByDeviceId(tenantId, deviceId);
        // 3.缓存db数据
        if (null != dbData) {
            DeviceCacheCoreUtils.cacheData(tenantId, deviceId, dbData, VersionEnum.V1);
        }
        return dbData;
    }

    @Transactional
    public GetDeviceInfoRespVo saveOrUpdate(UpdateDeviceInfoReq params) {
        Device sourceData = install(params);
        log.debug("saveOrUpdateDevice:{}", sourceData.toString());
        this.deviceService.insertOrUpdate(sourceData);


        GetDeviceInfoRespVo resultData = new GetDeviceInfoRespVo();
        DeviceCoreCopyUtils.copyDevice(sourceData, resultData);
        DeviceCacheCoreUtils.removeData(params.getTenantId(), params.getUuid(), VersionEnum.V1);
        return resultData;
    }

    public void saveOrUpdateBatch(List<UpdateDeviceInfoReq> paramsList) {
        if (CollectionUtils.isEmpty(paramsList)) {
            return;
        }
        List<Device> targetList = Lists.newArrayList();
        paramsList.forEach(
                params -> {
                    Device sourceData = install(params);
                    targetList.add(sourceData);
                });
        this.deviceService.insertOrUpdateBatch(targetList);
        paramsList.forEach(
                params -> {
                    DeviceCacheCoreUtils.removeData(params.getTenantId(), params.getUuid(), VersionEnum.V1);
                });
    }

    private Device install(UpdateDeviceInfoReq params) {
        Device sourceData = new Device();
        Device targetDevice = this.getDevice(params.getTenantId(), params.getUuid());
        String origParentDeviceId = null;
        if (null == targetDevice) {
            sourceData.setCreateTime(new Date());
            sourceData.setCreateBy(params.getCreateBy());
            sourceData.setUuid(params.getUuid());
            sourceData.setParentId(params.getParentId());
            sourceData.setIsDirectDevice(params.getIsDirectDevice());
            sourceData.setTenantId(params.getTenantId());//新增 设置租户  只有新增的时候才会设置对应的租户 其余都不会再修改eg:网关证书是固定的 子设备绑定到不同的租户时 did 是会发生变化的
            sourceData.setProductId(params.getProductId());//新增的时候 设置
            sourceData.setDeviceTypeId(params.getDeviceTypeId());//新增的时候设置
        } else {
            origParentDeviceId = targetDevice.getParentId();
            sourceData.setId(targetDevice.getId());
            sourceData.setLastUpdateDate(new Date());
            sourceData.setUpdateBy(params.getUpdateBy());
            DeviceCacheCoreUtils.removeData(params.getTenantId(), params.getUuid(), VersionEnum.V1);
        }
        String sourceParentDeviceId = params.getParentId();

        if (!StringUtils.isEmpty(sourceParentDeviceId)) {
            // 现在的parentId 跟原先的parentId不一致的情况 则删除parentDeviceId 对应的子缓存
            if (!StringUtils.isEmpty(origParentDeviceId)) {
                if (!sourceParentDeviceId.equals(origParentDeviceId)) {
                    DeviceCacheCoreUtils.removeDataByParentId(
                            params.getTenantId(), origParentDeviceId, VersionEnum.V1);
                }
            }
            DeviceCacheCoreUtils.removeDataByParentId(
                    params.getTenantId(), sourceParentDeviceId, VersionEnum.V1);
        } else {
            if (!StringUtils.isEmpty(origParentDeviceId)) {
                DeviceCacheCoreUtils.removeDataByParentId(
                        params.getTenantId(), origParentDeviceId, VersionEnum.V1);
            }
        }
        if(targetDevice!= null) {
            if (targetDevice.getProductId() == null) {
                sourceData.setProductId(params.getProductId());
            }
            if (targetDevice.getDeviceTypeId() == null) {
                sourceData.setDeviceTypeId(params.getDeviceTypeId());
            }
            if (targetDevice.getIsDirectDevice() == null) {
                if (params.getIsDirectDevice() != null) {
                    sourceData.setIsDirectDevice(params.getIsDirectDevice());
                }
            }
            if (targetDevice.getCreateTime() == null) {
                sourceData.setCreateTime(new Date());
            }
        }
        if (!StringUtils.isEmpty(params.getResetRandom())) {
            sourceData.setResetRandom(params.getResetRandom());
        }
        if (params.getParentId() != null) {
            if (!params.getParentId().equals("")) {
                sourceData.setParentId(params.getParentId());
            } else {
                sourceData.setParentId(""); // 更新parentId为 null
            }
        }
        if (!StringUtils.isEmpty(params.getName())) {
            sourceData.setName(params.getName());
        }

        if (!StringUtils.isEmpty(params.getIcon())) {
            sourceData.setIcon(params.getIcon());
        }
        if (!StringUtils.isEmpty(params.getBusinessTypeId())) {
            sourceData.setBusinessTypeId(params.getBusinessTypeId());
        }
        if (!StringUtils.isEmpty(params.getExtraName())) {
            sourceData.setExtraName(params.getExtraName());
        }
        if (!StringUtils.isEmpty(params.getVersion())) {
            sourceData.setVersion(params.getVersion());
        }
        if (!StringUtils.isEmpty(params.getRealityId())) {
            sourceData.setRealityId(params.getRealityId());
        }

        if (!StringUtils.isEmpty(params.getIp())) {
            sourceData.setIp(params.getIp());
        }

        if (params.getLocationId() != null) {
            sourceData.setLocationId(params.getLocationId());
        }
        if (!StringUtils.isEmpty(params.getMac())) {
            sourceData.setMac(params.getMac());
        }
        if (params.getTenantId() != null) {
            sourceData.setTenantId(params.getTenantId());
        }
        if (!StringUtils.isEmpty(params.getConditional())) {
            sourceData.setConditional(params.getConditional());
        }
        if (!StringUtils.isEmpty(params.getDevModel())) {
            sourceData.setDevModel(params.getDevModel());
        }
        if (!StringUtils.isEmpty(params.getHwVersion())) {
            sourceData.setHwVersion(params.getHwVersion());
        }
        if (!StringUtils.isEmpty(params.getSupplier())) {
            sourceData.setSupplier(params.getSupplier());
        }
        if (params.getIsAppDev() != null) {
            sourceData.setIsAppDev(params.getIsAppDev());
        }
        if (params.getDeviceTypeId() != null) {
            sourceData.setDeviceTypeId(params.getDeviceTypeId());
        }
        if (params.getOrgId() != null) {
            sourceData.setOrgId(params.getOrgId());
        }
        return sourceData;
    }

    public void saveOrUpdate(Long tenantId, String deviceId, Device device) {

        deviceService.insertOrUpdate(device);
        DeviceCacheCoreUtils.removeData(tenantId, deviceId, VersionEnum.V1);
    }

    public List<Device> findDevicesByParentDeviceId(Long tenantId, String parentDeviceId) {
        // cache get
        Set<String> deviceIds =
                DeviceCacheCoreUtils.getCacheDataByParentId(tenantId, parentDeviceId, VersionEnum.V1);
        if (!CollectionUtils.isEmpty(deviceIds)) {
            List<String> targetDeviceIds = Lists.newArrayList();
            targetDeviceIds.addAll(deviceIds);
            return listBatchDevices(tenantId, targetDeviceIds);
        }
        // 1.1防止穿透db处理
        // 2.db获取
        List<Device> dbDataResultList =
                this.deviceService.findListByDeviceParentId(tenantId, parentDeviceId);
        // 3.缓存db数据
        if (!CollectionUtils.isEmpty(dbDataResultList)) {
            DeviceCacheCoreUtils.cacheDataList(tenantId, dbDataResultList, VersionEnum.V1);
            List<String> childDeviceIds = Lists.newArrayList();
            dbDataResultList.forEach(
                    device -> {
                        childDeviceIds.add(device.getUuid());
                    });
            DeviceCacheCoreUtils.cacheDataByParentId(
                    tenantId, parentDeviceId, childDeviceIds, VersionEnum.V1);
        }
        return dbDataResultList;
    }

    public void delByDeviceId(Long tenantId, String deviceId) {
        Device device = getDevice(tenantId, deviceId);
        if (device != null) {
            deviceService.deleteById(device.getId());
            DeviceCacheCoreUtils.removeData(tenantId, deviceId, VersionEnum.V1);
        }
    }

    public void updateByCondition(UpdateDeviceConditionReq params) {
        EntityWrapper wrapper = new EntityWrapper();
        UpdateSetDeviceInfoReq entity = params.getSetValueEntity();
        Device targetEntity = new Device();
        BeanUtils.copyProperties(entity, targetEntity);
        // 最后更新状态
        targetEntity.setLastUpdateDate(new Date());
        UpdateWrapperDeviceReq conditionWrapper = params.getWrapper();
        if (conditionWrapper != null) {
            if (conditionWrapper.getTenantId() != null) {
                wrapper.eq("tenant_id", conditionWrapper.getTenantId());
            }
            if (!StringUtils.isEmpty(conditionWrapper.getParentId())) {
                wrapper.eq("parent_id", conditionWrapper.getParentId());
            }
            if (conditionWrapper.getLocationId() != null) {
                wrapper.eq("location_id", conditionWrapper.getLocationId());
            }
            if (conditionWrapper.getBusinessTypeId() != null) {
                wrapper.eq("business_type_id", conditionWrapper.getBusinessTypeId());
            }
            if (conditionWrapper.getProductId() != null) {
                wrapper.eq("product_id", conditionWrapper.getProductId());
            }
            if (!StringUtils.isEmpty(conditionWrapper.getUuid())) {
                wrapper.eq("uuid", conditionWrapper.getUuid());
            }
        }
        deviceService.update(targetEntity, wrapper);

        // remove cache
        if (conditionWrapper.getTenantId() != null
                && (!StringUtils.isEmpty(conditionWrapper.getParentId()))
                && (!StringUtils.isEmpty(
                conditionWrapper
                        .getUuid()))) { // if tenantId and parent_id and uuid not null remove cache
            DeviceCacheCoreUtils.removeData(
                    conditionWrapper.getTenantId(), conditionWrapper.getUuid(), VersionEnum.V1);
        } else if (conditionWrapper.getTenantId() != null
                && (!StringUtils.isEmpty(
                conditionWrapper.getParentId()))) { // if tenantId and parent_id not null remove cache
            Set<String> deviceIds =
                    DeviceCacheCoreUtils.getCacheDataByParentId(
                            conditionWrapper.getTenantId(), conditionWrapper.getParentId(), VersionEnum.V1);
            if (!CollectionUtils.isEmpty(deviceIds)) {
                DeviceCacheCoreUtils.removeData(
                        conditionWrapper.getTenantId(), Lists.newArrayList(deviceIds), VersionEnum.V1);
            }
        } else if (conditionWrapper.getTenantId() != null
                && (!StringUtils.isEmpty(
                conditionWrapper.getUuid()))) { // if tenantId and uuid not null  remove cache
            DeviceCacheCoreUtils.removeData(
                    conditionWrapper.getTenantId(), conditionWrapper.getUuid(), VersionEnum.V1);
        } else if (conditionWrapper.getTenantId() != null) {// if tenantId  not null  remove cache
            DeviceCacheCoreUtils.removeDataBlurry(conditionWrapper.getTenantId(), VersionEnum.V1);
        }
    }
}
