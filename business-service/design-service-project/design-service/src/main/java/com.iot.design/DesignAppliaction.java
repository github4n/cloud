package com.iot.design;

import com.iot.common.config.AbstractApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/27
 */
@SpringBootApplication(scanBasePackages = {"com.iot"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
@MapperScan(basePackages = "com.iot.design.**.mapper")
public class DesignAppliaction extends AbstractApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(DesignAppliaction.class);

    public static void main(String[] args) {
        SpringApplication.run(DesignAppliaction.class, args);
        LOGGER.debug("start control service....");
    }
}