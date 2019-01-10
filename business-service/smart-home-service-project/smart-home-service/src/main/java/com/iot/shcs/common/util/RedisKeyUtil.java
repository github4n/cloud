package com.iot.shcs.common.util;

/**
 * @Descrpiton: 公共redis key 工具类
 * @Author: yuChangXing
 * @Date: 2018/8/3 16:44
 * @Modify by:
 */
public class RedisKeyUtil {

    public static final String MQTT_SEQ_KEY = "seq:%s:%s";

    public static final String DEVICE_DEV_BIND_NOTIF = "device-devBindNotif:%s:%s:%d";

    /**
     * 加锁默认超时时间
     */
    public static final long MQTT_SEQ_EXPIRE_SECOND = 10;

    /**
     * mqtt seq的 redis锁key
     *
     * @param method
     * @param seq
     * @return
     */
    public static String getMqttSeqKey(String method, String seq) {
        return String.format(MQTT_SEQ_KEY, method, seq);
    }

    public static String getDevBindNotifIdKey(String userUuid, String deviceId, Long tenantId) {
        return String.format(DEVICE_DEV_BIND_NOTIF, userUuid, deviceId, tenantId);
    }
}
