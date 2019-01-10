package com.iot.robot.utils;

import com.iot.redis.RedisCacheUtil;

public class UserCheckUtil {

	private static final String OAUTH_PREFIX_NAME = "OAuth2.0-";
	private static final String MQTTPW = "mqttpw-";
	
	public static String getUserId(String token) {
		return RedisCacheUtil.valueGet(OAUTH_PREFIX_NAME + token);
	}
	public static String getMqttPw(String userId) {
		return RedisCacheUtil.valueGet(MQTTPW + userId);
	}
}
