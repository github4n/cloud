
package com.iot.video.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.iot.common.config.AbstractApplication;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.iot"})
@ComponentScan({"com.iot.video", "com.iot.locale","com.iot.redis"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
 public class VideoTimerApplication extends AbstractApplication {

     public static void main(String[] args) {
         SpringApplication.run(VideoTimerApplication.class, args);
     }

 }
