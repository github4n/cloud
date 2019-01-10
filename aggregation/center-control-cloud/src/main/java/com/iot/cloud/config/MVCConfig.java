package com.iot.cloud.config;

import com.iot.cloud.interceptor.DefualtTenanIDInterceptor;
import com.iot.common.config.AbstractWebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/25
 */
@Configuration
public class MVCConfig extends AbstractWebConfig {
    @Autowired
    private DefualtTenanIDInterceptor defualtTenanIDInterceptor;

    @Override
    protected void addCustomInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(defualtTenanIDInterceptor).addPathPatterns("/**");
    }
}
