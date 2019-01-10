package com.iot.control;

import com.iot.common.config.AbstractApplication;
import com.spring4all.mongodb.EnableMongoPlus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.iot"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})

@EnableMongoPlus
public class ControlServiceApplication extends AbstractApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(ControlServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ControlServiceApplication.class, args);
        LOGGER.debug("start control service....");
    }
}
