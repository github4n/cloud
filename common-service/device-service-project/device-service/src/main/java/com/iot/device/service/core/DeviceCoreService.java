//package com.iot.device.service.core;
//
//import com.alibaba.fastjson.JSON;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.iot.common.exception.BusinessException;
//import com.iot.common.util.StringUtil;
//import com.iot.device.core.service.DeviceExtendServiceCoreUtils;
//import com.iot.device.core.service.DeviceServiceCoreUtils;
//import com.iot.device.core.service.DeviceStatusServiceCoreUtils;
//import com.iot.device.core.service.DeviceTypeServiceCoreUtils;
//import com.iot.device.core.service.ProductServiceCoreUtils;
//import com.iot.device.core.service.UserDeviceServiceCoreUtils;
//import com.iot.device.core.utils.BeanCoreCopyUtils;
//import com.iot.device.exception.DeviceExceptionEnum;
//import com.iot.device.model.Device;
//import com.iot.device.model.DeviceExtend;
//import com.iot.device.model.DeviceStatus;
//import com.iot.device.model.DeviceType;
//import com.iot.device.model.Product;
//import com.iot.device.model.UserDevice;
//import com.iot.device.service.IDeviceExtendService;
//import com.iot.device.service.IDeviceService;
//import com.iot.device.service.IDeviceStateService;
//import com.iot.device.service.IDeviceStatusService;
//import com.iot.device.service.IUserDeviceService;
//import com.iot.device.vo.rsp.DeviceResp;
//import com.iot.util.AssertUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author: xfz
// * @Descrpiton:
// * @Date: 18:05 2018/6/20
// * @Modify by:
// */
//@Service
//public class DeviceCoreService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCoreService.class);
//
//    @Autowired
//    private IDeviceService deviceService;
//    @Autowired
//    private IDeviceStatusService deviceStatusService;
//    @Autowired
//    private IDeviceExtendService deviceExtendService;
//    @Autowired
//    private IUserDeviceService userDeviceService;
//
//
//    @Autowired
//    private IDeviceStateService deviceStateService;
//
//    /**
//     * 删除用户跟设备的关系、清空设备的parent_id
//     *
//     * @param userId
//     * @param deviceId
//     * @return
//     * @author lucky
//     * @date 2018/6/21 9:43
//     */
//    @Transactional
//    public void delDevByUserIdAndDeviceId(Long userId, String deviceId) {
//        AssertUtils.notEmpty(deviceId, "deviceId.notnull");
//        AssertUtils.notEmpty(userId, "userId.notnull");
//        //get cache or db
//        Device orig = DeviceServiceCoreUtils.getDeviceInfoByDeviceId(deviceId);
//        if (orig == null) {
//            throw new BusinessException(DeviceExceptionEnum.DEVICE_NOT_EXIST);
//        }
//        //原 parent device id
//        String origParentDeviceId = orig.getParentId();
//
//        //delete user_device relationship
//        UserDevice userDevice = UserDeviceServiceCoreUtils.getUserDeviceByUserId(userId, deviceId);
//        if (userDevice != null) {
//            userDeviceService.deleteById(userDevice.getId());
//
//            //remove cache user device
//            UserDeviceServiceCoreUtils.removeAllCacheUserDeviceByUserId(userId);
//            UserDeviceServiceCoreUtils.removeAllCacheUserDeviceByDeviceId(deviceId);
//        }
//        orig.setParentId(null);
//        orig.setName(StringUtil.EMPTY);
//        orig.setLastUpdateDate(new Date());
//        deviceService.updateAllColumnById(orig);
//
//        //remove device cache
//        DeviceServiceCoreUtils.removeDeviceByDeviceId(orig.getUuid());
//        //clear child device cache
//        DeviceServiceCoreUtils.removeCacheParentDeviceId(origParentDeviceId);
//    }
//
//
//    /**
//     * 获取设备列表
//     *
//     * @param userId
//     * @param deviceIds
//     * @return
//     * @author lucky
//     * @date 2018/7/12 11:10
//     */
//    public List<DeviceResp> findDeviceListByUserId(Long tenantId, Long userId, List<String> deviceIds) {
//        Date startTime = new Date();
//        LOGGER.info("findDeviceListByUserId-begin: startTime{}", DateFormatUtils.format(startTime, "yyyy-MM-dd HH:mm:ss"));
//        if (userId == null || userId <= 0) {
//            return null;
//        }
//
//
//        //1.获取用户对应的设备缓存
//        List<UserDevice> userToDeviceList = UserDeviceServiceCoreUtils.findUserDevicesByUserId(userId);
//        LOGGER.info("UserDeviceServiceCoreUtils-findDeviceIdsByUserId-user, {}", userToDeviceList);
//        if (CollectionUtils.isEmpty(userToDeviceList)) {
//            return null;
//        }
//        // get user all device ids
//        List<String> deviceIdList = Lists.newArrayList();
//        for (UserDevice userDevice : userToDeviceList) {
//            deviceIdList.add(userDevice.getDeviceId());
//        }
//        //get user deviceIds info
//        List<Device> userDeviceList = DeviceServiceCoreUtils.findDeviceListByDeviceIds(deviceIdList);
//        LOGGER.info("DeviceServiceCoreUtils-findDeviceListByDeviceIds, {}", userDeviceList);
//        if (CollectionUtils.isEmpty(userDeviceList)) {
//            return null;
//        }
//        List<DeviceResp> deviceRespList = Lists.newArrayList();
//        List<Device> tempDeviceList = Lists.newArrayList();
//        List<String> tempDeviceIds = Lists.newArrayList();
//        List<Long> tempProductIds = Lists.newArrayList();
//
//        //获取指定所有设备的缓存
//        List<Device> deviceList = null;
//        if (!CollectionUtils.isEmpty(deviceIds)) {
//            //get deviceIds info
//            deviceList = DeviceServiceCoreUtils.findDeviceListByDeviceIds(deviceIds);
//            LOGGER.info("DeviceServiceCoreUtils-findDeviceListByDeviceIds-deviceIds, {}", deviceList);
//        }
//
//        if (CollectionUtils.isEmpty(deviceList)) {
//            for (Device uDevice : userDeviceList) {
//                tempDeviceList.add(uDevice);
//                tempDeviceIds.add(uDevice.getUuid());
//                if (uDevice.getProductId() == null) {
//                    continue;
//                }
//                if (!tempProductIds.contains(uDevice.getProductId())) {
//                    tempProductIds.add(uDevice.getProductId());
//                }
//            }
//        } else {
//            Map<String, Device> deviceMap = Maps.newHashMap();
//            for (Device uDevice : userDeviceList) {
//                deviceMap.put(uDevice.getUuid(), uDevice);
//            }
//            for (Device device : deviceList) {
//                Device deviceTemp = deviceMap.get(device.getUuid());
//                if (deviceTemp == null) {
//                    continue;
//                }
//                tempDeviceList.add(device);
//                tempDeviceIds.add(device.getUuid());
//                if (device.getProductId() == null) {
//                    continue;
//                }
//                if (!tempProductIds.contains(device.getProductId())) {
//                    tempProductIds.add(device.getProductId());
//                }
//            }
//        }
//        //设备详情
//        BeanCoreCopyUtils.copyDeviceList(tempDeviceList, deviceRespList);
//
//        //返回产品的 对应 类型的 devType
//        if (!CollectionUtils.isEmpty(tempProductIds)) {
//            // 获取产品+device_type 的 type
//            List<Product> productList = ProductServiceCoreUtils.getProductsByProductIds(tempProductIds);
//            List<Long> deviceTypeIds = Lists.newArrayList();
//            if (!CollectionUtils.isEmpty(productList)) {
//                for (Product product : productList) {
//                    if (product.getDeviceTypeId() == null) {
//                        continue;
//                    }
//                    deviceTypeIds.add(product.getDeviceTypeId());
//                }
//                BeanCoreCopyUtils.copyProductList(productList, deviceRespList);
//            }
//
//            if (!CollectionUtils.isEmpty(deviceTypeIds)) {
//                List<DeviceType> deviceTypeList =
//                        DeviceTypeServiceCoreUtils.getDeviceTypeListByIds(deviceTypeIds);
//                // copy device type 到 devType
//                BeanCoreCopyUtils.copyDeviceTypeList(deviceTypeList, deviceRespList);
//            }
//        }
//
//        //设置state 属性
//        int i = 0;
//        for (DeviceResp device : deviceRespList) {
//            Map<String, Object> stateProperty = deviceStateService.findDeviceStateListByDeviceId(device.getDeviceId());
//            device.setStateProperty(stateProperty);
//            deviceRespList.set(i, device);
//            i++;
//        }
//
//        //增加密码返回
//        BeanCoreCopyUtils.copyUserDeviceList(userToDeviceList, deviceRespList);
//
//        if (!CollectionUtils.isEmpty(tempDeviceIds)) {
//            List<DeviceStatus> deviceStatusList = DeviceStatusServiceCoreUtils.findDeviceStatusListByDeviceIds(tenantId, tempDeviceIds);
//            BeanCoreCopyUtils.copyDeviceStatusList(deviceStatusList, deviceRespList);
//        }
//
//
//        //增加 p2pId 返回
//        if (!CollectionUtils.isEmpty(tempDeviceIds)) {
//            List<DeviceExtend> deviceExtendList = DeviceExtendServiceCoreUtils.findListByDeviceIds(tempDeviceIds);
//            BeanCoreCopyUtils.copyDeviceExtendList(deviceExtendList, deviceRespList);
//        }
//
//        Date endTime = new Date();
//        LOGGER.info("findDeviceListByUserId-end: return all{},startTime{},millSeconds:{}", JSON.toJSONString(deviceRespList), DateFormatUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"), endTime.getTime() - startTime.getTime());
//        LOGGER.info("findDeviceListByUserId-end--return all{}", JSON.toJSONString(deviceRespList));
//        return deviceRespList;
//    }
//}
