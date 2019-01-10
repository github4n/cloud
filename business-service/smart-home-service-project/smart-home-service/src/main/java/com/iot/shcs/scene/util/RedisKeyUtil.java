package com.iot.shcs.scene.util;

import com.iot.saas.SaaSContextHolder;

/**
 * 描述：scene redis key 工具类
 * 创建人： yuChangXing
 * 创建时间： 2018/07/24 09:40
 */
public class RedisKeyUtil {

    /**
     *  7天 单位秒
     */
    public static final long DEFAULT_EXPIRE_TIME_OUT = 604800;

    public static final String SCENE_WILL_DELETE = "scene-w-d:%d:%d";

    public static final String SCENE_USER_LIST = "scene-u-l:%d:%d";

    public static final String SCENE_DETAIL_LIST = "scene-d-l:%d:%d";

    public static final String SCENE_LIST = "scene-l:%d:%d";
    
    public static final String SCENE_GATEWAY_DEVICE_MAP = "scene-g-d-m:%d:%d";
    
    public static final String SCENE_EXTERNAL_DEVICE_LIST = "scene-e-d-l:%d:%d";

    /**
     *  将要删除的scene缓存key
     *
     * @param sceneId
     * @return
     */
    public static String getSceneWillDeleteIdKey(Long sceneId) {
        return String.format(SCENE_WILL_DELETE, sceneId, getTenantId());
    }

    /**
     *  用户scene列表 缓存key
     *
     * @param sceneId
     * @return
     */
    public static String getSceneListIdKey(Long sceneId) {
        return String.format(SCENE_LIST, sceneId, getTenantId());
    }

    /**
     *  用户scene列表 缓存key
     *
     * @param userId
     * @return
     */
    public static String getSceneUserListIdKey(Long userId) {
        return String.format(SCENE_USER_LIST, userId, getTenantId());
    }

    /**
     *  sceneId对应的 sceneDetail列表 缓存key
     *
     * @param sceneId
     * @return
     */
    public static String getSceneDetailListIdKey(Long sceneId) {
        return String.format(SCENE_DETAIL_LIST, sceneId, getTenantId());
    }
    
    /**
     *  用户scene关联网关子设备列表 缓存key
     *
     * @param sceneId
     * @return
     */
    public static String getSceneGatewayDeviceMapIdKey(Long sceneId) {
        return String.format(SCENE_GATEWAY_DEVICE_MAP, sceneId, getTenantId());
    }
    
    /**
     *  用户scene关联外接设备列表 缓存key
     *
     * @param sceneId
     * @return
     */
    public static String getSceneExternalDeviceListIdKey(Long sceneId) {
        return String.format(SCENE_EXTERNAL_DEVICE_LIST, sceneId, getTenantId());
    }

    private static Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }
}
