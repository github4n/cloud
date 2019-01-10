package com.iot.device.core;

import com.alibaba.fastjson.JSON;
import com.iot.common.util.StringUtil;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.model.*;
import com.iot.redis.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ServiceModuleCacheCoreUtils {
    //-----------------------service_module_begin-------------------------
    public static void cacheServiceModule(ServiceModule target) {
        if (target != null && target.getId() != null) {
            String key = ServiceModuleCacheKeyUtils.getServiceModuleKey(target.getId());
            RedisCacheUtil.valueObjSet(key, target, CacheKeyUtils.EXPIRE_TIME_OUT);
        }
    }

    public static void batchSetServiceModule(List<ServiceModule> data){
        if (data != null && data.size() > 0) {
            Map<String, String> dataMap = data.stream().
                    collect(Collectors.toMap(k -> ServiceModuleCacheKeyUtils.getServiceModuleKey(k.getId()), serviceModule -> JSON.toJSONString(serviceModule), (k1, k2) -> k1));
            RedisCacheUtil.mset(dataMap, CacheKeyUtils.EXPIRE_TIME_OUT);
        }
    }

    public static List<ServiceModule> batchGetServiceModule(List<Long> ids){
        if (ids != null && ids.size() > 0) {
            List<String> keyList = ids.stream().map(serviceModuleId -> ServiceModuleCacheKeyUtils.getServiceModuleKey(serviceModuleId)).collect(Collectors.toList());
            List<ServiceModule> data = RedisCacheUtil.mget(keyList, ServiceModule.class);
            return data;
        }
        return null;
    }

    public static ServiceModule getServiceModuleCacheData(Long serviceModuleId) {
        String key = ServiceModuleCacheKeyUtils.getServiceModuleKey(serviceModuleId);
        String json = RedisCacheUtil.valueGet(key);
        if (StringUtil.isNotEmpty(json)) {
            try {
                ServiceModule result = JSON.parseObject(json, ServiceModule.class);
                return result;
            } catch (Exception e) {
                log.error("转换缓存ServiceModule{}出错", json, e);
            }
        }
        return null;
    }

    public static void removeServiceModuleCache(Long serviceModuleId) {
        String key = ServiceModuleCacheKeyUtils.getServiceModuleKey(serviceModuleId);
        RedisCacheUtil.delete(key);
    }

    public static void batchRemoveServiceModuleCache(ArrayList<Long> serviceModuleIds) {
        if (serviceModuleIds != null && serviceModuleIds.size() > 0) {
            List<String> keys = serviceModuleIds.stream().map(id -> ServiceModuleCacheKeyUtils.getServiceModuleKey(id)).collect(Collectors.toList());
            RedisCacheUtil.delete(keys);
        }
    }
    //-----------------------service_module_end-------------------------

    //-----------------------service_to_action_begin-------------------------
    public static void removeServiceToActionByModule(Long serviceModuleId,Long tenantId) {
        List<Long> serviceModuleIds = new ArrayList<>();
        serviceModuleIds.add(serviceModuleId);
        ServiceModuleCacheCoreUtils.batchRemoveServiceToActionByModule(serviceModuleIds,tenantId);
    }

    public static void batchRemoveServiceToActionByModule(List<Long> serviceModuleIds,Long tenantId){
        List<String> keys = serviceModuleIds.stream().map(serviceModuleId -> ServiceModuleCacheKeyUtils.getServiceToActionByServicemoduleTenantKey(tenantId,serviceModuleId)).collect(Collectors.toList());
        RedisCacheUtil.delete(keys);
    }

    public static List<ServiceToAction> getServiceToActionCache(Long tenantId, Long serviceModuleId) {
        String key = ServiceModuleCacheKeyUtils.getServiceToActionByServicemoduleTenantKey(tenantId,serviceModuleId);
        Set<ServiceToAction> cacheSetData = RedisCacheUtil.setGetAll(key, ServiceToAction.class, false);
        if (cacheSetData != null && cacheSetData.size() > 0) {
            List<ServiceToAction> result = new ArrayList<>(cacheSetData);
            return result;
        }
        return null;
    }

    public static void cacheServiceToActionCache(Long tenantId, Long serviceModuleId, List<ServiceToAction> data) {
        String key = ServiceModuleCacheKeyUtils.getServiceToActionByServicemoduleTenantKey(tenantId,serviceModuleId);
        Set<ServiceToAction> set = new HashSet<>(data);
        RedisCacheUtil.setAdd(key, set, true);
        RedisCacheUtil.expireKey(key, CacheKeyUtils.EXPIRE_TIME_OUT);
    }
    //-----------------------service_to_action_end-------------------------

    //-----------------------service_to_event_begin-------------------------
    public static void removeServiceToEventByModule(Long serviceModuleId,Long tenantId) {
        List<Long> serviceModuleIds = new ArrayList<>();
        serviceModuleIds.add(serviceModuleId);
        ServiceModuleCacheCoreUtils.batchRemoveServiceToEventByModule(serviceModuleIds,tenantId);
    }

    public static void batchRemoveServiceToEventByModule(List<Long> serviceModuleIds,Long tenantId) {
        List<String> keys = serviceModuleIds.stream().map(serviceModuleId -> ServiceModuleCacheKeyUtils.getServiceToEventByServicemoduleTenantKey(tenantId,serviceModuleId)).collect(Collectors.toList());
        RedisCacheUtil.delete(keys);
    }

    public static List<ServiceToEvent> getServiceToEventCache(Long tenantId, Long serviceModuleId) {
        String key = ServiceModuleCacheKeyUtils.getServiceToEventByServicemoduleTenantKey(tenantId,serviceModuleId);
        Set<ServiceToEvent> cacheSetData = RedisCacheUtil.setGetAll(key, ServiceToEvent.class, false);
        if (cacheSetData != null && cacheSetData.size() > 0) {
            List<ServiceToEvent> result = new ArrayList<>(cacheSetData);
            return result;
        }
        return null;
    }

    public static void cacheServiceToEventCache(Long tenantId, Long serviceModuleId, List<ServiceToEvent> data) {
        String key = ServiceModuleCacheKeyUtils.getServiceToEventByServicemoduleTenantKey(tenantId,serviceModuleId);
        Set<ServiceToEvent> set = new HashSet<>(data);
        RedisCacheUtil.setAdd(key, set, true);
        RedisCacheUtil.expireKey(key, CacheKeyUtils.EXPIRE_TIME_OUT);
    }
    //-----------------------service_to_event_end-------------------------

    //-----------------------service_to_property_begin-------------------------
    public static void removeServiceToPropertyByModule(Long serviceModuleId,Long tenantId) {
        List<Long> serviceModuleIds = new ArrayList<>();
        serviceModuleIds.add(serviceModuleId);
        ServiceModuleCacheCoreUtils.batchRemoveServiceToPropertyByModule(serviceModuleIds,tenantId);
    }

    public static void batchRemoveServiceToPropertyByModule(List<Long> serviceModuleIds,Long tenantId) {
        List<String> keys = serviceModuleIds.stream().map(serviceModuleId -> ServiceModuleCacheKeyUtils.getServiceToPropertyByServicemoduleTenantKey(tenantId,serviceModuleId)).collect(Collectors.toList());
        RedisCacheUtil.delete(keys);
    }

    public static List<ServiceToProperty> getServiceToPropertyCache(Long tenantId, Long serviceModuleId) {
        String key = ServiceModuleCacheKeyUtils.getServiceToPropertyByServicemoduleTenantKey(tenantId,serviceModuleId);
        Set<ServiceToProperty> cacheSetData = RedisCacheUtil.setGetAll(key, ServiceToProperty.class, false);
        if (cacheSetData != null && cacheSetData.size() > 0) {
            List<ServiceToProperty> result = new ArrayList<>(cacheSetData);
            return result;
        }
        return null;
    }

    public static void cacheServiceToPropertyCache(Long tenantId, Long serviceModuleId, List<ServiceToProperty> data) {
        String key = ServiceModuleCacheKeyUtils.getServiceToPropertyByServicemoduleTenantKey(tenantId,serviceModuleId);
        Set<ServiceToProperty> set = new HashSet<>(data);
        RedisCacheUtil.setAdd(key, set, true);
        RedisCacheUtil.expireKey(key, CacheKeyUtils.EXPIRE_TIME_OUT);
    }
    //-----------------------service_to_property_end-------------------------

    //-----------------------service_module_action_begin-------------------------
    public static void removeServiceModuleActionCache(Long serviceModuleActionId) {
        List<Long> serviceModuleActionIds = new ArrayList<>();
        serviceModuleActionIds.add(serviceModuleActionId);
        ServiceModuleCacheCoreUtils.batchRemoveServiceModuleActionCache(serviceModuleActionIds);
    }

    public static void batchRemoveServiceModuleActionCache(List<Long> serviceModuleActionIds) {
        if (serviceModuleActionIds != null && serviceModuleActionIds.size() > 0) {
            List<String> keys = serviceModuleActionIds.stream().map(id -> ServiceModuleCacheKeyUtils.getServiceModuleActionKey(id)).collect(Collectors.toList());
            RedisCacheUtil.delete(keys);
        }
    }

    public static void batchSetServiceMouleActionCache(List<ServiceModuleAction> data) {
        if (data != null && data.size() > 0) {
            Map<String, String> dataMap = data.stream().
                    collect(Collectors.toMap(k -> ServiceModuleCacheKeyUtils.getServiceModuleActionKey(k.getId()), action -> JSON.toJSONString(action), (k1, k2) -> k1));
            RedisCacheUtil.mset(dataMap, CacheKeyUtils.EXPIRE_TIME_OUT);
        }
    }

    public static List<ServiceModuleAction> batchGetServiceMouleActionCache(List<Long> actionIds) {
        if (actionIds != null && actionIds.size() > 0) {
            List<String> keyList = actionIds.stream().map(actionId -> ServiceModuleCacheKeyUtils.getServiceModuleActionKey(actionId)).collect(Collectors.toList());
            List<ServiceModuleAction> data = RedisCacheUtil.mget(keyList, ServiceModuleAction.class);
            return data;
        }
        return null;
    }
    //-----------------------service_module_action_end-------------------------

    //-----------------------service_module_event_begin-------------------------
    public static void removeServiceModuleEventCache(Long serviceModuleEventId) {
        List<Long> serviceModuleEventIds = new ArrayList<>();
        serviceModuleEventIds.add(serviceModuleEventId);
        ServiceModuleCacheCoreUtils.batchRemoveServiceModuleEventCache(serviceModuleEventIds);
    }

    public static void batchRemoveServiceModuleEventCache(List<Long> serviceModuleEventIds) {
        if (serviceModuleEventIds != null && serviceModuleEventIds.size() > 0) {
            List<String> keys = serviceModuleEventIds.stream().map(id -> ServiceModuleCacheKeyUtils.getServiceModuleEventKey(id)).collect(Collectors.toList());
            RedisCacheUtil.delete(keys);
        }
    }

    public static void batchSetServiceModuleEventCache(List<ServiceModuleEvent> data) {
        if (data != null && data.size() > 0) {
            Map<String, String> dataMap = data.stream().
                    collect(Collectors.toMap(k -> ServiceModuleCacheKeyUtils.getServiceModuleEventKey(k.getId()), event -> JSON.toJSONString(event), (k1, k2) -> k1));
            RedisCacheUtil.mset(dataMap, CacheKeyUtils.EXPIRE_TIME_OUT);
        }
    }

    public static List<ServiceModuleEvent> batchGetServiceMouleEventCache(List<Long> eventIds) {
        if (eventIds != null && eventIds.size() > 0) {
            List<String> keyList = eventIds.stream().map(eventId -> ServiceModuleCacheKeyUtils.getServiceModuleEventKey(eventId)).collect(Collectors.toList());
            List<ServiceModuleEvent> data = RedisCacheUtil.mget(keyList, ServiceModuleEvent.class);
            return data;
        }
        return null;
    }
    //-----------------------service_module_event_end-------------------------

    //-----------------------service_module_property_begin-------------------------
    public static void removeServiceModulePropertyCache(Long serviceModulePropertyId) {
        List<Long> serviceModulePropertyIds = new ArrayList<>();
        serviceModulePropertyIds.add(serviceModulePropertyId);
        ServiceModuleCacheCoreUtils.batchRemoveServiceModulePropertyCache(serviceModulePropertyIds);
    }

    public static void batchRemoveServiceModulePropertyCache(List<Long> serviceModulePropertyIds) {
        if (serviceModulePropertyIds != null && serviceModulePropertyIds.size() > 0) {
            List<String> keys = serviceModulePropertyIds.stream().map(id -> ServiceModuleCacheKeyUtils.getServiceModulePropertyKey(id)).collect(Collectors.toList());
            RedisCacheUtil.delete(keys);
        }
    }

    public static void batchSetServiceModulePropertyCache(List<ServiceModuleProperty> data) {
        if (data != null && data.size() > 0) {
            Map<String, String> dataMap = data.stream().
                    collect(Collectors.toMap(k -> ServiceModuleCacheKeyUtils.getServiceModulePropertyKey(k.getId()), property -> JSON.toJSONString(property), (k1, k2) -> k1));
            RedisCacheUtil.mset(dataMap, CacheKeyUtils.EXPIRE_TIME_OUT);
        }
    }

    public static List<ServiceModuleProperty> batchGetServiceMoulePropertyCache(List<Long> eventIds) {
        if (eventIds != null && eventIds.size() > 0) {
            List<String> keyList = eventIds.stream().map(propertyId -> ServiceModuleCacheKeyUtils.getServiceModulePropertyKey(propertyId)).collect(Collectors.toList());
            List<ServiceModuleProperty> data = RedisCacheUtil.mget(keyList, ServiceModuleProperty.class);
            return data;
        }
        return null;
    }
    //-----------------------service_module_property_end-------------------------

    //-----------------------product_to_service_module_begin-------------------------
    public static void cacheProductToServiceModuleByProductId(Long productId,List<ProductToServiceModule> data){
        String key = ServiceModuleCacheKeyUtils.getProductToServiceModuleByProductKey(productId);
        Set<ProductToServiceModule> set = new HashSet<>(data);
        RedisCacheUtil.setAdd(key, set, true);
        RedisCacheUtil.expireKey(key, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static List<ProductToServiceModule> getProductToServiceModuleByProductIdCache(Long productId){
        String key = ServiceModuleCacheKeyUtils.getProductToServiceModuleByProductKey(productId);
        Set<ProductToServiceModule> cacheSetData = RedisCacheUtil.setGetAll(key, ProductToServiceModule.class, false);
        if (cacheSetData != null && cacheSetData.size() > 0) {
            List<ProductToServiceModule> result = new ArrayList<>(cacheSetData);
            return result;
        }
        return null;
    }

    public static void batchRemoveProductToServiceModuleCache(List<Long> productIds){
        List<String> keys = productIds.stream().map(productId -> ServiceModuleCacheKeyUtils.getProductToServiceModuleByProductKey(productId)).collect(Collectors.toList());
        RedisCacheUtil.delete(keys);
    }

    public static void removeProductToServiceModuleCache(Long productId){
        List<Long> productIds = new ArrayList<>();
        productIds.add(productId);
        ServiceModuleCacheCoreUtils.batchRemoveProductToServiceModuleCache(productIds);
    }
    //-----------------------product_to_service_module_end-------------------------
//    private static <T>  ArrayList<Long> getServiceToCacheByServiceModuleId(ArrayList<Long> ids, List<T> result, Long tenantId,Class<T> valueClass,String key) {
//        ArrayList<Long> noCacheIds = new ArrayList<>();
//        ids.forEach(serviceModuleId -> {
//            String curKey = key+ tenantId + ":" + serviceModuleId;
//            Set<T> cacheData = RedisCacheUtil.setGetAll(curKey,valueClass,false);
//            if (cacheData != null && cacheData.size() > 0) {
//                result.addAll(cacheData);
//            } else {
//                noCacheIds.add(serviceModuleId);
//            }
//        });
//        return noCacheIds;
//    }
}
