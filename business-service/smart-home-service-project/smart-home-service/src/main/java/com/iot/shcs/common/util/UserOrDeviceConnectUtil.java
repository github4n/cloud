package com.iot.shcs.common.util;

import com.iot.common.util.StringUtil;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.device.entity.DeviceStatus;
import com.iot.shcs.device.enums.OnlineStatusEnum;
import com.iot.shcs.device.enums.VersionEnum;
import com.iot.user.vo.FetchUserResp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * 项目名称：IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人：wucheng
 * 创建时间 2018/12/6 20:00
 */
public class UserOrDeviceConnectUtil {


    public static Long isUpdateStatus(String deviceId, Long tenantId, MqttMsg connectMsg) {
        Long currentTime = new Date().getTime();
        try {
            // 获取redisKey
            String redisKey = getDeviceConnectRedisKey(deviceId, tenantId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 获取payload
            Map<String, Object> payload = (Map<String, Object>) connectMsg.getPayload();
            // 判断是否存在payload
            if (payload != null && !payload.isEmpty() && payload.containsKey("timestamp")) {
                String currentTimeStr = payload.get("timestamp").toString(); // 获取信息中的时间
                if (StringUtil.isNotBlank(currentTimeStr)) {
                    currentTime = sdf.parse(currentTimeStr).getTime();
                }
            }
            // 判断reids中是否存在已缓存的信息
            if (RedisCacheUtil.hasKey(redisKey)) {
                DeviceStatus resultData = RedisCacheUtil.valueObjGet(redisKey, DeviceStatus.class);
                Long beforeTime =  resultData.getLastLoginTime().getTime();
                if (currentTime < beforeTime) {
                    return null;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTime;
    }

    /**
     *@description 是否要去更新时间
     *@author wucheng
     *@params [userId, connectMsg]
     *@create 2018/12/6 19:26
     *@return boolean
     */
    public static boolean isUpdateStatus(Long userId, String status, MqttMsg connectMsg) {
        try {
            // 获取是否是connect、disconnect
            String redisKey = getUserConnectStatusRedisKey(userId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            // 获取payload
            Map<String, Object> payload = (Map<String, Object>) connectMsg.getPayload();
            Long currentTime = new Date().getTime();
            if (payload != null && payload.containsKey("timestamp")) {
                String currentTimeStr = payload.get("timestamp").toString(); // 获取信息中的时间
                if (StringUtil.isNotBlank(currentTimeStr)) {
                    currentTime = sdf.parse(currentTimeStr).getTime();
                }
            }

            if (RedisCacheUtil.hasKey(redisKey)) {
                FetchUserResp user = RedisCacheUtil.valueObjGet(redisKey, FetchUserResp.class);
                Date updateTime = user.getUpdateTime();
                Long beforeTime = null;
                if (updateTime != null) {
                    beforeTime = updateTime.getTime();
                } else {
                    beforeTime = 0L;
                }
                if (currentTime < beforeTime) {
                    return false;
                }
            }
            Byte state = 3;
            if (OnlineStatusEnum.CONNECTED.getCode().equals(status)) {
                state = 2;
            } else if (OnlineStatusEnum.DISCONNECTED.getCode().equals(status)) {
                state = 3;
            }
            FetchUserResp user = new FetchUserResp();
            user.setState(state);
            user.setUpdateTime(new Date(currentTime));
            RedisCacheUtil.valueObjSet(redisKey, user);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     *@description 获取用户状态
     *@author wucheng
     *@params [userId]
     *@create 2018/12/6 17:21
     *@return java.lang.String
     */
    public static String getUserConnectStatusRedisKey(Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("user:status:").append(userId);
        String redisKey = sb.toString();
        return redisKey;
    }

    /**
     *@description 获取设备上线缓存key
     *@author wucheng
     *@params [deviceId, tenantId]
     *@create 2018/12/6 11:56
     *@return java.lang.String
     */
    public static String getDeviceConnectRedisKey(String deviceId, Long tenantId) {
        StringBuilder sb = new StringBuilder();
        if (tenantId != null) {
            sb.append(tenantId).append(":");
        }
        sb.append("device_status").append(":").append(VersionEnum.V1.toString()).append(":").append(deviceId);
        String redisKey = sb.toString();
        return redisKey;
    }
}
