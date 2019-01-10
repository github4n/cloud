package com.iot.shcs.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.iot.common.util.JsonUtil;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.message.api.MessageApi;
import com.iot.message.enums.MessageTempType;
import com.iot.shcs.contants.AppPushMessageKey;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 9:22 2018/10/15
 * @Modify by:
 */
@Slf4j
@Component
public class PushMessageService {

    @Autowired
    private UserApi userApi;

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Autowired
    private MessageApi messageApi;

    /**
     * 处理 推送消息给app
     *
     * @param device
     * @param attrMap
     * @param userId
     */
    public void dealPushMessage2App(GetDeviceInfoRespVo device, Map<String, Object> attrMap, Long userId) {
        log.info("dealPushMessage2App device:{},attrMap{},userId{} ",device, JSONObject.toJSONString(attrMap),userId);
        FetchUserResp user = userApi.getUser(userId);

        // 低电量报警处理
        if (attrMap != null && attrMap.containsKey("PowerLow") && attrMap.get("PowerLow") != null) {
            String batteryAlarm = String.valueOf(attrMap.get("PowerLow"));
            if ("1".equals(batteryAlarm)) {
                pushMessage2App(device, "home", AppPushMessageKey.LACK_OF_ELECTRICITY_ALARM, user.getUuid());
            }
        }

        //水浸检测报警
        if (attrMap != null && attrMap.containsKey("Alarm") && attrMap.get("Alarm") != null) {
            String leakingAlarm = String.valueOf(attrMap.get("Alarm"));
            if ("1".equals(leakingAlarm)) {
                pushMessage2App(device, "home", AppPushMessageKey.WATER_SENSOR_DETECTED_LEAKING, user.getUuid());
            }
        }

        //防拆报警处理
        if (attrMap != null && attrMap.containsKey("Tamper") && attrMap.get("Tamper") != null) {
            String tamper = String.valueOf(attrMap.get("Tamper"));
            if ("1".equals(tamper)) {
                log.info("pushMessage2App......");
                pushMessage2App(device, "tamper", AppPushMessageKey.ANTI_TAMPER_TRIGGER, user.getUuid());
            }
        }

        //烟感报警处理
        if (attrMap != null && attrMap.containsKey("Smoke") && attrMap.get("Smoke") != null) {
            String smoke = String.valueOf(attrMap.get("Smoke"));
            if ("1".equals(smoke)) {
                //判断上传的状态是否与上一次的状态相同，相同则不发送
                if (!smoke.equals(device.getLastState())){
                    log.info("pushMessage2App......");
                    pushMessage2App(device, "home", AppPushMessageKey.SMOKE_SENSOR_IS_ALARMING, user.getUuid());
                }
            }
        }



    }

    /**
     * 推送消息给app
     *
     * @param device
     * @param messageKey
     * @param userUuid
     */
    public void pushMessage2App(GetDeviceInfoRespVo device, String path, String messageKey, String userUuid) {
        // 封装推送至app消息内容
        Map<String, String> noticeMap = Maps.newHashMap();
        noticeMap.put("templateId", MessageTempType.IOS00001.getTempId());
        noticeMap.put("message", localeMessageSourceService.getMessage(messageKey, new Object[]{device.getName()}));

        if (path != null) {
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put("deviceId", device.getUuid());
            dataMap.put("path", path);
            dataMap.put("deviceName",device.getName());
            Map<String, Object> payloadMap = Maps.newHashMap();
            payloadMap.put("data", dataMap);

            noticeMap.put("customDictionary", JsonUtil.toJson(payloadMap));
            log.info("pushMessage2App...dataMap{}, payloadMap{}, noticeMap{}",
                    JSONObject.toJSONString(dataMap), JSONObject.toJSONString(payloadMap), JSONObject.toJSONString(noticeMap));
        }
        messageApi.systemSinglePush(userUuid, noticeMap, 10);
    }

}
