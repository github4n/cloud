package com.iot.common.mq.web;

import com.iot.common.mq.consumer.ThreadPoolConsumer;
import com.iot.common.mq.monitor.MQClientMonitorService;
import com.iot.common.mq.utils.CurrentThreadConsumerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: lucky
 * @Descrpiton: https://blog.csdn.net/u011424653/article/details/79824538
 * https://www.jianshu.com/p/4112d78a8753
 * @Date: 15:09 2018/8/15
 * @Modify by:
 */
@RestController
@RequestMapping("/mqManage")
public abstract class BaseMqManageController {

    /**
     * 线程池
     */
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    @Autowired
    private MQClientMonitorService mqClientMonitorService;

    /**
     * 重置消息队列并发消费者数量
     *
     * @param queueName
     * @param concurrentConsumers must greater than zero
     * @return
     */
    @GetMapping("/restConcurrentConsumers")
    public Object restConcurrentConsumers(@RequestParam(value = "queueName") String queueName,
                                          @RequestParam(value = "concurrentConsumers", defaultValue = "1") Integer concurrentConsumers) {
        return mqClientMonitorService.resetQueueConcurrentConsumers(queueName, concurrentConsumers);
    }

    /**
     * 重启对消息队列的监听
     *
     * @param queueName
     * @return
     */
    @GetMapping("/restartMessageListener")
    public Object restartMessageListener(@RequestParam("queueName") String queueName) {
        return mqClientMonitorService.restartMessageListener(queueName);
    }

    @GetMapping("/restartAllMessageListener")
    public Object restartAllMessageListener() {
        List<MQClientMonitorService.MessageQueueDetail> queueDetailList = getAllMessageQueueDetail();
        if (!CollectionUtils.isEmpty(queueDetailList)) {
            queueDetailList.forEach(messageQueueDetail -> {
                String queueName = messageQueueDetail.getQueueName();
                if (!StringUtils.isEmpty(queueName)) {
                    restartMessageListener(queueName);
                }
            });
        }
        return true;
    }

    /**
     * 停止对消息队列的监听
     *
     * @param queueName
     * @return
     */
    @GetMapping("/stopMessageListener")
    public Object stopMessageListener(String queueName) {
        return mqClientMonitorService.stopMessageListener(queueName);

    }

    /**
     * 停止所有队列对消息队列的监听
     *
     * @return
     */
    @GetMapping("/stopAllMessageListener")
    public Object stopAllMessageListener() {

        List<MQClientMonitorService.MessageQueueDetail> queueDetailList = getAllMessageQueueDetail();
        if (!CollectionUtils.isEmpty(queueDetailList)) {
            queueDetailList.forEach(messageQueueDetail -> {
                String queueName = messageQueueDetail.getQueueName();
                if (!StringUtils.isEmpty(queueName)) {
                    stopMessageListener(queueName);
                }
            });
        }
        return true;
    }

    /**
     * 统计所有消息队列详情
     *
     * @return
     */
    @GetMapping("/getAllMessageQueueDetail")
    public List<MQClientMonitorService.MessageQueueDetail> getAllMessageQueueDetail() {

        return mqClientMonitorService.getAllMessageQueueDetail();
    }

    @GetMapping("/destroyAllRegisterQueue")
    public Object destroyAllRegisterQueue() {
        CurrentThreadConsumerUtils.destroyRegisterConsumer();
        return true;
    }

    @GetMapping("/restartAllRegisterQueue")
    public Object restartAllRegisterQueue() {
        CurrentThreadConsumerUtils.startRegisterConsumer();
        return true;
    }

    /**
     * 重置消息队列并发消费者数量
     *
     * @param queueName
     * @param concurrentConsumers must greater than zero
     * @return
     */
    @GetMapping("/restRegisterConsumers")
    public Object restRegisterConsumers(@RequestParam(value = "queueName") String queueName,
                                        @RequestParam(value = "concurrentConsumers", defaultValue = "1") Integer concurrentConsumers) {
        Assert.state(queueName != null && queueName != "", "参数 'queueName' 必须不为空.");
        ThreadPoolConsumer threadPoolConsumer = CurrentThreadConsumerUtils.getRegisterThreadPoolConsumer(queueName);
        Assert.state(threadPoolConsumer != null, "线程 'threadPoolConsumer' 必须不存在.");
        //TODO
        return true;

    }
}
