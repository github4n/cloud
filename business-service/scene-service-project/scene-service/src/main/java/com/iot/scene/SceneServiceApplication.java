package com.iot.scene;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication(scanBasePackages = {"com.lds.iot"})
@EnableDiscoveryClient
@EnableHystrix
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"com.lds.iot"})
@MapperScan(basePackages = {"com.lds.iot.scene.mapper.**"})
public class SceneServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SceneServiceApplication.class, args);
    }
}
