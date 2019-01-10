package com.iot.building.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.URLEncoder;

public class Infrared {

    private static final Logger LOG = Logger.getLogger(Infrared.class);

    private static final String CALLBACK_URL = "";//回调地址
    private static final String OAuthLoginURL = "";//请求的地址
    private static final String client_id = "";
    private static final String client_secret = "";
    //private static final String code = "";
    //private static final String refresh_token_str = "";

    private static final String token ="token";//token
    private static final String refresh_token ="refresh_token";//刷新token
    private static final String discovery_device ="discovery_device";//发现设备
    private static final String control_device ="control_device";//控制设备
    private static final String get_user_info ="get_user_info";//获取用户信息

    //登录接口
    private static JSONObject login(String outputStr){
        String requestUrl = "https://"+OAuthLoginURL+"/?redirect_uri="+CALLBACK_URL+"&client_id="+client_id+"&state=1&response_type=code";
        JSONObject jsonObject = httpsRequestGet(requestUrl,null);
        if (null != jsonObject && !jsonObject.containsKey("errcode")) {
            return jsonObject;
        }
        return null;
    }

    //获取token请求接口
    private static JSONObject getToken(String code){
        String requestUrl = "https://"+OAuthLoginURL+"/oauth/v2/token";
        String outputStr = "grant_type=authorization_code&client_id="+client_id+"&client_secret="+client_secret+"&code="+code;
        //requestUrl = "https://"+OAuthLoginURL+"/oauth/v2/token?grant_type=authorization_code&client_id="+
        //client_id+"&client_secret="+client_secret+"&code="+code;
        JSONObject jsonObject = httpsRequestPost(requestUrl,outputStr,token);
        if (null != jsonObject && !jsonObject.containsKey("errcode")) {
            return jsonObject;
        }
        return null;
    }

    //获取刷新token请求接口
    private static JSONObject refreshToken(String refresh_token_str){
        String requestUrl =  "https://"+OAuthLoginURL+"/oauth/v2/token";
        String outputStr = "grant_type=refresh_token&client_id="+client_id+"&client_secret="+client_secret+"&refresh_token="+refresh_token_str;
        JSONObject jsonObject = httpsRequestPost(requestUrl,outputStr,refresh_token);
        if (null != jsonObject && !jsonObject.containsKey("errcode")) {
            return jsonObject;
        }
        return null;
    }

    //获取用户信息请求
    private static JSONObject getUserInfo(String token){
        String requestUrl = "http://"+OAuthLoginURL+"/oauth/v2/server/getlogindata?access_token="+token;
        JSONObject jsonObject = httpsRequestGet(requestUrl,get_user_info);
        if (null != jsonObject && !jsonObject.containsKey("errcode")) {
            return jsonObject;
        }
        return null;
    }






    /**
     *POST请求
     * @param requestUrl 请求的地址
     * @param outputStr
     * @return
     */
    public static JSONObject httpsRequestPost(String requestUrl,String outputStr,String typeStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Host","example.com");
            conn.setRequestProperty("Accept", "application/json, text/javascript");//设置参数类型是json格式
            conn.setRequestMethod("POST");

            if (null != outputStr) {
                //获取conn对象对应的输出流
                OutputStream outputStream = conn.getOutputStream();
                // 发送请求参数
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            //获取数据
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
            conn.disconnect();
            jsonObject = JSON.parseObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("buffer========================"+buffer.toString());
        }
        return jsonObject;
    }

    /**
     *GET请求
     * @param requestUrl 请求的地址
     * @return
     */
    public static JSONObject httpsRequestGet(String requestUrl,String methodType) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            if (null != methodType) {

            }else {
                conn.setRequestProperty("Host","example.com");
                conn.setRequestProperty("Accept", "application/json, text/javascript");
            }
            conn.setRequestMethod("GET");
            //建立到远程对象的实际连接
            conn.connect();
            //获取数据
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
            conn.disconnect();
            jsonObject = JSON.parseObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("buffer========================"+buffer.toString());
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
}
