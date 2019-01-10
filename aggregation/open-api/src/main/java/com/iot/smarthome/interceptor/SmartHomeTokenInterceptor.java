package com.iot.smarthome.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.iot.saas.SaaSContextHolder;
import com.iot.smarthome.constant.SmartHomeConstant;
import com.iot.smarthome.constant.SmartHomeErrorCode;
import com.iot.smarthome.util.ResponseUtil;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.user.vo.LoginResp;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.thread.ThreadLocalScopeCache;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 19:47
 * @Modify by:
 */

@Component
public class SmartHomeTokenInterceptor implements HandlerInterceptor {
    private static final String SMART_HOME_LOCAL_ACCESS_TOKEN = "smartHomeLocalAccessToken";
    private static ThreadLocalScopeCache localVar = new ThreadLocalScopeCache();
    private Logger log = LoggerFactory.getLogger(SmartHomeTokenInterceptor.class);
    @Autowired
    private UserApi userApi;

    /**
     *  获取localAccessToken
     * @return
     */
    public static String getLocalAccessToken() {
        return (String) localVar.get(SMART_HOME_LOCAL_ACCESS_TOKEN);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Class cl = handler.getClass();
        if (cl != HandlerMethod.class) {
            return true;
        }

        String token = request.getHeader("accessToken");
        StringBuffer requestURL = request.getRequestURL();
        log.info("***** SmartHomeTokenInterceptor, preHandle, token={}, requestURL={}", token, requestURL);

        if (token == null) {
            // 无效 token
            /*OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_OK)
                    .setErrorDescription("invalid_token")
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .buildJSONMessage();
            String returnBody = oAuthResponse.getBody();*/
            Map<String, Object> resultMap = ResponseUtil.buildErrorPayload(SmartHomeErrorCode.INVALID_TOKEN.getCode());
            JSONObject resultJsonObj = new JSONObject(resultMap);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(resultJsonObj.toJSONString());
            return false;
        }

        LoginResp loginResp = userApi.getOauthUserInfo(SmartHomeConstant.CLIENT_TYPE, token);
        if (loginResp == null) {
            // token 过期
            /*OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_OK)
                    .setErrorDescription("expired_token")
                    .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                    .buildJSONMessage();
            String returnBody = oAuthResponse.getBody();*/
            Map<String, Object> resultMap = ResponseUtil.buildErrorPayload(SmartHomeErrorCode.EXPIRED_TOKEN.getCode());
            JSONObject resultJsonObj = new JSONObject(resultMap);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(resultJsonObj.toJSONString());
            return false;
        }

        Long userId = loginResp.getUserId();
        String userUUID = loginResp.getUserUuid();

        FetchUserResp fetchUserResp = userApi.getUser(userId);
        Long tenantId = fetchUserResp.getTenantId();
        log.info("***** preHandle, tenantId={}, userId={}, userUUID={}", tenantId, userId, userUUID);

        SaaSContextHolder.setCurrentUserId(userId);
        SaaSContextHolder.setCurrentUserUuid(userUUID);
        SaaSContextHolder.setCurrentTenantId(tenantId);
        localVar.put(SMART_HOME_LOCAL_ACCESS_TOKEN, token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // 清除变量
        SaaSContextHolder.removeCurrentContextMap();
        localVar.clear();

        if (e != null) {
            log.info("***** afterCompletion, has exception:{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
