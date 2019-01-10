package com.iot;

import com.iot.common.config.AbstractApplication;
import com.iot.file.YmlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot.file.api", "com.iot.schedule.api"})
@SpringBootApplication(scanBasePackages = "com.iot")
@EnableConfigurationProperties(YmlConfig.class)
@EnableSwagger2
public class FileApplication extends AbstractApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileApplication.class, args);
	}

}
