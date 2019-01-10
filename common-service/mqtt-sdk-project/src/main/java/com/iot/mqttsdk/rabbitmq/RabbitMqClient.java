package com.iot.mqttsdk.rabbitmq;


import com.iot.mqttsdk.common.Arguments;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.DeadBackProcessor;
import com.iot.mqttsdk.common.ModuleConstants;
import com.iot.mqttsdk.rabbitmq.model.RabbitmqProperty;
import com.iot.mqttsdk.rabbitmq.model.RegisterModel;
import com.iot.mqttsdk.rabbitmq.monitor.MQMessageProcessor;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：RabbitMq客户端
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:34
 */
@Component
@Service
public class RabbitMqClient {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqClient.class);

    /**
     * rabbitmq连接工厂
     */
    private static ConnectionFactory factory;

    /**
     * rabbitmq连接
     */
    private static Connection connection;

    /**
     * 通道
     */
    private static volatile Channel channel;

    /**
     * 连接属性
     */
    private static RabbitmqProperty rabbitMqProperty;

    /**
     * 回调缓存
     */
    public  static Map<String, RegisterModel> registerModelMap =  new HashMap<>();

    /**
     * 存储死信队列名称
     */
    public static Set<String> DEAD_QUEUES =  new HashSet<>();

    /**
     * 描述：连接 Rabbitmq
     * @author 490485964@qq.com
     * @date 2018/4/20 15:55
     * @param property 配置信息
     * @return
     */
    public void connectRabbitmq(RabbitmqProperty property){
        this.rabbitMqProperty = property;
        LOGGER.info("connecting mq on "+ rabbitMqProperty.getHost()+" : "+ rabbitMqProperty.getPort());
        factory = new ConnectionFactory();
        factory.setUsername(rabbitMqProperty.getUserName());
        factory.setPassword(rabbitMqProperty.getPassWord());
        factory.setHost(rabbitMqProperty.getHost());
        factory.setPort(rabbitMqProperty.getPort());
        factory.setAutomaticRecoveryEnabled(true);
        factory.setTopologyRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(60000);
        factory.setConnectionTimeout(60000);
        try {
            Channel channel = getChannel();
            channel.exchangeDeclare(ModuleConstants.DEFAULT_EXCHANGE, "direct", true, false, null);
            LOGGER.info("RabbitMQ Connection Success");
        } catch (Exception e) {
            LOGGER.error("create rabbitmq connection fail.. ", e);
            try {
                TimeUnit.SECONDS.sleep(1);
                connectRabbitmq(this.rabbitMqProperty);
            } catch (InterruptedException e1) {
                LOGGER.error("InterruptedException.. ", e);
                e1.printStackTrace();
            }
        }
    }

    private Channel getChannel() throws Exception{
        if(channel != null && channel.isOpen()) {
            return channel;
        }
        synchronized (RabbitMqClient.class){
            if(channel != null && channel.isOpen()) {
                return channel;
            }
            if(connection == null || !connection.isOpen()) {
                connection = factory.newConnection();
            }
            channel = connection.createChannel();
        }
       return channel;
    }

    /**
     * 描述：设置回调
     * @author 490485964@qq.com
     * @date 2018/4/20 15:56
     * @param queueName 队列名
     * @param callBackProcessor 正常回调实例
     * @param callBackProcessor 死信回调实例
     * @param arguments 其他参数
     * @return
     */
    public void registerCallBack(String queueName, CallBackProcessor callBackProcessor, DeadBackProcessor deadBackProcessor, Arguments arguments) throws Exception{
        Map<String, Object> args = this.getDefaultArgs(queueName);
        if(arguments.isDeadSwitchOn()){
            if(StringUtils.isEmpty(arguments.getDeadQueueName())){
                throw new Exception("deadQueueName is null.");
            }
            if(StringUtils.isEmpty(arguments.getDeadLetterRoutingKey())){
                throw new Exception("deadLetterRoutingKey is null.");
            }
            if(StringUtils.isEmpty(arguments.getMessageTtl())){
                throw new Exception("messageTtl is null.");
            }
            args.put("x-dead-letter-exchange", ModuleConstants.DEFAULT_EXCHANGE);
            args.put("x-dead-letter-routing-key", arguments.getDeadLetterRoutingKey());
            args.put("x-message-ttl", arguments.getMessageTtl());
            DEAD_QUEUES.add(arguments.getDeadQueueName());
            this.binding(arguments.getDeadQueueName(), null);
            if(arguments.isDeadAutoConsume()){
                String queueUrl = this.createQueueUrl(arguments.getDeadQueueName(), Boolean.FALSE);
                this.register(arguments.getDeadQueueName(), null, deadBackProcessor, queueUrl);
            }
        }
        this.binding(queueName, args);
        if(arguments.isNormAutoConsume()){
            String queueUrl = arguments.isDeadSwitchOn()?this.createQueueUrl(queueName, Boolean.TRUE):this.createQueueUrl(queueName, Boolean.FALSE);
            this.register(queueName, callBackProcessor, null, queueUrl);
        }
    }

    /** 
     * 描述：注册事件
     * @author maochengyuan
     * @created 2018/11/19 15:24
     * @param queueName 队列名称
     * @param callBackProcessor 正常回调函数
     * @param deadBackProcessor 死信回调函数
     * @param queueUrl 消费URL
     * @return void
     */
    private void register(String queueName, CallBackProcessor callBackProcessor, DeadBackProcessor deadBackProcessor, String queueUrl){
        RegisterModel registerModel = new RegisterModel(queueUrl);
        registerModel.setMqMessageProcessor(new MQMessageProcessor(callBackProcessor, deadBackProcessor));
        registerModelMap.put(queueName, registerModel);
    }

    /**
     * 绑定操作
     * @param queueName 队列名称
     * @param args 参数
     * @throws Exception
     */
    private void binding(String queueName, Map<String, Object> args) throws Exception{
        channel.queueDeclare(queueName, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, args);
        channel.queueBind(queueName, ModuleConstants.DEFAULT_EXCHANGE, queueName);
    }

    /**
     * 描述：构建MQ消费URL
     * @author maochengyuan
     * @created 2018/11/19 11:00
     * @param queueName 队列名称
     * @param skipQueueDeclare 是否跳过定义
     * @return java.lang.String
     */
    private String createQueueUrl(String queueName, Boolean skipQueueDeclare){
        StringBuilder queueUrl = new StringBuilder("rabbitmq://");
        queueUrl.append(rabbitMqProperty.getHost()).append(":").append(rabbitMqProperty.getPort());
        queueUrl.append("/").append(ModuleConstants.DEFAULT_EXCHANGE);
        queueUrl.append("?username=").append(rabbitMqProperty.getUserName());
        queueUrl.append("&password=").append(rabbitMqProperty.getPassWord());
        queueUrl.append("&autoDelete=false");
        queueUrl.append("&routingKey=").append(queueName);
        queueUrl.append("&queue=").append(queueName);
        queueUrl.append("&prefetchEnabled=true");
        queueUrl.append("&prefetchCount="+ModuleConstants.BLOCKING_QUEUE_SIZE);
        queueUrl.append("&automaticRecoveryEnabled=true");
        queueUrl.append("&autoAck=false");
        if(ModuleConstants.specialQueueArray.contains(queueName)){
            queueUrl.append("&concurrentConsumers="+ModuleConstants.THREAD_NUM_LARGE);
        }else{
            queueUrl.append("&concurrentConsumers="+ModuleConstants.THREAD_NUM_MIN);
        }
        if(skipQueueDeclare){
            queueUrl.append("&skipQueueDeclare=true");
        }
        return queueUrl.toString();
    }

    /**
     * 描述：获取默认参数
     * @author maochengyuan
     * @created 2018/11/16 11:58
     * @param queueName
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    private Map<String, Object> getDefaultArgs(String queueName){
        Map<String, Object> args = new HashMap<>();
        args.put("routingKey", queueName);
        return args;
    }

    /** 
     * 描述：发送消息
     * @author maochengyuan
     * @created 2018/11/20 19:13
     * @param routingKey 路由key
     * @param message 消息内容
     * @return void
     */
    public void basicPublish(String routingKey, String message) {
        if(StringUtils.isEmpty(routingKey)){
            LOGGER.error("RoutingKey is null."); return;
        }
        if(StringUtils.isEmpty(message)){
            LOGGER.error("Message is null."); return;
        }
        try {
            Channel channel = getChannel();
            channel.basicPublish(ModuleConstants.DEFAULT_EXCHANGE, routingKey, null, message.getBytes());
            LOGGER.error("publish Success. message content:"+message);
        } catch (Exception e) {
            LOGGER.error("publish failed. message content:"+message, e);
        }
    }

    public Map<String, RegisterModel> getRegisterModelMap(){
        return registerModelMap;
    }

}
