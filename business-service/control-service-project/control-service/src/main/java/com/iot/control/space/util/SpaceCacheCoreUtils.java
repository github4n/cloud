package com.iot.control.space.util;

import com.iot.control.space.domain.Space;
import com.iot.control.space.enums.MultiCacheKeyEnum;
import com.iot.control.space.enums.SpaceEnum;
import com.iot.control.space.enums.VersionEnum;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: chq
 * @Descrpiton:
 * @Date: 16:56 2018/6/21
 * @Modify by:
 */
public class SpaceCacheCoreUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(SpaceCacheCoreUtils.class);

    /**
     * 获取缓存空间列表信息，Homelist,roomlist
     *
     * @param offset
     * @return
     * @author chq
     * @date 2018/6/20 13:59
     */
    public static List<Long> getCacheSpaceListByPage(int offset, int pageSize, Long tenantId, Long parentId, Long userId) {
        try {
            int start = offset * pageSize;
            int end = offset * pageSize + pageSize - 1;
            if (userId == null) {
                userId = SaaSContextHolder.getCurrentUserId();
            }
            String spaceListKey;
            if (parentId == null || parentId == -1) {
                spaceListKey = RedisKeyUtil.getHomeslistKey(tenantId, userId);
            } else {
                spaceListKey = RedisKeyUtil.getRoomlistKey(tenantId, parentId);
            }
            List<Long> spaceIdlist = null;
            if (RedisCacheUtil.hasKey(spaceListKey)) {
                if (start == 0) {
                    spaceIdlist = RedisCacheUtil.listGet(spaceListKey, Long.class, start, end - 1);
                } else {
                    spaceIdlist = RedisCacheUtil.listGet(spaceListKey, Long.class, start, end);
                }
            }
            return spaceIdlist;
        } catch (Exception e) {
            LOGGER.info("SpaceCacheCoreUtils-getCacheSpaceListByPage-error", e);
        }
        return null;
    }

    /**
     * 批量获取房间、家庭详情列表
     * 调用：getHomeList
     *
     * @param spaceIds
     * @return
     * @author chq
     * @date 2018/6/20 14:08
     */
    public static List<Space> getCacheSpaceInfoListBySpaceIds(Long tenantId, List<Long> spaceIds) {
        try {
            if (CollectionUtils.isEmpty(spaceIds)) {
                return null;
            }
            LOGGER.info("SpaceCacheCoreUtils-tenantId:{}", tenantId);
            List<String> spaceKeysList = RedisKeyUtil.getMultiKey(tenantId, VersionEnum.V1, spaceIds, MultiCacheKeyEnum.SPACE);
            return RedisCacheUtil.mget(spaceKeysList, Space.class);
        } catch (Exception e) {
            LOGGER.info("SpaceCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return null;
    }

    /**
     * @param space
     * @param isAddToRedis
     * @Description: 保存/删除space对象，并将相对应的spaceId加入/删除相对应的spaceList
     * @return: void
     * @author: chq
     * @date: 2018/6/21 20:15
     **/
    public static void saveOrDeleteCacheSpace(Space space, Boolean isAddToRedis) {
        //放入space对象缓存,有效期7天
        try {
            if (space == null) {
                return;
            }
            Long tenantId = space.getTenantId();
            Long userId = space.getUserId();
            if (space.getUserId() == null) {
                userId = SaaSContextHolder.getCurrentUserId();
            }
            LOGGER.info("SpaceCacheCoreUtils-tenantId:{}-userId:{}", tenantId, userId);
            Long spaceId = space.getId();
            String spaceKey = RedisKeyUtil.getSpaceKey(tenantId, spaceId);
            if (isAddToRedis) {
                RedisCacheUtil.valueObjSet(spaceKey, space, RedisKeyUtil.LOST_EFFICACY_TIME);
            } else {
                RedisCacheUtil.delete(spaceKey);
            }
            if (SpaceEnum.HOME.getCode().equals(space.getType())) {
                String homeListKey = RedisKeyUtil.getHomeslistKey(tenantId, userId);
                //删除spacelist
                RedisCacheUtil.delete(homeListKey);
            } else if (SpaceEnum.ROOM.getCode().equals(space.getType())) {
                String roomListKey = RedisKeyUtil.getRoomlistKey(tenantId, space.getParentId());
                //删除spacelist
                RedisCacheUtil.delete(roomListKey);

            }
        } catch (Exception e) {
            LOGGER.info("SpaceCacheCoreUtils-saveOrDeleteCacheSpace-error", e);
        }
    }

    /**
     * @param spaces
     * @param isAddToRedis
     * @Description: 批量保存/删除space对象，并将相对应的spaceId加入/删除相对应的spaceList
     * @return: void
     * @author: chq
     * @date: 2018/6/22 10:15
     **/
    public static List<Long> saveOrDeleteCacheSpaces(List<Space> spaces, Boolean isAddToRedis) {
        try {
            if (CollectionUtils.isEmpty(spaces)) {
                return null;
            }
            Map<String, Object> spaceMap = new HashMap<>();
            List<String> keyList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            List<Long> spaceIds = new ArrayList<>();
            spaces.forEach(space -> {
                Long tenantId = space.getTenantId();
                String spaceKey = RedisKeyUtil.getSpaceKey(tenantId, space.getId());
                spaceMap.put(spaceKey, space);
                keyList.add(spaceKey);
                spaceIds.add(space.getId());
                if (SpaceEnum.HOME.getCode().equals(space.getType())) {
                    String homeListKey = RedisKeyUtil.getHomeslistKey(tenantId, space.getUserId());
                    //删除spacelist
                    list.add(homeListKey);
                } else if (SpaceEnum.ROOM.getCode().equals(space.getType())) {
                    String roomListKey = RedisKeyUtil.getRoomlistKey(tenantId, space.getParentId());
                    //删除spacelist
                    list.add(roomListKey);

                }
            });
            if (isAddToRedis) {
                RedisCacheUtil.multiSet(spaceMap, RedisKeyUtil.LOST_EFFICACY_TIME);
            } else {
            }
            RedisCacheUtil.delete(list);
            return spaceIds;
        } catch (Exception e) {
            LOGGER.info("SpaceCacheCoreUtils-saveOrDeleteCacheSpaces-error", e);
        }
        return null;
    }

    public static void saveOrDeleteCacheSpacesAndList(List<Space> spaces, Boolean isAddToRedis) {
        try {
            if (CollectionUtils.isEmpty(spaces)) {
                return;
            }
            //保存space对象
            List<Long> spaceIds = saveOrDeleteCacheSpaces(spaces, isAddToRedis);
            Long parentId = spaces.get(0).getParentId();
            Long tenantId = spaces.get(0).getTenantId();
            Long userId = spaces.get(0).getUserId();
            String spaceListKey;
            if (parentId == null || parentId == -1) {
                spaceListKey = RedisKeyUtil.getHomeslistKey(tenantId, userId);
            } else {
                spaceListKey = RedisKeyUtil.getRoomlistKey(tenantId, parentId);
            }
            if (isAddToRedis) {
                RedisCacheUtil.listSet(spaceListKey, spaceIds, RedisKeyUtil.LOST_EFFICACY_TIME, true);
            } else {
                RedisCacheUtil.delete(spaceListKey);
            }
        }catch (Exception e){
            LOGGER.info("SpaceCacheCoreUtils-saveOrDeleteCacheSpacesAndList-error", e);
        }
    }

}
