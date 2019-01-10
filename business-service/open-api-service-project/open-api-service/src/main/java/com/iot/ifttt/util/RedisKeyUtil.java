package com.iot.ifttt.util;

/**
 * 描述：redis key 生成工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/27 13:40
 */
public class RedisKeyUtil {

    private RedisKeyUtil() {
    }

    public static final long DEFAULT_EXPIRE_TIME_OUT_2 = 2 * 60 * 60l;

    public static final String IFTTT_DEVICE_KEY = "ifttt-com:device:%s:%s"; //设备状态缓存
    public static final String IFTTT_SCENE_KEY = "ifttt-com:scene:%s"; //情景缓存
    public static final String IFTTT_SECURITY_KEY = "ifttt-com:security:%s"; //安防缓存
    public static final String IFTTT_SECURITY_TYPE_KEY = "ifttt-com:securityType:%s:%s"; //安防缓存


    public static final String IFTTT_TRIGGER_KEY = "ifttt-com:trigger:%s"; //trigger缓存
    public static final String IFTTT_CHECK_KEY = "ifttt-com:check:%s"; //重复执行缓存

    public static String getIftttDeviceKey(String deviceId, String attr) {
        return String.format(IFTTT_DEVICE_KEY, deviceId, attr);
    }

    public static String getIftttSceneKey(String sceneId) {
        return String.format(IFTTT_SCENE_KEY, sceneId);
    }

    public static String getIftttSecurityKey(String userId) {
        return String.format(IFTTT_SECURITY_KEY, userId);
    }

    public static String getIftttSecurityTypeKey(String userId, String status) {
        return String.format(IFTTT_SECURITY_TYPE_KEY, userId, status);
    }

    public static String getIftttTriggerKey(String identity) {
        return String.format(IFTTT_TRIGGER_KEY, identity);
    }

    public static String getIftttCheckKey(String identity) {
        return String.format(IFTTT_CHECK_KEY, identity);
    }

}
