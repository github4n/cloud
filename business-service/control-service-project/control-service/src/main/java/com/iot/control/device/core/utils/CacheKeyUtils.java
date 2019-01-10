package com.iot.control.device.core.utils;

import com.google.common.collect.Lists;
import com.iot.control.space.enums.MultiCacheKeyEnum;
import com.iot.control.space.enums.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;


@Slf4j
public class CacheKeyUtils {
    public static final String BUSINESS_KEY_NAME = "user_device";
    public static final long EXPIRE_TIME_OUT = 604800;//存储7天  秒为单位

    /**
     * 批量拼装key
     *
     * @param tenantId
     * @param versionEnum
     * @param origKeys
     * @param multiCacheKeyEnum
     * @return
     * @author lucky
     * @date 2018/6/20 10:00
     */
    public static <T> List<String> getMultiKey(Long tenantId, Long orgId, Long userId, VersionEnum versionEnum, List<T> origKeys, MultiCacheKeyEnum multiCacheKeyEnum) {
        List<String> keys = Lists.newArrayList();
        if (CollectionUtils.isEmpty(origKeys)) {
            return keys;
        }
        for (T key : origKeys) {
            if (MultiCacheKeyEnum.USER_DEVICE.equals(multiCacheKeyEnum)) {
                keys.add(getUserDeviceInfoKey(tenantId, orgId, userId, versionEnum, (String) key));
                continue;
            }
        }
        return keys;
    }

    /**
     * key
     * [tenantId:orgId:userId:user_device:v:deviceId]、[tenantId:orgId:userId:user_device:v]
     * 、[tenantId:userId:user_device:v:deviceId]、[tenantId:userId:user_device:v]
     * 、[tenantId:orgId:user_device:v:deviceId]、[tenantId:orgId:user_device:v]
     * 、[tenantId:user_device:v:deviceId]、[tenantId:user_device:v]
     *
     * @param tenantId    租户
     * @param orgId       所属组织id 可选
     * @param userId      所属用户 可选
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getUserDeviceInfoKey(Long tenantId, Long orgId, Long userId, VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append(BUSINESS_KEY_NAME).append(":").append(versionEnum.toString());

        if (userId != null) {
            sb.append(":").append(userId);
        }

        if (!StringUtils.isEmpty(deviceId)) {
            sb.append(":").append(deviceId);
        }
        return sb.toString();
    }

    public static String getDeviceIdKey(Long tenantId, String deviceId, VersionEnum versionEnum) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append(BUSINESS_KEY_NAME).append(":deviceId:").append(":").append(versionEnum.toString());

        if (!StringUtils.isEmpty(deviceId)) {
            sb.append(":").append(deviceId);
        }
        return sb.toString();
    }
}
