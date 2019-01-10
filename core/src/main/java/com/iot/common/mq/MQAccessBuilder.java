package com.iot.common.mq;

import com.alibaba.fastjson.JSON;
import com.iot.common.mq.consumer.MessageConsumer;
import com.iot.common.mq.enums.ExchangeTypeEnum;
import com.iot.common.mq.process.MessageProcess;
import com.iot.common.mq.process.PoolExecutorProcess;
import com.iot.common.mq.sender.ExecutorMessageSender;
import com.iot.common.mq.sender.MessageSender;
import com.iot.common.mq.utils.DetailRes;
import com.iot.common.mq.utils.MessageWithTime;
import com.iot.common.mq.utils.MqConstants;
import com.iot.common.mq.utils.RetryCache;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by littlersmall on 16/5/11.
 */
@Slf4j
public class MQAccessBuilder {
    private ConnectionFactory connectionFactory;

    private Environment environment;

    public MQAccessBuilder(ConnectionFactory connectionFactory, Environment environment) {
        this.connectionFactory = connectionFactory;
        this.environment = environment;
    }

    public MessageSender buildFountMessageSender(final String exchange, final String routingKey, final String queue) throws IOException {
        return buildMessageSender(exchange, routingKey, queue, ExchangeTypeEnum.FANOUT);
    }

    public MessageSender buildDirectMessageSender(final String exchange, final String routingKey, final String queue) throws IOException {
        return buildMessageSender(exchange, routingKey, queue, ExchangeTypeEnum.DIRECT);
    }

    public MessageSender buildTopicMessageSender(final String exchange, final String routingKey) throws IOException {
        return buildMessageSender(exchange, routingKey, null, ExchangeTypeEnum.TOPIC);
    }

    public MessageSender buildFanoutMessageSender(final String exchange, final String routingKey) throws IOException {
        return buildMessageSender(exchange, routingKey, null, ExchangeTypeEnum.FANOUT);
    }
    //1 构造template, exchange, routingkey等
    //2 构造sender方法
    public MessageSender buildMessageSender(final String exchange, final String routingKey,
                                            final String queue, final ExchangeTypeEnum type) throws IOException {
        //1
        buildSendQueue(exchange,routingKey, queue, type, false);
        //2
        return new MessageSender() {
            Connection connection;
            Channel channel;
            ExecutorMessageSender sendMessageTask;
            {
                connection = connectionFactory.createConnection();
                channel = connection.createChannel(false);
                sendMessageTask = new ExecutorMessageSender(connection, channel, exchange, routingKey);
            }

            public DetailRes send(Object message) {
                sendMessageTask.addQueueMsg(message);
                return new DetailRes(true, "");
            }

            public DetailRes send(MessageWithTime messageWithTime) {
                try {
                    channel.basicPublish(exchange, routingKey, null, JSON.toJSONBytes(messageWithTime.getMessage()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return new DetailRes(false, "");
                }
                return new DetailRes(true, "");
            }

            public void close() {
                sendMessageTask.close();
            }
        };
    }

    public <T> MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue, boolean autoDelete,
                                                    final MessageProcess<T> messageProcess, Class<T> clazz) throws IOException {
        return buildMessageConsumer(exchange, routingKey, queue, autoDelete, messageProcess, ExchangeTypeEnum.DIRECT, clazz);
    }

    public <T> MessageConsumer buildTopicMessageConsumer(String exchange, String routingKey, final String queue, boolean autoDelete,
                                                         final MessageProcess<T> messageProcess, Class<T> clazz) throws IOException {
        return buildMessageConsumer(exchange, routingKey, queue, autoDelete, messageProcess, ExchangeTypeEnum.TOPIC, clazz);
    }

    public <T> MessageConsumer buildFanoutMessageConsumer(String exchange, String routingKey, final String queue, boolean autoDelete,
                                                          final MessageProcess<T> messageProcess, Class<T> clazz) throws IOException {
        return buildMessageConsumer(exchange, routingKey, queue, autoDelete, messageProcess, ExchangeTypeEnum.FANOUT, clazz);
    }

    //1 创建连接和channel
    //2 设置message序列化方法
    //3 consume
    public <T> MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue, boolean autoDelete,
                                                    final MessageProcess<T> messageProcess, ExchangeTypeEnum type, Class<T> clazz) throws IOException {
        final Connection connection = connectionFactory.createConnection();

        //1
        buildConsumerQueue(exchange, routingKey, queue, type, autoDelete);

        //2
        final MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();
        final MessageConverter messageConverter = new Jackson2JsonMessageConverter();
    // 3
    return new MessageConsumer() {
      Channel channel;
      PoolExecutorProcess<T> poolExecutorProcess;

      {
        channel = connection.createChannel(false);
        channel.basicQos(0, 1, false);
        poolExecutorProcess = new PoolExecutorProcess<>(environment, messageProcess, queue);
      }

      // 1 通过basicGet获取原始数据
      // 2 将原始数据转换为特定类型的包
      // 3 处理数据
      // 4 手动发送ack确认
      @Override
      public DetailRes consume() {
        try {
          if (!poolExecutorProcess.checkPoolIde()) {
            Thread.sleep(10);
            // 非空闲
            return new DetailRes(true, "当前队列:{" + queue + "}正在忙碌中...");
          }
          GetResponse response = channel.basicGet(queue, false);

          while (response == null) {
            response = channel.basicGet(queue, false);
            Thread.sleep(100);
          }
          // 2
          T messageBean = JSON.parseObject(new String(response.getBody(), "UTF-8"), clazz);
          // 3
          DetailRes detailRes;

          try {
            poolExecutorProcess.process(messageBean);
            detailRes = new DetailRes(true, "");
          } catch (Exception e) {
            log.error("exception", e);
            detailRes = new DetailRes(false, "process exception: " + e);
          }

          // 4
          if (detailRes.isSuccess()) {
            channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
          } else {
            // 避免过多失败log
            Thread.sleep(MqConstants.ONE_SECOND);
            log.debug("process message failed: " + detailRes.getErrMsg());
            channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);
          }

          return detailRes;
        } catch (InterruptedException e) {
          log.error("exception", e);
          return new DetailRes(false, "interrupted exception " + e.toString());
        } catch (ShutdownSignalException | ConsumerCancelledException | IOException e) {
          log.error("exception", e);

          try {
            channel.close();
          } catch (IOException | TimeoutException ex) {
            log.error("exception", ex);
          }

          channel = connection.createChannel(false);

          return new DetailRes(false, "shutdown or cancelled exception " + e.toString());
        } catch (Exception e) {
          e.printStackTrace();
          log.error("exception : ", e);

          try {
            channel.close();
          } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
          }

          channel = connection.createChannel(false);

          return new DetailRes(false, "exception " + e.toString());
        }
      }

