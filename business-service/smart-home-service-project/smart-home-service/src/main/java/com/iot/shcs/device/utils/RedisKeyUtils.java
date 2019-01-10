package com.iot.shcs.device.utils;

public class RedisKeyUtils {
    public static final String UPDATE_SUB_DEVICE_DETAIL = "updateSubDeviceDetail:%s:%s";
    public static final String UPDATE_SUB_DEVICE_DETAIL_LOCK = "updateSubDeviceDetailLock:%s:%s";

    public static final String DEV_ACTIVATED = "dev-activated:%s:%d";

    public static final String DEV_ACTIVATED_STATUS = "dev-activated-status:%s";

    public static String getUpdateSubDeviceDetailKey(String deviceId, String batchNum) {
        return String.format(UPDATE_SUB_DEVICE_DETAIL, deviceId, batchNum);
    }

    public static String getUpdateSubDeviceDetailLockKey(String deviceId, String batchNum) {
        return String.format(UPDATE_SUB_DEVICE_DETAIL_LOCK, deviceId, batchNum);
    }

    public static String getDevActivatedKey(String date, Long tenantId) {
        return String.format(DEV_ACTIVATED, date, tenantId);
    }

    public static String getDevActivatedStatusKey(String deviceId) {
        return String.format(DEV_ACTIVATED_STATUS, deviceId);
    }
}
