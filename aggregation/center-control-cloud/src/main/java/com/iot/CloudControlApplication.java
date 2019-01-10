package com.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 创建人:chenweida
 * 创建时间:2018/8/9
 */

@SpringBootApplication(
        exclude = {HibernateJpaAutoConfiguration.class}
)
@EnableDiscoveryClient
@EnableHystrix
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"com.iot"})
public class CloudControlApplication {

    public static void main(String[] args) {
        //ThreadPoolTaskScheduler

        SpringApplication.run(CloudControlApplication.class, args);
    }
}
