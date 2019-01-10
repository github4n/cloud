package com.iot.common.mq.sender;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @Author: lucky
 * @Descrpiton: 单个线程处理 接收发送端缓冲消息 进行发送
 * @Date: 17:02 2018/12/6
 * @Modify by:
 */
@Slf4j
public class ExecutorMessageSender {

    Connection connection;

    Channel channel;

    ConcurrentLinkedQueue<Object> msgQueue = new ConcurrentLinkedQueue<>();//消息缓冲队列

    Queue<Object> errorMsgQueue = new LinkedBlockingQueue<>();//错误消息缓冲队列

    String exchange;

    String routingKey;


    public ExecutorMessageSender(Connection connection, Channel channel, String exchange, String routingKey) {
        this.connection = connection;
        this.channel = channel;
        this.exchange = exchange;
        this.routingKey = routingKey;

        new ExecutorTask(channel).start();
    }


    public void close(){
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException ex) {
            log.error("exception", ex);
        }
    }
    public void addQueueMsg(Object msg) {
        msgQueue.add(msg);
    }

    private void send(Object msgData, Channel channel) throws IOException {
        channel.basicPublish(exchange, routingKey != null ? routingKey : "", null, JSON.toJSONBytes(msgData));
    }


    public class ExecutorTask extends Thread {

        private Channel channel;

        public ExecutorTask(Channel channel) {
            this.channel = channel;
        }

        public void run() {
            while (true) {
                Object msgData = msgQueue.poll();
                if (msgData == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log.info("executor sender msg task sleep error.", e);
                    }
                } else {
                    try {
                        send(msgData, channel);
                    } catch (ShutdownSignalException | ConsumerCancelledException | IOException e) {
                        log.error("exception", e);
                        try {
                            channel.close();
                        } catch (IOException | TimeoutException ex) {
                            log.error("exception", ex);
                        }
                        channel = connection.createChannel(false);
                        log.info("shutdown or cancelled exception " + e.toString());
                    } catch (Exception e) {
                        log.info("executor send channel error.", e);
                        errorMsgQueue.add(msgData);
                        try {
                            channel.close();
                        } catch (IOException | TimeoutException ex) {
                            log.error("exception", ex);
                        }
                        channel = connection.createChannel(false);
                    }
                }
            }
        }
    }
}
