package com.iot.config;

import com.iot.ifttt.interceptor.IftttTokenInterceptor;
import com.iot.smarthome.interceptor.SmartHomeTokenInterceptor;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Component
public class IntreceptorRegister extends WebMvcConfigurerAdapter {

	@Autowired
	private IftttTokenInterceptor iftttTokenInterceptor;
	@Autowired
	private SmartHomeTokenInterceptor smartHomeTokenInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registration = registry.addInterceptor(iftttTokenInterceptor);
		registration.addPathPatterns("/ifttt/v1/**");
		registration.excludePathPatterns("/ifttt/v1/status");
		registration.excludePathPatterns("/ifttt/v1/test/setup");

		// smartHome 拦截器
		registry.addInterceptor(smartHomeTokenInterceptor)
				.addPathPatterns("/smarthome/v1/**")
				//.excludePathPatterns("/smarthome/v1/device/attribute/**")
		;

		super.addInterceptors(registry);
	}
	
	@Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }
}
