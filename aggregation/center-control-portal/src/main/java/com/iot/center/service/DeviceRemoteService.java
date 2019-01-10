package com.iot.center.service;

import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.api.DeviceRemoteApi;
import com.iot.building.device.vo.*;
import com.iot.building.remote.api.RemoteControlApi;
import com.iot.common.helper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
@Service
public class DeviceRemoteService {
    @Autowired
    private DeviceRemoteApi deviceRemoteApi;
    @Autowired
    private DeviceBusinessTypeApi deviceBusinessTypeApi;
    @Autowired
    private RemoteControlApi remoteControlApi;
    
    public void addDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq) {
        deviceRemoteApi.addDeviceRemoteTemplate(deviceRemoteTemplateReq);
    }

    public void updateDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq) {

        deviceRemoteApi.updateDeviceRemoteTemplate(deviceRemoteTemplateReq);
    }

    public void deleteDeviceRemoteTemplate(Long tenantId, Long id, Long userId) {

        deviceRemoteApi.deleteDeviceRemoteTemplate(tenantId, id, userId);
    }

    public DeviceRemoteTemplateResp getDeviceRemoteTemplateById(Long tenantId, Long id) {
        return deviceRemoteApi.getDeviceRemoteTemplateById(tenantId, id);
    }

    public Page<DeviceRemoteTemplateSimpleResp> pageDeviceRemoteTemplatePage(DeviceRemoteTemplatePageReq pageReq) {
        return deviceRemoteApi.pageDeviceRemoteTemplatePage(pageReq);
    }

    public List<DeviceRemoteTypeResp> listDeviceRemoteType(Long tenantId) {
        return deviceRemoteApi.listDeviceRemoteType(tenantId);
    }

    /**
     * 获取遥控器的用途
     *
     * @param tenantId
     * @return
     */
    public List<DeviceBusinessTypeResp> listDeviceRemoteBusinessType(Long orgId,Long tenantId,Long remoteId) {
        return deviceBusinessTypeApi.listDeviceRemoteBusinessType(orgId,tenantId,remoteId);
    }

    /**
     * 遥控器下发  先找到所有的遥控器模板的businessTypeID 去匹配space下的设备的businessTypeID 一样就下发模板
     */
    public void synchronousRemoteControl(Long tenantId, Long spaceId) {
    	remoteControlApi.synchronousRemoteControl(tenantId, spaceId);
    }

}
