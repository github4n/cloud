package com.iot.design.optical.helper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/7
 */
@Configuration
@EnableAsync
public class ComputeThreadPool {

    @Bean("computeFirstThreadPool")
    public ThreadPoolTaskExecutor computeFirstThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);//当前线程数
        taskExecutor.setMaxPoolSize(12);//最大线程数
        taskExecutor.setQueueCapacity(200);//线程池所使用的缓冲队列
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);//等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setKeepAliveSeconds(3000);//线程最大空闲时间
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("computeFirstThreadPool-");
        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean("computeSecondThreadPool")
    public ThreadPoolTaskExecutor computeSecondThreadPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);//当前线程数
        taskExecutor.setMaxPoolSize(12);//最大线程数
        taskExecutor.setQueueCapacity(200);//线程池所使用的缓冲队列
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);//等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setKeepAliveSeconds(3000);//线程最大空闲时间
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("computeSecondThreadPool-");
        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }
}