      @Override
      public void close() {
        poolExecutorProcess.shutdown();
      }
    };
    }

    private void buildQueue(String exchange, String routingKey,
                            final String queue, boolean autoDelete, Connection connection, ExchangeTypeEnum type) throws IOException {
        Channel channel = connection.createChannel(false);

        if (type.equals(ExchangeTypeEnum.DIRECT)) {
            channel.exchangeDeclare(exchange, "direct", true, false, null);
        } else if (type.equals(ExchangeTypeEnum.TOPIC)) {
            channel.exchangeDeclare(exchange, "topic", true, false, null);
        } else if (type.equals(ExchangeTypeEnum.FANOUT)) {
            channel.exchangeDeclare(exchange, "fanout", true, false, null);
        }

        if (autoDelete) {
            // autodelete：当没有任何消费者使用时，自动删除该队列。this means that the queue will be deleted when there are no more processes consuming messages from it.
            channel.queueDeclare(queue, true, true, true, null);
        } else {
            channel.queueDeclare(queue, true, false, false, null);
        }

        if (!type.equals(ExchangeTypeEnum.FANOUT)) {
            channel.queueBind(queue, exchange, routingKey);
        } else {
            channel.queueBind(queue, exchange, "");

        }
        try {
            channel.close();
        } catch (TimeoutException e) {
            log.info("close channel time out ", e);
        }
    }

    public void buildSendQueue(String exchange, String routingKey,
                           final String queue, ExchangeTypeEnum type, boolean autoDelete) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        if (type.equals(ExchangeTypeEnum.DIRECT)) {
            Queue adminQueue = new Queue(queue);
            admin.declareQueue(adminQueue);
            DirectExchange directExchange = new DirectExchange(exchange, true, autoDelete, null);
            admin.declareExchange(directExchange);
            admin.declareBinding(
                    BindingBuilder.bind(adminQueue).to(directExchange).with(routingKey));
        } else if (type.equals(ExchangeTypeEnum.TOPIC)) {
            TopicExchange topicExchange = new TopicExchange(exchange, true, autoDelete, null);
            admin.declareExchange(topicExchange);
        } else if (type.equals(ExchangeTypeEnum.FANOUT)) {
            FanoutExchange fanoutExchange = new FanoutExchange(exchange, true, autoDelete, null);
            admin.declareExchange(fanoutExchange);
        }
    }

    public void buildConsumerQueue(String exchange, String routingKey,
                           final String queue, ExchangeTypeEnum type, boolean autoDelete) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        Queue adminQueue;
        if (autoDelete) {
            // autodelete：当没有任何消费者使用时，自动删除该队列。this means that the queue will be deleted when there are no more processes consuming messages from it.
            adminQueue = new Queue(queue,true, true,true, null);
        } else {
            adminQueue = new Queue(queue);
        }
        admin.declareQueue(adminQueue);

        if (type.equals(ExchangeTypeEnum.DIRECT)) {
            DirectExchange directExchange = new DirectExchange(exchange, true, false, null);
            admin.declareExchange(directExchange);
            admin.declareBinding(
                    BindingBuilder.bind(adminQueue).to(directExchange).with(routingKey));
        } else if (type.equals(ExchangeTypeEnum.TOPIC)) {
            TopicExchange topicExchange = new TopicExchange(exchange, true, false, null);
            admin.declareExchange(topicExchange);
            admin.declareBinding(
                    BindingBuilder.bind(adminQueue).to(topicExchange).with(routingKey));

        } else if (type.equals(ExchangeTypeEnum.FANOUT)) {
            FanoutExchange fanoutExchange = new FanoutExchange(exchange, true, false, null);
            admin.declareExchange(fanoutExchange);
            admin.declareBinding(
                    BindingBuilder.bind(adminQueue).to(fanoutExchange));
        }
    }

    private void buildTopic(String exchange, Connection connection) throws IOException {
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare(exchange, "topic", true, false, null);
    }

    private void buildFanout(String exchange, Connection connection) throws IOException {
        Channel channel = connection.createChannel(false);
        channel.exchangeDeclare(exchange, "fanout", true, false, null);
    }

    //for test
    public int getMessageCount(final String queue) throws IOException {
        Connection connection = connectionFactory.createConnection();
        final Channel channel = connection.createChannel(false);
        final AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queue);

        return declareOk.getMessageCount();
    }
}