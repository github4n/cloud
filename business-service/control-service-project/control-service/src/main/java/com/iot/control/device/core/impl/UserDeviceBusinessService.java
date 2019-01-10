package com.iot.control.device.core.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.control.device.core.IUserDeviceBusinessService;
import com.iot.control.device.core.cache.UserDeviceCacheCoreUtils;
import com.iot.control.device.core.utils.HitCacheUtils;
import com.iot.control.device.entity.UserDevice;
import com.iot.control.device.service.IUserDeviceService;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import com.iot.control.space.enums.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:46 2018/10/9
 * @Modify by:
 */
@Slf4j
@Component
public class UserDeviceBusinessService implements IUserDeviceBusinessService {

    private static final int batchSize = 15;

    @Autowired
    private IUserDeviceService userDeviceService;


    @Override
    public UserDevice getUserDevice(Long tenantId, String deviceId) {

        UserDevice resultData = null;
        if (StringUtils.isEmpty(deviceId)) {
            log.debug("getUserDevice({},{})", tenantId, deviceId);
            return resultData;
        }

        List<UserDevice> userDevices;
        Long userId = UserDeviceCacheCoreUtils.getCacheDataUserIdByDeviceId(tenantId, deviceId, VersionEnum.V1);
        if (userId == null) {
            userDevices = userDeviceService.getByParams(tenantId, null, null, deviceId, null);
            if (!CollectionUtils.isEmpty(userDevices)) {
                resultData = userDevices.get(0);
                //保存用户跟设备的关系 【设备--》用户】
                UserDeviceCacheCoreUtils.cacheDataUserIdByDeviceId(tenantId, deviceId, resultData.getUserId(), VersionEnum.V1);
            }
            return resultData;
        } else {
            //
            log.debug("getUserDevice({},{}) 获取缓存的对应的userDevice详细", tenantId, deviceId);
            resultData = UserDeviceCacheCoreUtils.getCacheData(tenantId, null, userId, deviceId, VersionEnum.V1);
            if (resultData == null) {
                log.debug("getUserDevice({},{}) 缓存未命中获取db", tenantId, deviceId);
                //db获取
                userDevices = userDeviceService.getByParams(tenantId, null, null, deviceId, null);
                if (!CollectionUtils.isEmpty(userDevices)) {
                    resultData = userDevices.get(0);
                    //保存用户跟设备的关系 【设备--》用户】
                    UserDeviceCacheCoreUtils.cacheDataUserIdByDeviceId(tenantId, deviceId, resultData.getUserId(), VersionEnum.V1);
                    //保存用户跟设备的关系 【用户--》设备】
                    Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
                    userDeviceMap.put(deviceId, resultData);
                    UserDeviceCacheCoreUtils.cacheData(tenantId, null, resultData.getUserId(), userDeviceMap, VersionEnum.V1);
                }
            }
        }

        return resultData;
    }


    @Override
    public UserDevice getUserDevice(Long tenantId, Long orgId, Long userId, String deviceId) {
        UserDevice resultData;
        //cache get
        resultData = UserDeviceCacheCoreUtils.getCacheData(tenantId, null, userId, deviceId, VersionEnum.V1);
        if (resultData == null) {
            //db获取
            List<UserDevice> userDevices = userDeviceService.getByParams(tenantId, orgId, userId, deviceId, null);
            if (!CollectionUtils.isEmpty(userDevices)) {
                resultData = userDevices.get(0);
                //保存用户跟设备的关系 【用户--》设备】
                Map<String, UserDevice> userDeviceMap = Maps.newHashMap();
                userDeviceMap.put(deviceId, resultData);
                UserDeviceCacheCoreUtils.cacheData(tenantId, null, resultData.getUserId(), userDeviceMap, VersionEnum.V1);

                //保存用户跟设备的关系 【设备--》用户】
                UserDeviceCacheCoreUtils.cacheDataUserIdByDeviceId(tenantId, deviceId, resultData.getUserId(), VersionEnum.V1);
            }
        }

        return resultData;
    }

    private List<UserDevice> listUserDevices(Long tenantId, Long orgId, Long userId, List<String> deviceIds) {
        List<UserDevice> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return resultDataList;
        }

