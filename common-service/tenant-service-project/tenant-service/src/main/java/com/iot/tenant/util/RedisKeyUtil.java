package com.iot.tenant.util;

import java.io.Serializable;

public class RedisKeyUtil implements Serializable {
    /**
     * redis key 拼接
     */
    //1.tenant
    public static final String TENANT_KEY = "tenant:%d";
    //2.tenant-code
    public static final String TENANT_CODE_KEY = "tenant-code:%s";
    //user-default-org
    public static final String USER_DEFAULT_ORG_KEY = "user-default-org:%d";
    //user-org
    public static final String USER_ORG_KEY = "user-org:%d";

    /**
     * app能否使用标志（type: String类型  key: app:used:${appId} value:1 能使用， 0：禁用)
     */
    public final static String APP_USED_KEY = "app:used:%d";

    public static String getTenantKey(Long tenantId) {
        return String.format(TENANT_KEY, tenantId);
    }

    public static String getTenantCodeKey(String code) {
        return String.format(TENANT_CODE_KEY, code);
    }

    public static String getUserDefaultOrgKey(Long userId) {
        return String.format(USER_DEFAULT_ORG_KEY, userId);
    }

    public static String getUserOrgKey(Long userId) {
        return String.format(USER_ORG_KEY, userId);
    }

    public static String getAppUsedKey(Long appId) { return String.format(APP_USED_KEY, appId);}
}
