package com.iot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.iot.common.config.AbstractApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.iot")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
@MapperScan(basePackages = {"com.iot.report.dao"})
@EnableScheduling
public class ReportApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportApplication.class, args);
	}

}
