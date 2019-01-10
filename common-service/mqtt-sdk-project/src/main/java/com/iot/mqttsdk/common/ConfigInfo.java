package com.iot.mqttsdk.common;

import com.iot.mqttsdk.mqtt.MqttProperty;
import com.iot.mqttsdk.rabbitmq.model.RabbitmqProperty;

import java.util.List;
/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：配置信息
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:34
 */
public class ConfigInfo {

	/**rabbitmq配置**/
	private RabbitmqProperty rabbitmqProperty;

	/**mqtt配置**/
	private List<MqttProperty> mqttPropertyList;

	/**是否使用rabbitmq,true:使用**/
	private boolean useRabbitMQ;

	public ConfigInfo() {
	}

	public ConfigInfo(boolean useRabbitMQ) {
		this.useRabbitMQ = useRabbitMQ;
	}

	public ConfigInfo(List<MqttProperty> mqttPropertyList, boolean useRabbitMQ) {
		this.mqttPropertyList = mqttPropertyList;
		this.useRabbitMQ = useRabbitMQ;
	}

	public ConfigInfo(RabbitmqProperty rabbitmqProperty, List<MqttProperty> mqttPropertyList, boolean useRabbitMQ) {
		this.rabbitmqProperty = rabbitmqProperty;
		this.mqttPropertyList = mqttPropertyList;
		this.useRabbitMQ = useRabbitMQ;
	}

	public RabbitmqProperty getRabbitmqProperty() {
		return rabbitmqProperty;
	}

	public void setRabbitmqProperty(RabbitmqProperty rabbitmqProperty) {
		this.rabbitmqProperty = rabbitmqProperty;
	}

	public boolean getUseRabbitMQ() {
		return useRabbitMQ;
	}

	public void setUseRabbitMQ(boolean useRabbitMQ) {
		this.useRabbitMQ = useRabbitMQ;
	}

	public List<MqttProperty> getMqttPropertyList() {
		return mqttPropertyList;
	}

	public void setMqttPropertyList(List<MqttProperty> mqttPropertyList) {
		this.mqttPropertyList = mqttPropertyList;
	}
}
