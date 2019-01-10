package com.iot.building;

import com.iot.common.config.AbstractApplication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.iot"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
@MapperScan(basePackages = "com.iot.building.**.mapper")
public class SmartBuildingServiceApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartBuildingServiceApplication.class, args);
	}
}
