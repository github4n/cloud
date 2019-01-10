package com.iot.common.mq.process;

import com.iot.common.mq.consumer.MessageConsumer;
import com.iot.common.mq.utils.DetailRes;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 10:42 2018/8/20
 * @Modify by:
 */
@Slf4j
public class AsyncMessageProcessingConsumer implements Runnable {

    private final CountDownLatch start;

    private MessageConsumer messageConsumer;

    private long intervalMils;

    private volatile boolean stop = false;

    public AsyncMessageProcessingConsumer(MessageConsumer messageConsumer, long intervalMils, boolean stop) {
        this.intervalMils = intervalMils;
        this.stop = stop;
        this.messageConsumer = messageConsumer;
        start = new CountDownLatch(1);
    }

    public AsyncMessageProcessingConsumer(MessageConsumer messageConsumer, long intervalMils) {
        this.intervalMils = intervalMils;
        this.messageConsumer = messageConsumer;
        start = new CountDownLatch(1);
    }

    public AsyncMessageProcessingConsumer(MessageConsumer messageConsumer, boolean stop) {
        this.stop = stop;
        this.messageConsumer = messageConsumer;
        start = new CountDownLatch(1);
    }

    public AsyncMessageProcessingConsumer(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
        start = new CountDownLatch(1);
    }

    public void run() {
        while (!stop) {
            log.info("---------------start>>>>>>>>>>>>>>>>>>>>>>>>>>>>-------------------");
            try {
                DetailRes detailRes = messageConsumer.consume();
                if (intervalMils > 0) {
                    try {
                        Thread.sleep(intervalMils);
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
            // In all cases count down to allow container to progress beyond startup
            this.start.countDown();
        }
    }

    public void stop() {
        this.stop = true;
    }

    public void start() {
        this.stop = false;
    }
}
