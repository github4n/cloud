package com.iot.center.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WxManagerService {

	private static final Logger LOG = Logger.getLogger(WxManagerService.class);
	
	//token 接口
	private static final String TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	
	//创建菜单
	private static final String MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
	
	//删除菜单
	private static final String MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";
	
	//查询菜单
	
	private static final String MENU_SELECT = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";
	
	//发送客服消息
	private static final String CUSTOM_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
	
	//发送模板消息
	private static final String TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
	
	//网页授权OAuth2.0获取code
	private static final String GET_OAUTH_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s#wechat_redirect";
	
	//网页授权OAuth2.0获取token
	private static final String GET_OAUTH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	
	//网页授权OAuth2.0获取用户信息
	private static final String GET_OAUTH_USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	
	//获取WXJSJDK TICKET
	private static final String GET_WEB_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
	
	//微信支付-统一下单接口
	private static final String WXPAY_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	//获取关注公众号的用户信息
	private static final String GET_SUBSCRIBE_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
	
	//获取微信统一下单接口
	public static String getWxPayUnif(){
		return WXPAY_UNIFIEDORDER;
	}
	
	//获取token接口
	public static String getTokenUrl(String appId,String appSecret){
		
		return String.format(TOKEN, new String[]{appId,appSecret});
	}
	
	//获取菜单创建接口
	public static String getMenuCreateUrl(String token){
		return String.format(MENU_CREATE, new String[]{ token});
	}
	
	//获取菜单删除接口
	public static String getMenuDeleteUrl(String token){
		return String.format(MENU_DELETE, new String[] {token});
	}
	
	//获取菜单查询接口
	public static String getMenuSelectUrl(String token){
		return String.format(MENU_SELECT, new String[] {token});
	}
	
	//获取发送客服消息接口
	public static String getCustomMessageUrl(String token){
		return String.format(CUSTOM_MESSAGE, new String[]{token});
	}
	
	//获取发送模板消息接口
	public static String getTemplateMessageUrl(String token){
		return String.format(TEMPLATE_MESSAGE, new String[]{token});
	}
	
	//网页授权OAuth2.0获取code
	public static String getOAuthCodeUrl(String appId ,String redirectUrl ,String scope ,String state){
		return String.format(GET_OAUTH_CODE, new String[]{appId, urlEncodeUTF8(redirectUrl), "code", scope, state});
	}
	
	//网页授权OAuth2.0获取token
	public static String getOAuthTokenUrl(String appId ,String appSecret ,String code ){
		return String.format(GET_OAUTH_TOKEN, new String[]{appId, appSecret, code});
	}
	
	//网页授权OAuth2.0获取用户信息
	public static String getOAuthUserinfoUrl(String token ,String openid){
		return String.format(GET_OAUTH_USERINFO, new String[]{token, openid});
	}
	
	//获取WXJSJDK TICKET
	public static String getTicketStrUrl(String token){
		return String.format(GET_WEB_TICKET, new String[]{token});
	}
	
	//获取关注公众号的用户信息
	public static String getUserinfoStrUrl(String token, String openid) {
		return String.format(GET_SUBSCRIBE_USERINFO, new String[]{token, openid});
	}
	
	/**
	 * 通用接口
	 */
	//获取接口访问凭证
	public static JSONObject getToken(String appId, String appSecret) {
		String tockenUrl = WxManagerService.getTokenUrl(appId, appSecret);
		JSONObject jsonObject = httpsRequest(tockenUrl, "GET", null);
		if (null != jsonObject && !jsonObject.containsKey("errcode")) {
				return jsonObject;
		}
		return null;
	}
	
	//发送请求
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
				TrustManager[] tm = { new JEEWeiXinX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				
				URL url = new URL(requestUrl);
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setSSLSocketFactory(ssf);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod(requestMethod);
				
				if (null != outputStr) {
					OutputStream outputStream = conn.getOutputStream();
					outputStream.write(outputStr.getBytes("UTF-8"));
					outputStream.close();
				}
				InputStream inputStream = conn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
				inputStream = null;
				conn.disconnect();
//				jsonObject = JSONObject.parseObject(buffer.toString());
				jsonObject = JSON.parseObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("buffer========================"+buffer.toString());
		}
		return jsonObject;
	}
	
	//发送请求
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr,Integer flag) {
		
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			synchronized (flag) {
				if(flag==0) {
				
				TrustManager[] tm = { new JEEWeiXinX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				
				URL url = new URL(requestUrl);
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setSSLSocketFactory(ssf);

				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod(requestMethod);
				conn.setConnectTimeout(10000);
				if (null != outputStr) {
					OutputStream outputStream = conn.getOutputStream();
					outputStream.write(outputStr.getBytes("UTF-8"));
					outputStream.close();
				}
				InputStream inputStream = conn.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				bufferedReader.close();
				inputStreamReader.close();
				inputStream.close();
				inputStream = null;
				conn.disconnect();
				jsonObject = JSON.parseObject(buffer.toString());
				flag++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	public static String urlEncodeUTF8(String str){
        String result = str;
        try {
            result = URLEncoder.encode(str,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	
	//获取openId
	public static JSONObject getOpenIdForUrl(String code,String appId,String appSecret){
		return httpsRequest(WxManagerService.getOAuthTokenUrl(appId, appSecret, code),"GET",null);
	}
	
	//获取ticket
	public static JSONObject getTicketForUrl(String appId,String appSecret){
		String token = getToken(appId,appSecret).getString("access_token");
		LOG.info("token========================"+token);
		return httpsRequest(WxManagerService.getTicketStrUrl(token),"GET",null);
	}
}

class JEEWeiXinX509TrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	}
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
