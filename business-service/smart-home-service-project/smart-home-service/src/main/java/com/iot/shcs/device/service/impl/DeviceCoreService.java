package com.iot.shcs.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.device.api.UserDeviceCoreApi;
import com.iot.control.device.vo.req.DelUserDeviceInfoReq;
import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
import com.iot.device.api.*;
import com.iot.device.exception.DeviceExceptionEnum;
import com.iot.device.vo.req.device.*;
import com.iot.device.vo.req.group.UpdateGroupReq;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.*;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.device.enums.DeviceCoreExceptionEnum;
import com.iot.shcs.device.utils.BeanCopyUtils;
import com.iot.shcs.device.utils.DeviceCoreUtils;
import com.iot.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:29 2018/10/11
 * @Modify by:
 */
@Slf4j
@Component
public class DeviceCoreService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceCoreService.class);

    public static final String GROUP="group";

    @Autowired
    private UserDeviceCoreApi userDeviceCoreApi;

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private DeviceExtendsCoreApi deviceExtendsCoreApi;

    @Autowired
    private DeviceStatusCoreApi deviceStatusCoreApi;

    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;

    @Autowired
    private ProductCoreApi productCoreApi;

    @Autowired
    private DeviceTypeCoreApi deviceTypeCoreApi;

    @Autowired
    private DeviceMQTTService deviceMQTTService;
    @Autowired
    private GroupApi groupApi;

    public Long getRootUserIdByDeviceId(Long tenantId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(null)
                .deviceId(deviceId).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        if (!CollectionUtils.isEmpty(userDeviceInfoList)) {
            for (ListUserDeviceInfoRespVo userDeviceInfo : userDeviceInfoList) {
                if (userDeviceInfo.getUserType().equals("root")) {
                    return userDeviceInfo.getUserId();
                }
            }
        }
        return null;
    }


    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, Long userId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");

        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(userId)
                .deviceId(null).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        return userDeviceInfoList;
    }

    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(null)
                .deviceId(deviceId).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        return userDeviceInfoList;
    }

    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, Long userId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(userId)
                .deviceId(deviceId).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        return userDeviceInfoList;
    }

    public ListUserDeviceInfoRespVo getUserDevice(Long tenantId, Long userId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(userId)
                .deviceId(deviceId).build();
        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
        if (CollectionUtils.isEmpty(userDeviceInfoList)) {
            return null;
        }
        return userDeviceInfoList.get(0);
    }

    public List<ListUserDeviceInfoRespVo> listBatchUserDevices(Long tenantId, Long userId, List<String> deviceIds) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(deviceIds, "deviceId.notnull");
        List<ListUserDeviceInfoRespVo> resultDataList = userDeviceCoreApi.listBatchUserDevice(GetUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(userId)
                .deviceIds(deviceIds)
                .build());
        return resultDataList;
    }

    public List<ListUserDeviceInfoRespVo> listBatchUserDevices(Long tenantId, List<String> deviceIds) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceIds, "deviceId.notnull");
        List<ListUserDeviceInfoRespVo> resultDataList = userDeviceCoreApi.listBatchUserDevice(GetUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .deviceIds(deviceIds)
                .build());
        List<ListUserDeviceInfoRespVo> resultDataRetList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resultDataList)) {
            for (ListUserDeviceInfoRespVo userDeviceInfo : resultDataList) {
                if (userDeviceInfo.getUserType().equals("root")) {
                    resultDataRetList.add(userDeviceInfo);
                }
            }
        }
        return resultDataRetList;
    }


    public Map<String, ListUserDeviceInfoRespVo> listBatchUserDeviceMap(Long tenantId, List<String> deviceIds) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceIds, "deviceId.notnull");
        Map<String, ListUserDeviceInfoRespVo> userDeviceInfoRespVoMap = Maps.newHashMap();
        List<ListUserDeviceInfoRespVo> resultDataList = userDeviceCoreApi.listBatchUserDevice(GetUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .deviceIds(deviceIds)
                .build());
        if (!CollectionUtils.isEmpty(resultDataList)) {
            resultDataList.forEach(userDevice ->{
                userDeviceInfoRespVoMap.put(userDevice.getDeviceId(), userDevice);
            });
        }
        return userDeviceInfoRespVoMap;
    }

    public UpdateUserDeviceInfoResp saveOrUpdateUserDevice(UpdateUserDeviceInfoReq updateUserDeviceParam) {
        AssertUtils.notNull(updateUserDeviceParam.getTenantId(), "tenantId.notnull");
        AssertUtils.notEmpty(updateUserDeviceParam.getDeviceId(), "deviceId.notnull");
        //检查 user_device 是否存在
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = listUserDevices(updateUserDeviceParam.getTenantId(), updateUserDeviceParam.getDeviceId());
        if (!CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            updateUserDeviceParam.setId(userDeviceInfoRespVoList.get(0).getId());//修改id必须传
        } else {
            updateUserDeviceParam.setPassword(StringUtil.getRandomString(12));
        }
        UpdateUserDeviceInfoResp userDeviceInfoResp = userDeviceCoreApi.saveOrUpdate(updateUserDeviceParam);

        /**
         * 更新设备是否被激活的状态
         */
        saveOrUpdateDeviceStatus(UpdateDeviceStatusReq.builder()
                .tenantId(updateUserDeviceParam.getTenantId())
                .deviceId(updateUserDeviceParam.getDeviceId())
                .activeStatus(1)
                .activeTime(new Date()).build());
        return userDeviceInfoResp;
    }

    public List<UpdateUserDeviceInfoResp> saveOrUpdateBatchUserDevice(Long tenantId, Long userId, String userType, List<String> deviceIds) {
        List<UpdateUserDeviceInfoResp> updateUserDeviceInfoRespList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(deviceIds)) {
            return updateUserDeviceInfoRespList;
        }
        List<ListUserDeviceInfoRespVo> listUserDeviceInfoRespVoList = this.listBatchUserDevices(tenantId, userId, deviceIds);
        Map<String, Long> deviceToUserDeviceIdMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(listUserDeviceInfoRespVoList)) {
            listUserDeviceInfoRespVoList.forEach(userDevice ->{
                deviceToUserDeviceIdMap.put(userDevice.getDeviceId(), userDevice.getId());
            });
        }
        List<UpdateUserDeviceInfoReq> updateUserDeviceInfoReqList = Lists.newArrayList();
        deviceIds.forEach(deviceId ->{
            //建立用户与子设备的关系(user_device)
            UpdateUserDeviceInfoReq updateUserDeviceParam = UpdateUserDeviceInfoReq
                    .builder()
                    .id(deviceToUserDeviceIdMap.get(deviceId))
                    .tenantId(tenantId)
                    .deviceId(deviceId)
                    .userType(userType)
                    .userId(userId)
                    .build();
            updateUserDeviceInfoReqList.add(updateUserDeviceParam);
        });
        updateUserDeviceInfoRespList = userDeviceCoreApi.saveOrUpdateBatch(updateUserDeviceInfoReqList);

        try {
            //批量更新设备是否被激活的状态
            List<UpdateDeviceStatusReq> updateDeviceStatusReqList = Lists.newArrayList();
            deviceIds.forEach(deviceId ->{
                UpdateDeviceStatusReq updateDeviceStatusReq =  UpdateDeviceStatusReq.builder()
                        .tenantId(tenantId)
                        .deviceId(deviceId)
                        .activeStatus(1)
                        .activeTime(new Date()).build();
                updateDeviceStatusReqList.add(updateDeviceStatusReq);
            });
            this.saveOrUpdateBatchDeviceStatus(updateDeviceStatusReqList);
        }catch (Exception e) {
            log.error("saveOrUpdateDeviceStatus ....error.", e);
        }

        return updateUserDeviceInfoRespList;
    }
    /**
     * 检查用户是否已经存在过其他的直连设备【一个用户只能存在一个直连设备】
     *
     * @param alreadyDirectDeviceId
     * @param userId
     */
    public void checkUserOnlyDirectDevice(String alreadyDirectDeviceId, Long tenantId, Long userId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(alreadyDirectDeviceId, "deviceId.notnull");
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(alreadyDirectDeviceId);
        GetProductInfoRespVo productInfo = productCoreApi.getByProductId(deviceInfo.getProductId());
        if (productInfo == null) {
            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_NOT_EXIST);
        }
        if (DeviceCoreUtils.isGateWayProduct(productInfo)) {
            // 当前正要添加的设备是 网关
            List<ListUserDeviceInfoRespVo> userDeviceInfoList = this.listUserDevices(tenantId, userId);
            if (CollectionUtils.isEmpty(userDeviceInfoList)) {
                return;
            }

            boolean isExist = false;
            for (ListUserDeviceInfoRespVo deviceResp : userDeviceInfoList) {
                if (!deviceResp.getDeviceId().equals(alreadyDirectDeviceId)) {
                    GetDeviceInfoRespVo otherDeviceInfo = deviceCoreApi.get(deviceResp.getDeviceId());
                    GetProductInfoRespVo productResp = productCoreApi.getByProductId(otherDeviceInfo.getProductId());
                    if (productResp != null) {
                        if (DeviceCoreUtils.isGateWayProduct(productResp)) {
                            // 说明存在多个直连设备 目前不被允许
                            isExist = true;
                            break;
                        }
                    }
                }
            }
            if (isExist) {
                throw new BusinessException(DeviceCoreExceptionEnum.USER_EXIST_DIRECT_DEVICE);
            }
        }
    }

    public void delUserDeviceParams(Long tenantId, Long userId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        //删除user_device关系
        userDeviceCoreApi.delUserDevice(tenantId, userId, deviceId);
        //清空 device parentId
        deviceCoreApi.saveOrUpdate(UpdateDeviceInfoReq.builder().tenantId(tenantId).uuid(deviceId).parentId("").updateBy(userId).build());
    }

    public GetProductInfoRespVo getProductByDeviceId(String deviceId) {
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
        if (deviceInfo == null) {
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NOT_EXIST);
        }
        GetProductInfoRespVo productInfo = productCoreApi.getByProductId(deviceInfo.getProductId());
        if (productInfo == null) {
            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_NOT_EXIST);
        }
        return productInfo;
    }

    public GetDeviceTypeInfoRespVo getDeviceTypeByDeviceId(String deviceId) {
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
        if (deviceInfo == null) {
            throw new BusinessException(DeviceCoreExceptionEnum.DEVICE_NOT_EXIST);
        }
        GetProductInfoRespVo productInfo = this.getProductById(deviceInfo.getProductId());
        if (productInfo == null) {
            throw new BusinessException(DeviceCoreExceptionEnum.PRODUCT_NOT_EXIST);
        }

        GetDeviceTypeInfoRespVo deviceTypeInfo = this.getDeviceTypeById(productInfo.getDeviceTypeId());
        return deviceTypeInfo;
    }

    public GetProductInfoRespVo getProductById(Long productId) {
        AssertUtils.notEmpty(productId, "productId.notnull");
        GetProductInfoRespVo productInfo = productCoreApi.getByProductId(productId);
        return productInfo;
    }

    public GetDeviceTypeInfoRespVo getDeviceTypeById(Long deviceTypeId) {
        AssertUtils.notEmpty(deviceTypeId, "deviceTypeId.notnull");
        GetDeviceTypeInfoRespVo deviceTypeInfo = deviceTypeCoreApi.get(deviceTypeId);
        return deviceTypeInfo;
    }

    public List<ListDeviceInfoRespVo> listDevicesByParentId(String parentDeviceId) {
        return deviceCoreApi.listDevicesByParentId(parentDeviceId);
    }

    public List<String> listDeviceIdsByParentId(String parentDeviceId) {
        List<String> deviceIds = Lists.newArrayList();
        List<ListDeviceInfoRespVo> resultDataList = deviceCoreApi.listDevicesByParentId(parentDeviceId);
        if (!CollectionUtils.isEmpty(resultDataList)) {
            resultDataList.forEach(resultData ->{
                deviceIds.add(resultData.getUuid());
            });
        }
        return deviceIds;
    }

    public GetProductInfoRespVo getByProductModel(String productModel) {
        return productCoreApi.getByProductModel(productModel);
    }

    public List<GetProductInfoRespVo> listBatchByProductModel(Collection<String> productModelList) {
        if (CollectionUtils.isEmpty(productModelList)) {
            return Lists.newArrayList();
        }
        log.info("---------:{}", JSON.toJSONString(productModelList));
        return productCoreApi.listByProductModel(productModelList);
    }

    public GetDeviceInfoRespVo getDeviceInfoByDeviceId(String deviceId) {
        return deviceCoreApi.get(deviceId);
    }

    public GetDeviceStatusInfoRespVo getDeviceStatusByDeviceId(Long tenantId, String deviceId) {
        return deviceStatusCoreApi.get(tenantId, deviceId);
    }

    public GetDeviceInfoRespVo saveOrUpdateDeviceInfo(UpdateDeviceInfoReq deviceInfoParam) {
        AssertUtils.notNull(deviceInfoParam.getTenantId(), "tenantId.notnull");
        AssertUtils.notEmpty(deviceInfoParam.getUuid(), "deviceId.notnull");
        return deviceCoreApi.saveOrUpdate(deviceInfoParam);
    }

    public void saveOrUpdateBatchDeviceInfo(List<UpdateDeviceInfoReq> batchDeviceInfoReqList) {
        if (CollectionUtils.isEmpty(batchDeviceInfoReqList)){
            return;
        }
         deviceCoreApi.saveOrUpdateBatch(batchDeviceInfoReqList);

    }
    public void saveOrUpdateDeviceStatus(UpdateDeviceStatusReq deviceStatusParam) {
        AssertUtils.notNull(deviceStatusParam.getTenantId(), "tenantId.notnull");
        AssertUtils.notEmpty(deviceStatusParam.getDeviceId(), "deviceId.notnull");
        deviceStatusCoreApi.saveOrUpdate(deviceStatusParam);
    }
    public void saveOrUpdateBatchDeviceStatus(List<UpdateDeviceStatusReq> deviceStatusReqList) {
        if (CollectionUtils.isEmpty(deviceStatusReqList)) {
            return;
        }
        deviceStatusCoreApi.saveOrUpdateBatch(deviceStatusReqList);
    }

    public GetDeviceExtendInfoRespVo getDeviceExtendByDeviceId(Long tenantId, String deviceId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
        return deviceExtendsCoreApi.get(tenantId, deviceId);
    }

    public void saveOrUpdateExtend(UpdateDeviceExtendReq updateDeviceExtendReq) {
        AssertUtils.notNull(updateDeviceExtendReq.getTenantId(), "tenantId.notnull");
        AssertUtils.notEmpty(updateDeviceExtendReq.getDeviceId(), "deviceId.notnull");
        deviceExtendsCoreApi.saveOrUpdate(updateDeviceExtendReq);
    }

    public void delChildDeviceByDeviceId(Long tenantId, String subDevId, Long userId) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(subDevId, "deviceId.notnull");

        userDeviceCoreApi.delUserDevice(tenantId, userId, subDevId);
        //清除 subDevice parentId
        UpdateDeviceInfoReq deviceInfoReq = UpdateDeviceInfoReq.builder()
                .tenantId(tenantId)
                .uuid(subDevId)
                .parentId("")
                .build();
        deviceCoreApi.saveOrUpdate(deviceInfoReq);

        this.delMultiSubDeviceGroup(tenantId, userId, Lists.newArrayList(subDevId));

    }
    public void delBatchSubDeviceId(Long tenantId, Long userId, List<String> subDeviceIds) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(subDeviceIds, "deviceId.notnull");
        DelUserDeviceInfoReq delUserDeviceInfoReq = DelUserDeviceInfoReq.builder()
                .tenantId(tenantId)
                .userId(userId)
                .subDeviceIds(subDeviceIds)
                .build();
        userDeviceCoreApi.delBatchUserDevice(delUserDeviceInfoReq);

        List<UpdateDeviceInfoReq> updateDeviceInfoReqList = Lists.newArrayList();
        //清除 subDevice parentId
        subDeviceIds.forEach(subDevId ->{
            UpdateDeviceInfoReq deviceInfoReq = UpdateDeviceInfoReq.builder()
                    .tenantId(tenantId)
                    .uuid(subDevId)
                    .parentId("")
                    .build();
            updateDeviceInfoReqList.add(deviceInfoReq);
        });
        if (!CollectionUtils.isEmpty(updateDeviceInfoReqList)) {
            deviceCoreApi.saveOrUpdateBatch(updateDeviceInfoReqList);
        }
        this.delMultiSubDeviceGroup(tenantId, userId, subDeviceIds);

    }

    private void delMultiSubDeviceGroup(Long tenantId, Long userId, List<String> subDevIdList) {
        if (CollectionUtils.isEmpty(subDevIdList)) {
            return;
        }
        subDevIdList.forEach(subDevId ->{
            //TODO 批量删除
            try {
                //如果该子设备包含组属性，则删除响应的group，groupDetail表
                List<Long> groupIds=new ArrayList<>();
                Map<String, Object> deviceStateMap=deviceStateCoreApi.get(tenantId,subDevId);
                logger.info("delChildDeviceByDeviceId:deviceStateMap"+JSON.toJSONString(deviceStateMap));
                if(deviceStateMap!=null){
                    for(Map.Entry<String, Object> entry : deviceStateMap.entrySet()){
                        if(entry.getKey().contains(GROUP)){
                            Long groupId=Long.parseLong((String)entry.getValue());
                            groupIds.add(groupId);
                        }
                    }
                    logger.info("delChildDeviceByDeviceId:deviceStateMap-->groupIds"+JSON.toJSONString(groupIds));
                    UpdateGroupReq updateGroupReq=new UpdateGroupReq();
                    updateGroupReq.setGroupIds(groupIds);
                    updateGroupReq.setTenantId(tenantId);
                    updateGroupReq.setCreateBy(userId);
                    groupApi.delGroupById(updateGroupReq);
                }
            }catch (Exception e){
                logger.info("deleteGroup error!!!   "+e.toString());
            }
        });

    }

    public List<ListUserDeviceInfoRespVo> listUserDevicesByDeviceIds(Long tenantId, Long userId, List<String> deviceIdList) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        AssertUtils.notNull(userId, "userId.notnull");
        AssertUtils.notEmpty(deviceIdList, "deviceId.notnull");
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = userDeviceCoreApi.listBatchUserDevice(GetUserDeviceInfoReq.builder()
                .deviceIds(deviceIdList)
                .userId(userId)
                .tenantId(tenantId).build());

        return userDeviceInfoRespVoList;
    }

    public List<ListDeviceInfoRespVo> listDevicesByDeviceIds(List<String> deviceIdList) {
        if (CollectionUtils.isEmpty(deviceIdList)) {
            return Lists.newArrayList();
        }
        List<ListDeviceInfoRespVo> resultDeviceList = deviceCoreApi.listDevices(ListDeviceInfoReq.builder()
                .deviceIds(deviceIdList).build());
        return resultDeviceList;
    }

    public List<ListDeviceStatusRespVo> listDeviceStatusByDeviceIds(Long tenantId, List<String> deviceIdList) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        if (CollectionUtils.isEmpty(deviceIdList)) {
            return Lists.newArrayList();
        }
        return deviceStatusCoreApi.listDeviceStatus(ListDeviceStateReq.builder()
                .tenantId(tenantId)
                .deviceIds(deviceIdList)
                .build());
    }

    public Map<String, ListDeviceStatusRespVo> getDeviceStatusMapByDeviceIs(Long tenantId, List<String> deviceIdList) {
        Map<String, ListDeviceStatusRespVo> deviceStatusMap = new HashMap<>();
        List<ListDeviceStatusRespVo> deviceStatusList = listDeviceStatusByDeviceIds(tenantId, deviceIdList);
        for(ListDeviceStatusRespVo deviceStatus : deviceStatusList) {
            deviceStatusMap.put(deviceStatus.getDeviceId(), deviceStatus);
        }
        return deviceStatusMap;
    }

    public Map<String, Map<String, Object>> listDeviceStateByDeviceIds(Long tenantId, List<String> deviceIdList) {
        AssertUtils.notNull(tenantId, "tenantId.notnull");
        if (CollectionUtils.isEmpty(deviceIdList)) {
            return Maps.newHashMap();
        }
        return deviceStateCoreApi.listStates(ListDeviceStateReq.builder()
                .tenantId(tenantId)
                .deviceIds(deviceIdList)
                .build());
    }

    public List<ListProductRespVo> listProductByProductIds(List<Long> productIds) {

        List<ListProductRespVo> productRespVoList = productCoreApi.listProducts(ListProductInfoReq.builder()
                .productIds(productIds)
                .build());
        return productRespVoList;
    }

    public Map<Long, ListProductRespVo> getProductMapByProductIds(List<Long> productIds) {
        Map<Long, ListProductRespVo> productMap = new HashMap<>();
        List<ListProductRespVo> productList = listProductByProductIds(productIds);
        for (ListProductRespVo product : productList) {
            productMap.put(product.getId(), product);
        }

        return productMap;
    }

    public List<ListDeviceTypeRespVo> listDeviceTypeByDeviceTypeIds(List<Long> deviceTypeIds) {
        List<ListDeviceTypeRespVo> deviceTypeRespVoList = deviceTypeCoreApi.listDeviceType(ListDeviceTypeReq.builder()
                .deviceTypeIds(deviceTypeIds)
                .build());
        return deviceTypeRespVoList;
    }

    public Map<Long, ListDeviceTypeRespVo> getDeviceTypeMapByIds(List<Long> deviceTypeIds) {
        Map<Long, ListDeviceTypeRespVo> deviceTypeMap = new HashMap<>();
        List<ListDeviceTypeRespVo> deviceTypeList = listDeviceTypeByDeviceTypeIds(deviceTypeIds);
        for (ListDeviceTypeRespVo deviceType : deviceTypeList) {
            deviceTypeMap.put(deviceType.getId(), deviceType);
        }

        return deviceTypeMap;
    }

    public List<ListDeviceExtendRespVo> listDeviceExtendByDeviceIds(Long tenantId, List<String> deviceIdList) {
        List<ListDeviceExtendRespVo> deviceExtendList = deviceExtendsCoreApi.listDeviceExtends(ListDeviceExtendReq.builder()
                .deviceIds(deviceIdList)
                .tenantId(tenantId)
                .build());
        return deviceExtendList;
    }

    public Map<String, ListDeviceExtendRespVo> getDeviceExtendMapByDeviceIds(Long tenantId, List<String> deviceIdList) {
        Map<String, ListDeviceExtendRespVo> deviceExtendMap = new HashMap<>();
        List<ListDeviceExtendRespVo> deviceExtendList = listDeviceExtendByDeviceIds(tenantId, deviceIdList);
        for (ListDeviceExtendRespVo deviceExtend : deviceExtendList) {
            deviceExtendMap.put(deviceExtend.getDeviceId(), deviceExtend);
        }

        return deviceExtendMap;
    }

    public Map<String, ListUserDeviceInfoRespVo> getListUserDeviceInfoMapByList(List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList) {
        Map<String, ListUserDeviceInfoRespVo> userDeviceMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            for (ListUserDeviceInfoRespVo userDevice : userDeviceInfoRespVoList) {
                userDeviceMap.put(userDevice.getDeviceId(), userDevice);
            }
        }

        return userDeviceMap;
    }

    /**
     * 聚合获取设备详细列表
     *
     * @param tenantId
     * @param userId
     * @param deviceIdList
     * @return
     */
    public List<DeviceResp> findDeviceListByUserId(Long tenantId, Long userId, List<String> deviceIdList) {

        List<DeviceResp> resultDataList = Lists.newArrayList();
        if (tenantId == null) {
            throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
        }
        if (userId == null) {
            throw new BusinessException(DeviceExceptionEnum.USERID_IS_NULL);
        }
        if (CollectionUtils.isEmpty(deviceIdList)) {
            // 没有id 可获取
            log.info("findDeviceListByUserId deviceIds size ==0 ");
            return resultDataList;
        }
        log.info("findDeviceListByUserId-tenantId:{},userId:{}:deviceIds:{}", tenantId, userId, JSON.toJSONString(deviceIdList));
        //1.获取user_device
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = listUserDevicesByDeviceIds(tenantId, userId, deviceIdList);
        if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            log.info("listUserDevicesByDeviceIds deviceIds size ==0 ");
            return resultDataList;
        }
        //2.获取设备明细 device
        Map<String, ListUserDeviceInfoRespVo> userDeviceMap = getListUserDeviceInfoMapByList(userDeviceInfoRespVoList);
        //2.1 获取具体有多少个的deviceIds
        List<String> targetDeviceIds = Lists.newArrayList(userDeviceMap.keySet());
        log.info("targetDeviceIds-size:{},content:{}", targetDeviceIds.size(), JSON.toJSONString(targetDeviceIds));
        //3.返回具体设备信息
        ListCommDeviceReq commReqParams = ListCommDeviceReq.builder()
                .tenantId(tenantId)
                .deviceIds(targetDeviceIds)
                .build();
        resultDataList = deviceCoreApi.findDevRelationListByDeviceIds(commReqParams);

        if (CollectionUtils.isEmpty(resultDataList)) {
            return resultDataList;
        }
        BeanCopyUtils.copyUserDeviceList(userDeviceMap, resultDataList);//设备密码

        return resultDataList;
    }

    private List<Long> getDistinctProductIdList(List<ListDeviceInfoRespVo> deviceInfoList) {
        List<Long> targetProductIds = new ArrayList<>();
        Set<Long> productIdsSet = Sets.newHashSet();
        deviceInfoList.forEach(deviceInfo->{
            Long productId = deviceInfo.getProductId();
            if(productId != null && !productIdsSet.contains(productId)){
                productIdsSet.add(productId);
                targetProductIds.add(productId);
            }
        });

        return targetProductIds;
    }

    private List<Long> getDistinctDeviceTypeIdList(Collection<ListProductRespVo> productInfoList) {
        Set<Long> resDeviceTypeIds = Sets.newHashSet();
        List<Long> targetDeviceTypeIds = new ArrayList<>();
        productInfoList.forEach(product -> {
            Long deviceTypeId = product.getDeviceTypeId();
            if (deviceTypeId != null && !resDeviceTypeIds.contains(deviceTypeId)) {
                resDeviceTypeIds.add(deviceTypeId);
                targetDeviceTypeIds.add(deviceTypeId);
            }
        });
        return targetDeviceTypeIds;
    }

    /**
     * 聚合获取设备详细列表
     *
     * @param tenantId
     * @param userId
     * @return
     */
    public List<DeviceResp> findDeviceListByUserId(Long tenantId, Long userId) {

        List<DeviceResp> resultDataList = Lists.newArrayList();
        if (tenantId == null) {
            throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
        }
        if (userId == null) {
            throw new BusinessException(DeviceExceptionEnum.USERID_IS_NULL);
        }

        log.info("findDeviceListByUserId-tenantId:{},userId:{}:deviceIds:{}", tenantId, userId);
        //1.获取user_device
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = listUserDevices(tenantId, userId);
        if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            log.info("listUserDevicesByDeviceIds deviceIds size ==0 ");
            return resultDataList;
        }
        //2.获取设备明细 device
        Map<String, ListUserDeviceInfoRespVo> userDeviceMap = getListUserDeviceInfoMapByList(userDeviceInfoRespVoList);
        //2.1 获取具体有多少个的deviceIds
        List<String> targetDeviceIds = Lists.newArrayList(userDeviceMap.keySet());
        log.info("targetDeviceIds-size:{},content:{}", targetDeviceIds.size(), JSON.toJSONString(targetDeviceIds));

        //3.返回具体设备信息
        ListCommDeviceReq commReqParams = ListCommDeviceReq.builder()
                .tenantId(tenantId)
                .deviceIds(targetDeviceIds)
                .build();
        resultDataList = deviceCoreApi.findDevRelationListByDeviceIds(commReqParams);

        if (CollectionUtils.isEmpty(resultDataList)) {
            return resultDataList;
        }
        BeanCopyUtils.copyUserDeviceList(userDeviceMap, resultDataList);//设备密码

        return resultDataList;
    }


    /**
     * 聚合获取所有的直连设备详细列表
     *
     * @param tenantId
     * @param userId
     * @return
     */
    public List<DeviceResp> findDirectDeviceListByUserId(Long tenantId, Long userId) {

        List<DeviceResp> resultDataList = Lists.newArrayList();
        if (tenantId == null) {
            throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
        }
        if (userId == null) {
            throw new BusinessException(DeviceExceptionEnum.USERID_IS_NULL);
        }

        log.info("findDeviceListByUserId-tenantId:{},userId:{}:deviceIds:{}", tenantId, userId);
        //1.获取user_device
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = listUserDevices(tenantId, userId);
        if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
            log.info("listUserDevicesByDeviceIds deviceIds size ==0 ");
            return resultDataList;
        }
        //2.获取设备明细 device
        Map<String, ListUserDeviceInfoRespVo> userDeviceMap = getListUserDeviceInfoMapByList(userDeviceInfoRespVoList);
        //2.1 获取具体有多少个的deviceIds
        List<String> targetDeviceIds = Lists.newArrayList(userDeviceMap.keySet());
        log.info("targetDeviceIds-size:{},content:{}", targetDeviceIds.size(), JSON.toJSONString(targetDeviceIds));

        List<ListDeviceInfoRespVo> deviceInfoList = listDevicesByDeviceIds(targetDeviceIds);
        if (CollectionUtils.isEmpty(deviceInfoList)) {
            return resultDataList;
        }
        //过滤出所有的直连设备
        targetDeviceIds = Lists.newArrayList();//重置 所有的目标设备
        Map<String, ListUserDeviceInfoRespVo> targetUserDeviceMap = Maps.newHashMap();
        List<ListDeviceInfoRespVo> directDeviceInfoList = Lists.newArrayList();
        for (ListDeviceInfoRespVo deviceInfo : deviceInfoList) {
            if (deviceInfo.getIsDirectDevice() != null && deviceInfo.getIsDirectDevice() == 1) {
                directDeviceInfoList.add(deviceInfo);
                targetDeviceIds.add(deviceInfo.getUuid());
                ListUserDeviceInfoRespVo userDevice = userDeviceMap.get(deviceInfo.getUuid());
                if (userDevice != null) {
                    targetUserDeviceMap.put(deviceInfo.getUuid(), userDevice);
                }
            }
        }

        BeanCopyUtils.copyDeviceInfoList(directDeviceInfoList, resultDataList);//设备详情
        BeanCopyUtils.copyUserDeviceList(targetUserDeviceMap, resultDataList);//设备密码

        //3.获取设备扩展 deviceExtends
        Map<String, ListDeviceExtendRespVo> deviceExtendMap = getDeviceExtendMapByDeviceIds(tenantId, targetDeviceIds);
        BeanCopyUtils.copyDeviceExtendList(deviceExtendMap, resultDataList);//设备p2p id

        //4.获取设备状态
        Map<String, Map<String, Object>> deviceStateMap = listDeviceStateByDeviceIds(tenantId, targetDeviceIds);
        BeanCopyUtils.copyDeviceStateList(deviceStateMap, resultDataList);//设备状态
        //4.1 设备状态【上下线】
        Map<String, ListDeviceStatusRespVo> deviceStatusMap = getDeviceStatusMapByDeviceIs(tenantId, targetDeviceIds);
        BeanCopyUtils.copyDeviceStatusList(deviceStatusMap, resultDataList);//设备上下线状态

        //5.获取设备对应产品
        List<Long> targetProductIds = getDistinctProductIdList(deviceInfoList);
        Map<Long, ListProductRespVo> productInfoMap = getProductMapByProductIds(targetProductIds);
        BeanCopyUtils.copyProductList(productInfoMap, resultDataList);//产品信息

        Set<Long> resDeviceTypeIds = Sets.newHashSet();
        productInfoMap.values().forEach(product -> {
            if (product.getDeviceTypeId() != null) {
                resDeviceTypeIds.add(product.getDeviceTypeId());
            }
        });
        //6.获取设备对应的设备类型
        List<Long> targetDeviceTypeIds = getDistinctDeviceTypeIdList(productInfoMap.values());
        Map<Long, ListDeviceTypeRespVo> deviceTypeMap = getDeviceTypeMapByIds(targetDeviceTypeIds);
        BeanCopyUtils.copyDeviceTypeList(deviceTypeMap, resultDataList);//设备类型

        return resultDataList;
    }

    public List<DeviceResp> findDeviceListByDeviceIds(Long tenantId, List<String> deviceIdList) {

        List<DeviceResp> resultDataList = Lists.newArrayList();
        if (tenantId == null) {
            throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
        }

        if (CollectionUtils.isEmpty(deviceIdList)) {
            // 没有id 可获取
            log.info("findDeviceListByUserId deviceIds size ==0 ");
            return resultDataList;
        }

        log.info("targetDeviceIds-size:{},content:{}", deviceIdList.size(), JSON.toJSONString(deviceIdList));
        List<ListDeviceInfoRespVo> deviceInfoList = listDevicesByDeviceIds(deviceIdList);
        if (CollectionUtils.isEmpty(deviceInfoList)) {
            return resultDataList;
        }
        BeanCopyUtils.copyDeviceInfoList(deviceInfoList, resultDataList);//设备详情

        //3.获取设备扩展 deviceExtends
        Map<String, ListDeviceExtendRespVo> deviceExtendMap = getDeviceExtendMapByDeviceIds(tenantId, deviceIdList);
        BeanCopyUtils.copyDeviceExtendList(deviceExtendMap, resultDataList);//设备p2p id

        //4.获取设备状态
        Map<String, Map<String, Object>> deviceStateMap = listDeviceStateByDeviceIds(tenantId, deviceIdList);
        BeanCopyUtils.copyDeviceStateList(deviceStateMap, resultDataList);//设备状态

        //4.1 设备状态【上下线】
        Map<String, ListDeviceStatusRespVo> deviceStatusMap = getDeviceStatusMapByDeviceIs(tenantId, deviceIdList);
        BeanCopyUtils.copyDeviceStatusList(deviceStatusMap, resultDataList);//设备上下线状态

        //5.获取设备对应产品
        List<Long> targetProductIds = getDistinctProductIdList(deviceInfoList);
        Map<Long, ListProductRespVo> productInfoMap = getProductMapByProductIds(targetProductIds);
        BeanCopyUtils.copyProductList(productInfoMap, resultDataList);//产品信息

        //6.获取设备对应的设备类型
        List<Long> targetDeviceTypeIds = getDistinctDeviceTypeIdList(productInfoMap.values());
        Map<Long, ListDeviceTypeRespVo> deviceTypeMap = getDeviceTypeMapByIds(targetDeviceTypeIds);
        BeanCopyUtils.copyDeviceTypeList(deviceTypeMap, resultDataList);//设备类型

        return resultDataList;
    }

    public void saveOrUpdateDeviceStates(List<UpdateDeviceStateReq> deviceStateList) {
        if (CollectionUtils.isEmpty(deviceStateList)) {
            return;
        }
        deviceStateCoreApi.saveOrUpdateBatch(deviceStateList);
    }

    public void cancelUser(Long userId, String userUuid, Long tenantId){
        //通过用户id 看看是否有直连设备存在
        List<DeviceResp> deviceResps=this.findDirectDeviceListByUserId(tenantId,userId);
        //当该用户不存在设备时，直接删除用户表
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(deviceResps)){
            for(DeviceResp resp:deviceResps){
                String topic="iot/v1/s/"+userUuid+"/device/devUnbindReq";
                MqttMsg mqttMsg=new MqttMsg();
                Map<String, Object> payload = new HashMap<>();
                mqttMsg.setTopic(topic);
                mqttMsg.setMethod("devUnbindReq");
                mqttMsg.setService("device");
                mqttMsg.setSrcAddr(userUuid);
                mqttMsg.setSeq("123456789");
                payload.put("unbindDevId",resp.getDeviceId());
                payload.put("unbindUserId",userUuid);
                payload.put("resetFlag",0);
                mqttMsg.setPayload(payload);
               deviceMQTTService.devUnbindReq(mqttMsg,topic);
            }
        }
    }

}
