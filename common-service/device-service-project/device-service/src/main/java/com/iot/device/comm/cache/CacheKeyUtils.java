package com.iot.device.comm.cache;

import com.google.common.collect.Lists;
import com.iot.util.AssertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:51 2018/6/12
 * @Modify by:
 */
public class CacheKeyUtils {

    public static final long EXPIRE_TIME_OUT = 604800;//存储7天  秒为单位

    public static final String INIT_CACHE_KEY = "init:device_model:cache";
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheKeyUtils.class);

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
    public static <T> List<String> getMultiKey(Long tenantId, VersionEnum versionEnum, List<T> origKeys, MultiCacheKeyEnum multiCacheKeyEnum) {
        AssertUtils.notNull(tenantId, "tenantId.is.not.null");
        List<String> keys = Lists.newArrayList();
        if (CollectionUtils.isEmpty(origKeys)) {
            return null;
        }
        for (T key : origKeys) {
            if (MultiCacheKeyEnum.DEVICE_INFO.equals(multiCacheKeyEnum)) {
                keys.add(getDeviceInfoKey(tenantId, versionEnum, (String) key));
            } else if (MultiCacheKeyEnum.DEVICE_EXTENDS.equals(multiCacheKeyEnum)) {
                keys.add(getDeviceExtendsKey(tenantId, versionEnum, (String) key));
            } else if (MultiCacheKeyEnum.DEVICE_STATUS.equals(multiCacheKeyEnum)) {
                keys.add(getDeviceStatusKey(tenantId, versionEnum, (String) key));
            } else if (MultiCacheKeyEnum.DEVICE_STATE.equals(multiCacheKeyEnum)) {
                keys.add(getDeviceStateKey(tenantId, versionEnum, (String) key));
            } else if (MultiCacheKeyEnum.PRODUCT_INFO.equals(multiCacheKeyEnum)) {
                keys.add(getProductInfoKey(tenantId, versionEnum, (Long) key));
            } else if (MultiCacheKeyEnum.DEVICE_TYPE.equals(multiCacheKeyEnum)) {
                keys.add(getDeviceTypeKey(versionEnum, (Long) key));
            }
        }
        return keys;
    }


    /**
     * 获取设备明细 key[tenantId:device:v:deviceId]
     *
     * @param tenantId    租户
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceInfoKey(Long tenantId, VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("device").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }


    /**
     * 获取父级 设备下的子设备 key[device:v:parent:parentDeviceId]
     *
     * @param parentDeviceId
     * @return
     * @author lucky
     * @date 2018/6/21 19:37
     */
    public static String getDeviceInfoKey(VersionEnum versionEnum, String parentDeviceId) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("device").append(":").append(versionEnum.toString()).append(":parent:").append(parentDeviceId);
        return sb.toString();
    }


    /**
     * 获取设备扩展明细 key[tenantId:device_extends:v:deviceId]
     *
     * @param tenantId    租户
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceExtendsKey(Long tenantId, VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("device_extends").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }

    /**
     * 获取设备动态状态明细 key[tenantId:device_status:v:deviceId]
     *
     * @param tenantId    租户
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceStatusKey(Long tenantId, VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append(tenantId).append(":");
        sb.append("device_status").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }

    /**
     * 获取设备属性状态明细 key[tenantId:device_state:v:deviceId]
     *
     * @param tenantId    租户
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceStateKey(Long tenantId, VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("device_state").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }

    /**
     * 获取设备属性状态明细 key[device_state:v:deviceId]
     *
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceStateKey(VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_state").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }

    /**
     * 获取设备版本明细 key[device_to_version:v:deviceId]
     *
     * @param versionEnum 版本
     * @param deviceId    设备id
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceToVersionKey(VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_to_version").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }

    /**
     * 获取版本 hash key
     *
     * @param fwType 固件类型
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getDeviceToVersionHashKey(String fwType) {
        return fwType;
    }

    /**
     * 获取用户设备 key[tenantId:user_device:v:list]
     *
     * @param tenantId    租户
     * @param versionEnum 版本
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getUserDeviceKey(Long tenantId, VersionEnum versionEnum) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("user_device").append(":").append(versionEnum.toString()).append(":").append("list");
        return sb.toString();
    }

    /**
     * 获取用户设备 key[tenantId:user_device:v:orgId:userId]
     *
     * @param tenantId    租户
     * @param versionEnum 版本
     * @param orgId       组织id
     * @param userId      用户
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getUserDeviceKey(Long tenantId, VersionEnum versionEnum, Long orgId, Long userId) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("user_device").append(":").append(versionEnum.toString()).append(":");
//        sb.append(orgId).append(":");
        sb.append(userId);
        return sb.toString();
    }


    /**
     * 获取用户设备 key 设备找用户的关系[]
     *
     * @param versionEnum
     * @param deviceId
     * @return
     * @author lucky
     * @date 2018/6/21 10:59
     */
    public static String getUserDeviceKey(VersionEnum versionEnum, String deviceId) {
        StringBuilder sb = new StringBuilder();
        sb.append("user_device").append(":").append(versionEnum.toString()).append(":").append(deviceId);
        return sb.toString();
    }

    /**
     * 获取用户设备 key[orgId:userId]
     *
     * @param orgId  组织id
     * @param userId 用户
     * @return
     * @author lucky
     * @date 2018/6/12 16:55
     */
    public static String getUserDeviceHashKey(Long orgId, Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(orgId).append(":").append(userId);
        return sb.toString();
    }

    /**
     * 获取设备类型key
     *
     * @param versionEnum
     * @param deviceTypeId
     * @return
     * @author lucky
     * @date 2018/6/20 16:37
     */
    public static String getDeviceTypeKey(VersionEnum versionEnum, Long deviceTypeId) {
        StringBuilder sb = new StringBuilder();
        sb.append("device_type").append(":").append(versionEnum.toString()).append(":").append(deviceTypeId);
        return sb.toString();
    }


    /**
     * 获取产品的key
     *
     * @param tenantId
     * @param versionEnum
     * @param productId
     * @return
     * @author lucky
     * @date 2018/6/20 17:07
     */
    public static String getProductInfoKey(Long tenantId, VersionEnum versionEnum, Long productId) {
        StringBuilder sb = new StringBuilder();
//        sb.append(tenantId).append(":");
        sb.append("product").append(":").append(versionEnum.toString()).append(":").append(productId);
        return sb.toString();
    }

    /**
     * 获取产品的key
     *
     * @param versionEnum
     * @param productModel
     * @return
     * @author lucky
     * @date 2018/6/20 17:07
     */
    public static String getProductInfoKey(VersionEnum versionEnum, String productModel) {
        StringBuilder sb = new StringBuilder();
        sb.append("product").append(":").append(versionEnum.toString()).append(":").append(productModel);
        return sb.toString();
    }

    /**
     * @despriction：获取uuid_apply_record自增主键
     * @author  yeshiyuan
     * @created 2018/6/29 15:41
     * @return
     */
    public static String getTableOfUuidApplyRecordKey(){
        return "max-row-id:uuid_apply_record";
    }

}
