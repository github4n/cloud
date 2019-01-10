package com.iot.shcs.device.queue.sender;

import com.iot.common.mq.core.AbsSender;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.shcs.common.config.MonitorPropertiesConfig;
import com.iot.shcs.device.queue.bean.EmailTemplateMessage;
import com.iot.shcs.device.queue.utils.DeviceQueueConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: lucky @Descrpiton: @Date: 16:42 2018/8/10 @Modify by:
 */
@Slf4j
@Component
public class EmailSender extends AbsSender<EmailTemplateMessage> {

    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private MonitorPropertiesConfig monitorPropertiesConfig;
    //所属环境
    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 必须实现的
     */
    @PostConstruct
    public void init() {
        super.preInit();
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    public String exchange() {
        return DeviceQueueConstants.DEV_EMAIL_WARN_EXCHANGE;
    }

    @Override
    public String routing() {
        return DeviceQueueConstants.DEV_EMAIL_WARN_ROUTING;
    }

    @Override
    public String queue() {
        return DeviceQueueConstants.DEV_EMAIL_WARN_QUEUE;
    }

    @Override
    public ExchangeTypeEnum type() {
        return ExchangeTypeEnum.DIRECT;
    }

    public void sendEmailNotify(String title, String content) {
        if (monitorPropertiesConfig.getEmail() != null
                && monitorPropertiesConfig.getEmail().getEnabled()) {
            log.debug("发送短信通知：标题：{}，内容：{}", title, content);

            this.send(EmailTemplateMessage.builder()
                    .content(content)
                    .receiveTos(monitorPropertiesConfig.getEmail().getEmails())
                    .title("[" + active + "]" + title).build());
        }
    }
}
