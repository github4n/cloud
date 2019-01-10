package com.iot.message.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import com.iot.common.exception.BusinessException;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.contants.ModuleConstants;
import com.iot.message.entity.AppCertInfo;
import com.iot.redis.RedisCacheUtil;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.exceptions.InvalidSSLConfig;
import com.notnoop.exceptions.NetworkIOException;

public class ApnsUtil {
	
//	private static Map<String, ApnsService> apnsMap = new HashMap<String, ApnsService>();
	
	public static void apnsNotification(Long appId, AppCertInfo appCertInfo, String phoneId, String systemBody, Map<String, String> noticeMap) {
		
			if(systemBody.length() >= 128) {
				throw new BusinessException(BusinessExceptionEnum.IOS_MESSAGE_TOO_LONG);
			}
			Integer unReadNum = RedisCacheUtil.valueObjGet(ModuleConstants.UNREADNUM + phoneId, Integer.class);
			if(unReadNum == null) {
				unReadNum = 0;
			}
			ApnsService service = null;
			ByteArrayInputStream streamCert = null;
			if(appCertInfo.getServiceHost().equals("gateway.sandbox.push.apple.com")) {
//				service = apnsMap.get("Sandbox_" + appId);
//				if(service == null) {
					streamCert = new ByteArrayInputStream(appCertInfo.getCertInfo());
					service = APNS.newService().withCert(streamCert, appCertInfo.getCertPassWord()).withSandboxDestination().build();
//					apnsMap.put("Sandbox_" + appId, service);
//				}
				
			}else {
//				service = apnsMap.get("Product_" + appId);
//				if(service == null) {
					streamCert = new ByteArrayInputStream(appCertInfo.getCertInfo());
				    service = APNS.newService().withCert(streamCert, appCertInfo.getCertPassWord()).withProductionDestination().build();
//				    apnsMap.put("Product_" + appId, service);
//				}
			}
			String payload = "";
			try {
			if(noticeMap.containsKey("customDictionary")) {
				payload = APNS.newPayload()
						.alertBody(systemBody)
						.sound("default")
						.badge(unReadNum + 1)
						.customField("customDictionary", noticeMap.get("customDictionary"))
						.build();
			}else {
				payload = APNS.newPayload()
						.alertBody(systemBody)
						.sound("default")
						.badge(unReadNum + 1)
						.build();
			}
			service.push(phoneId, payload);
			RedisCacheUtil.valueObjSet(ModuleConstants.UNREADNUM + phoneId, unReadNum + 1);
		} catch (InvalidSSLConfig e) {
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
		} catch (NetworkIOException e) {
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
		}finally {
			closeCertStream(streamCert);
		}
	}
	
	private static void closeCertStream(ByteArrayInputStream streamCert) {
		if(streamCert == null) {
			return;
		}
		try {
			streamCert.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void clearApnsMap() {
//		apnsMap = new HashMap<String, ApnsService>();
//    }
}
