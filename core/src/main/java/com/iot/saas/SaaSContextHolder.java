package com.iot.saas;

import java.util.HashMap;
import java.util.Map;

public class SaaSContextHolder {

    public static final String KEY_USER_ID = "currentUserId";

    /**
     * 用户uuid
     */
    public static final String KEY_USER_UUID = "currentUserUuid";

    public static final String KEY_TENANT_ID = "tenantId";
    //租户下-- 组织id
    public static final String KEY_ORG_ID = "orgId";

    public static final String KEY_LOG_REQUEST_ID = "logRequestId";

    //    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_CONTEXTS = new NamedThreadLocal<>("Saas Context");
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL_CONTEXTS = new ThreadLocal<>();

    public static Map<String, Object> currentContextMap() {
        Map<String, Object> context = THREAD_LOCAL_CONTEXTS.get();

        if (context == null) {
            context = initContextMap();
        }
        return context;
    }

    protected static Map<String, Object> initContextMap() {
        Map<String, Object> context = new HashMap<>();
        THREAD_LOCAL_CONTEXTS.set(context);
        return context;
    }

    protected static boolean has(String key) {
        return currentContextMap().containsKey(key);
    }

    protected static <T> T get(String key, Class<T> valueClass) {
        Map<String, Object> currentContextMap = currentContextMap();
        if (currentContextMap.containsKey(key)) {
            Object o = currentContextMap.get(key);
            if (valueClass.isInstance(o)) {
                return valueClass.cast(o);
            }
        }
        return null;
    }

    protected static <T> void set(String key, T value) {
        Map<String, Object> context = currentContextMap();
        if (context.containsKey(key)) {
            context.remove(key);
        }

        context.put(key, value);
    }

    protected static void delete(String key) {
        Map<String, Object> currentContextMap = currentContextMap();
        if (currentContextMap.containsKey(key)) {
            currentContextMap.remove(key);
        }
    }

    public static void removeCurrentContextMap() {
        THREAD_LOCAL_CONTEXTS.remove();
    }

    /* currentTenantId */
    public static Long currentTenantId() {
        Long currentTenantId = get(KEY_TENANT_ID, Long.class);
        if (currentTenantId == null) {
            throw new IllegalStateException("tenant.id.is.null");
        }
        return currentTenantId;
    }

    public static boolean hasCurrentTenantId() {
        return has(KEY_TENANT_ID);
    }

    public static void setCurrentTenantId(Long tenantId) {
        set(KEY_TENANT_ID, tenantId);
    }

    public static void removeCurrentTenantId() {
        delete(KEY_TENANT_ID);
    }

    /* logRequestId */
    public static String getLogRequestId() {
        return get(KEY_LOG_REQUEST_ID, String.class);
    }

    public static void setLogRequestId(String logRequestId) {
        set(KEY_LOG_REQUEST_ID, logRequestId);
    }

    public static void removeLogRequestId() {
        delete(KEY_LOG_REQUEST_ID);
    }

    /* currentUserId */
    public static Long getCurrentUserId() {
        return get(KEY_USER_ID, Long.class);
    }

    public static void setCurrentUserId(Long userId) {
        set(KEY_USER_ID, userId);
    }

    public static void removeCurrentUserId() {
        delete(KEY_USER_ID);
    }

    public static String getCurrentUserUuid() {
        return get(KEY_USER_UUID, String.class);
    }

    public static void setCurrentUserUuid(String userUuid) {
        set(KEY_USER_UUID, userUuid);
    }

    public static Long getCurrentOrgId() {
        return get(KEY_ORG_ID, Long.class);
    }

    public static void setCurrentOrgId(Long orgId) {
        set(KEY_ORG_ID, orgId);
    }

}
