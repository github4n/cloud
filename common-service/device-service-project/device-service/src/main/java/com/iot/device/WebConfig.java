package com.iot.device;

import com.iot.common.config.AbstractWebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig extends AbstractWebConfig {

    @Override
    protected void addCustomInterceptors(InterceptorRegistry registry) {
    }
}
