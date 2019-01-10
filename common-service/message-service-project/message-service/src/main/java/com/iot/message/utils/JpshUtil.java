package com.iot.message.utils;

import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.exception.BusinessException;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.contants.ModuleConstants;
import com.iot.redis.RedisCacheUtil;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class JpshUtil {
	
	public final static String DEFAULT_CHARSET = "utf-8";
	
	public static void jpushNotification(Long appId, String masterSecret, String appKey, String phoneId,
			String systemBody, Map<String, String> noticeMap) throws IOException {
		JSONObject info = new JSONObject();
		Integer unReadNum = RedisCacheUtil.valueObjGet(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", ""), Integer.class);
		if (unReadNum == null) {
			unReadNum = 0;
		}
		String body = "{\"body\": \"" + systemBody +"\",\"unReadNum\": \"" + (unReadNum + 1) + "\"}";
		try {
			JPushClient jPushClient = new JPushClient(masterSecret, appKey);
			PushPayload pushPayload = JpshUtil.buildPushObject_all_registrationId_alertWithTitle(phoneId, body, "leedarson", noticeMap.get("customDictionary"));
			System.out.println(pushPayload);
			PushResult pushResult = jPushClient.sendPush(pushPayload);
			System.out.println(pushResult);
			if (pushResult.getResponseCode() != 200) {
				throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
			}
			RedisCacheUtil.valueObjSet(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", ""), unReadNum + 1);
		} catch (APIConnectionException e) {
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
		} catch (APIRequestException e) {
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
		} finally {
			info.clear();
		}
        
	}
	
	private static PushPayload buildPushObject_all_registrationId_alertWithTitle(String regId, String body, String title, String extrasparam) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(regId))
				.setMessage(Message.newBuilder()
						.setMsgContent(body)
						.setTitle(title)
						.addExtra("tag", extrasparam)
						.build())
				.setOptions(Options.newBuilder()
						.setApnsProduction(false)
						.setSendno(1)
						.setTimeToLive(86400)
						.build())
				.build();
	}
	
}
