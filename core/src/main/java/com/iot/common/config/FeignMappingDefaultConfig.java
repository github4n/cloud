package com.iot.common.config;

import feign.Feign;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * <p>描述：@FeignClient with top level @RequestMapping </p>
 *
 * @author xiangyitao
 * @date 2017/11/03
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class FeignMappingDefaultConfig {

    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignFilterRequestMappingHandlerMapping();
            }
        };
    }

    /**
     * @author xiangyitao
     * @FeignClient中的@RequestMapping也被SpringMVC加载的问题解决
     * @FeignClient with top level @RequestMapping annotation is also registered as Spring MVC handler
     * @date 2018/03/06
     */
    private static class FeignFilterRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) && ((AnnotationUtils.findAnnotation(beanType, RestController.class) != null) || (AnnotationUtils.findAnnotation(beanType, Controller.class) != null));
        }
    }

    /**
     * @author xiangyitao
     * @FeignClient中的@RequestMapping也被SpringMVC加载的问题解决
     * @FeignClient with top level @RequestMapping annotation is also registered as Spring MVC handler
     * @date 2018/03/06
     */
    //TODO(xiangyitao) 此方法存在bug识别不了swagger标签
    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
        @Override
        protected boolean isHandler(Class<?> beanType) {
            return super.isHandler(beanType) &&
                    !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
        }
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
}
