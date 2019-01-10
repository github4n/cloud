package com.iot.portal;

import com.iot.common.config.AbstractWebConfig;
import com.iot.portal.comm.interceptor.AccessTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebConfig extends AbstractWebConfig {

   /* @Autowired
    AuthenticationManager authenticationManager;
*/
    @Override
    protected void addCustomInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessTokenInterceptor());
        //registry.addInterceptor(new TokenAuthenticationFilter(authenticationManager));
    }
}
