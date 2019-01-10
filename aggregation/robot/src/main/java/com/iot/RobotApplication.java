package com.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
/**
 * 创建人：chenxiaolin
 * 创建时间：2018年4月26日 下午5:11:08
 * 修改人： chenxiaolin
 * 修改时间：2018年4月26日 下午5:11:08
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
public class RobotApplication {
	public static void main(String[] args) {
		System.out.println("---------------------------000");
		SpringApplication.run(RobotApplication.class, args);
	}
}
