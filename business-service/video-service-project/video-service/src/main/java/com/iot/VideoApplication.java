package com.iot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.iot.common.config.AbstractApplication;



@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = {"com.iot"})
@MapperScan(basePackages = { "com.iot.video.dao" })
@EnableFeignClients(basePackages = {"com.iot"})
//@ServletComponentScan(basePackages = {"com.iot.druid"})
public class VideoApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoApplication.class, args);
	}

}
