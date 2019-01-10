package com.iot;

import com.iot.common.config.AbstractApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot.mqttploy.api"})
@ComponentScan(value = {"com.iot", "com.iot.locale"})
@EnableHystrix
@EnableSwagger2
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class MQTTPloyApplication extends AbstractApplication {

    public static void main(String[] args) {
        SpringApplication.run(MQTTPloyApplication.class, args);
    }

}
