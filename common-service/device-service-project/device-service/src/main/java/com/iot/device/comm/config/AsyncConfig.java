package com.iot.device.comm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:01 2018/7/6
 * @Modify by:
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * 自定义异步线程池
     *
     * @param
     * @return
     * @author lucky
     * @date 2018/7/6 10:02
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        LOGGER.info("init async pool");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("device-Executor");
        executor.setMaxPoolSize(10);

        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // .....
                LOGGER.info("rejected thread handler.-------->");
            }
        });

        return executor;
    }

}
