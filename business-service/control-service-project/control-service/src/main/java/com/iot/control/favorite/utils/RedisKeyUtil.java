package com.iot.control.favorite.utils;

import com.iot.saas.SaaSContextHolder;

public class RedisKeyUtil {

    /**
     *  7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT = 604800;

    //根据用户id存的favorite
    public static final String FAVORITE_LIST_USER = "%d:favorite_list_user:%d";

    //根据space_id存的favorite
    public static final String FAVORITE_LIST_SPACE = "%d:favorite_list_space:%d";

    public static String getFavoriteListUser(Long userId,Long tenantId) {
        return String.format(FAVORITE_LIST_USER, tenantId, userId);
    }

    public static String getFavoriteListSpace(Long spaceId,Long tenantId) {
        return String.format(FAVORITE_LIST_SPACE, tenantId, spaceId);
    }
}
