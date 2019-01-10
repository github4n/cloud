package com.iot;

import com.iot.common.config.AbstractApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * 描述：IFTTT接入服务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/19 10:12
 */
@Slf4j
@EnableHystrix
@EnableTurbine
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.iot",
        exclude = {DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.iot")
@EnableDiscoveryClient
@ServletComponentScan
public class OpenApiServiceApplication extends AbstractApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenApiServiceApplication.class, args);
        log.info("open api service start success!");
    }
}
