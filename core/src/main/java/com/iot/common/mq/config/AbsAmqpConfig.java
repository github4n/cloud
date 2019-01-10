package com.iot.common.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Slf4j
public abstract class AbsAmqpConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        log.info("init connectionFactory by mq.");
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(environment.getProperty("rabbit-mq-common.host"));
        connectionFactory.setUsername(environment.getProperty("rabbit-mq-common.username"));
        connectionFactory.setPassword(environment.getProperty("rabbit-mq-common.pwd"));
        String port = environment.getProperty("rabbit-mq-common.port");
        connectionFactory.setPort(Integer.valueOf(port));
        connectionFactory.setVirtualHost("/");
        connectionFactory.setChannelCacheSize(100);
//        connectionFactory.setPublisherConfirms(true); // enable confirm mode
        return connectionFactory;
    }
}
