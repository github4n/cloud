package com.iot.boss.interceptor;


import com.iot.common.annotation.LoginRequired;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.SpringUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.UserTokenResp;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean passFlag;
        LoginRequired loginRequired = ((HandlerMethod) handler).getMethodAnnotation(LoginRequired.class);
        if (loginRequired == null) {
            //没 注解 强校验
            passFlag = checkLogin(request);
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

        //判空
        if (StringUtils.isEmpty(accessToken)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }
        if (!accessToken.startsWith(SystemTypeEnum.USER_BOSS.getCode())) {
            throw new BusinessException(UserExceptionEnum.ACCESSTOKEN_IS_ERROR); // accessToken不属于该系统
        }
        if (StringUtils.isEmpty(terminalMark)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }

        /**
         * 1.检查token 和 deviceCode 是否匹配 不匹配重新登入 【1.1 解密、检验token 参数,1.2 校验解密出来的跟当前请求头是否一致 】
         */
        UserApi userApiService = SpringUtil.getBean(UserApi.class);
//        //根据accessToken获取凭证
//        UserTokenResp userToken1 = userApiService.getUserToken(accessToken);
//        if (StringUtils.isEmpty(userToken1)) {
//            //提醒前端刷新凭证
//            throw new BusinessException(UserExceptionEnum.AUTH_REFRESH);
//        }
//
//        if (!terminalMark.equals(userToken1.getTerminalMark())) { //平台类型是否一致--- eg:  如果拿app的token 登入web 將無法登入
//            //重新登入
//            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
//        }
        /**
         * 2.检测凭证
         */
        UserTokenResp userToken2 = userApiService.checkUserToken(0L, accessToken, terminalMark);

        // 设置上下文局部变量
        SaaSContextHolder.setCurrentUserId(userToken2.getUserId());
        SaaSContextHolder.setCurrentUserUuid(userToken2.getUserUuid());
        SaaSContextHolder.setCurrentTenantId(userToken2.getTenantId());
        result = true;
        return result;
    }
}
