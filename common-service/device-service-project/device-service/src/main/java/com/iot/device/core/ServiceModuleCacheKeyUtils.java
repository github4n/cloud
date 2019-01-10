package com.iot.device.core;

public class ServiceModuleCacheKeyUtils {
    public static final String SERVICE_MODULE_KEY = "serviceModule:%s";
    public static final String SERVICE_TO_ACTION_BY_SERVICEMODULE_TENANT_KEY = "serviceToActionSt:%s:%s";
    public static final String SERVICE_TO_EVENT_BY_SERVICEMODULE_TENANT_KEY = "serviceToEventSt:%s:%s";
    public static final String SERVICE_TO_PROPERTY_BY_SERVICEMODULE_TENANT_KEY = "serviceToPropertySt:%s:%s";
    public static final String SERVICE_MODULE_ACTION_KEY = "serviceModuleAction:%s";
    public static final String SERVICE_MODULE_EVENT_KEY = "serviceModuleEvent:%s";
    public static final String SERVICE_MODULE_PROPERTY_KEY = "serviceModuleProperty:%s";
    public static final String PRODUCT_TO_SERVICEMODULE_BY_PRODUCT_KEY = "productToSmByPid:%s";

    public static String getServiceModuleKey(Long serviceModuleId) {
        return String.format(SERVICE_MODULE_KEY, serviceModuleId);
    }

    public static String getServiceToActionByServicemoduleTenantKey(Long tenantId, Long serviceModuleId) {
        return String.format(SERVICE_TO_ACTION_BY_SERVICEMODULE_TENANT_KEY, tenantId, serviceModuleId);
    }

    public static String getServiceToEventByServicemoduleTenantKey(Long tenantId, Long serviceModuleId) {
        return String.format(SERVICE_TO_EVENT_BY_SERVICEMODULE_TENANT_KEY, tenantId, serviceModuleId);
    }

    public static String getServiceToPropertyByServicemoduleTenantKey(Long tenantId, Long serviceModuleId) {
        return String.format(SERVICE_TO_PROPERTY_BY_SERVICEMODULE_TENANT_KEY, tenantId, serviceModuleId);
    }

    public static String getServiceModuleActionKey(Long serviceModuleActionId) {
        return String.format(SERVICE_MODULE_ACTION_KEY, serviceModuleActionId);
    }

    public static String getServiceModuleEventKey(Long serviceModuleEventId) {
        return String.format(SERVICE_MODULE_EVENT_KEY, serviceModuleEventId);
    }

    public static String getServiceModulePropertyKey(Long serviceModulePropertyId) {
        return String.format(SERVICE_MODULE_PROPERTY_KEY, serviceModulePropertyId);
    }

    public static String getProductToServiceModuleByProductKey(Long productId) {
        return String.format(PRODUCT_TO_SERVICEMODULE_BY_PRODUCT_KEY, productId);
    }
}
