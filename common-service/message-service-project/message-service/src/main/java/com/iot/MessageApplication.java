package com.iot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.iot.common.config.AbstractApplication;
import com.iot.message.YmlConfig;

@SpringBootApplication(scanBasePackages = "com.iot")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
@ServletComponentScan(basePackages = {"com.iot.druid"})
@MapperScan(basePackages = {"com.iot.message.dao"})
@EnableConfigurationProperties(YmlConfig.class)
public class MessageApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageApplication.class, args);
	}

}
