package com.iot.building.device.service;

import java.util.List;

import com.iot.building.device.vo.*;
import com.iot.common.helper.Page;
import com.iot.device.vo.rsp.ProductResp;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
public interface IDeviceRemoteService {
    void addDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq);

    void updateDeviceRemoteTemplate(DeviceRemoteTemplateReq deviceRemoteTemplateReq);

    void deleteDeviceRemoteTemplate(Long tenantId, Long id, Long userId);

    DeviceRemoteTemplateResp getDeviceRemoteTemplateById(Long tenantId, Long id);

    Page<DeviceRemoteTemplateSimpleResp> findDeviceRemoteTemplatePage(DeviceRemoteTemplatePageReq pageReq);

    List<DeviceRemoteTypeResp> listDeviceRemoteTypes(Long tenantId);

    DeviceRemoteTemplateResp getDeviceRemoteTemplateByBusinessTypeId(Long tenantId, Long id);

    void deleteDeviceRemoteControlIfExsit(Long tenantId, String deviceId, Long businessTypeId);

    void addDeviceRemoteControl(List<DeviceRemoteControlReq> deviceRemoteControlReqs);

    List<Long> listDeviceRemoteBusinessType(Long tenantId);

    List<DeviceRemoteControlResp> listDeviceRemoteControlByBusinessTypeId(Long tenantId, Long businessTypeId);
    
    List<DeviceRemoteControlResp> findRemoteControlByDeviceType(Long tenantId, Long deviceTypeId);

    List<DeviceRespVo> findDevListByDeviceIds(List<String> deviceIdList, boolean isCheckUserNotNull, Long userId);

    List<ProductResp> listProducts(List<Long> productIds);
}
