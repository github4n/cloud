package com.iot.control.space.util;

import com.iot.control.space.domain.Space;
import com.iot.control.space.enums.MultiCacheKeyEnum;
import com.iot.control.space.enums.VersionEnum;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: chq
 * @Descrpiton:
 * @Date: 16:56 2018/6/21
 * @Modify by:
 */
public class SpaceBaseCacheUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(SpaceBaseCacheUtils.class);

    /**
     * @param spaceId
     * @Description: 删除单个空间下spaceDevice缓存
     * @return: void
     * @author: chq
     * @date: 2018/6/21 20:16
     **/
    public static void deleteCacheSpaceDevice(Long tenantId, Long spaceId) {
        try {
            if (spaceId == null) {
                return;
            }
            String spaceDevKey = RedisKeyUtil.getSpaceDevKey(tenantId, spaceId);
            RedisCacheUtil.delete(spaceDevKey);
        } catch (Exception e) {
            LOGGER.info("SpaceBaseCacheUtils-deleteCacheSpaceDevice-error", e);
        }
    }
    /**
     * @Description: 批量删除spaceDevice缓存
     *
     * @param spaceIds
     * @return: void
     * @author: chq
     * @date: 2018/8/4 12:23
     **/
    public static void  deleteCacheSpaceDevices(Long tenantId, List<Long> spaceIds) {
        try {
            if (CollectionUtils.isEmpty(spaceIds)) {
                return;
            }
            List<String> spaceDevKeys = RedisKeyUtil.getMultiKey(tenantId, VersionEnum.V1, spaceIds, MultiCacheKeyEnum.SPACE_DEVICE);
            RedisCacheUtil.delete(spaceDevKeys);
        } catch (Exception e) {
            LOGGER.info("SpaceBaseCacheUtils-deleteCacheSpaceDevices-error", e);
        }
    }

    /**
     * @Description: 批量删除space缓存
     *
     * @param spaceIds
     * @return: void
     * @author: chq
     * @date: 2018/8/4 12:23
     **/
    public static void deleteCacheSpaces(Long tenantId, List<Long> spaceIds) {
        try {
            if (CollectionUtils.isEmpty(spaceIds)) {
                return;
            }
            List<String> spaceKeys = RedisKeyUtil.getMultiKey(tenantId, VersionEnum.V1, spaceIds, MultiCacheKeyEnum.SPACE);
            RedisCacheUtil.delete(spaceKeys);
        } catch (Exception e) {
            LOGGER.info("SpaceBaseCacheUtils-deleteCacheSpaceDevices-error", e);
        }
    }

    /**
     * @param key
     * @param object
     * @Description: 将对象保存到list
     * @return: void
     * @author: chq
     * @date: 2018/6/21 20:16
     **/
    public static void saveCacheObjectToList(String key, Object object) {
        //排除list中已经存在的spaceId，去重
        if (RedisCacheUtil.hasKey(key)) {
            List cacheList = RedisCacheUtil.listGetAll(key, Object.class);
            cacheList.add(object);
            List listWithoutDup = new ArrayList(new HashSet(cacheList));
            RedisCacheUtil.listSet(key, listWithoutDup, RedisKeyUtil.LOST_EFFICACY_TIME, true);
        } else {
            List<Object> list = new ArrayList<>();
            list.add(object);
            RedisCacheUtil.listSet(key, list, RedisKeyUtil.LOST_EFFICACY_TIME, true);
        }
    }


    //将spaceDeviceIdlist放入相对应的spaceId
    public static void saveCacheSpaceDevListToSpaceDev(Long spaceId, List<Long> spaceDevIds) {
        LOGGER.info("saveDevListToSpaceDev-spaceId:{}", spaceId);
        if (spaceId == null || CollectionUtils.isEmpty(spaceDevIds)) {
            return;
        }
        try {
            Long tenantId = SaaSContextHolder.currentTenantId();
            String spaceDevIdKey = RedisKeyUtil.getSpaceDevIdBySpaceIdKey(tenantId, spaceId);
            RedisCacheUtil.listSet(spaceDevIdKey, spaceDevIds, RedisKeyUtil.LOST_EFFICACY_TIME, true);

        } catch (Exception e) {
            LOGGER.info("SpaceBaseServiceUtils-saveDevListToSpaceDev-error", e);
        }
    }

    /**
     * 根据空间Id获取设备列表
     * 调用：getHomeList
     * 可替换 spaceDeviceMapper.getDeviceIdBySpaceId(spaceResp.getId());
     *
     * @param spaceId
     * @return
     * @author chq
     * @date 2018/6/20 14:08
     */
    public static Set<Object> getCacheDeviceListBySpaceId(Long tenantId, Long spaceId) {
        try {
            if (spaceId == null) {
                return null;
            }
            LOGGER.info("SpaceCacheCoreUtils-tenantId:{}", tenantId);
            String spaceDevKey = RedisKeyUtil.getSpaceDevKey(tenantId, spaceId);
            return RedisCacheUtil.zSetReverseRangeAll(spaceDevKey);
        } catch (Exception e) {
            LOGGER.info("SpaceCacheCoreUtils-getCacheDeviceListBySpaceId-error", e);
        }
        return null;
    }

    /**
     * 根据空间Id获取SpaceDeviceId列表
     * 调用：getHomeList
     * 可替换 spaceDeviceMapper.getDeviceIdBySpaceId(spaceResp.getId());
     *
     * @param spaceId
     * @return
     * @author chq
     * @date 2018/6/20 14:08
     */
    public static List<Long> getCacheSpaceDevIdListBySpaceId(Long tenantId, Long spaceId) {
        try {
            if (spaceId == null) {
                return null;
            }
            LOGGER.info("SpaceCacheCoreUtils-tenantId:{}", tenantId);
            String spaceDevIdKey = RedisKeyUtil.getSpaceDevIdBySpaceIdKey(tenantId, spaceId);
            return RedisCacheUtil.listGetAll(spaceDevIdKey, Long.class);
        } catch (Exception e) {
            LOGGER.info("SpaceCacheCoreUtils-getCacheDeviceListBySpaceId-error", e);
        }
        return null;
    }

    /**
     * 更新space详细信息
     *
     * @param space
     * @return
     * @author chq
     * @date 2018/6/20 14:05
     */
    public static void updateCacheSpace(Space space) {
        try {
            if (space == null) {
                return;
            }
            Long tenantId = space.getTenantId();
            Long spaceId = space.getId();
            LOGGER.info("SpaceBaseCacheUtils-tenantId:{}", tenantId);
            String spaceKey = RedisKeyUtil.getSpaceKey(tenantId, spaceId);
            RedisCacheUtil.valueObjSet(spaceKey, space, RedisKeyUtil.LOST_EFFICACY_TIME);
        } catch (Exception e) {
            LOGGER.info("SpaceBaseCacheUtils-updateCacheSpace-error", e);
        }
    }

    /**
     * 删除space详细信息
     *
     * @param space
     * @return
     * @author chq
     * @date 2018/6/20 14:05
     */
    public static void delCacheSpace(Space space) {
        try {
            if (space == null) {
                return;
            }
            Long tenantId = space.getTenantId();
            Long spaceId = space.getId();
            LOGGER.info("SpaceBaseCacheUtils-tenantId:{}", tenantId);
            String spaceKey = RedisKeyUtil.getSpaceKey(tenantId, spaceId);
            RedisCacheUtil.delete(spaceKey);
        } catch (Exception e) {
            LOGGER.info("SpaceBaseCacheUtils-updateCacheSpace-error", e);
        }
    }

    public static Space getCacheSpace(Long tenantId, Long spaceId) {
        try {
            if (spaceId == null) {
                return null;
            }
            LOGGER.info("SpaceBaseCacheUtils-tenantId:{}", tenantId);
            String spaceKey = RedisKeyUtil.getSpaceKey(tenantId, spaceId);
            return RedisCacheUtil.valueObjGet(spaceKey, Space.class);
        } catch (Exception e) {
            LOGGER.info("SpaceBaseCacheUtils-getCacheSpace-error", e);
        }
        return null;
    }
}
