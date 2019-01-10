package com.iot.robot.utils.google;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.robot.utils.RedisKeyUtil;
import com.iot.robot.vo.google.GoogleServiceAccountAccessToken;
import com.iot.tenant.api.VoiceBoxConfigApi;
import com.iot.tenant.vo.req.VoiceBoxConfigReq;
import com.iot.tenant.vo.resp.VoiceBoxConfigResp;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Descrpiton: googleHome 上报设备状态工具类
 * @Author: yuChangXing
 * @Date: 2018/8/20 16:48
 * @Modify by:
 */
public class ReportStateUtil {
    private static final Logger logger = LoggerFactory.getLogger(ReportStateUtil.class);

    // json配置文件
    //private static String ServiceAccountConfigFile = "googlehome/serviceAccountCredentials-{env}.json";

    static {
        /*Environment environment = ApplicationContextHelper.getBean(Environment.class);
        String evnVal = environment.getProperty("spring.profiles.active");
        logger.info("***** get evnVal={}", evnVal);
        if (StringUtil.isBlank(evnVal)) {
            evnVal = "dev";
        }
        ServiceAccountConfigFile = ServiceAccountConfigFile.replace("{env}", evnVal);
        logger.info("***** final ServiceAccountConfigFile={}", ServiceAccountConfigFile);*/
    }


