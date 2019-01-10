package com.iot.message.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.exception.BusinessException;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.message.contants.ModuleConstants;
import com.iot.redis.RedisCacheUtil;

public class FireBaseUtil {
	
	public final static String DEFAULT_CHARSET = "utf-8";
	
	public static void fireBaseNotification(Long appId, String pushUrl, String pushKey, String phoneId,
			String systemBody, Map<String, String> noticeMap) throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(pushUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "key=" + pushKey);
        
		JSONObject json = new JSONObject();
		json.put("to", phoneId);
		// 推送到哪台客户端机器，方法一推一个token,方法二，批量推送 ，最多1000个token ，此处的tokens是一个token
		// JSONArray数组json.put("registration_ids", tokens);
		JSONObject info = new JSONObject();
		info.put("title", "leedarson");
		Integer unReadNum = RedisCacheUtil.valueObjGet(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", ""), Integer.class);
		if (unReadNum == null) {
			unReadNum = 0;
		}
//		info.put("sound", unReadNum + 1);
		info.put("body", "{\"body\": \"" + systemBody +"\",\"unReadNum\": \"" + (unReadNum + 1) + "\"}");
		if (noticeMap.containsKey("customDictionary")) {
			info.put("tag", noticeMap.get("customDictionary"));
		}
		// json 还可以put其他你想要的参数
		json.put("data", info);
		System.out.println("json : " + json.toString());
		
		StringEntity entity = new StringEntity(json.toString(), DEFAULT_CHARSET);//解决中文乱码问题
        entity.setContentEncoding(DEFAULT_CHARSET);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        HttpResponse resp;
		try {
			resp = client.execute(httpPost);
			if(resp.getStatusLine().getStatusCode() == 200) {
	        	System.out.println(EntityUtils.toString(resp.getEntity(), DEFAULT_CHARSET));
	        }
			RedisCacheUtil.valueObjSet(ModuleConstants.UNREADNUM + phoneId.replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5.，,。？“”]+", ""), unReadNum + 1);
		} catch (ClientProtocolException e) {
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
		} catch (IOException e) {
			throw new BusinessException(BusinessExceptionEnum.SYSTEM_SINGLE_PUSH_ERROR);
		}finally {
			info.clear();
			json.clear();
			EntityUtils.consume(entity);
			closeCloseableHttpClient(client);
		}
        
	}
	
	private static void closeCloseableHttpClient(CloseableHttpClient client) {
		if(client == null) {
			return;
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
