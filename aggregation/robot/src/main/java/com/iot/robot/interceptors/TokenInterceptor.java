package com.iot.robot.interceptors;


import com.alibaba.fastjson.JSONObject;
import com.iot.robot.controller.AlexaRobotController;
import com.iot.robot.controller.GoogleRobotController;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.common.costants.VoiceBoxConfigConstant;
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
import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserApi userApi;

    private Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    private static final String USERID = "userId";
    private static final String USERUUID = "userUUID";
    private static final String TYPE = "type";

    // 需要忽略token 的方法名称
    private static final String DEVICE_TYPE_MEASUREMENTS = "deviceTypeMeasurements";

    private static ThreadLocalScopeCache localVar = new ThreadLocalScopeCache();

    private Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    /**
     *  判断 是否需要验证 token
     *
     * @param request
     * @param handlerMethod
     * @return
     */
    private boolean needCheckToken(HttpServletRequest request, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();
        if (method != null) {
            String methodName = method.getName();
            if (DEVICE_TYPE_MEASUREMENTS.equals(methodName)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Class cl = handler.getClass();
        if (cl != HandlerMethod.class) { return true; }

        HandlerMethod hand = (HandlerMethod) handler;

        String token = null;
        String type = "";
        String userUUID = null;
        String uri = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();

        Map<String, String> headerMap = getHeaderMap(request);
        log.info("***** preHandle, requestURL={}, headerMap={}", requestURL, headerMap);

        if (hand.getBeanType() == AlexaRobotController.class) {
            // alexa
            type = VoiceBoxConfigConstant.VOICE_BOX_TYPE_ALEXA;
            token = request.getHeader("accessToken");
        } else if (hand.getBeanType() == GoogleRobotController.class) {
            // googleHome
            type = VoiceBoxConfigConstant.VOICE_BOX_TYPE_GOOGLE_HOME;

            JSONObject body = null;
            if (uri.contains("customAction")) {
                BufferedReader br = request.getReader();
                StringBuilder sb = new StringBuilder();

                String info = null;
                while ((info = br.readLine()) != null) {
                    sb.append(info);
                }
                log.debug("***** preHandle(), sb={}", sb);

                try {
                    body = (JSONObject) JSONObject.parse(sb.toString());
                    if (body == null) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
                log.debug("***** preHandle(), body={}", body);

                body = body.getJSONObject("originalDetectIntentRequest");
                if (body == null) {
                    return false;
                }

                body = body.getJSONObject("payload");
                if (body == null) {
                    return false;
                }

                body = body.getJSONObject("user");
                if (body == null) {
                    return false;
                }

                token = body.getString("accessToken");
            } else {
                token = request.getHeader("authorization").split(" ")[1];
            }

            log.debug("***** preHandle(), final googleHome token={}", token);
            if (token == null) {
                // googleHome错误返回(文字内容放在 fulfillmentText里)
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                body.clear();
                body.put("fulfillmentText", "Sorry, we can't identify you, please 'Account link' this action first.");
                response.getWriter().write(body.toJSONString());
                return false;
            }

        } else {
            // 未授权
            OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_OK)
                    .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                    .setErrorDescription("unauthorized_client")
                    .buildJSONMessage();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(oAuthResponse.getBody());
            return false;
        }

        log.info("***** preHandle, token={}", token);

        if (token == null) {
            // 无效 token
            OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_OK)
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .setErrorDescription("invalid_token")
                    .buildJSONMessage();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(oAuthResponse.getBody());
            return false;
        }

        LoginResp res = userApi.getOauthUserInfo(type, token);
        if (res == null) {
            // token 过期
            OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_OK)
                    .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                    .setErrorDescription("expired_token")
                    .buildJSONMessage();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(oAuthResponse.getBody());
            return false;
        }

        Long userId = res.getUserId();
        userUUID = res.getUserUuid();
        if (userId == null) {
            log.info("***** preHandle, userId is null, break request.");
            return false;
        }
        
        FetchUserResp fetchUserResp = userApi.getUser(userId);
        Long tenantId = fetchUserResp.getTenantId();
        SaaSContextHolder.setCurrentTenantId(tenantId);

        log.info("***** preHandle, userId={}, tenantId:{}", userId, tenantId);
        
        localVar.put(USERID, userId);
        localVar.put(USERUUID, userUUID);
        localVar.put(TYPE, type);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        localVar.clear();
        if (ex != null) {
            log.info("***** afterCompletion(), has exception:{}", ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static String get(String key) {
        return (String) localVar.get(key);
    }
    public static String getUserUUID() {
        return (String) localVar.get(USERUUID);
    }
    public static Long getUserId() {
        return  (Long) localVar.get(USERID);
    }
}
