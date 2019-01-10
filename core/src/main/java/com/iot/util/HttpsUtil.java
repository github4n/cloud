package com.iot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.iot.common.beans.CommonResponse;
import com.iot.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	static {
		try {
			SSLContext sslcontext = SSLContext.getInstance("SSL","SunJSSE");  
	         sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					
				}
	         }}, new java.security.SecureRandom());  
	         HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {  
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					System.out.println("WARNING: Hostname is not matched for cert.");  
					return true;
				}  
	         };  
	         HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);  
	         HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
		} catch (Exception e) {
		}
	}

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws
     * @throws Exception
     */
    public static String sendGet(String path, String param) throws Exception {
        URL url = new URL(path);
        HttpsURLConnection conn =  (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //必须设置false，否则会自动redirect到重定向后的地址
        conn.setInstanceFollowRedirects(false);
        conn.connect();
		CommonResponse response = getReturn(conn);
		String resultData = (String)response.getData();
		return resultData;
    }

    public static String sendPostForForm(String path, String param) throws Exception {
    	URL url = new URL(path);  
    	HttpsURLConnection conn =  (HttpsURLConnection) url.openConnection();
    	conn.setRequestMethod("POST");  
    	conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");  
    	//必须设置false，否则会自动redirect到重定向后的地址  
    	conn.setInstanceFollowRedirects(false);  
    	
	    conn.setDoOutput(true);
        conn.setDoInput(true);
           // 获取URLConnection对象对应的输出流
        PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
           // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();

		CommonResponse response = getReturn(conn);
		String resultData = (String)response.getData();
    	return resultData;
    }
    
    public static CommonResponse sendPostForJson(String path, String param, Map<String,String> headers) throws Exception {
    	URL url = new URL(path);  
    	HttpsURLConnection conn =  (HttpsURLConnection) url.openConnection();
    	conn.setRequestMethod("POST");
    	conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
    	if (headers != null) {
    		headers.forEach((key, val) -> {
    			if (!key.equals("Content-type")) {
    				conn.setRequestProperty(key, val);
    			}
    		});
    	}
    	//必须设置false，否则会自动redirect到重定向后的地址  
    	conn.setInstanceFollowRedirects(false);  
    	
    	conn.setDoOutput(true);
    	conn.setDoInput(true);
    	// 获取URLConnection对象对应的输出流
    	PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
    	// 发送请求参数
    	out.print(param);
    	// flush输出流的缓冲
    	out.flush();
		CommonResponse response = getReturn(conn);
    	return response;
    }

    private static CommonResponse getReturn(HttpsURLConnection connection) throws IOException{
        StringBuffer buffer = new StringBuffer();  
        //将返回的输入流转换成字符串
		CommonResponse response = new CommonResponse();
		int responseCode = connection.getResponseCode();
		response.setCode(responseCode);

		logger.debug("response status : {}", responseCode);
        if (connection.getResponseCode() < 250) {
            System.out.println();
            InputStream inputStream = connection.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
        } else {
            InputStream inputStream = connection.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
        }

        String resultData = buffer.toString();
        //logger.debug("response body : {}", resultData);

		if (StringUtil.isNotBlank(resultData)) {
			response.setData(resultData);
		}
        return response;
    }  

}
