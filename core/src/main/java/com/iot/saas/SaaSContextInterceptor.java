package com.iot.saas;

import com.iot.common.constant.SystemConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class SaaSContextInterceptor implements HandlerInterceptor {

    private static String PACKAGE_PREFIX_IOT = "com.iot";

    @Autowired
    private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        fillCurrentTenantId(request, handler);

        fillCurrentUserId(request);

        fillLogRequestId(request, response);

        return true;
    }

    protected void fillCurrentTenantId(HttpServletRequest request, Object handler) {
        if ((handler instanceof HandlerMethod)  && AnnotationUtils.findAnnotation(((HandlerMethod) handler).getMethod(),
                IgnoreHeaderTenantId.class) == null && AnnotationUtils.findAnnotation(((HandlerMethod) handler).getBeanType(),
                IgnoreHeaderTenantId.class) == null && StringUtils.startsWith(((HandlerMethod) handler).getBeanType().getPackage().getName(), PACKAGE_PREFIX_IOT)) {
            String tenantId = getTenantIdFromRequest(request);
            if (StringUtils.isNotEmpty(tenantId)) {
                SaaSContextHolder.setCurrentTenantId(Long.valueOf(tenantId));
            } else {
//                throw new IllegalStateException("not found tenantId from request");
            }
        }
    }

    protected String getTenantIdFromRequest(HttpServletRequest request) {
        String tenantId = request.getHeader(SystemConstants.HEADER_TENANT_ID);
        if (StringUtils.isEmpty(tenantId)) {
            return null;
        }
        return tenantId;
    }

    protected void fillCurrentUserId(HttpServletRequest request) {
        String userId = request.getHeader(SystemConstants.HEADER_USER_ID);
        if (StringUtils.isNotEmpty(userId)) {
            SaaSContextHolder.setCurrentUserId(Long.parseLong(userId));
        }
    }

    protected void fillLogRequestId(HttpServletRequest request, HttpServletResponse response) {
        Boolean needLogRequestId = Boolean.parseBoolean(environment.getProperty("iotLog.needLogRequestId", "false"));
        if (!needLogRequestId) {
            return;
        }

        String logRequestId = getLogRequestId(request, response);
        SaaSContextHolder.setLogRequestId(logRequestId);
        MDC.put(SystemConstants.LOG_REQUEST_ID, logRequestId);
    }

    protected String getLogRequestId(HttpServletRequest request, HttpServletResponse response) {
        String logRequestId = request.getHeader(SystemConstants.HEADER_LOG_ID);
        if (StringUtils.isEmpty(logRequestId)) {
            logRequestId = UUID.randomUUID().toString();
        }

        try {
            response.setHeader(SystemConstants.HEADER_LOG_ID, URLEncoder.encode(logRequestId, StandardCharsets.UTF_8.displayName()));
        } catch (UnsupportedEncodingException ignore) {
        }
        return logRequestId;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SaaSContextHolder.removeCurrentTenantId();
        SaaSContextHolder.removeCurrentUserId();
        SaaSContextHolder.removeLogRequestId();
    }

}
