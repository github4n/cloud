package com.iot.common.mq.core;

import com.iot.common.mq.MQAccessBuilder;
import com.iot.common.mq.bean.BaseMessageEntity;
import com.iot.common.mq.consumer.ThreadPoolConsumer;
import com.iot.common.mq.process.AbsMessageProcess;
import com.iot.common.mq.process.DefaultMessageProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 14:06 2018/9/3
 * @Modify by:
 */
@Slf4j
public abstract class AbsPoolConsumer<T extends BaseMessageEntity> extends BaseAccessBuilder {


    @Autowired
    private Environment environment;

    private ThreadPoolConsumer<?> threadPoolConsumer;

    //当没有任何消费者使用时，自动删除该队列 针对集群 随机数队列
    private boolean autoDelete = false;


    protected AbsMessageProcess<T> process() {
        if (messageProcess() == null) {
            return new DefaultMessageProcess();
        }
        return messageProcess();
    }

    protected int getIntervalMils() {
        String intervalMils = environment.getProperty("queue." + queue() + "."+"intervalMils");
        if (!StringUtils.isEmpty(intervalMils)) {
            return Integer.parseInt(intervalMils);
        }
        intervalMils =environment.getProperty("queue.comm.intervalMils");//公共默认配置大小
        if (!StringUtils.isEmpty(intervalMils)) {
            return Integer.parseInt(intervalMils) <= 0 ? DEFAULT_INTERVAL_MILS : Integer.parseInt(intervalMils);
        }
        if (setIntervalMils() <= 0) {
            return DEFAULT_INTERVAL_MILS;
        }
        return setIntervalMils();
    }

    protected int getThreadCount() {
        return DEFAULT_THREAD_COUNT;
    }

    public  int setThreadCount() {
        return DEFAULT_THREAD_COUNT;
    }

    public abstract int setIntervalMils();

    public abstract AbsMessageProcess<T> messageProcess();

    public void preInit() {
        preInitPool();
        this.start();
    }

    private void preInitPool() {
        try {
            MQAccessBuilder mqAccessBuilder = new MQAccessBuilder(connectionFactory(), getEnvironment());
            threadPoolConsumer =
                    new ThreadPoolConsumer.ThreadPoolConsumerBuilder<T>()
                            .setThreadCount(getThreadCount())
                            .setIntervalMils(getIntervalMils())
                            .setExchange(getExchange())
                            .setRoutingKey(routing())
                            .setQueue(queue())
                            .setType(getType())
                            .setClazz(initClazz())
                            .setAutoDelete(autoDelete)
                            .setMQAccessBuilder(mqAccessBuilder)
                            .setMessageProcess(process())
                            .build();

        } catch (Exception e) {
            log.error("init pool consumer error", e);
        }
    }

    public void start() {
        try {
            threadPoolConsumer.start();
        } catch (Exception e) {
            log.error("start error.", e);
        }
    }

    public abstract Class<T> initClazz();

    @PreDestroy
    protected void destroy() {

        this.stop();
    }

    public void stop() {
        threadPoolConsumer.stop();
    }


    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }
}
