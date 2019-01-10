package com.iot.control.space.util;

import com.google.common.collect.Lists;
import com.iot.control.space.enums.MultiCacheKeyEnum;
import com.iot.control.space.enums.VersionEnum;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 描述：redis key 生成工具类
 * 创建人： CHQ
 * 创建时间： 2018/6/14 13:40
 */
public class RedisKeyUtil {
    public static final Long LOST_EFFICACY_TIME = 60 * 60 * 24 * 7L;
    public static final String SPACE_KEY = ":space:";
    public static final String HOMESLIST_KEY = ":homelist:";
    public static final String ROOMLIST_KEY = ":roomlist:";
    public static final String SPACE_DEVICE_KEY = ":space_device:";
    public static final String VERSION = "v1:";
    public static final String SPACE_DEVICE_ID_KEY = "space_device_id:";
    public static final String SPACE_STATUS_KEY = ":space_status:";

    public static String getSpaceKey(Long tenantId, Long spaceId) {
        return tenantId + SPACE_KEY + VERSION + spaceId;
    }

    public static String getHomeslistKey(Long tenantId, Long userId) {
        return tenantId + HOMESLIST_KEY + VERSION + userId;
    }

    public static String getRoomlistKey(Long tenantId, Long homeId) {
        return tenantId + ROOMLIST_KEY + VERSION + homeId;
    }

    public static String getSpaceDevKey(Long tenantId, Long spaceId) {
        return tenantId + SPACE_DEVICE_KEY + VERSION + spaceId;
    }

    public static String getSpaceDevIdBySpaceIdKey(Long tenantId, Long spaceId) {
        return SPACE_DEVICE_ID_KEY + VERSION + spaceId;
    }

    public static String getSpaceDevInfoKey(Long tenantId, Long spaceDeviceId) {
        return SPACE_DEVICE_KEY + VERSION + spaceDeviceId;
    }
    
    public static String getSpaceStatusKey(Long tenantId, Long spaceId) {
        return tenantId + SPACE_STATUS_KEY + VERSION + spaceId;
    }

    public static List<String> getMultiKey(Long tenantId, VersionEnum versionEnum, List<Long> list, MultiCacheKeyEnum multiCacheKeyEnum) {
        List<String> keys = Lists.newArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (Long id : list) {
            if (MultiCacheKeyEnum.SPACE.equals(multiCacheKeyEnum)) {
                keys.add(getSpaceKey(tenantId, id));
            } else if (MultiCacheKeyEnum.SPACE_DEVICE_ID.equals(multiCacheKeyEnum)) {
                keys.add(getSpaceDevIdBySpaceIdKey(tenantId, id));
            } else if (MultiCacheKeyEnum.SPACE_DEVICE.equals(multiCacheKeyEnum)) {
                keys.add(getSpaceDevKey(tenantId,id));
            }
        }
        return keys;
    }

}
