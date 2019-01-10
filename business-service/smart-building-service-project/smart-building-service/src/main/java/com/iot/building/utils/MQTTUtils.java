package com.iot.building.utils;

import com.iot.common.exception.BusinessException;
import com.iot.common.exception.IBusinessException;
import com.iot.common.util.StringUtil;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:58 2018/5/3
 * @Modify by:
 */
public class MQTTUtils {

    public static final String DEFAULT_CLOUD_SOURCE_ADDR = "000000";

    /**
     * 解析 mqtt 请求的topic 获取设备id 或用户id
     *
     * @param reqTopic
     * @return
     */
    public static String parseReqTopic(String reqTopic) {
        String[] split = reqTopic.split("/");
        String devIdOrUserId = split[3];
        devIdOrUserId = parseContent(devIdOrUserId);
        return devIdOrUserId;
    }

    /**
     * 解析内容有小数点的第二位内容体
     *
     * @param deviceIdOrUserId
     * @return
     */
    public static String parseContent(String deviceIdOrUserId) {
        if (!StringUtils.isEmpty(deviceIdOrUserId)) {
            if (deviceIdOrUserId.contains(".")) {
                deviceIdOrUserId = deviceIdOrUserId.split("\\.")[1];
            }
        }
        return deviceIdOrUserId;
    }

    /**
     * 从 topic中解析 method名称
     *
     * @param reqTopic
     * @return
     */
    public static String getMethodFromTopic(String reqTopic) {
        String[] split = reqTopic.split("/");
        String methodName = split[5];
        return methodName;
    }

    /**
     * 从 mqtt消息 payload 里获取指定key的 String类型值
     *
     * @param payload
     * @param key
     * @return
     */
    public static String getMustString(Map<String, Object> payload, String key) {
        if (payload == null) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 500;
                }

                @Override
                public String getMessageKey() {
                    return "message.body.payload.is.null";
                }
            });
        }

        Object obj = payload.get(key);
        if (obj == null) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 500;
                }

                @Override
                public String getMessageKey() {
                    return "parameter." + key + ".not.exist";
                }
            });
        }

        String value = String.valueOf(obj);
        if (StringUtil.isBlank(value)) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 500;
                }

                @Override
                public String getMessageKey() {
                    return "parameter." + key + ".is.empty";
                }
            });
        }

        return value;
    }

    /**
     * 从 mqtt消息 payload 里获取指定key的 long类型值
     *
     * @param payload
     * @param key
     * @return
     */
    public static Long getMustLong(Map<String, Object> payload, String key) {
        String value = getMustString(payload, key);

        Long returnVal = null;
        try {
            returnVal = Long.parseLong(value);
        } catch (Exception e) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 500;
                }

                @Override
                public String getMessageKey() {
                    return "parameter." + key + ".is.not.long.value";
                }
            });
        }

        return returnVal;
    }

    /**
     * 从 mqtt消息 payload 里获取指定key的 int类型值
     *
     * @param payload
     * @param key
     * @return
     */
    public static Integer getMustInteger(Map<String, Object> payload, String key) {
        String value = getMustString(payload, key);

        Integer returnVal = null;
        try {
            returnVal = Integer.parseInt(value);
        } catch (Exception e) {
            throw new BusinessException(new IBusinessException() {
                @Override
                public int getCode() {
                    return 500;
                }

                @Override
                public String getMessageKey() {
                    return "parameter." + key + ".is.not.int.value";
                }
            });
        }

        return returnVal;
    }
}
