package com.iot.building.device.service;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/29
 */
public interface DeviceTobService {
    boolean deleteDeviceRelation(Long orgId,String deviceId,Long tenantId,boolean check,String clientId);

    void resetReq(String clientId);
}
