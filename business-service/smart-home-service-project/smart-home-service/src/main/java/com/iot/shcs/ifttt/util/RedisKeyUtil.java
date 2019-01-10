package com.iot.shcs.ifttt.util;

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
    public static final long DEFAULT_EXPIRE_TIME_OUT_7 = 604800l;

    public static final long DEFAULT_EXPIRE_TIME_OUT_1 = 86400l;

    public static final String IFTTT_AUTO_KEY = "ifttt-auto:%d:%d"; //auto缓存

    public static final String IFTTT_USER_KEY = "ifttt-user:%d:%d"; //用户id缓存
    public static final String IFTTT_DEL_KEY = "ifttt-del:%d:%d"; //直连转跨直连 删除缓存
    public static final String IFTTT_EDIT_KEY = "ifttt-edit:%d:%d"; //跨直连->直连 编辑缓存


    public static String getIftttUserKey(Long ruleId) {
        return String.format(IFTTT_USER_KEY, ruleId, getTenantId());
    }

    public static String getIftttAutoKey(Long autoId) {
        return String.format(IFTTT_AUTO_KEY, autoId, getTenantId());
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
