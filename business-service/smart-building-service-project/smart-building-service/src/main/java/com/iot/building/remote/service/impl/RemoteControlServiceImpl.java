package com.iot.building.remote.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.building.device.vo.DeviceRemoteControlReq;
import com.iot.building.device.vo.DeviceRemoteTemplateResp;
import com.iot.building.remote.service.RemoteControlService;
import com.iot.building.space.service.IBuildingSpaceService;


/**
 * @Author: linjihuang
 * @Descrpiton: 
 * @Date: 10:54 2018/10/12 
 * @Modify by:
 */
@Service
public class RemoteControlServiceImpl implements RemoteControlService{

    private static final Logger log = LoggerFactory.getLogger(RemoteControlServiceImpl.class);
    @Autowired
    private IDeviceRemoteService deviceRemoteService;
    @Autowired
    private IBuildingSpaceService spaceService;
    
    /**
     * 遥控器下发
     * @param spaceId
     */
    public void synchronousRemoteControl(Long tenantId, Long spaceId) {
        //遥控器全部的模板的businessTypeId
//        Set<Long> businessTypeIdsSet = new HashSet<>(deviceRemoteService.listDeviceRemoteBusinessType(null));
//        //根据spaceId找到房间下的device_type是遥控器的设备
//        List<Map<String, Object>> deviceList = spaceService.findDeviceByRoom(spaceId,tenantId);
//        if (CollectionUtils.isNotEmpty(deviceList)) {
//            //一个房间可能有多个遥控器
//            for (Map<String, Object> deviceMap : deviceList) {
//                //根据businessTypeID查找遥控器模板
//                Long businessTypeId = Long.valueOf(deviceMap.get("businessTypeId").toString());
//                //如果用途不是遥控器就跳过
//                if (!businessTypeIdsSet.contains(businessTypeId)) {
//                    continue;
//                }
//                String deviceId = deviceMap.get("deviceId").toString();
//                Long deviceTypeId = Long.valueOf(deviceMap.get("deviceTypeId").toString());
//                //判断设备是否已经下发过 如果有就先删除之前的数据
//                deviceRemoteService.deleteDeviceRemoteControlIfExsit(tenantId, deviceId, businessTypeId);
//                DeviceRemoteTemplateResp deviceRemoteTemplateResp = deviceRemoteService.getDeviceRemoteTemplateByBusinessTypeId(tenantId, businessTypeId);
//                //保存新的数据到device_remote_control  device_remote_control_scene
//                List<DeviceRemoteControlReq> deviceRemoteControlReqs = new ArrayList<>();
//                deviceRemoteTemplateResp.getDeviceRemoteControlTemplateReqs().stream().forEach(one -> {
//                    DeviceRemoteControlReq deviceRemoteControlReq = new DeviceRemoteControlReq();
//                    BeanUtils.copyProperties(one, deviceRemoteControlReq);
//                    deviceRemoteControlReq.setDeviceId(deviceId);
//                    deviceRemoteControlReq.setDeviceTypeId(deviceTypeId);
//                    deviceRemoteControlReq.setControlTemplateId(one.getId());
//                    deviceRemoteControlReqs.add(deviceRemoteControlReq);
//                });
//                deviceRemoteService.addDeviceRemoteControl(deviceRemoteControlReqs);
//            }
//        }
    }
    
}
