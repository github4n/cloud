package com.iot.boss;

import com.iot.boss.interceptor.AccessTokenInterceptor;
import com.iot.common.config.AbstractWebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig extends AbstractWebConfig {

    @Override
    protected void addCustomInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AccessTokenInterceptor());
        registry.addInterceptor(new AccessTokenInterceptor()).excludePathPatterns("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**");
    }

}
