package com.iot.robot.rabbit;

import com.iot.common.mq.config.AbsAmqpConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Slf4j
@Configuration
public class RobotAmqpConfig extends AbsAmqpConfig {

    //@Value("${rabbit-mq.host}")
    //@Value("${rabbit-mq-common.host}")
    //private String addresses;
    //@Value("${rabbit-mq.username}")
    //@Value("${rabbit-mq-common.username}")
    //private String username;
    //@Value("${rabbit-mq.pwd}")
    //@Value("${rabbit-mq-common.pwd}")
    //private String password;
    //@Value("${rabbit-mq.port}")
   // @Value("${rabbit-mq-common.port}")
    //private Integer port;

    //private boolean publisherConfirms = true;

    /*@Bean
    public ConnectionFactory connectionFactory() {
        log.info("***** init rabbitMq ConnectionFactory start");

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(port);

        *//** 如果要进行消息回调，则这里必须要设置为true *//*
        connectionFactory.setPublisherConfirms(publisherConfirms);
        return connectionFactory;
    }*/

    /*@Bean
    @Scope(scopeName = "prototype")
    public RabbitTemplate rabbitTemplate() {
        log.info("***** init rabbitMq RabbitTemplate start");

        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }*/
}
