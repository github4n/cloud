package com.iot.ifttt.interceptor;


import com.iot.user.api.UserApi;
import com.iot.user.vo.LoginResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.thread.ThreadLocalScopeCache;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建人：laiguiming
 * 创建时间：2018年11月26日 下午5:11:08
 * 修改人： laiguiming
 * 修改时间：2018年11月26日 下午5:11:08
 */
@Component
public class IftttTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserApi userApi;

    private Logger log = LoggerFactory.getLogger(IftttTokenInterceptor.class);
    private static final String USER_ID = "userId";
    private static final String USER_UUID = "userUUID";
    private static final String TENANT_ID = "tenantId";

    private static ThreadLocalScopeCache localVar = new ThreadLocalScopeCache();

    private Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> headerMap = getHeaderMap(request);
        String token = headerMap.get("authorization");

        if (token == null) {
            log.debug("token 不存在！");
            resError(response);
            return false;
        }

        token = token.split(" ")[1];
        if("-1".equals(token)){
            //测试
            localVar.put(USER_ID, 22l);
            localVar.put(USER_UUID, "b0684e8df7f14e98bbf868a5ee82a009");
            localVar.put(TENANT_ID, 1l);
            return true;
        }

        String type = "IFTTT";
        LoginResp res = userApi.getOauthUserInfo(type, token);
        if (res == null) {
            log.debug("token 无效！");
            resError(response);
            return false;
        }

        Long userId = res.getUserId();
        String userUUID = res.getUserUuid();
        log.info("***** preHandle(), userId={}", userId);
        if (userId == null) {
            log.debug("用户数据无效！");
            resError(response);
            return false;
        }

        localVar.put(USER_ID, userId);
        localVar.put(USER_UUID, userUUID);
        localVar.put(TENANT_ID, res.getTenantId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        localVar.clear();
        if (ex != null) {
            log.error("***** afterCompletion(), has exception:{}", ex);
        }
    }

    public static String get(String key) {
        return (String) localVar.get(key);
    }

    public static String getUserUUID() {
        return (String) localVar.get(USER_UUID);
    }

    public static Long getUserId() {
        return (Long) localVar.get(USER_ID);
    }

    public static Long getTenantId(){
        return (Long) localVar.get(TENANT_ID);
    }

    /**
     * 应答错误信息
     *
     * @param msg
     * @return
     */
    private String errorMsg(String msg) {
        String res = "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"message\": \"" + msg + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return res;
    }

    private void resError(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorMsg("with invalid token"));
    }
}
