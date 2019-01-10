package com.iot.shcs.security.util;

import com.iot.saas.SaaSContextHolder;

/**
 * 描述：security redis key 工具类
 * 创建人： yuChangXing
 * 创建时间： 2018/6/26 14:40
 */
public class RedisKeyUtil {

    public static final Long DEFAULT_CACHE_TIME = 60 * 60 * 24 * 7L;

    //安防主表主键：tenantId：security-rule：homeId
    public static final String SECURITY_HOME_ID_KEY = "%d:security-home:%d";
    public static String getSecurityHomeIdKey(Long homeId,Long tenantId) {
        return String.format(SECURITY_HOME_ID_KEY, tenantId,homeId);
    }

    /**
     *  7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT = 604800;

    public static final String DEVICE_IFTTT_KEY = "device-ifttt:%s";
    public static final String DEVICE_RULE_KEY = "device-rule:%s";
    //安防规则主键：tenantId：security-rule：securityId，type
    public static final String SECURITY_RULE_KEY = "%d:security-rule:%d:%s";


    public static String getDeviceIftttKey(String deviceId) {
        return String.format(DEVICE_IFTTT_KEY, deviceId);
    }

    public static String getDeviceRuleKey(String deviceId) {
        return String.format(DEVICE_RULE_KEY, deviceId);
    }

    public static String getSecurityRuleKey(Long tenantId,Long securityId,String type) {
        return String.format(SECURITY_RULE_KEY, tenantId,securityId, type);
    }



    private static Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }
}
