package com.iot;

import com.iot.common.config.AbstractApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.iot"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot.payment.api", "com.iot.system.api"})
//@ComponentScan({"com.iot.payment","com.iot.payment.rabbitmq", "com.iot.swagger","com.iot.redis"})
public class PaymentApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

}
