package com.iot.device.core;


import com.iot.common.util.StringUtil;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.Group;
import com.iot.device.vo.rsp.group.GetGroupDetailResp;
import com.iot.device.vo.rsp.group.GetGroupInfoResp;
import com.iot.redis.RedisCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCacheCoreUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(GroupCacheCoreUtils.class);

    /**
     *  7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT = 604800;

    public static final String GROUP_USER_LIST = "group-u-l";
    public static final String GROUP = "group";
    public static final String GROUP_DETAIL_LIST = "group_detail-l";
    public static final String GROUP_DETAIL_DEV_LIST = "group_detail_dev-l";




    public static String getGroupUserListKey(Long tenantId, VersionEnum versionEnum, Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(":");
        sb.append(GROUP_USER_LIST).append(":").append(versionEnum.toString()).append(":").append(userId);
        return sb.toString();
    }

    public static String getGroupKey(Long tenantId, VersionEnum versionEnum, Long groupId) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(":");
        sb.append(GROUP).append(":").append(versionEnum.toString()).append(":").append(groupId);
        return sb.toString();
    }

    public static String getGroupDetailListKey(Long tenantId, VersionEnum versionEnum, Long groupId) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(":");
        sb.append(GROUP_DETAIL_LIST).append(":").append(versionEnum.toString()).append(":").append(groupId);
        return sb.toString();
    }

    public static String getGroupDetailDevListKey(Long tenantId, VersionEnum versionEnum, String devId) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(":");
        sb.append(GROUP_DETAIL_DEV_LIST).append(":").append(versionEnum.toString()).append(":").append(devId);
        return sb.toString();
    }







    /**
     * 获取缓存group
     *
     * @param groupId
     * @return
     */
    public static Group getCacheGroupByGroupId(Long tenantId, Long groupId) {
        try {
            if (groupId == null) {
                return null;
            }

            String redisKey = getGroupKey(tenantId, VersionEnum.V1, groupId);
            Group group = RedisCacheUtil.valueObjGet(redisKey, Group.class);
            return group;
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return null;
    }

    /**
     * 保存缓存group
     *
     * @param groupId
     * @return
     */
    public static void saveCacheGroup(Long tenantId, Long groupId, Group group) {
        try {
            if (groupId == null) {
                return ;
            }
            String redisKey = getGroupKey(tenantId, VersionEnum.V1, groupId);
            RedisCacheUtil.valueObjSet(redisKey, group, DEFAULT_EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-saveCacheGroup-error", e);
        }
    }

    /**
     * 批量保存groups
     *
     * @param list
     * @return
     */
    public static void saveCacheGroups(Long tenantId, List<GetGroupInfoResp> list) {
        if (CollectionUtils.isEmpty(list)) {
            return ;
        }
        try {
            Map groupMap = new HashMap();
            list.forEach(group->{
                Long groupId = group.getId();
                String redisKey = getGroupKey(tenantId, VersionEnum.V1, groupId);
                groupMap.put(redisKey, group);
            });
            RedisCacheUtil.multiSet(groupMap, DEFAULT_EXPIRE_TIME_OUT);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-saveCacheGroups-error", e);
        }
    }

    /**
     * 批量获取缓存group
     *
     * @param groupIds
     * @return
     */
    public static List<GetGroupInfoResp> getCacheGroupByGroupId(Long tenantId, List<Map> groupIds) {
        try {
            if (CollectionUtils.isEmpty(groupIds)) {
                return null;
            }
            List<String> groupIdsKey = new ArrayList<>();
            groupIds.forEach(group -> {
                Long groupId = new Long(group.get("groupId").toString());
                groupIdsKey.add(getGroupKey(tenantId, VersionEnum.V1, groupId));
            });
            return RedisCacheUtil.mget(groupIdsKey, GetGroupInfoResp.class);

        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return null;
    }

    /**
     * 批量删除缓存group
     *
     * @param groupIds
     * @return
     */
    public static void delCacheGroupByGroupId(Long tenantId, List<Map> groupIds) {
        try {
            if (CollectionUtils.isEmpty(groupIds)) {
                return ;
            }
            List<String> groupIdsKey = new ArrayList<>();
            groupIds.forEach(group -> {
                Long groupId = new Long(group.get("groupId").toString());
                groupIdsKey.add(getGroupKey(tenantId, VersionEnum.V1, groupId));
            });
            RedisCacheUtil.delete(groupIdsKey);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
    }



    /**
     * 获取用户家庭下groupIds
     *
     * @param userId
     * @return
     */
    public static List<Map> getCacheGroupIdListByUser(Long tenantId, Long userId) {
        if (userId == null) {
            return null;
        }
        try {
            String redisKey = getGroupUserListKey(tenantId, VersionEnum.V1, userId);
            List<Map> groupIdList = RedisCacheUtil.listGetAll(redisKey, Map.class);
            return groupIdList;
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheGroupIdListByUser-error", e);
        }
        return null;
    }

    /**
     * 保存用户家庭下groupIds
     *
     * @param userId
     * @return
     */
    public static void saveCacheGroupIdListByUser(Long tenantId, Long userId, List<GetGroupInfoResp> list) {
        if (userId == null) {
            return ;
        }
        try {
            String redisKey = getGroupUserListKey(tenantId, VersionEnum.V1, userId);
            List listCach = new ArrayList();
            list.forEach(m->{
                Map map = new HashMap();
                map.put("groupId", m.getId());
                listCach.add(map);
            });
            RedisCacheUtil.listSet(redisKey, listCach, DEFAULT_EXPIRE_TIME_OUT, true);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-saveCacheGroupIdListByUser-error", e);
        }
    }

    /**
     * 删除用户家庭下groupIds
     *
     * @param userId
     * @return
     */
    public static void delCacheGroupIdListByUser(Long tenantId, Long userId) {
        if (userId == null) {
            return ;
        }
        try {
            String redisKey = getGroupUserListKey(tenantId, VersionEnum.V1, userId);
            RedisCacheUtil.delete(redisKey);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-delCacheGroupIdListByUser-error", e);
        }
    }







    /**
     * 获取缓存groupDetail
     *
     * @param groupId
     * @return
     */
    public static List<GetGroupDetailResp> getCacheGroupDetailByGroupId(Long tenantId, Long groupId) {
        try {
            if (groupId == null) {
                return null;
            }
            String redisKey = getGroupDetailListKey(tenantId, VersionEnum.V1, groupId);
            return RedisCacheUtil.listGetAll(redisKey, GetGroupDetailResp.class);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return null;
    }

    /**
     * 通过groupId保存缓存group
     *
     * @param groupId
     * @return
     */
    public static void saveCacheGroupDetailByGroupId(Long tenantId, Long groupId, List<GetGroupDetailResp> groupDetails) {
        try {
            if (groupId == null) {
                return ;
            }
            String redisKey = getGroupDetailListKey(tenantId, VersionEnum.V1, groupId);
            RedisCacheUtil.listSet(redisKey, groupDetails, DEFAULT_EXPIRE_TIME_OUT, true);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-saveCacheGroup-error", e);
        }
    }

    /**
     * 根据group删除缓存groupDetail
     *
     * @param groupId
     * @return
     */
    public static void delCacheGroupDetailByGroupId(Long tenantId, Long groupId) {
        try {
            if (groupId == null) {
                return ;
            }
            String redisKey = getGroupDetailListKey(tenantId, VersionEnum.V1, groupId);
            RedisCacheUtil.delete(redisKey);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
    }

    /**
     * 根据groups删除缓存groupDetail
     *
     * @param groupIds
     * @return
     */
    public static void delCacheGroupDetailByGroupId(Long tenantId, List<Long> groupIds) {
        try {
            if (CollectionUtils.isEmpty(groupIds)) {
                return ;
            }
            List<String> groupIdsKey = new ArrayList<>();
            groupIds.forEach(groupId->{
                groupIdsKey.add(getGroupDetailListKey(tenantId, VersionEnum.V1, groupId));
            });
            RedisCacheUtil.delete(groupIdsKey);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
    }





    /**
     * 获取dev缓存groupDetail
     *
     * @param devId
     * @return
     */
    public static List<GetGroupDetailResp> getCacheGroupDetailByDevId(Long tenantId, String devId) {
        try {
            if (StringUtil.isEmpty(devId)) {
                return null;
            }

            String redisKey = getGroupDetailDevListKey(tenantId, VersionEnum.V1, devId);
            return RedisCacheUtil.listGetAll(redisKey, GetGroupDetailResp.class);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
        return null;
    }

    /**
     * 通过devId保存缓存groupDetail
     *
     * @param devId
     * @return
     */
    public static void saveCacheGroupDetailByDevId(Long tenantId, String devId, List<GetGroupDetailResp> groupDetails) {
        try {
            if (StringUtil.isEmpty(devId)) {
                return ;
            }
            String redisKey = getGroupDetailDevListKey(tenantId, VersionEnum.V1, devId);
            RedisCacheUtil.listSet(redisKey, groupDetails, DEFAULT_EXPIRE_TIME_OUT, true);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-saveCacheGroup-error", e);
        }
    }

    /**
     * 通过devId删除缓存groupDetail
     *
     * @param devId
     * @return
     */
    public static void delCacheGroupDetailByDevId(Long tenantId, String devId) {
        try {
            if (StringUtil.isEmpty(devId)) {
                return ;
            }
            String redisKey = getGroupDetailDevListKey(tenantId, VersionEnum.V1, devId);
            RedisCacheUtil.delete(redisKey);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
    }


    /**
     * 批量删除缓存groupDetail
     *
     * @param devIds
     * @return
     */
    public static void delCacheGroupDetailByDevIds(Long tenantId, List<String> devIds) {
        try {
            if (CollectionUtils.isEmpty(devIds)) {
                return ;
            }
            List<String> devIdsKey = new ArrayList<>();
            devIds.forEach(devId->{
                devIdsKey.add(getGroupDetailDevListKey(tenantId, VersionEnum.V1, devId));
            });
            RedisCacheUtil.delete(devIdsKey);
        } catch (Exception e) {
            LOGGER.info("GroupCacheCoreUtils-getCacheDeviceInfoByDeviceId-error", e);
        }
    }
}
