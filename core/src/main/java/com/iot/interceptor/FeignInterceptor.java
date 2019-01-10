package com.iot.interceptor;

import com.iot.common.constant.SystemConstants;
import com.iot.saas.SaaSContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Configuration
public class FeignInterceptor implements RequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        transferIotHeaderInfo(template);

        if (!template.headers().containsKey(SystemConstants.HEADER_ACTIVE_LANGUAGE)) {
            template.header(SystemConstants.HEADER_ACTIVE_LANGUAGE, LocaleContextHolder.getLocale().toString());
        }
        //兼容header未传tenant_id
        if (!template.headers().containsKey(SystemConstants.HEADER_TENANT_ID) && SaaSContextHolder.hasCurrentTenantId()) {
            template.header(SystemConstants.HEADER_TENANT_ID, SaaSContextHolder.currentTenantId().toString());
        }
        //兼容header未传user_id
        if (!template.headers().containsKey(SystemConstants.HEADER_USER_ID) && SaaSContextHolder.getCurrentUserId() != null) {
            template.header(SystemConstants.HEADER_USER_ID, SaaSContextHolder.getCurrentUserId().toString());
        }
        //兼容header未传iot_log_id
        if (!template.headers().containsKey(SystemConstants.HEADER_LOG_ID) && SaaSContextHolder.getLogRequestId() != null) {
            template.header(SystemConstants.HEADER_LOG_ID, SaaSContextHolder.getLogRequestId().toString());
        }
//        logRequest(template);
    }

    private void transferIotHeaderInfo(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();

                if (!name.startsWith("iot_")) {
                    continue;
                }

                String values = request.getHeader(name);
                template.header(name, values);
            }
        }
    }

    private void logRequest(RequestTemplate template) {
        if (template != null) {
            LOGGER.debug(template.toString());
        }
    }
}
