package com.iot.schedule;

import com.iot.common.config.AbstractApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "com.iot")
@EnableDiscoveryClient
@EnableHystrix
@EnableFeignClients(basePackages = "com.iot")
@ImportResource("quartz.xml")
public class ScheduleServiceApplication extends AbstractApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScheduleServiceApplication.class, args);
        LOGGER.debug("start iot scheduler....");
    }
}
