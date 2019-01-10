package com.iot.robot.utils;

import com.iot.common.util.StringUtil;
import com.iot.robot.common.constant.VoiceBoxConst;

import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/8 16:01
 * @Modify by:
 */
public class VoiceBoxUtil {

    /**
     *  生成带"robot_" 前缀的seq
     *
     * @return
     */
    public static String generateRobotSeq() {
        return VoiceBoxConst.SEQ_PREFIX + StringUtil.getRandomString(8);
    }

    /**
     * 从 Map 里获取指定key的 String类型值
     *
     * @param payload
     * @param key
     * @return
     */
    public static String getMustString(Map<String, Object> payload, String key) {
        if (payload == null) {
            return null;
        }

        Object obj = payload.get(key);
        if (obj == null) {
            return null;
        }

        String value = String.valueOf(obj);
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return value;
    }

    /**
     * 从 Map 里获取指定key的 long类型值
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
            return null;
        }

        return returnVal;
    }

    /**
     * 从 Map 里获取指定key的 int类型值
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
            return null;
        }

        return returnVal;
    }

    /**
     * 从 Map 里获取指定key的 int类型值
     *
     * @param payload
     * @param key
     * @return
     */
    public static Integer getMustInteger(Map<String, Object> payload, String key, int defaultVal) {
        String value = getMustString(payload, key);
        if (value == null) {
            return defaultVal;
        }

        Integer returnVal = null;
        try {
            returnVal = Integer.parseInt(value);
        } catch (Exception e) {
            return defaultVal;
        }

        return returnVal;
    }
}
