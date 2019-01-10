package com.iot.user;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.iot.common.config.AbstractApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;


@SpringBootApplication(scanBasePackages = "com.iot")
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients(basePackages = "com.iot")
@MapperScan(basePackages = {"com.iot.user.mapper"})
public class UserServiceApplication extends AbstractApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        LOGGER.debug("start user service....");
    }
}
