package com.iot.portal.config.security;

import com.iot.portal.config.security.filter.MyFilterSecurityInterceptor;
import com.iot.portal.config.security.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 项目名称：cloud
 * 功能描述：spring security配置
 * 创建人： yeshiyuan
 * 创建时间：2018/7/11 17:34
 * 修改人： yeshiyuan
 * 修改时间：2018/7/11 17:34
 * 修改描述：
 */
@Configuration
@EnableWebSecurity //注解开启Spring Security的功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    PermitUrlConfig permitUrlProperties;

    @Autowired
    private MyInvocationSecurityMetadataSource myInvocationSecurityMetadataSource;

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws  Exception{
        // 加载不被拦截的url;
        String[] permitUrls = permitUrlProperties.getUrls();
        http.authorizeRequests().antMatchers(permitUrls).permitAll() //这些路径不被拦截（permitall没有绕过spring security，其中包含了登录的以及匿名的）
             .anyRequest().authenticated();   //其他路径全部需要权限
        //禁止csrf
        http.csrf().disable();
        // 禁用headers缓存
        http.headers().cacheControl();
        //token验证
        http.addFilterBefore(new TokenAuthenticationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);
        //url鉴权验证
        http.addFilterBefore(new MyFilterSecurityInterceptor(accessDecisionManager(),authenticationManager(),myInvocationSecurityMetadataSource), FilterSecurityInterceptor.class);
        //权限不足异常处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    }

    @Autowired
    public  void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.eraseCredentials(false);
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new MyAccessDecisionManager();
    }

    /**
      * @despriction：配置不需要拦截的url（ingore是完全绕过了spring security的所有filter）
      * @author  yeshiyuan
      * @created 2018/7/14 16:43
      * @param null
      * @return
      */
    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] permitUrls = permitUrlProperties.getUrls();
        web.ignoring().antMatchers(permitUrls);
    }

    /**
      * @despriction：异常处理
      * @author  yeshiyuan
      * @created 2018/7/16 9:35
      * @param null
      * @return
      */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedHandlerImpl();
    }
}
