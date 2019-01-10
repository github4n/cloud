
package com.iot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.iot.common.config.AbstractApplication;

@SpringBootApplication(scanBasePackages = "com.iot")
@EnableDiscoveryClient
//@ComponentScan(value = {"com.iot"})
@MapperScan(basePackages = { "com.iot.system.dao" })
@EnableFeignClients(basePackages = {"com.iot"})
 public class SystemServiceApplication extends AbstractApplication {

     public static void main(String[] args) {
         SpringApplication.run(SystemServiceApplication.class, args);
     }

 }
