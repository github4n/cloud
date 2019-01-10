package com.iot.device.business.core;

import com.google.common.collect.Lists;
import com.iot.device.business.DeviceBusinessService;
import com.iot.device.business.DeviceExtendBusinessService;
import com.iot.device.business.DeviceStateBusinessService;
import com.iot.device.business.DeviceStatusBusinessService;
import com.iot.device.model.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: lucky
 * @Descrpiton: 聚合
 * @Date: 16:24 2018/10/8
 * @Modify by:
 */

@Slf4j
@Component
public class DeviceCoreBusinessService {

    @Autowired
    private DeviceBusinessService deviceBusinessService;

    @Autowired
    private DeviceExtendBusinessService deviceExtendBusinessService;

    @Autowired
    private DeviceStateBusinessService deviceStateBusinessService;

    @Autowired
    private DeviceStatusBusinessService deviceStatusBusinessService;

//    public List<DeviceInfoListResp> listDevices(ListDeviceInfoReq params) {
//
//        if (CollectionUtils.isEmpty(params.getDeviceIds())) {
//            log.info("当前请求,没有设定查询的deviceId集合");
//            return Lists.newArrayList();
//        }
//        List<DeviceInfoListResp> resultList = Lists.newArrayList();
//        Map<String, DeviceInfoListResp> tempMap = Maps.newHashMap();
//        params.getDeviceIds().forEach(deviceId -> {
//            tempMap.put(deviceId, new DeviceInfoListResp());
//        });
//
//        List<Long> productIds = Lists.newArrayList();
//        //返回device
//        if (params.getResultTypeEnums().contains(DeviceResultTypeEnum.DEVICE_INFO)) {
//            Map<String, CommDeviceInfoResp> tempDeviceMap = Maps.newHashMap();
//            List<Device> deviceList = deviceBusinessService.listBatchDevices(null, params.getDeviceIds(), params.getBatchSize());
//            if (!CollectionUtils.isEmpty(deviceList)) {
//                deviceList.forEach(orig -> {
//                    productIds.add(orig.getProductId());
//                    CommDeviceInfoResp dest = new CommDeviceInfoResp();
//                    BeanUtils.copyProperties(dest, orig);
//                    tempDeviceMap.put(orig.getUuid(), dest);
//                });
//                tempDeviceMap.keySet().forEach(deviceId -> {
//                    CommDeviceInfoResp dest = tempDeviceMap.get(deviceId);
//                    DeviceInfoListResp target = tempMap.get(deviceId);
//                    target.setDeviceInfo(dest);
//                    tempMap.put(deviceId, target);
//                });
//            }
//        }
//        //返回deviceExtend
//        if (params.getResultTypeEnums().contains(DeviceResultTypeEnum.DEVICE_EXTEND)) {
//            Map<String, CommDeviceExtendResp> tempDeviceMap = Maps.newHashMap();
//            List<DeviceExtend> deviceExtendList = deviceExtendBusinessService.listBatchDeviceExtends(null, params.getDeviceIds(), params.getBatchSize());
//            if (!CollectionUtils.isEmpty(deviceExtendList)) {
//                deviceExtendList.forEach(orig -> {
//                    CommDeviceExtendResp dest = new CommDeviceExtendResp();
//                    BeanUtils.copyProperties(dest, orig);
//                    tempDeviceMap.put(orig.getDeviceId(), dest);
//                });
//                tempDeviceMap.keySet().forEach(deviceId -> {
//                    CommDeviceExtendResp dest = tempDeviceMap.get(deviceId);
//                    DeviceInfoListResp target = tempMap.get(deviceId);
//                    target.setDeviceExtend(dest);
//                    tempMap.put(deviceId, target);
//                });
//            }
//        }
//
//        return null;
//    }

    public List<String> deleteByDeviceId(String deviceId) {
        List<String> resultDeleteIds = Lists.newArrayList();
        Device device = deviceBusinessService.getDevice(null, deviceId);
        if (device != null) {
            if (StringUtils.isEmpty(device.getParentId())) {//他的parentId 为空 说明当前为直连设备 删除直连必须将子设备也一并清理掉
                //删除子设备
                List<Device> deviceChildList = deviceBusinessService.findDevicesByParentDeviceId(null, device.getUuid());
                if (!CollectionUtils.isEmpty(deviceChildList)) {
                    deviceChildList.forEach(childDevice -> {
                        Long tenantId = childDevice.getTenantId();
                        String childDeviceId = childDevice.getUuid();
                        commDeleteByDeviceId(tenantId, childDeviceId);
                        resultDeleteIds.add(childDeviceId);
                    });
                }
            }
            Long tenantId = device.getTenantId();
            commDeleteByDeviceId(tenantId, deviceId);
            resultDeleteIds.add(deviceId);
        }
        return resultDeleteIds;
    }

    private void commDeleteByDeviceId(Long tenantId, String deviceId) {
        deviceBusinessService.delByDeviceId(null, deviceId);

        deviceExtendBusinessService.delByDeviceId(tenantId, deviceId);
        deviceStatusBusinessService.delByDeviceId(tenantId, deviceId);
        deviceStateBusinessService.delByDeviceId(tenantId, deviceId);
    }

    public void deleteBatchDeviceIds(List<String> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return;
        }
        deviceIds.forEach(deviceId -> {
            deleteByDeviceId(deviceId);
        });
    }
}
