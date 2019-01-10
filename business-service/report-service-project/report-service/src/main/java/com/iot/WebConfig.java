package com.iot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.iot.common.config.AbstractWebConfig;

@Configuration
public class WebConfig extends AbstractWebConfig {

    @Override
    protected void addCustomInterceptors(InterceptorRegistry registry) {
    }
    
}
