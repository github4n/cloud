package com.iot.portal.comm.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.iot.common.annotation.LoginRequired;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.SpringUtil;
import com.iot.device.util.RedisKeyUtil;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.UserTokenResp;

/**
 * @Description: 登入token 校验拦截
 */
public class AccessTokenInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);

    private final String[] permitUrl = new String[]{"/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/info", "/health"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        for (String url : permitUrl ) {
            if (request.getRequestURI().contains(url)){
                return true;
            }
        }
        boolean passFlag;
        LoginRequired loginRequired = ((HandlerMethod) handler).getMethodAnnotation(LoginRequired.class);
        if (loginRequired == null) {
            //没 注解 强校验
            checkLogin(request);
            //这里暂时先默认true，避免拦截swagger
            passFlag = true;
            return passFlag;
        }
        switch (loginRequired.value()) {
            case Skip:
                passFlag = true;
                break;
            default:
                passFlag = checkLogin(request);
                break;
        }
        return passFlag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清除变量
        SaaSContextHolder.removeCurrentContextMap();
    }

    /**
     * 检查调用凭证
     *
     * @param request
     */
    public boolean checkLogin(HttpServletRequest request) {
        boolean result;
        String accessToken = request.getHeader(UserConstants.HEADER_ACCESS_TOKEN); //请求 header token
        String terminalMark = request.getHeader(UserConstants.HEADER_TERMINALMARK); //header 終端類型 app web等
        logger.debug("token:" + accessToken + ", terminalMark:" + terminalMark);


        //判空
        if (StringUtils.isEmpty(accessToken)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }
        if (!accessToken.startsWith(SystemTypeEnum.USER_PORTAL.getCode())) {
            throw new BusinessException(UserExceptionEnum.ACCESSTOKEN_IS_ERROR); // accessToken不属于该系统
        }
        if (StringUtils.isEmpty(terminalMark)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }

        /**
         * 1.检查token 和 deviceCode 是否匹配 不匹配重新登入 【1.1 解密、检验token 参数,1.2 校验解密出来的跟当前请求头是否一致 】
         */
        UserApi userApiService = SpringUtil.getBean(UserApi.class);
        //根据accessToken获取凭证
//        UserTokenResp userToken1 = userApiService.getUserToken(accessToken);
//        if (StringUtils.isEmpty(userToken1)) {
//            //提醒前端刷新凭证
//            throw new BusinessException(UserExceptionEnum.AUTH_REFRESH);
//        }

        /*if (!terminalMark.equals(userToken1.getTerminalMark())) { //平台类型是否一致--- eg:  如果拿app的token 登入web 將無法登入
            //重新登入
            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
        }*/
        /**
         * 2.检测凭证
         */
        UserTokenResp userToken2 = userApiService.checkUserToken(0L, accessToken, terminalMark);

        //检验用户企业信息是否完善
        String key = "tenant:perfectFlag:" + userToken2.getTenantId();
        Boolean improveTenantInfo = RedisCacheUtil.valueObjGet(key, Boolean.class);
        if(!request.getRequestURI().contains("improveTenantInfo") && (improveTenantInfo == null || !improveTenantInfo)){
        	throw new BusinessException(UserExceptionEnum.TENANT_INFO_IS_NOT_COMPLETE);
        }
        // 设置上下文局部变量
        SaaSContextHolder.setCurrentUserId(userToken2.getUserId());
        SaaSContextHolder.setCurrentTenantId(userToken2.getTenantId());
        SaaSContextHolder.setCurrentUserUuid(userToken2.getUserUuid());
        SaaSContextHolder.setCurrentOrgId(userToken2.getOrgId());
        result = true;
        return result;
    }
}
