package com.iot.common.mq.core;

import com.iot.common.mq.enums.ExchangeTypeEnum;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 13:44 2018/9/3
 * @Modify by:
 */
public abstract class BaseAccessBuilder {


    @Autowired
    private Environment environment;

    public final static String DEFAULT_EXCHANGE = "exchange";


    public final static String DEFAULT_ROUTING = "routing";


    public final static String DEFAULT_QUEUE = "routing";

    //线程数
    public final static int DEFAULT_THREAD_COUNT = 1;

    //处理间隔时间
    //mils
    public final static int DEFAULT_INTERVAL_MILS = 0;


    public abstract ConnectionFactory connectionFactory();

    public abstract String exchange();

    public abstract String routing();

    public abstract String queue();


    public abstract ExchangeTypeEnum type();



    public abstract void init();


    protected String getExchange() {
        if (StringUtils.isEmpty(exchange())) {
            return DEFAULT_EXCHANGE;
        }
        return exchange();
    }


    protected ExchangeTypeEnum getType() {
        if (type() == null) {
            return ExchangeTypeEnum.DIRECT;
        }
        return type();
    }

    public Environment getEnvironment() {
        return environment;
    }
}
