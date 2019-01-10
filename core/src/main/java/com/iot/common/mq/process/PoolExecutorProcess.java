package com.iot.common.mq.process;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.mq.utils.DetailRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lucky
 * @Descrpiton: 多线程执行器
 * @Date: 9:44 2018/12/4
 * @Modify by:
 */
@Slf4j
public class PoolExecutorProcess<T> {

    // 线程池维护线程的最少数量
    private final static int DEFAULT_CORE_POOL_SIZE = 5;
    // 线程池维护线程的最大数量
    private final static int DEFAULT_MAX_POOL_SIZE = 20;

    private  ExecutorService executorService;

    //执行器线程池的状态 所有任务是否执行完毕
    private volatile Integer state = 0;//0 ide  1 running

    private MessageProcess<T> messageProcess;
    private String queueName;
    private Environment environment;
    private Integer corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private Integer maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    public PoolExecutorProcess(Environment environment, MessageProcess<T> messageProcess, String queueName) {
        this.environment = environment;
        this.messageProcess = messageProcess;
        this.queueName = queueName;

        initProperties();
        executorService = Executors.newFixedThreadPool(corePoolSize);
    }
    private void initProperties() {
       maxPoolSize = getMaxThreadSize();
       corePoolSize = getThreadCount();

    }

    protected int getMaxThreadSize() {
        int maxPoolSize = 0;
        String maxSize = null;

        if (!StringUtils.isEmpty(queueName)) {
            maxSize = environment.getProperty("queue."+queueName+".maxPoolSize");//指定默认配置大小
        }
        if (!StringUtils.isEmpty(maxSize)) {
            maxPoolSize = Integer.parseInt(maxSize);
        }

        if (StringUtils.isEmpty(maxSize)) {
            maxSize =environment.getProperty("queue.comm.maxPoolSize");//公共默认配置大小
        }
        if (!StringUtils.isEmpty(maxSize)) {
            maxPoolSize = Integer.parseInt(maxSize);
        }

        if (maxPoolSize <= 0) {
            maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        }
        return maxPoolSize;
    }

    protected int getThreadCount() {
        int targetThreadCount = 0;
        String threadCount = null;

        if (!StringUtils.isEmpty(queueName)) {
            threadCount = environment.getProperty("queue."+queueName+".corePoolSize");//指定默认配置大小
        }

        if (StringUtils.isEmpty(threadCount)) {
            threadCount =environment.getProperty("queue.comm.corePoolSize");//公共默认配置大小

        }

        if (!StringUtils.isEmpty(threadCount)) {
            targetThreadCount = Integer.parseInt(threadCount);
        }

        if (targetThreadCount > maxPoolSize) {
            targetThreadCount = maxPoolSize;
        }
        if (targetThreadCount <= 0) {
            targetThreadCount = DEFAULT_CORE_POOL_SIZE;
        }
        return targetThreadCount;
    }

    public boolean checkPoolIde() {
        int threadCount = ((ThreadPoolExecutor)executorService).getActiveCount();
        if (threadCount >= corePoolSize) {
            this.state = 1;
            return false;
        } else {
            this.state = 0;//空闲
            return true;
        }
    }


    public void process(T data) {
        if (checkPoolIde()) {//空闲才能将数据放入线程池执行
            TaskProcess<T> taskProcess = new TaskProcess(data, messageProcess);
            executorService.submit(taskProcess);
        }

    }


    //终止数据线程池
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();

    }


    public MessageProcess<T> getMessageProcess() {
        return messageProcess;
    }

    public void setMessageProcess(MessageProcess<T> messageProcess) {
        this.messageProcess = messageProcess;
    }
}
