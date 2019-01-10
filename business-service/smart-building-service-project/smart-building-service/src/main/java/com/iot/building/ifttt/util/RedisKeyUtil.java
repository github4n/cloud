package com.iot.building.ifttt.util;

import com.iot.saas.SaaSContextHolder;

/**
 * 描述：redis key 生成工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/27 13:40
 */
public class RedisKeyUtil {

    /**
     *  7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT = 604800;

    public static final String DEVICE_IFTTT_KEY = "device-ifttt:%s";
    public static final String DEVICE_RULE_KEY = "device-rule:%s";
    public static final String IFTTT_RULE_KEY = "ifttt-rule:%d:%d";
    public static final String IFTTT_SENSOR_KEY = "ifttt-sensor:%d:%d";
    public static final String IFTTT_RELATION_KEY = "ifttt-relation:%d:%d";
    public static final String IFTTT_TRIGGER_KEY = "ifttt-trigger:%d:%d";
    public static final String IFTTT_ACTUATOR_KEY = "ifttt-actuator:%d:%d";
    public static final String IFTTT_USER_KEY = "ifttt-user:%d:%d";
    public static final String IFTTT_RULE_FOR_LIST_KEY = "ifttt-r-f-l:%d:%d";
    public static final String IFTTT_DEL_KEY = "ifttt-del:%d:%d";
    public static final String IFTTT_EDIT_KEY = "ifttt-edit:%d:%d";

    public static String getDeviceIftttKey(String deviceId) {
        return String.format(DEVICE_IFTTT_KEY, deviceId);
    }

    public static String getDeviceRuleKey(String deviceId) {
        return String.format(DEVICE_RULE_KEY, deviceId);
    }

    public static String getIftttRuleKey(Long ruleId) {
        return String.format(IFTTT_RULE_KEY, ruleId, getTenantId());
    }

    public static String getIftttSensorKey(Long ruleId) {
        return String.format(IFTTT_SENSOR_KEY, ruleId, getTenantId());
    }

    public static String getIftttRelationKey(Long ruleId) {
        return String.format(IFTTT_RELATION_KEY, ruleId, getTenantId());
    }

    public static String getIftttTriggerKey(Long ruleId) {
        return String.format(IFTTT_TRIGGER_KEY, ruleId, getTenantId());
    }

    public static String getIftttActuatorKey(Long ruleId) {
        return String.format(IFTTT_ACTUATOR_KEY, ruleId, getTenantId());
    }

    public static String getIftttUserKey(Long ruleId) {
        return String.format(IFTTT_USER_KEY, ruleId, getTenantId());
    }

    public static String getIftttRuleForListIdKey(Long ruleId) {
        return String.format(IFTTT_RULE_FOR_LIST_KEY, ruleId, getTenantId());
    }

    public static String getIftttDelKey(Long ruleId){
        return String.format(IFTTT_DEL_KEY,ruleId,getTenantId());
    }

    public static String getIftttEditKey(Long ruleId){
        return String.format(IFTTT_EDIT_KEY,ruleId,getTenantId());
    }

    private static Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }
}
