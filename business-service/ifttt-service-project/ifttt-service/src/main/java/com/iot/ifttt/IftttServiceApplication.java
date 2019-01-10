package com.iot.ifttt;

import com.iot.common.config.AbstractApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.iot"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
public class IftttServiceApplication extends AbstractApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(IftttServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(IftttServiceApplication.class, args);
        LOGGER.debug("IFTTT Service started!");
    }
}
