package com.iot.center.interceptor;


import com.iot.center.utils.ConstantUtil;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.SpringUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.UserTokenResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 登入token 校验拦截
 */
public class AccessTokenInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	setHolder(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除变量
        SaaSContextHolder.removeCurrentContextMap();
    }

    /**
     * 检查调用凭证
     *
     * @param request
     */
    public boolean setHolder(HttpServletRequest request) {
    	ConstantUtil constantUtil = SpringUtil.getBean(ConstantUtil.class);
    	Long tenantId = 11l;
    	logger.info("**************** current request tenantId is : "+tenantId);
    	//constantUtil.setTenantId2Holder(tenantId);
    	SaaSContextHolder.setCurrentTenantId(tenantId);
        return true;
    }
}
