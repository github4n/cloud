package com.iot.ifttt.util;

/**
 * 描述：redis key 生成工具类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/27 13:40
 */
public class RedisKeyUtil {

    /**
     * 7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT_7 = 604800l;

    public static final String APPLET_RULE_KEY = "applet-rule:%d"; //applet规则缓存

    public static final String APPLET_STATUS_KEY = "applet-status:%d"; //applet状态缓存

    public static final String APPLET_THIS_MULTI_KEY = "applet-this-multi:%d"; //applet-this 缓存

    public static final String APPLET_THIS_KEY = "applet-this:%d"; //this 缓存

    public static final String APPLET_THAT_MULTI_KEY = "applet-that-multi:%d"; //applet-that 缓存

    public static final String APPLET_THAT_KEY = "applet-that:%d"; //that 缓存

    public static final String APPLET_ITEM_MULTI_KEY = "applet-item-multi:%d:%s"; //applet-item 缓存

    public static final String APPLET_ITEM_KEY = "applet-item:%d"; //item 缓存

    public static final String APPLET_RELATION_KEY = "applet-relation:%s:%s"; //关系缓存

    public static final String APPLET_DEV_CHECK_KEY = "applet-dev-check:%s:%d:%s"; //设备状态满足条件缓存


    public static String getAppletRuleKey(Long appletId) {
        return String.format(APPLET_RULE_KEY, appletId);
    }

    public static String getAppletStatusKey(Long appletId) {
        return String.format(APPLET_STATUS_KEY, appletId);
    }

    public static String getAppletThisMultiKey(Long appletId) {
        return String.format(APPLET_THIS_MULTI_KEY, appletId);
    }

    public static String getAppletThisKey(Long id) {
        return String.format(APPLET_THIS_KEY, id);
    }


    public static String getAppletThatMultiKey(Long appletId) {
        return String.format(APPLET_THAT_MULTI_KEY, appletId);
    }

    public static String getAppletThatKey(Long id) {
        return String.format(APPLET_THAT_KEY, id);
    }

    public static String getAppletItemMultiKey(Long eventId, String type) {
        return String.format(APPLET_ITEM_MULTI_KEY, eventId, type);
    }

    public static String getAppletItemKey(Long id) {
        return String.format(APPLET_ITEM_KEY, id);
    }

    public static String getAppletRelationKey(String type, String key) {
        return String.format(APPLET_RELATION_KEY, type, key);
    }

    public static String getAppletDevCheckKey(String devId, Long appletId, String field) {
        return String.format(APPLET_DEV_CHECK_KEY, devId, appletId, field);
    }
}
