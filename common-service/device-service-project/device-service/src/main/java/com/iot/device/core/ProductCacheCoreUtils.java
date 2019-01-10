package com.iot.device.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iot.device.comm.cache.CacheKeyUtils;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.Product;
import com.iot.redis.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:05 2018/6/20
 * @Modify by:
 */
@Slf4j
public class ProductCacheCoreUtils {

    public static void removeCacheAll() {
        try {
            String redisKey = "product:" + "*";
            Set<String> keys = RedisCacheUtil.keys(redisKey);
            if (!CollectionUtils.isEmpty(keys)) {
                RedisCacheUtil.delete(keys);
            }
        } catch (Exception e) {
            log.error("ProductCacheCoreUtils-removeCacheAll-error", e);
        }
    }

    /*******************************######2018-9-28#######新的缓存修改#############*****************************************/

    public static List<Product> getCacheDataList(List<Long> targetIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        targetIds.forEach(targetId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("product").append(":").append(version.toString()).append(":").append(targetId);
            redisKeys.add(sb.toString());
        });
        List<Product> resultDataList = RedisCacheUtil.mget(redisKeys, Product.class);
        return resultDataList;
    }

    public static List<Long> getCacheDataIdList(List<String> targetModels, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetModels)) {
            return Lists.newArrayList();
        }
        List<String> redisKeys = Lists.newArrayList();
        targetModels.forEach(targetId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("product").append(":").append(version.toString()).append(":").append("model:").append(targetId);
            redisKeys.add(sb.toString());
        });
        List<Long> resultDataList = RedisCacheUtil.mget(redisKeys, Long.class);
        return resultDataList;
    }


    public static Product getCacheData(Long targetId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();

        sb.append("product").append(":").append(version.toString()).append(":").append(targetId);
        String redisKey = sb.toString();
        Product resultData = RedisCacheUtil.valueObjGet(redisKey, Product.class);
        return resultData;
    }

    public static Long getCacheData(String targetId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(version.toString()).append(":").append("model:").append(targetId);
        String redisKey = sb.toString();
        Long resultData = RedisCacheUtil.valueObjGet(redisKey, Long.class);
        return resultData;
    }

    public static void cacheData(Long productId, Product target, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(version.toString()).append(":").append(productId);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, target);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheData(String targetProductModelKey, Long targetProductIdValue, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(version.toString()).append(":").append("model:").append(targetProductModelKey);
        String redisKey = sb.toString();
        RedisCacheUtil.valueObjSet(redisKey, targetProductIdValue);
        RedisCacheUtil.expireKey(redisKey, CacheKeyUtils.EXPIRE_TIME_OUT);
    }

    public static void cacheDataList(List<Product> targetList, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetList)) {
            return;
        }
        targetList.forEach(product -> {
            Long targetId = product.getId();
            String productModel = product.getModel();
            cacheData(targetId, product, version);
            cacheData(productModel, targetId, version);
        });

    }

    public static void removeData(String targetProductModelKey, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(version.toString()).append(":").append("model:").append(targetProductModelKey);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeData(Long productId, VersionEnum version) {
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(version.toString()).append(":").append(productId);
        String redisKey = sb.toString();
        RedisCacheUtil.delete(redisKey);
    }

    public static void removeDataProductIds(List<Long> targetIds, VersionEnum version) {
        if (CollectionUtils.isEmpty(targetIds)) {
            return;
        }
        Set<String> targetRedisKeys = Sets.newLinkedHashSet();
        targetIds.forEach(targetId -> {
            StringBuilder sb = new StringBuilder();
            sb.append("product").append(":").append(version.toString()).append(":").append(targetId);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }

    public static void removeDataProductModels(List<String> productModelKeys, VersionEnum version) {
        if (CollectionUtils.isEmpty(productModelKeys)) {
            return;
        }
        Set<String> targetRedisKeys = Sets.newLinkedHashSet();
        productModelKeys.forEach(productModel -> {
            StringBuilder sb = new StringBuilder();
            sb.append("product").append(":").append(version.toString()).append(":").append("model:").append(productModel);
            String redisKey = sb.toString();
            targetRedisKeys.add(redisKey);
        });
        RedisCacheUtil.delete(targetRedisKeys);
    }

    public static boolean exist(Long productId, VersionEnum version) {

        if (productId == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(version.toString()).append(":").append(productId);
        String redisKey = sb.toString();
        return RedisCacheUtil.hasKey(redisKey);
    }
}
