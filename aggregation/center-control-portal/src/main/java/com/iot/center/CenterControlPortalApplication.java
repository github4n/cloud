package com.iot.center;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import com.iot.common.config.AbstractApplication;
import com.iot.common.exception.CustomErrorDecoder;
import com.netflix.discovery.DiscoveryManager;

import feign.codec.ErrorDecoder;

@SpringBootApplication(scanBasePackages = {"com.iot"},
	exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableHystrix
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"com.iot"})
@ServletComponentScan
public class CenterControlPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CenterControlPortalApplication.class, args);
	}
	
	/**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("1000000KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("1000000KB");
        return factory.createMultipartConfig();
    }
    
    /**
     * 错误转码业务异常
     *
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @PreDestroy
    public void eurekaOffline() {
        DiscoveryManager.getInstance().shutdownComponent();
    }

    @Bean
    public Converter<String, Date> dateConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String dateStr) {
                long lt = Long.parseLong(dateStr);
                return new Date(lt);
            }
        };
    }

}
