package com.iot.smarthome.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/14 11:13
 * @Modify by:
 */
public class ResponseUtil {
    // 成功/失败 标志
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";

    public static Map<String, Object> buildSuccessPayload() {
        Map<String, Object> payloadMap = Maps.newHashMap();
        payloadMap.put("status", SUCCESS);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", payloadMap);

        return resultMap;
    }

    public static Map<String, Object> buildErrorPayload(String errorCode) {
        Map<String, Object> payloadMap = Maps.newHashMap();
        payloadMap.put("status", ERROR);
        payloadMap.put("errorCode", errorCode);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", payloadMap);

        return resultMap;
    }

    public static void main(String[] args) {
        System.out.println(ResponseUtil.buildErrorPayload("not_found"));
        System.out.println(ResponseUtil.buildSuccessPayload());
    }
}