        // 1.获取缓存
        List<UserDevice> cacheDataList = UserDeviceCacheCoreUtils.getMultiCacheDataMap(tenantId, orgId, userId, deviceIds, VersionEnum.V1);
        List<String> cacheIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            cacheDataList.forEach(device -> {
                cacheIds.add(device.getDeviceId());
            });
        }
        // 2.检查部分缓存过期的 ---捞出未命中的deviceIds
        List<String> noHitDeviceIds = HitCacheUtils.getNoHitCacheIds(cacheIds, deviceIds);
        // 3.db获取
        List<UserDevice> dbDataList = this.userDeviceService.findListByDeviceIds(tenantId, orgId, userId, noHitDeviceIds);
        // 4.cache
        UserDeviceCacheCoreUtils.cacheDataList(tenantId, orgId, userId, dbDataList, VersionEnum.V1);
        // 5.组转返回
        resultDataList = HitCacheUtils.installList(cacheDataList, dbDataList);
        return resultDataList;
    }

    @Override
    public List<UserDevice> listBatchUserDevice(Long tenantId, Long orgId, Long userId) {
        List<UserDevice> resultDataList = Lists.newArrayList();
        //get cache
        Map<String, UserDevice> userDeviceMap = UserDeviceCacheCoreUtils.getCacheDataMap(tenantId, orgId, userId, VersionEnum.V1);
        if (CollectionUtils.isEmpty(userDeviceMap)) {
            //不存在 db获取
            resultDataList = this.userDeviceService.getByParams(tenantId, orgId, userId, null, null);
            //cache db data.
            UserDeviceCacheCoreUtils.cacheDataList(tenantId, orgId, userId, resultDataList, VersionEnum.V1);
            return resultDataList;
        } else {
            return Lists.newArrayList(userDeviceMap.values());
        }
    }

    @Override
    public List<UserDevice> listBatchUserDevice(Long tenantId, Long orgId, Long userId, List<String> deviceIds) {
        return this.listBatchUserDevice(tenantId, orgId, userId, deviceIds, batchSize);
    }

    @Override
    public List<UserDevice> listBatchUserDevice(Long tenantId, Long orgId, Long userId, List<String> deviceIds, int batchSize) {
        List<UserDevice> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return resultDataList;
        }
        if (userId == null) {
            log.debug("listBatchUserDevice - userId null find getUserDevice....");
            deviceIds.forEach(deviceId -> {
                UserDevice resultData = getUserDevice(tenantId, deviceId);
                if (resultData != null) {
                    resultDataList.add(resultData);
                }
            });
            return resultDataList;
        }
        List<String> batchIds = Lists.newArrayList();
        for (int i = 0; i < deviceIds.size(); i++) {
            batchIds.add(deviceIds.get(i));
            if (i >= 1 && i % batchSize == 0) {
                //获取一次 并清空batchIds
                List<UserDevice> tempResultDataList = this.listUserDevices(tenantId, orgId, userId, batchIds);
                if (!CollectionUtils.isEmpty(tempResultDataList)) {
                    resultDataList.addAll(tempResultDataList);
                }
                batchIds.clear();
            }
        }
        List<UserDevice> tempResultDataList = this.listUserDevices(tenantId, orgId, userId, batchIds);
        if (!CollectionUtils.isEmpty(tempResultDataList)) {
            resultDataList.addAll(tempResultDataList);
        }
        return resultDataList;
    }

    @Transactional
    public UpdateUserDeviceInfoResp saveOrUpdate(UpdateUserDeviceInfoReq params) {
        UserDevice userDevice = null;
        if (params.getId() != null) {
            userDevice = userDeviceService.selectById(params.getId());
        }
        if (userDevice == null) {
            userDevice = new UserDevice();
            BeanUtils.copyProperties(params, userDevice);
            userDevice.setCreateTime(new Date());
        } else {
            if (!StringUtils.isEmpty(params.getDeviceId())) {
                userDevice.setDeviceId(params.getDeviceId());
            }
            if (!StringUtils.isEmpty(params.getPassword())) {
                userDevice.setPassword(params.getPassword());
            }
            if (!StringUtils.isEmpty(params.getUserType())) {
                userDevice.setUserType(params.getUserType());
            }
            if (params.getOrgId() != null) {
                userDevice.setOrgId(params.getOrgId());
            }
            if (params.getUserId() != null) {
                userDevice.setUserId(params.getUserId());
            }
            if (params.getTenantId() != null) {
                userDevice.setTenantId(params.getTenantId());
            }
            if (params.getEventNotifyEnabled() != null) {
                userDevice.setEventNotifyEnabled(params.getEventNotifyEnabled());
            }
        }

        //删除用户跟设备的关系 【用户--》设备】
        UserDeviceCacheCoreUtils.removeCacheData(userDevice.getTenantId(), null, userDevice.getUserId(), VersionEnum.V1);

        //删除用户跟设备的关系 【设备--》用户】
        UserDeviceCacheCoreUtils.removeDataUserIdByDeviceId(userDevice.getTenantId(), userDevice.getDeviceId(), VersionEnum.V1);

        userDeviceService.saveOrUpdate(userDevice);
        UpdateUserDeviceInfoResp resultData = new UpdateUserDeviceInfoResp();
        BeanUtils.copyProperties(userDevice, resultData);
        return resultData;
    }

    @Transactional
    public List<UpdateUserDeviceInfoResp> saveOrUpdateBatch(List<UpdateUserDeviceInfoReq> paramsList) {
        List<UpdateUserDeviceInfoResp> resultDataList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(paramsList)) {
            return resultDataList;
        }
        Long userId = null;
        Long tenantId = null;
        List<String> deviceIds = Lists.newArrayList();
        List<UserDevice> userDeviceList = Lists.newArrayList();
        for (UpdateUserDeviceInfoReq params : paramsList) {
            if (!StringUtils.isEmpty(params.getDeviceId())) {
                deviceIds.add(params.getDeviceId());
            }
            if (userId == null) {
                userId = params.getUserId();
            }
            if(tenantId == null) {
                tenantId = params.getTenantId();
            }
            UserDevice userDevice = null;
            if (params.getId() != null) {
                userDevice = userDeviceService.selectById(params.getId());
            }
            if (userDevice == null) {
                userDevice = new UserDevice();
                BeanUtils.copyProperties(params, userDevice);
                userDevice.setCreateTime(new Date());
            } else {
                if (!StringUtils.isEmpty(params.getDeviceId())) {
                    userDevice.setDeviceId(params.getDeviceId());
                }
                if (!StringUtils.isEmpty(params.getPassword())) {
                    userDevice.setPassword(params.getPassword());
                }
                if (!StringUtils.isEmpty(params.getUserType())) {
                    userDevice.setUserType(params.getUserType());
                }
                if (params.getOrgId() != null) {
                    userDevice.setOrgId(params.getOrgId());
                }
                if (params.getUserId() != null) {
                    userDevice.setUserId(params.getUserId());
                }
                if (params.getTenantId() != null) {
                    userDevice.setTenantId(params.getTenantId());
                }
                if (params.getEventNotifyEnabled() != null) {
                    userDevice.setEventNotifyEnabled(params.getEventNotifyEnabled());
                }
            }
            userDeviceList.add(userDevice);
        }
        userDeviceService.saveOrUpdateBatch(userDeviceList);

        //删除用户跟设备的关系 【用户--》设备】
        UserDeviceCacheCoreUtils.removeCacheData(tenantId, null, userId, VersionEnum.V1);

        //删除用户跟设备的关系 【设备--》用户】
        UserDeviceCacheCoreUtils.removeDataUserIdByBatchDeviceId(tenantId, deviceIds, VersionEnum.V1);
        userDeviceList.forEach(userDevice -> {
            UpdateUserDeviceInfoResp resultData = new UpdateUserDeviceInfoResp();
            BeanUtils.copyProperties(userDevice, resultData);
            resultDataList.add(resultData);
        });

        return resultDataList;
    }

    @Override
    public void delUserDevice(Long tenantId, Long userId, String deviceId) {
        UserDevice userDevice = this.getUserDevice(tenantId, null, userId, deviceId);
        if (userDevice != null) {

            UserDeviceCacheCoreUtils.removeCacheData(tenantId, null, userId, VersionEnum.V1);
            UserDeviceCacheCoreUtils.removeDataUserIdByDeviceId(tenantId, deviceId, VersionEnum.V1);
            userDeviceService.delUserDevice(tenantId, userId, deviceId);
        }
    }
}
