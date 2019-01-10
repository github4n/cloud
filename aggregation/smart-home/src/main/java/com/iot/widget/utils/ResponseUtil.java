package com.iot.widget.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2019/1/4 17:45
 * 修改人:
 * 修改时间：
 */
public class ResponseUtil {
    private final static int FAIL_CODE = 400;
    private final static int SUCCESS_CODE = 200;
    private final static String SUCCESS_DESC = "Success.";

    public static Map<String, Object> buildSuccessMap(Map<String, Object> payloadMap) {
        Map<String, Object> payload = Maps.newHashMap();
        if (payloadMap != null) {
            payload.putAll(payloadMap);
        }

        Map<String, Object> ackMap = Maps.newHashMap();
        ackMap.put("code", SUCCESS_CODE);
        ackMap.put("desc", SUCCESS_DESC);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", payload);
        resultMap.put("ack", ackMap);
        return resultMap;
    }

    public static Map<String, Object> buildFailMap(String failDesc) {
        Map<String, Object> payloadMap = Maps.newHashMap();
        Map<String, Object> ackMap = Maps.newHashMap();
        ackMap.put("code", FAIL_CODE);
        ackMap.put("desc", failDesc);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("payload", payloadMap);
        resultMap.put("ack", ackMap);
        return resultMap;
    }

    public static Map<String, Object> buildSuccessControlResponse(String command, Map<String, Object> payloadMap) {
        Map<String, Object> ackMap = Maps.newHashMap();
        ackMap.put("code", SUCCESS_CODE);
        ackMap.put("desc", SUCCESS_DESC);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("command", command);
        resultMap.put("payload", payloadMap);
        resultMap.put("ack", ackMap);
        return resultMap;
    }

    public static Map<String, Object> buildFailControlResponse(String command, Map<String, Object> payloadMap, String failDesc) {
        Map<String, Object> ackMap = Maps.newHashMap();
        ackMap.put("code", FAIL_CODE);
        ackMap.put("desc", failDesc);

        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap.put("command", command);
        resultMap.put("payload", payloadMap);
        resultMap.put("ack", ackMap);
        return resultMap;
    }

    public static void main(String[] args) {
        System.out.println(new JSONObject(buildFailMap("")));
    }
}
