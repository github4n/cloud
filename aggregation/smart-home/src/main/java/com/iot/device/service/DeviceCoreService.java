//package com.iot.device.service;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import com.iot.common.exception.BusinessException;
//import com.iot.common.helper.Page;
//import com.iot.common.util.StringUtil;
//import com.iot.control.device.api.UserDeviceCoreApi;
//import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
//import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
//import com.iot.control.device.vo.req.PageUserDeviceInfoReq;
//import com.iot.control.device.vo.req.UpdateUserDeviceInfoReq;
//import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
//import com.iot.control.device.vo.resp.PageUserDeviceInfoRespVo;
//import com.iot.control.device.vo.resp.UpdateUserDeviceInfoResp;
//import com.iot.device.api.DeviceCoreApi;
//import com.iot.device.api.DeviceExtendsCoreApi;
//import com.iot.device.api.DeviceStateCoreApi;
//import com.iot.device.api.DeviceStatusCoreApi;
//import com.iot.device.api.DeviceTypeCoreApi;
//import com.iot.device.api.ProductCoreApi;
//import com.iot.device.exception.DeviceExceptionEnum;
//import com.iot.device.exception.ProductExceptionEnum;
//import com.iot.device.utils.BeanCoreCopyUtils;
//import com.iot.device.vo.req.device.ListDeviceExtendReq;
//import com.iot.device.vo.req.device.ListDeviceInfoReq;
//import com.iot.device.vo.req.device.ListDeviceStateReq;
//import com.iot.device.vo.req.device.ListDeviceTypeReq;
//import com.iot.device.vo.req.device.ListProductInfoReq;
//import com.iot.device.vo.req.device.UpdateDeviceExtendReq;
//import com.iot.device.vo.req.device.UpdateDeviceInfoReq;
//import com.iot.device.vo.req.device.UpdateDeviceStatusReq;
//import com.iot.device.vo.rsp.DeviceResp;
//import com.iot.device.vo.rsp.device.GetDeviceExtendInfoRespVo;
//import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
//import com.iot.device.vo.rsp.device.GetDeviceTypeInfoRespVo;
//import com.iot.device.vo.rsp.device.GetProductInfoRespVo;
//import com.iot.device.vo.rsp.device.ListDeviceExtendRespVo;
//import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
//import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;
//import com.iot.device.vo.rsp.device.ListDeviceTypeRespVo;
//import com.iot.device.vo.rsp.device.ListProductRespVo;
//import com.iot.util.AssertUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * @Author: lucky
// * @Descrpiton:
// * @Date: 16:29 2018/10/11
// * @Modify by:
// */
//@Slf4j
//@Component
//public class DeviceCoreService {
//
//    @Autowired
//    private UserDeviceCoreApi userDeviceCoreApi;
//
//    @Autowired
//    private DeviceCoreApi deviceCoreApi;
//
//    @Autowired
//    private DeviceExtendsCoreApi deviceExtendsCoreApi;
//
//    @Autowired
//    private DeviceStatusCoreApi deviceStatusCoreApi;
//
//    @Autowired
//    private DeviceStateCoreApi deviceStateCoreApi;
//
//    @Autowired
//    private ProductCoreApi productCoreApi;
//
//    @Autowired
//    private DeviceTypeCoreApi deviceTypeCoreApi;
//
//    public Long getRootUserIdByDeviceId(Long tenantId, String deviceId) {
//        AssertUtils.notNull(tenantId, "tenantId.notnull");
//        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
//        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
//                .tenantId(tenantId)
//                .userId(null)
//                .deviceId(deviceId).build();
//        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
//        if (!CollectionUtils.isEmpty(userDeviceInfoList)) {
//            return userDeviceInfoList.get(0).getUserId();
//        }
//        return null;
//    }
//
//
//    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, Long userId) {
//        AssertUtils.notNull(tenantId, "tenantId.notnull");
//        AssertUtils.notNull(userId, "userId.notnull");
//
//        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
//                .tenantId(tenantId)
//                .userId(userId)
//                .deviceId(null).build();
//        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
//        return userDeviceInfoList;
//    }
//
//    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, String deviceId) {
//        AssertUtils.notNull(tenantId, "tenantId.notnull");
//        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
//        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
//                .tenantId(tenantId)
//                .userId(null)
//                .deviceId(deviceId).build();
//        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
//        return userDeviceInfoList;
//    }
//
//    public List<ListUserDeviceInfoRespVo> listUserDevices(Long tenantId, Long userId, String deviceId) {
//        AssertUtils.notNull(tenantId, "tenantId.notnull");
//        AssertUtils.notNull(userId, "userId.notnull");
//        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
//        ListUserDeviceInfoReq params = ListUserDeviceInfoReq.builder()
//                .tenantId(tenantId)
//                .userId(userId)
//                .deviceId(deviceId).build();
//        List<ListUserDeviceInfoRespVo> userDeviceInfoList = userDeviceCoreApi.listUserDevice(params);
//        return userDeviceInfoList;
//    }
//
//    public List<ListUserDeviceInfoRespVo> listBatchUserDevices(Long tenantId, Long userId, List<String> deviceIds) {
//        List<ListUserDeviceInfoRespVo> resultDataList = userDeviceCoreApi.listBatchUserDevice(GetUserDeviceInfoReq.builder()
//                .tenantId(tenantId)
//                .userId(userId)
//                .deviceIds(deviceIds)
//                .build());
//        return resultDataList;
//    }
//
//    public UpdateUserDeviceInfoResp saveOrUpdateUserDevice(UpdateUserDeviceInfoReq updateUserDeviceParam) {
//        //检查 user_device 是否存在
//        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = listUserDevices(updateUserDeviceParam.getTenantId(), updateUserDeviceParam.getDeviceId());
//        if (!CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
//            updateUserDeviceParam.setId(userDeviceInfoRespVoList.get(0).getId());//修改id必须传
//        } else {
//            updateUserDeviceParam.setPassword(StringUtil.getRandomString(12));
//        }
//        UpdateUserDeviceInfoResp userDeviceInfoResp = userDeviceCoreApi.saveOrUpdate(updateUserDeviceParam);
//        return userDeviceInfoResp;
//    }
//
//
//    public void delUserDeviceParams(Long tenantId, Long userId, String deviceId) {
//        //删除user_device关系
//        userDeviceCoreApi.delUserDevice(tenantId, userId, deviceId);
//        //清空 device parentId
//    }
//
//
//    public GetProductInfoRespVo getProductByDeviceId(String deviceId) {
//        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
//        if (deviceInfo == null) {
//            throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
//        }
//        GetProductInfoRespVo productInfo = productCoreApi.getByProductId(deviceInfo.getProductId());
//        if (productInfo == null) {
//            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
//        }
//        return productInfo;
//    }
//
//    public GetDeviceTypeInfoRespVo getDeviceTypeByDeviceId(String deviceId) {
//        GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(deviceId);
//        if (deviceInfo == null) {
//            throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
//        }
//        GetProductInfoRespVo productInfo = this.getProductById(deviceInfo.getProductId());
//        if (productInfo == null) {
//            throw new BusinessException(ProductExceptionEnum.PRODUCT_NOT_EXIST);
//        }
//
//        GetDeviceTypeInfoRespVo deviceTypeInfo = this.getDeviceTypeById(productInfo.getDeviceTypeId());
//        return deviceTypeInfo;
//    }
//
//    public GetProductInfoRespVo getProductById(Long productId) {
//        GetProductInfoRespVo productInfo = productCoreApi.getByProductId(productId);
//        return productInfo;
//    }
//
//    public GetDeviceTypeInfoRespVo getDeviceTypeById(Long deviceTypeId) {
//        GetDeviceTypeInfoRespVo deviceTypeInfo = deviceTypeCoreApi.get(deviceTypeId);
//        return deviceTypeInfo;
//    }
//
//    public List<ListDeviceInfoRespVo> listDevicesByParentId(String parentDeviceId) {
//        return deviceCoreApi.listDevicesByParentId(parentDeviceId);
//    }
//
//    public GetProductInfoRespVo getByProductModel(String productModel) {
//        return productCoreApi.getByProductModel(productModel);
//    }
//
//    public GetDeviceInfoRespVo getDeviceInfoByDeviceId(String deviceId) {
//        return deviceCoreApi.get(deviceId);
//    }
//
//    public GetDeviceInfoRespVo saveOrUpdateDeviceInfo(UpdateDeviceInfoReq deviceInfoParam) {
//        return deviceCoreApi.saveOrUpdate(deviceInfoParam);
//    }
//
//    public void saveOrUpdateDeviceStatus(UpdateDeviceStatusReq deviceStatusParam) {
//        deviceStatusCoreApi.saveOrUpdate(deviceStatusParam);
//    }
//
//    public GetDeviceExtendInfoRespVo getDeviceExtendByDeviceId(Long tenantId, String deviceId) {
//        return deviceExtendsCoreApi.get(tenantId, deviceId);
//    }
//
//    public void saveOrupdateExtend(UpdateDeviceExtendReq updateDeviceExtendReq) {
//        deviceExtendsCoreApi.saveOrUpdate(updateDeviceExtendReq);
//    }
//
//    public void delChildDeviceByDeviceId(Long tenantId, String subDevId) {
//        //清除 subDevice parentId
//        UpdateDeviceInfoReq deviceInfoReq = UpdateDeviceInfoReq.builder()
//                .tenantId(tenantId)
//                .uuid(subDevId)
//                .parentId("")
//                .build();
//        deviceCoreApi.saveOrUpdate(deviceInfoReq);
//
//        userDeviceCoreApi.delUserDevice(tenantId, null, subDevId);
//    }
//
//    public List<ListUserDeviceInfoRespVo> listUserDevicesByDeviceIds(Long tenantId, Long userId, List<String> deviceIdList) {
//
//        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = userDeviceCoreApi.listBatchUserDevice(GetUserDeviceInfoReq.builder()
//                .deviceIds(deviceIdList)
//                .userId(userId)
//                .tenantId(tenantId).build());
//
//        return userDeviceInfoRespVoList;
//    }
//
//    public Page<PageUserDeviceInfoRespVo> findPageUserDevice(PageUserDeviceInfoReq page) {
//        Page<PageUserDeviceInfoRespVo> resultUserDevicePage = userDeviceCoreApi.pageUserDevice(page);
//        return resultUserDevicePage;
//    }
//
//
//    public List<ListDeviceInfoRespVo> listDevicesByDeviceIds(List<String> deviceIdList) {
//        List<ListDeviceInfoRespVo> resultDeviceList = deviceCoreApi.listDevices(ListDeviceInfoReq.builder()
//                .deviceIds(deviceIdList).build());
//        return resultDeviceList;
//    }
//
//    public List<ListDeviceStatusRespVo> listDeviceStatusByDeviceIds(Long tenantId, List<String> deviceIdList) {
//
//        return deviceStatusCoreApi.listDeviceStatus(ListDeviceStateReq.builder()
//                .tenantId(tenantId)
//                .deviceIds(deviceIdList)
//                .build());
//    }
//
//    public Map<String, Map<String, Object>> listDeviceStateByDeviceIds(Long tenantId, List<String> deviceIdList) {
//
//        return deviceStateCoreApi.listStates(ListDeviceStateReq.builder()
//                .tenantId(tenantId)
//                .deviceIds(deviceIdList)
//                .build());
//    }
//
//
//    public List<ListProductRespVo> listProductByProductIds(List<Long> productIds) {
//
//        List<ListProductRespVo> productRespVoList = productCoreApi.listProducts(ListProductInfoReq.builder()
//                .productIds(productIds)
//                .build());
//        return productRespVoList;
//    }
//
//    public List<ListDeviceTypeRespVo> listDeviceTypeByDeviceTypeIds(List<Long> deviceTypeIds) {
//        List<ListDeviceTypeRespVo> deviceTypeRespVoList = deviceTypeCoreApi.listDeviceType(ListDeviceTypeReq.builder()
//                .deviceTypeIds(deviceTypeIds)
//                .build());
//        return deviceTypeRespVoList;
//    }
//
//    public List<ListDeviceExtendRespVo> listDeviceExtendByDeviceIds(Long tenantId, List<String> deviceIdList) {
//        List<ListDeviceExtendRespVo> deviceExtendList = deviceExtendsCoreApi.listDeviceExtends(ListDeviceExtendReq.builder()
//                .deviceIds(deviceIdList)
//                .tenantId(tenantId)
//                .build());
//        return deviceExtendList;
//    }
//
//    /**
//     * 聚合获取设备详细列表
//     *
//     * @param tenantId
//     * @param userId
//     * @return
//     */
//    public List<DeviceResp> findDirectDeviceListByUserId(Long tenantId, Long userId) {
//
//        List<DeviceResp> resultDataList = Lists.newArrayList();
//        if (tenantId == null) {
//            throw new BusinessException(DeviceExceptionEnum.TENANTID_IS_NULL);
//        }
//        if (userId == null) {
//            throw new BusinessException(DeviceExceptionEnum.USERID_IS_NULL);
//        }
//
//        log.info("findDirectDeviceListByUserId-tenantId:{},userId:{}", tenantId, userId);
//        //1.获取user_device
//        List<ListUserDeviceInfoRespVo> userDeviceInfoRespVoList = listUserDevices(tenantId, userId);
//        if (CollectionUtils.isEmpty(userDeviceInfoRespVoList)) {
//            log.info("listUserDevicesByDeviceIds deviceIds size ==0 ");
//            return resultDataList;
//        }
//        //2.获取设备明细 device
//        Map<String, ListUserDeviceInfoRespVo> userDeviceMap = userDeviceInfoRespVoList.stream().collect(Collectors.toMap(ListUserDeviceInfoRespVo::getDeviceId, userDevice -> userDevice));
//        //2.1 获取具体有多少个的deviceIds
//        List<String> targetDeviceIds = Lists.newArrayList(userDeviceMap.keySet());
//        log.info("targetDeviceIds-size:{},content:{}", targetDeviceIds.size(), JSON.toJSONString(targetDeviceIds));
//        List<ListDeviceInfoRespVo> deviceInfoList = listDevicesByDeviceIds(targetDeviceIds);
//        if (CollectionUtils.isEmpty(deviceInfoList)) {
//            return resultDataList;
//        }
//        //过滤出所有的直连设备
//        targetDeviceIds = Lists.newArrayList();//重置 所有的目标设备
//        Map<String, ListUserDeviceInfoRespVo> targetUserDeviceMap = Maps.newHashMap();
//        List<ListDeviceInfoRespVo> directDeviceInfoList = Lists.newArrayList();
//        for (ListDeviceInfoRespVo deviceInfo : deviceInfoList) {
//            if (deviceInfo.getIsDirectDevice() != null && deviceInfo.getIsDirectDevice() == 1) {
//                directDeviceInfoList.add(deviceInfo);
//                targetDeviceIds.add(deviceInfo.getUuid());
//                ListUserDeviceInfoRespVo userDevice = userDeviceMap.get(deviceInfo.getUuid());
//                if (userDevice != null) {
//                    targetUserDeviceMap.put(deviceInfo.getUuid(), userDevice);
//                }
//            }
//        }
//        BeanCoreCopyUtils.copyDeviceInfoList(directDeviceInfoList, resultDataList);//设备详情
//        BeanCoreCopyUtils.copyUserDeviceList(targetUserDeviceMap, resultDataList);//设备密码
//
//        //3.获取设备扩展 deviceExtends
//        List<ListDeviceExtendRespVo> deviceExtendList = listDeviceExtendByDeviceIds(tenantId, targetDeviceIds);
//        Map<String, ListDeviceExtendRespVo> deviceExtendMap = Maps.newHashMap();
//        if (!CollectionUtils.isEmpty(deviceExtendList)) {
//            deviceExtendMap = deviceExtendList.stream().collect(Collectors.toMap(ListDeviceExtendRespVo::getDeviceId, deviceExtend -> deviceExtend));
//        }
//        BeanCoreCopyUtils.copyDeviceExtendList(deviceExtendMap, resultDataList);//设备p2p id
//
//        //4.获取设备状态
//        Map<String, Map<String, Object>> deviceStateMap = listDeviceStateByDeviceIds(tenantId, targetDeviceIds);
//        BeanCoreCopyUtils.copyDeviceStateList(deviceStateMap, resultDataList);//设备状态
//
//        //4.1 设备状态【上下线】
//        Map<String, ListDeviceStatusRespVo> deviceStatusMap = Maps.newHashMap();
//        List<ListDeviceStatusRespVo> deviceStatusList = listDeviceStatusByDeviceIds(tenantId, targetDeviceIds);
//        if (!CollectionUtils.isEmpty(deviceStatusList)) {
//            deviceStatusMap = deviceStatusList.stream().collect(Collectors.toMap(ListDeviceStatusRespVo::getDeviceId, deviceStatus -> deviceStatus));
//        }
//        BeanCoreCopyUtils.copyDeviceStatusList(deviceStatusMap, resultDataList);
//        //5.获取设备对应产品
//        Set<Long> productIdsSet = Sets.newHashSet();
//        deviceInfoList.forEach(deviceInfo -> {
//            if (deviceInfo.getProductId() != null) {
//                productIdsSet.add(deviceInfo.getProductId());
//            }
//        });
//        List<Long> targetProductIds = Lists.newArrayList(productIdsSet);
//        List<ListProductRespVo> productInfoList = listProductByProductIds(targetProductIds);
//        if (CollectionUtils.isEmpty(productInfoList)) {
//            return resultDataList;
//        }
//        Map<Long, ListProductRespVo> productInfoMap = productInfoList.stream().collect(Collectors.toMap(ListProductRespVo::getId, productInfo -> productInfo));
//        BeanCoreCopyUtils.copyProductList(productInfoMap, resultDataList);//产品信息
//
//        Set<Long> resDeviceTypeIds = Sets.newHashSet();
//        productInfoMap.values().forEach(product -> {
//            if (product.getDeviceTypeId() != null) {
//                resDeviceTypeIds.add(product.getDeviceTypeId());
//            }
//        });
//        //6.获取设备对应的设备类型
//        List<Long> targetDeviceTypeIds = Lists.newArrayList(resDeviceTypeIds);
//        List<ListDeviceTypeRespVo> deviceTypeList = listDeviceTypeByDeviceTypeIds(targetDeviceTypeIds);
//        if (CollectionUtils.isEmpty(deviceTypeList)) {
//            return resultDataList;
//        }
//        Map<Long, ListDeviceTypeRespVo> deviceTypeMap = deviceTypeList.stream().collect(Collectors.toMap(ListDeviceTypeRespVo::getId, deviceType -> deviceType));
//        BeanCoreCopyUtils.copyDeviceTypeList(deviceTypeMap, resultDataList);//设备类型
//
//        return resultDataList;
//    }
//}
