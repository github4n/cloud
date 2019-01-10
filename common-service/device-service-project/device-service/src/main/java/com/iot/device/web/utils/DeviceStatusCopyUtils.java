package com.iot.device.web.utils;

import com.iot.device.model.DeviceStatus;
import com.iot.device.vo.rsp.device.GetDeviceStatusInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceStatusRespVo;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:14 2018/11/15
 * @Modify by:
 */
public class DeviceStatusCopyUtils {

    public static void copyProperties(DeviceStatus res, GetDeviceStatusInfoRespVo target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setActiveStatus(res.getActiveStatus());
            target.setActiveTime(res.getActiveTime());
            target.setOnOff(res.getOnOff());
            target.setId(res.getId());
            target.setTenantId(res.getTenantId());
            target.setDeviceId(res.getDeviceId());
            target.setLastLoginTime(res.getLastLoginTime());
            target.setOnlineStatus(res.getOnlineStatus());
            target.setToken(res.getToken());
        }
    }

    public static void copyProperties(DeviceStatus res, ListDeviceStatusRespVo target) {
        if (target == null) {
            return;
        }
        if (res != null) {
            target.setActiveStatus(res.getActiveStatus());
            target.setActiveTime(res.getActiveTime());
            target.setOnOff(res.getOnOff());
            target.setId(res.getId());
            target.setTenantId(res.getTenantId());
            target.setDeviceId(res.getDeviceId());
            target.setLastLoginTime(res.getLastLoginTime());
            target.setOnlineStatus(res.getOnlineStatus());
            target.setToken(res.getToken());
        }
    }
}
