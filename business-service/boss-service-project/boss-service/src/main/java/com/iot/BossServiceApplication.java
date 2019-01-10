package com.iot;

import com.iot.common.config.AbstractApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.iot"})
@EnableDiscoveryClient
@ComponentScan(value = {"com.iot"})
@EnableFeignClients(basePackages = "com.iot")
@MapperScan(basePackages = "com.iot.boss.dao")
public class BossServiceApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(BossServiceApplication.class, args);
	}

}