    /**
     * 上报设备状态
     *
     * @param jsonContent
     */
    public static void callReportState(String jsonContent, Long tenantId) {
        if (StringUtil.isBlank(jsonContent)) {
            logger.debug("***** callReportState() end. because jsonContent is empty!");
            return;
        }

        String token = getToken(tenantId);
        HttpEntity entity = null;
        try {
            StringEntity params = new StringEntity(jsonContent);
            entity = httpRequest("https://homegraph.googleapis.com/v1/devices:reportStateAndNotification", token,
                    params, "application/json");

            if (entity != null) {
                StringBuilder out = readResponse(entity);
                logger.debug("callReportState(), out={}", out.toString());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 access_token
     *
     * @return
     */
    public static String getToken(Long tenantId) {
        logger.debug("***** getToken, start, tenantId={}", tenantId);

        String token = "";

        // 先从 redis获取
        GoogleServiceAccountAccessToken accessToken = RedisCacheUtil.valueObjGet(RedisKeyUtil.getGoogleServiceAccountAccessTokenCacheKey(tenantId), GoogleServiceAccountAccessToken.class);
        logger.debug("***** getToken(), redis cache accessToken={}", JSON.toJSON(accessToken));

        boolean isExpire = isExpire(accessToken);
        logger.debug("***** getToken(), isExpire={}", isExpire);
        // 判断是否过期
        if (isExpire) {
            // 重新调用google api生成accessToken
            String resultContent = createAccessToken(tenantId);
            logger.debug("***** getToken(), createAccessToken(), resultContent={}", resultContent);

            if (StringUtil.isNotBlank(resultContent)) {
                accessToken = JSON.parseObject(resultContent, GoogleServiceAccountAccessToken.class);
                if (accessToken != null) {
                    // 保存到缓存
                    RedisCacheUtil.valueObjSet(RedisKeyUtil.getGoogleServiceAccountAccessTokenCacheKey(tenantId), accessToken, RedisKeyUtil.DEFAULT_CACHE_TIME);
                    token = accessToken.getAccess_token();
                }
            }
        }else{
            token = accessToken.getAccess_token();
        }

        logger.debug("***** getToken end. token = {}", token);
        return token;
    }

    public static String createAccessToken(Long tenantId) {
        String resultContent = null;

        try {
            String jwt = getJwt(tenantId);
            Map m = new HashMap();
            m.put("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
            m.put("assertion", jwt);

            // Request parameters and other properties.
            StringEntity params = new StringEntity(formEncode(m));

            HttpEntity entity = httpRequest("https://accounts.google.com/o/oauth2/token", jwt, params,
                    "application/x-www-form-urlencoded");

            if (entity != null) {
                final StringBuilder out = readResponse(entity);
                resultContent = out.toString();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultContent;
    }

    public static String getJwt(Long tenantId) throws IOException {
        JwtBuilder jwts = Jwts.builder();

        VoiceBoxConfigApi voiceBoxConfigApi = ApplicationContextHelper.getBean(VoiceBoxConfigApi.class);
        VoiceBoxConfigResp voiceBoxConfigResp = voiceBoxConfigApi.getByVoiceBoxConfigReq(VoiceBoxConfigReq.createGoogleHomeSmartHomeVoiceBoxConfigReq(tenantId));
        logger.debug("***** getJwt, voiceBoxConfigResp = {}", JSON.toJSONString(voiceBoxConfigResp));

        JSONObject oriContent = new JSONObject();
        oriContent.put("type", "service_account");
        oriContent.put("project_id", voiceBoxConfigResp.getProjectId());
        oriContent.put("private_key_id", voiceBoxConfigResp.getPrivateKeyId());
        oriContent.put("private_key", voiceBoxConfigResp.getPrivateKey());
        oriContent.put("client_email", voiceBoxConfigResp.getClientEmail());
        oriContent.put("client_id", voiceBoxConfigResp.getReportClientId());
        oriContent.put("auth_uri", "https://accounts.google.com/o/oauth2/auth");
        oriContent.put("token_uri", "https://oauth2.googleapis.com/token");
        oriContent.put("auth_provider_x509_cert_url", "https://www.googleapis.com/oauth2/v1/certs");
        //oriContent.put("client_x509_cert_url", "https://www.googleapis.com/robot/v1/metadata/x509/pre-commercial-electric%40appspot.gserviceaccount.com");
        oriContent.put("client_x509_cert_url", "https://www.googleapis.com/robot/v1/metadata/x509/"+voiceBoxConfigResp.getProjectId()+"%40appspot.gserviceaccount.com");

        String oriContentJsonString = oriContent.toJSONString();
        logger.debug("***** getJwt, oriContentJsonString1 = {}", oriContentJsonString);
        //oriContentJsonString = oriContentJsonString.replaceAll("\\n", "\n");
        //logger.debug("***** getJwt, oriContentJsonString2 = {}", oriContentJsonString);

        InputStream stream = new ByteArrayInputStream(oriContentJsonString.getBytes());
        ServiceAccountCredentials serviceAccount = ServiceAccountCredentials.fromStream(stream);

        // set claims
        Map claims = new HashMap<>();
        // 设置过期时间
        claims.put("exp", System.currentTimeMillis() / 1000 + 3600);
        // 设置当前时间
        claims.put("iat", System.currentTimeMillis() / 1000);
        claims.put("iss", serviceAccount.getClientEmail());
        claims.put("aud", "https://accounts.google.com/o/oauth2/token");
        claims.put("scope", "https://www.googleapis.com/auth/homegraph");

        jwts.setClaims(claims).signWith(SignatureAlgorithm.RS256, serviceAccount.getPrivateKey());

        return jwts.compact();
    }

    // 是否过期
    public static boolean isExpire(GoogleServiceAccountAccessToken accessToken) {
        boolean isExpire = true;
        if (accessToken == null) {
            return isExpire;
        }

        if (accessToken.getExpires_in() == null) {
            return isExpire;
        }

        long currentTimeMillis = System.currentTimeMillis();
        // 时间差
        long valTimeMillis = currentTimeMillis - accessToken.getCreateTimeMillis();
        if (valTimeMillis < 3400000) {
            // 提前 200秒去 重新生成accessToken
            isExpire = false;
        }

        return isExpire;
    }

    /*
     * Helper methods below
     */
    private static HttpEntity httpRequest(String url, String token, StringEntity params, String type)
            throws UnsupportedEncodingException, IOException, ClientProtocolException {

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Authorization", "Bearer " + token);

        // Request parameters and other properties.
        httppost.addHeader("content-type", type);
        httppost.setEntity(params);

        // Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        return entity;
    }

    private static StringBuilder readResponse(HttpEntity entity) throws IOException, UnsupportedEncodingException {
        InputStream instream = entity.getContent();
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();

        try {
            Reader in = new InputStreamReader(instream, "UTF-8");
            while (true) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
        } finally {
            instream.close();
        }
        return out;
    }

    private static String formEncode(Map<String, String> m) {
        String s = "";
        for (String key : m.keySet()) {
            if (s.length() > 0)
                s += "&";
            s += key + "=" + m.get(key);
        }
        return s;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String jsonContent = "{\"agent_user_id\":\"feadbabe75df45a29d7f775e4e1c671c\",\"payload\":{\"devices\":{\"states\":{\"f845355ec6014fe4ac9e881a8c8579fd\":{\"on\":false}}}},\"requestId\":\"6266015939477122990\"}";

        String token = "ya29.c.EloABoqaGFNbnXGqiyj7dXFR14cOf6XvwTcLX64PqeMuuMLNxzwMjyJaWwuagF3D0Y7TXK95dLU-nleWHGnbokTpNQItN5-OU0eZWvg1262L3hdTlB24IQ_x23I";
        HttpEntity entity = null;
        try {
            StringEntity params = new StringEntity(jsonContent);
            System.out.println("**********"+params.getContent());
            entity = httpRequest("https://homegraph.googleapis.com/v1/devices:reportStateAndNotification", token,
                    params, "application/json");

            if (entity != null) {
                StringBuilder out = readResponse(entity);
                System.out.println("callReportState(), out="+out.toString());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
