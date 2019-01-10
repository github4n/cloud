package com.iot.pack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述：app打包服务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/5/28 15:55
 */
@SpringBootApplication
public class AppPackApplication {
    public static final Logger LOGGER = LoggerFactory.getLogger(AppPackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AppPackApplication.class, args);
        LOGGER.info("app pack service start....");
    }
}
