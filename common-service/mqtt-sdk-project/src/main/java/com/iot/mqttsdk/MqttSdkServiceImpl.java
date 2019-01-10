package com.iot.mqttsdk;

import com.alibaba.fastjson.JSON;
import com.iot.mqttsdk.common.*;
import com.iot.mqttsdk.mqtt.ClientCloseListener;
import com.iot.mqttsdk.mqtt.MQTTClient;
import com.iot.mqttsdk.mqtt.MqttProperty;
import com.iot.mqttsdk.rabbitmq.RabbitMqClient;
import com.iot.mqttsdk.rabbitmq.model.RabbitmqProperty;
import com.iot.mqttsdk.rabbitmq.monitor.MQMessageMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mqttSdkService")
public class MqttSdkServiceImpl implements MqttSdkService {

	private static final Logger logger = LoggerFactory.getLogger(MqttSdkServiceImpl.class);

	@Autowired
	private RabbitMqClient rabbitMqClient;

	@Autowired
	private MQMessageMonitor messageMonitor;

	@Autowired
	private MQTTClient mqttClient;

	private ConfigInfo configInfo;
	@Override
    public void createConnections(ConfigInfo configInfo) {
        createConnections(configInfo, null);
    }
    

    @Override
    public void createConnections(ConfigInfo configInfo,
            ClientCloseListener listener) {
        if (null == configInfo) return;
        this.configInfo = configInfo;
        List<MqttProperty> mqttPropertyList = configInfo.getMqttPropertyList();
        if(null != mqttPropertyList && mqttPropertyList.size()>0){
            mqttClient.connectMqtt(mqttPropertyList, listener);
        }
        RabbitmqProperty rabbitmqProperty = configInfo.getRabbitmqProperty();
        if(null != rabbitmqProperty){
            rabbitMqClient.connectRabbitmq(rabbitmqProperty);
        }
    }

	@Override
	public boolean sendMessage(String clientId, MqttMsg msg, int qos) {
		msg.setClientId(clientId);
		String jsonMessage = JSON.toJSONString(msg);
		return mqttClient.publish(clientId,msg.getTopic(),jsonMessage,qos);
	}

	@Override
	public boolean sendMessage(List<String> clientIdList, MqttMsg msg, int qos) {
		if (null == clientIdList || clientIdList.size()==0) return false;
		boolean result = true;
		for (String clientId : clientIdList){
			msg.setClientId(clientId);
			String jsonMessage = JSON.toJSONString(msg);
			boolean resultTemp = mqttClient.publish(clientId,msg.getTopic(),jsonMessage,qos);
			if(!resultTemp){
				result = resultTemp;
			}
		}
		return result;
	}

	@Override
	public boolean addCallBack(String queueName, CallBackProcessor callBackProcessor) {
		return this.addCallBack(queueName, new Arguments(callBackProcessor));
	}

	/**
	 * 描述：注册回调实例
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:59
	 * @param  queueName 队列名称
	 * @param  arguments 其他参数
	 * @return
	 */
	@Override
	public boolean addCallBack(String queueName, Arguments arguments){
		if (this.configInfo  == null){
			logger.error("addCallBack error,configInfo is null");
			return false;
		}
		if(arguments == null){
			arguments = new Arguments();
		}
		try {
			if(this.configInfo.getUseRabbitMQ()){
				logger.info("registerCallBack to rabbitMq,queueName->"+queueName);
				this.rabbitMqClient.registerCallBack(queueName, arguments.getCallBackProcessor(), arguments.getDeadBackProcessor(), arguments);
				if(arguments.isNormAutoConsume()){
					this.messageMonitor.addRoutes(queueName, this.rabbitMqClient.getRegisterModelMap().get(queueName));
				}
				if(arguments.isDeadSwitchOn() && arguments.isDeadAutoConsume()){
					this.messageMonitor.addRoutes(arguments.getDeadQueueName(), this.rabbitMqClient.getRegisterModelMap().get(arguments.getDeadQueueName()));
				}
			}else {
				logger.info("registerCallBack to mqtt,serviceName->"+queueName);
				this.mqttClient.registerCallBack(queueName, arguments.getCallBackProcessor());
			}
			return true;
		}catch (Exception e){
			logger.error("addCallBack error->",e);
			return false;
		}
	}

