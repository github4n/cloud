package com.iot.payment.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 
 * 项目名称：IOT云平台
 * 模块名称：rabbitmq 连接，发送
 * 功能描述：
 * 创建人： zhouzongwei
 * 创建时间：2017年12月11日 下午2:12:13
 * 修改人： zhouzongwei
 * 修改时间：2017年12月11日 下午2:12:13
 */
@Component
public class RabbitMqUtil {
	
   private  final static Logger logger = LoggerFactory.getLogger(RabbitMqUtil.class);
	
   private  ConnectionFactory factory;
   
   private  Connection connection;
   
   private  static Channel channel;
   
   @Value("${spring.rabbitmq.host}")
   private String host;
   
   @Value("${spring.rabbitmq.port}")
   private int port;
   
   @Value("${spring.rabbitmq.username}")
   private String username;
   
   @Value("${spring.rabbitmq.password}")
   private String password;
   
   
   @PostConstruct
   private void init() {
	   
	   logger.info(String.format("host =%s, port=%d,  username=%s, password=%s ", host,port,username,password));
	   
	   factory = new ConnectionFactory();
	   factory.setUsername(username);
	   factory.setPassword(password);
	   factory.setHost(host);
	   factory.setPort(port);
	   try {
		   factory.setAutomaticRecoveryEnabled(true);
		   factory.setNetworkRecoveryInterval(10000);
		   connection = factory.newConnection();
		   channel =  connection.createChannel();
	   } catch (IOException e) {
		   logger.error("create rabbitmq connection fail..",e);
	   } catch (TimeoutException e) {
		   logger.error("create rabbitmq connection fail..",e);
	   }
   }
   
   
	
   /**
    * 
    * 描述：发送消息到队列
    * @author zhouzongwei
    * @created 2017年12月11日 下午2:31:47
    * @since 
    * @param queueName
    * @param exchangeName
    * @param routeKey
    * @param message
    * @throws IOException 
    */
	public static void sendQueue(String queueName,String exchangeName,String routeKey,String message) throws IOException{
		try {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicPublish(exchangeName,routeKey, null, message.getBytes());
		} catch (IOException e) {
			logger.error("send to rabbitmq fail..",e);
			throw e;
		}
	}

}
