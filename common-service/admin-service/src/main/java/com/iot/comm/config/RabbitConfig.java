
package com.iot.comm.config;

import com.iot.comm.utils.MqQueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit初始化配置
 */
@Configuration
public class RabbitConfig {


    /**
     * 初始化服务状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initMobileServiceStatusChangeQueue() {
        return new Queue(MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE);
    }

    /**
     * 初始化邮件状态改变队列
     *
     * @return
     */
    @Bean
    public Queue initEmailServiceStatusChangeQueue() {
        return new Queue(MqQueueConstant.EMAIL_SERVICE_STATUS_CHANGE);
    }


}
