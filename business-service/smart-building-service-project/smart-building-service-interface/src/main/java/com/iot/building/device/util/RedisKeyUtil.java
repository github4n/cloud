package com.iot.building.device.util;

import com.iot.saas.SaaSContextHolder;

/**
 * 描述：device redis key 工具类
 * 创建人： linjihuang
 * 创建时间： 2018/12/07 09:40
 */
public class RedisKeyUtil {

    /**
     *  7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT = 604800;

    public static final String DEVICE_NUMBER = "device-number:%d";

    /**
     *  设备名称编号缓存key
     *
     * @param deviceId
     * @param tenantId
     * @return
     */
    public static String getDeviceNumberKey(Long tenantId) {
        return String.format(DEVICE_NUMBER, tenantId);
    }

    private static Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }
}
