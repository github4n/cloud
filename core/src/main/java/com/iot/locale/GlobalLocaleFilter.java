package com.iot.locale;

import com.iot.common.constant.SystemConstants;
import com.iot.common.util.StringUtil;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class GlobalLocaleFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Locale locale = Locale.US;
        String lang = request.getParameter(SystemConstants.LOCALE_REQUEST_PARAM_LANG);
        if (!StringUtil.isEmpty(lang)) {
            locale = LocaleUtils.toLocale(lang);
        }
        if (locale == null) {
            String activeLanguage = request.getHeader(SystemConstants.HEADER_ACTIVE_LANGUAGE);
            if (!StringUtils.isEmpty(activeLanguage)) {
                locale = LocaleUtils.toLocale(activeLanguage);
            }
        }

        if (locale != null) {
            LocaleContextHolder.setLocale(locale);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocaleContextHolder.resetLocaleContext();
    }


}
