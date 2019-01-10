package com.iot.robot.utils;

import com.iot.saas.SaaSContextHolder;

/**
 * 描述：security redis key 工具类
 * 创建人： yuChangXing
 * 创建时间： 2018/6/26 14:40
 */
public class RedisKeyUtil {

    public static final Long DEFAULT_CACHE_TIME = 60 * 60 * 24 * 7L;

    // GoogleServiceAccountAccessToken 缓存key值
    public static final String GOOGLE_SERVICE_ACCOUNT_ACCESS_TOKEN_CACHE_KEY = "googleHome:accessToken:%d";

    public static String getGoogleServiceAccountAccessTokenCacheKey(Long tenantId) {
        return String.format(GOOGLE_SERVICE_ACCOUNT_ACCESS_TOKEN_CACHE_KEY, tenantId);
    }
}
