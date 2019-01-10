package com.iot.cloud.interceptor;

import com.iot.common.constant.SystemConstants;
import com.iot.saas.IgnoreHeaderTenantId;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DefualtTenanIDInterceptor implements HandlerInterceptor {

    private static String PACKAGE_PREFIX_IOT = "com.iot";

    private static final Long defualtTenanID = 11L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        fillCurrentTenantId(request, handler);

        return true;
    }

    protected void fillCurrentTenantId(HttpServletRequest request, Object handler) {
        if ((handler instanceof HandlerMethod) && AnnotationUtils.findAnnotation(((HandlerMethod) handler).getMethod(),
                IgnoreHeaderTenantId.class) == null && AnnotationUtils.findAnnotation(((HandlerMethod) handler).getBeanType(),
                IgnoreHeaderTenantId.class) == null && StringUtils.startsWith(((HandlerMethod) handler).getBeanType().getPackage().getName(), PACKAGE_PREFIX_IOT)) {
            String tenantId = getTenantIdFromRequest(request);
            if (StringUtils.isNotEmpty(tenantId)) {
                SaaSContextHolder.setCurrentTenantId(Long.valueOf(tenantId));
            } else {
                SaaSContextHolder.setCurrentTenantId(defualtTenanID);
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


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SaaSContextHolder.removeCurrentTenantId();
        SaaSContextHolder.removeCurrentUserId();
    }

}
