package com.iot.user.util.alexa;

import com.alibaba.fastjson.JSONObject;
import com.iot.user.vo.SmartTokenReq;
import com.iot.util.HttpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUtil {
    private static Logger log = LoggerFactory.getLogger(AuthUtil.class);
    private static final String AUTH_API = "https://api.amazon.com/auth/o2/token";
    private static final String GRANTINFO_CODE = "grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s";
    private static final String GRANTINFO_REFRESH = "grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s";

    // 根据 code 获取 token相关信息
    public static SmartTokenReq getAccessTokenByCode(String code, String clientId, String secret) {
        log.debug("***** getAccessTokenByCode(), code={}, clientId={}, secret={}", code, clientId, secret);
        String grantInfo = String.format(GRANTINFO_CODE, code, clientId, secret);
        SmartTokenReq vo = null;
        try {
            String info = HttpsUtil.sendPostForForm(AUTH_API, grantInfo);
            log.debug("***** getAccessTokenByCode(), info={}", info);
            vo = handlerToken(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    // 根据 refreshToken 获取 token相关信息
    public static SmartTokenReq refreshAccessToken(String refreshToken, String clientId, String secret) {
        log.debug("***** refreshAccessToken(), refreshToken={}, clientId={}, secret={}", refreshToken, clientId, secret);

        String grantInfo = String.format(GRANTINFO_REFRESH, refreshToken, clientId, secret);
        SmartTokenReq vo = null;
        try {
            String info = HttpsUtil.sendPostForForm(AUTH_API, grantInfo);
            log.debug("***** refreshAccessToken(), info={}", info);
            vo = handlerToken(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    private static SmartTokenReq handlerToken(String info) {
        SmartTokenReq vo = new SmartTokenReq();
        JSONObject json = (JSONObject) JSONObject.parse(info);
        String accessToken = json.getString("access_token");
        String refreshToken = json.getString("refresh_token");
        int expiresIn = json.getIntValue("expires_in");
        vo.setAccessToken(accessToken);
        vo.setExpiresIn(expiresIn);
        vo.setRefreshToken(refreshToken);
        return vo;
    }
}
