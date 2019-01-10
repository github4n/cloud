package com.iot.robot.config;


import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.iot.robot.interceptors.ReqCacheFilter;
import com.iot.robot.interceptors.TokenInterceptor;




@Component
public class IntreceptorRegister extends WebMvcConfigurerAdapter {

	@Autowired
	private TokenInterceptor tokenInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(tokenInterceptor);
		registration.excludePathPatterns("/appUser/checkUserName/**");
		registration.excludePathPatterns("/appUser/appUserRegist");
		registration.excludePathPatterns("/appUser/appUserLogin");
		registration.excludePathPatterns("/appUser/activeAppUser");
		registration.excludePathPatterns("/appUser/appUserResetPassword");
		registration.excludePathPatterns("/swagger-resources");
		registration.excludePathPatterns("/swagger-resources/**");
		registration.excludePathPatterns("/v2/**");
		super.addInterceptors(registry);
	}
	
	@Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ReqCacheFilter());//添加过滤器
        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
        registration.setOrder(1);//设置优先级
        return registration;
    }
	
}
