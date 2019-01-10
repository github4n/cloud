package com.iot.common.mq.consumer;

import com.google.common.collect.Lists;
import com.iot.common.mq.MQAccessBuilder;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.MessageProcess;
import com.iot.common.mq.utils.CurrentThreadConsumerUtils;
import com.iot.common.mq.utils.DetailRes;
import com.iot.common.mq.utils.MqConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by littlersmall on 16/5/23.
 */
@Slf4j
public class ThreadPoolConsumer<T> {
    private ThreadPoolConsumerBuilder<T> infoHolder;
    private ExecutorService executor;
    private volatile boolean stop = false;

    private List<MessageConsumer> messageConsumerList = Lists.newArrayList();

    private ThreadPoolConsumer(ThreadPoolConsumerBuilder<T> threadPoolConsumerBuilder) {
        this.infoHolder = threadPoolConsumerBuilder;
        executor = Executors.newFixedThreadPool(threadPoolConsumerBuilder.threadCount);
    }

    //1 构造messageConsumer
    //2 执行consume
    public void start() throws IOException {
        // 将消费者加入到map为后期直接做关闭等动作使用.
        CurrentThreadConsumerUtils.setRegisterThreadPoolConsumer(infoHolder.queue, this);
        for (int i = 0; i < infoHolder.threadCount; i++) {
            // 1
            final MessageConsumer messageConsumer =
                    infoHolder.mqAccessBuilder.buildMessageConsumer(
                            infoHolder.exchange,
                            infoHolder.routingKey,
                            infoHolder.queue,
                            infoHolder.autoDelete,
                            infoHolder.messageProcess,
                            infoHolder.type,
                            infoHolder.clazz);

            messageConsumerList.add(messageConsumer);
            executor.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            while (!stop) {
                                try {
                                    // 2
                                    DetailRes detailRes = messageConsumer.consume();

                                    if (infoHolder.intervalMils > 0) {
                                        try {
                                            Thread.sleep(infoHolder.intervalMils);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            log.info("interrupt ", e);
                                        }
                                    }

                                    if (!detailRes.isSuccess()) {
                                        log.info("run error meg:{} ", detailRes.getErrMsg());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.info("run exception ", e);
                                }
                            }
                        }
                    });
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

    }

    public void stop() {
        this.stop = true;
        CurrentThreadConsumerUtils.setRegisterThreadPoolConsumer(infoHolder.queue, this);
        try {
            closeChannel();
            Thread.sleep(MqConstants.ONE_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeChannel(){
        messageConsumerList.forEach(messageConsumer -> {
            messageConsumer.close();
        });
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void setThreadCountConsumer(int threadCount) {
        this.infoHolder.setThreadCount(threadCount);
    }

    //构造器
    public static class ThreadPoolConsumerBuilder<T> {
        public int threadCount;
        public long intervalMils;
        public MQAccessBuilder mqAccessBuilder;
        public String exchange;
        public String routingKey;
        public String queue;
        public boolean autoDelete = false;//默认消费者队列不删除 当没有任何消费者使用时，自动删除该队列
        public ExchangeTypeEnum type = ExchangeTypeEnum.DIRECT;
        public MessageProcess<T> messageProcess;
        public Class<T> clazz;
        public ThreadPoolConsumerBuilder<T> setThreadCount(int threadCount) {
            this.threadCount = threadCount;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setIntervalMils(long intervalMils) {
            this.intervalMils = intervalMils;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setMQAccessBuilder(MQAccessBuilder mqAccessBuilder) {
            this.mqAccessBuilder = mqAccessBuilder;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setExchange(String exchange) {
            this.exchange = exchange;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setRoutingKey(String routingKey) {
            this.routingKey = routingKey;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setQueue(String queue) {
            this.queue = queue;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setType(ExchangeTypeEnum type) {
            this.type = type;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setClazz(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public ThreadPoolConsumerBuilder<T> setMessageProcess(MessageProcess<T> messageProcess) {
            this.messageProcess = messageProcess;

            return this;
        }

        public ThreadPoolConsumerBuilder<T> setAutoDelete(boolean autoDelete) {
            this.autoDelete = autoDelete;

            return this;
        }
        public ThreadPoolConsumer<T> build() {
            ThreadPoolConsumer<T> threadPoolConsumer = new ThreadPoolConsumer<T>(this);
            return threadPoolConsumer;
        }
    }
}