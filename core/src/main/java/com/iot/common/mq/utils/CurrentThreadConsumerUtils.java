package com.iot.common.mq.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.common.mq.consumer.ThreadPoolConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:56 2018/8/16
 * @Modify by:
 */
@Slf4j
@Component
public class CurrentThreadConsumerUtils {
    /**
     * 缓存所有的线程消费者
     */
    public static Map<String, ThreadPoolConsumer> registerThreadPoolConsumerMap = Maps.newConcurrentMap();


    public static void clearRegisterConsumers() {
        registerThreadPoolConsumerMap.clear();
    }

    public static void setRegisterThreadPoolConsumer(String queueName, ThreadPoolConsumer threadPoolConsumer) {
        registerThreadPoolConsumerMap.put(queueName, threadPoolConsumer);
    }

    public static void setRegisterThreadConsumer(String queueName, int threadConsumerCount) {
        ThreadPoolConsumer consumer = getRegisterThreadPoolConsumer(queueName);
        consumer.stop();
        consumer.setThreadCountConsumer(threadConsumerCount);
        try {
            consumer.setStop(false);
            consumer.start();
        } catch (IOException e) {
            log.error("start register consumer:{} error.", JSON.toJSONString(consumer));
        }
    }

    public static ThreadPoolConsumer getRegisterThreadPoolConsumer(String queueName) {
        return registerThreadPoolConsumerMap.get(queueName);
    }

    public static void destroyRegisterConsumer() {
        Set<Map.Entry<String, ThreadPoolConsumer>> entries = registerThreadPoolConsumerMap.entrySet();
        entries.forEach(entry -> {
            ThreadPoolConsumer consumer = entry.getValue();
            consumer.stop();
        });
    }

    public static void startRegisterConsumer() {
        Set<Map.Entry<String, ThreadPoolConsumer>> entries = registerThreadPoolConsumerMap.entrySet();
        entries.forEach(entry -> {
            ThreadPoolConsumer consumer = entry.getValue();
            try {
                consumer.setStop(false);
                consumer.start();
            } catch (IOException e) {
                log.error("start register consumer:{} error.", JSON.toJSONString(consumer));
            }
        });

    }

    @PreDestroy
    public void destroy() {
        Collection<ThreadPoolConsumer> consumers = registerThreadPoolConsumerMap.values();
        if (!CollectionUtils.isEmpty(consumers)) {
            consumers.forEach(consumer -> {
                consumer.stop();
            });
        }
    }
}
