package com.iot.interceptors;


import com.google.common.collect.Maps;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.MD5SaltUtil;
import com.iot.common.util.SpringUtil;
import com.iot.exception.BusinessExceptionEnum;
import com.iot.saas.SaaSContextHolder;
import com.iot.tenant.api.AppApi;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.enums.SystemTypeEnum;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.UserTokenResp;
import com.iot.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 登入token 校验拦截
 */
public class AccessTokenInterceptor implements HandlerInterceptor {

    //随机数
    public static final String SECRET_KEY = "r0gccwKq^X^6050e";
    //时间戳
    public static final String HEADER_TIMESTAMP = "timestamp";
    //签名
    public static final String HEADER_SIGN = "sign";
    //单次请求时间超过20秒则 请求禁止
    public static final Integer REQ_TIMEOUT_FORBIDEN = 20;

    private final Logger logger = LoggerFactory.getLogger(AccessTokenInterceptor.class);

    /**
     * 创建签名
     *
     * @param params
     * @return
     */
    public static String createSign(Map params) {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueString = "";
            if (null != value) {
                valueString = String.valueOf(value);
            }
            temp.append(valueString);
        }
        return MD5SaltUtil.MD5(temp.toString()).toUpperCase();
    }

    /**
     * 获取 request 中用POST方式"Content-type"是 "text/plain"发送的 json数据
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getPostByTextPlain(HttpServletRequest request) throws IOException {

        BufferedReader reader = request.getReader();
        char[] buf = new char[512];
        int len = 0;
        StringBuffer contentBuffer = new StringBuffer();
        while ((len = reader.read(buf)) != -1) {
            contentBuffer.append(buf, 0, len);
        }
        String content = contentBuffer.toString();
        return content;
    }

    public void checkSign(HttpServletRequest request) {
        String accessToken = request.getHeader(UserConstants.HEADER_ACCESS_TOKEN); //请求 header token
        String timestamp = request.getHeader(HEADER_TIMESTAMP); //请求 时间戳
        String sign = request.getHeader(HEADER_SIGN); //签名
        if (StringUtils.isEmpty(timestamp)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }
        if (StringUtils.isEmpty(sign)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }
        Long reqCanAccessTime = DateUtil.addSecond(new Date(Long.parseLong(timestamp)), REQ_TIMEOUT_FORBIDEN).getTime();//请求的有效时间

        Long currentDateTime = new Date().getTime();//服务器时间
        if (currentDateTime < reqCanAccessTime) {
            //if 请求的有效时间 < 现在服务器接受请求的时间 即该请求失效
            throw new BusinessException(BusinessExceptionEnum.REQ_TIMEOUT_FORBIDDEN);
        }
        //未将 请求参数进行一起验证签名 后面做
        Map params = Maps.newHashMap();
        params.put("token", accessToken);
        params.put("timestamp", timestamp);
        params.put("secret_key", SECRET_KEY);
        String signMd5 = createSign(params);
        if (!sign.equals(signMd5)) {
            //签名 ERROR
            throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
    public boolean checkLogin(HttpServletRequest request) {
        boolean result;
        String accessToken = request.getHeader(UserConstants.HEADER_ACCESS_TOKEN); //请求 header token
        String terminalMark = request.getHeader(UserConstants.HEADER_TERMINALMARK); //header 終端類型 app web等


        //判空
        if (StringUtils.isEmpty(accessToken)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }
        if (!accessToken.startsWith(SystemTypeEnum.USER_2C.getCode())) {
            throw new BusinessException(UserExceptionEnum.ACCESSTOKEN_IS_ERROR); // accessToken不属于该系统
        }
        if (StringUtils.isEmpty(terminalMark)) {
            throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
        }
        //检验签名
        //this.checkSign(request);

        /**
         * 1.检查token 和 deviceCode 是否匹配 不匹配重新登入 【1.1 解密、检验token 参数,1.2 校验解密出来的跟当前请求头是否一致 】
         */
        UserApi userApiService = SpringUtil.getBean(UserApi.class);
        /**
         * 2.检测凭证
         */
        UserTokenResp userToken2 = userApiService.checkUserToken(0L, accessToken, terminalMark);

        /**
         * 3. 如果是app登录，需要校验app是否可用
         */
        if (UserConstants.TERMINALMARK_APP.equals(terminalMark.toLowerCase())) {
            AppApi appApi = SpringUtil.getBean(AppApi.class);
            //校验app是否已被禁用
            if (!appApi.checkAppCanUsed(userToken2.getAppId(), userToken2.getTenantId())) {
                throw new BusinessException(BusinessExceptionEnum.CHECK_NO_PASS, "this app is forbidden used");
            }
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
