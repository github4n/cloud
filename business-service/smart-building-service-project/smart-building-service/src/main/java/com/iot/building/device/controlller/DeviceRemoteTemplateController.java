package com.iot.building.device.controlller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.device.api.DeviceRemoteApi;
import com.iot.building.device.service.IDeviceRemoteService;
import com.iot.building.device.vo.DeviceRemoteControlReq;
import com.iot.building.device.vo.DeviceRemoteControlResp;
import com.iot.building.device.vo.DeviceRemoteTemplatePageReq;
import com.iot.building.device.vo.DeviceRemoteTemplateReq;
import com.iot.building.device.vo.DeviceRemoteTemplateResp;
import com.iot.building.device.vo.DeviceRemoteTemplateSimpleResp;
import com.iot.building.device.vo.DeviceRemoteTypeResp;
import com.iot.common.helper.Page;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 * 遥控器模板相关
 */
@RestController
public class DeviceRemoteTemplateController implements DeviceRemoteApi {

    @Autowired
    private IDeviceRemoteService deviceRemoteService;

    @Override
    public void addDeviceRemoteTemplate(@RequestBody DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        deviceRemoteService.addDeviceRemoteTemplate(deviceRemoteTemplateReq);
    }

    @Override
    public void updateDeviceRemoteTemplate(@RequestBody DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        deviceRemoteService.updateDeviceRemoteTemplate(deviceRemoteTemplateReq);

    }

    @Override
    public void deleteDeviceRemoteTemplate(Long tenantId, Long id, Long userId) {
        deviceRemoteService.deleteDeviceRemoteTemplate(tenantId, id, userId);
    }

    @Override
    public DeviceRemoteTemplateResp getDeviceRemoteTemplateById(Long tenantId, Long id) {
        return deviceRemoteService.getDeviceRemoteTemplateById(tenantId, id);
    }

    @Override
    public DeviceRemoteTemplateResp getDeviceRemoteTemplateByBusinessTypeId(Long tenantId, Long id) {
        return deviceRemoteService.getDeviceRemoteTemplateByBusinessTypeId(tenantId, id);
    }

    @Override
    public Page<DeviceRemoteTemplateSimpleResp> pageDeviceRemoteTemplatePage(@RequestBody DeviceRemoteTemplatePageReq pageReq) {
        return deviceRemoteService.findDeviceRemoteTemplatePage(pageReq);
    }

    @Override
    public List<DeviceRemoteTypeResp> listDeviceRemoteType(@RequestParam(required = false) Long tenantId) {
        List<DeviceRemoteTypeResp> deviceRemoteTypes = deviceRemoteService.listDeviceRemoteTypes(tenantId);
        return deviceRemoteTypes.stream().map(one -> new DeviceRemoteTypeResp(one.getId(), one.getName())).collect(Collectors.toList());
    }

    @Override
    public List<Long> listDeviceRemoteBusinessType(Long tenantId) {
        return deviceRemoteService.listDeviceRemoteBusinessType(tenantId);
    }


    @Override
    public void deleteDeviceRemoteControlIfExsit(Long tenantId, String deviceId, Long businessTypeId) {
        deviceRemoteService.deleteDeviceRemoteControlIfExsit(tenantId, deviceId, businessTypeId);
    }

    @Override
    public List<DeviceRemoteControlResp> listDeviceRemoteControlByBusinessTypeId(Long tenantId, Long businessTypeId) {
        return deviceRemoteService.listDeviceRemoteControlByBusinessTypeId(tenantId, businessTypeId);
    }

    @Override
    public void addDeviceRemoteControl(List<DeviceRemoteControlReq> deviceRemoteControlReqs) {
        deviceRemoteService.addDeviceRemoteControl(deviceRemoteControlReqs);
    }
    
    @Override
    public List<DeviceRemoteControlResp> findRemoteControlByDeviceType(Long tenantId, @RequestParam("deviceTypeId") Long deviceTypeId) {
        return deviceRemoteService.findRemoteControlByDeviceType(tenantId, deviceTypeId);
    }

}