	@Override
	public boolean subscribe(String clientId, String topic, int qos) {
		if(null == this.configInfo) return false;
		boolean useRabbitMQ = this.configInfo.getUseRabbitMQ();
		if(useRabbitMQ) return false;
		return mqttClient.subscribe(clientId,topic,qos);
	}

	@Override
	public boolean subscribe(List<String> clientIdList, String topic, int qos) {
		if(null == this.configInfo) return false;
		boolean useRabbitMQ = this.configInfo.getUseRabbitMQ();
		if(useRabbitMQ) return false;
		if (null == clientIdList || clientIdList.size()==0) return false;
		boolean result = true;
		for (String clientId : clientIdList){
			boolean resultTemp = mqttClient.subscribe(clientId,topic,qos);
			if(!resultTemp){
				result = resultTemp;
			}
		}
		return result;
	}

	@Override
	public boolean unSubscribe(String clientId, String topic) {
		if(null == this.configInfo) return false;
		boolean useRabbitMQ = this.configInfo.getUseRabbitMQ();
		if(useRabbitMQ) return false;
		return mqttClient.unSubscribe(clientId,topic);
	}

	@Override
	public boolean unSubscribe(List<String> clientIdList, String topic) {
		if(null == this.configInfo) return false;
		boolean useRabbitMQ = this.configInfo.getUseRabbitMQ();
		if(useRabbitMQ) return false;
		if (null == clientIdList || clientIdList.size()==0) return false;
		boolean result = true;
		for (String clientId : clientIdList){
			boolean resultTemp = mqttClient.unSubscribe(clientId,topic);
			if(!resultTemp){
				result = resultTemp;
			}
		}
		return result;
	}

	@Override
	public boolean disconnect(String clientId) {
		return mqttClient.disconnect(clientId);
	}

	@Override
	public boolean addCompatibleCallBack(String callBackKey, CallBackProcessor callBackProcessor, String function) {
		try{
			if(ModuleConstants.RABBITMQ_SELECT.equals(function)){
				boolean useRabbitMQ = this.configInfo.getUseRabbitMQ();
				if(!useRabbitMQ){
					logger.info("rabbitMq switch off addCallBack fail............");
				}else {
					rabbitMqClient.registerCallBack(callBackKey, callBackProcessor, null, new Arguments());
					messageMonitor.addRoutes(callBackKey, rabbitMqClient.getRegisterModelMap().get(callBackKey));
				}
			}else if(ModuleConstants.MQTT_SELECT.equals(function)){
				logger.info("registerCallBack to mqtt,callBackKey->"+callBackKey);
				mqttClient.registerCallBack(callBackKey,callBackProcessor);
			}else {
				logger.info("param error, addCallBack fail............");
			}
		}catch (Exception e){
			logger.error("addCompatibleCallBack error->",e);
		}
		return false;
	}

	/**
	 * 描述：发送消息
	 * @author maochengyuan
	 * @created 2018/11/20 18:24
	 * @param routingKey 路由key
	 * @param message 消息内容
	 * @return void
	 */
	@Override
	public void basicPublish(String routingKey, String message) {
		this.rabbitMqClient.basicPublish(routingKey, message);
	}

	/**
	 * 描述：发送消息
	 * @author maochengyuan
	 * @created 2018/11/20 18:24
	 * @param routingKey 路由key
	 * @param messages 消息内容
	 * @return void
	 */
	@Override
	public void basicPublish(String routingKey, List<String> messages) {
		if(messages == null || messages.isEmpty()){
			return;
		}
		messages.forEach(message ->{
			this.rabbitMqClient.basicPublish(routingKey, message);
		});
	}

}
