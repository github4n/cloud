package com.iot.portal.config.security.filter;

import com.iot.portal.config.security.MyInvocationSecurityMetadataSource;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;


/**
 * 项目名称：cloud
 * 功能描述：spring security配置拦截过程使用哪些自定义的类
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 17:34
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 17:34
 * 修改描述：
 */
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private MyInvocationSecurityMetadataSource myInvocationSecurityMetadataSource;

    public MyFilterSecurityInterceptor(AccessDecisionManager accessDecisionManager, AuthenticationManager authenticationManager, FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource) {
        super.setAuthenticationManager(authenticationManager);
        super.setAccessDecisionManager(accessDecisionManager);
        this.myInvocationSecurityMetadataSource = (MyInvocationSecurityMetadataSource) filterInvocationSecurityMetadataSource;
    }


    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.myInvocationSecurityMetadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilterSecurityInterceptor初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation( servletRequest, servletResponse, filterChain );
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException,
            ServletException {
        // fi里面有一个被拦截的url
        // 里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
        // 再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
        System.out.println("MyFilterSecurityInterceptor拦截中... -> " + fi.getRequestUrl());
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            // 执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
        System.out.println("MyFilterSecurityInterceptor销毁");
    }


}
