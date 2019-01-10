package com.iot.portal.config.security.filter;

import com.iot.common.exception.BusinessException;
import com.iot.common.util.SpringUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.user.constant.UserConstants;
import com.iot.user.exception.UserExceptionEnum;
import com.iot.user.vo.UserTokenResp;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 项目名称：cloud
 * 功能描述：token校验，获取相关信息
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 17:34
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 17:34
 * 修改描述：
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter implements HandlerInterceptor {


    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*暂时屏蔽此代码，先用旧的AccessTokenInterceptor做token校验*/
       /* String header = request.getHeader(UserConstants.HEADER_ACCESS_TOKEN);
        if (!StringUtil.isBlank(header)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }*/
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("111",null,null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    /**
      * @despriction：根据token获取用户相关信息
      * @author  yeshiyuan
      * @created 2018/7/14 16:46
      * @return
      */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String accessToken = request.getHeader(UserConstants.HEADER_ACCESS_TOKEN); //请求 header token
        String terminalMark = request.getHeader(UserConstants.HEADER_TERMINALMARK); //header 終端類型 app web等
        logger.debug("token:" + accessToken + ", terminalMark:" + terminalMark);
        if (accessToken != null) {
            //判空
            if (StringUtils.isEmpty(accessToken)) {
                throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
            }
            if (StringUtils.isEmpty(terminalMark)) {
                throw new BusinessException(UserExceptionEnum.AUTH_HEADER_ILLEGAL); // 请求头参数非法 不能为空
            }
            /**
             * 1.检查token 和 deviceCode 是否匹配 不匹配重新登入 【1.1 解密、检验token 参数,1.2 校验解密出来的跟当前请求头是否一致 】
             */
            UserApi userApiService = SpringUtil.getBean(UserApi.class);
//            //根据accessToken获取凭证
//            UserTokenResp userToken1 = userApiService.getUserToken(accessToken);
//            if (StringUtils.isEmpty(userToken1)) {
//                //提醒前端刷新凭证
//                throw new BusinessException(UserExceptionEnum.AUTH_REFRESH);
//            }
//            if (!terminalMark.equals(userToken1.getTerminalMark())) { //平台类型是否一致--- eg:  如果拿app的token 登入web 將無法登入
//                //重新登入
//                throw new BusinessException(UserExceptionEnum.AUTH_LOGIN_RETRY);
//            }
            /**
             * 2.检测凭证
             */
            UserTokenResp userToken2 = userApiService.checkUserToken(0L, accessToken, terminalMark);

            // 设置上下文局部变量
            SaaSContextHolder.setCurrentUserId(userToken2.getUserId());
            SaaSContextHolder.setCurrentTenantId(userToken2.getTenantId());
            SaaSContextHolder.setCurrentUserUuid(userToken2.getUserUuid());
            SaaSContextHolder.setCurrentOrgId(userToken2.getOrgId());
            return new UsernamePasswordAuthenticationToken(userToken2.getUserId(),null,null);
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
      * @despriction：清除spring security线程变量
      * @author  yeshiyuan
      * @created 2018/7/12 15:53
      * @return
      */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null){
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        }
        // 清除变量
        SaaSContextHolder.removeCurrentContextMap();
    }
}
