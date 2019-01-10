package com.iot.common.mq.utils;

/**
 * @author : lucky
 * @Description mq队列属性
 * @Date : 2017-09-11
 * @Time : 16:49
 * Blogs http://www.cnblogs.com/xfzlovezjj/
 */
public class MqConstants {


    public static final String topicEXCHANGE = "topicExchange";

    public static final String defaultQueueName = "topic.messages";

    public static final String topicROUTINGKEY = "topic.messages";


    public static final String notifyQueueName = "topic.notify";

    public static final String notifyROUTINGKEY = "topic.notify";

    public static final String queryQueueName = "topic.query";

    public static final String queryROUTINGKEY = "topic.query";


    public static final String channelRequestQueueName = "topic.channelRequest";

    public static final String channelRequestROUTINGKEY = "topic.channelRequest";


    public static final String smsSendQueueName = "topic.smsSend";

    public static final String smsSendROUTINGKEY = "topic.smsSend";


    public static final String emailQueueName = "topic.email";

    public static final String emailROUTINGKEY = "topic.email";

    /**
     * 路由配置状态队列
     */
    public static final String ROUTE_CONFIG_CHANGE = "route_config_change";


    //线程数
    public final static int THREAD_COUNT = 5;

    //线程数
    public final static int MAX_THREAD_COUNT = 20;
    //处理间隔时间
    //mils
    public final static int INTERVAL_MILS = 0;

    //consumer失败后等待时间(mils)
    public static final int ONE_SECOND = 1 * 1000;

    //异常sleep时间(mils)
    public static final int ONE_MINUTE = 1 * 60 * 1000;
    //MQ消息retry时间
    public static final int RETRY_TIME_INTERVAL = ONE_MINUTE;
    //MQ消息有效时间
    public static final int VALID_TIME = ONE_MINUTE;
}
