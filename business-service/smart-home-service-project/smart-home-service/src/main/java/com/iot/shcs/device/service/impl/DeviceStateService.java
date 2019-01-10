package com.iot.shcs.device.service.impl;

import com.iot.device.api.DeviceStateCoreApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton: 设备状态
 * @Date: 13:44 2018/6/19
 * @Modify by:
 */
@Slf4j
@Service
public class DeviceStateService {

    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;


    /**
     * 恢复设备默认的属性值【灯的默认值，设备默认在线等】
     *
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/19 13:46
     */
    public void recoveryDefaultState(Long tenantId, String deviceId) {
        deviceStateCoreApi.recoveryDefaultState(tenantId, deviceId);
    }


    /**
     * 获取设备属性
     *@param tenantId
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/19 15:19
     */
    public Map<String, Object> getDeviceStateByDeviceId(Long tenantId, String deviceId) {
        try {
            return deviceStateCoreApi.get(tenantId, deviceId);
        } catch (Exception e) {
            log.info("getDeviceStateByDeviceId-error", e);
        }
        return null;
    }

}
