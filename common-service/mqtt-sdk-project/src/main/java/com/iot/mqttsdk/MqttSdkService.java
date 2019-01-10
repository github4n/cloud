package com.iot.mqttsdk;

import com.iot.mqttsdk.common.Arguments;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.ConfigInfo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.mqtt.ClientCloseListener;

import java.util.List;
/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：sdk服务接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:34
 */
public interface MqttSdkService {
	/**
	 * 描述：初始化服务连接信息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:58
	 * @param configInfo
	 * @return
	 */
	void createConnections(ConfigInfo configInfo);
	void createConnections(ConfigInfo configInfo, ClientCloseListener listener);
	/**
	 * 描述：发布消息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:58
	 * @param clientId
	 * @param msg
	 * @param qos
	 * @return
	 */
	boolean sendMessage(String clientId, MqttMsg msg, int qos);
	/**
	 * 描述：发布消息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:58
	 * @param clientIdList
	 * @param msg
	 * @param qos
	 * @return
	 */
	boolean sendMessage(List<String> clientIdList, MqttMsg msg,int qos);

	/**
	 * 描述：发送消息
	 * @author maochengyuan
	 * @created 2018/11/20 18:24
	 * @param routingKey 路由key
	 * @param message 消息内容
	 * @return void
	 */
	void basicPublish(String routingKey, String message);

	/**
	 * 描述：发送消息
	 * @author maochengyuan
	 * @created 2018/11/20 18:24
	 * @param routingKey 路由key
	 * @param messages 消息内容
	 * @return void
	 */
	void basicPublish(String routingKey, List<String> messages);

	/**
	 * 描述：注册回调实例
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:59
	 * @param  queueName
	 * @param  callBackProcessor
	 * @return
	 */
	boolean addCallBack(String queueName, CallBackProcessor callBackProcessor);

	/**
	 * 描述：注册回调实例
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:59
	 * @param  queueName 队列名称
	 * @param  arguments 其他参数
	 * @return
	 */
	boolean addCallBack(String queueName, Arguments arguments);

	/**
	 * 描述：订阅主题消息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:59
	 * @param clientId
	 * @param topic
	 * @param qos
	 * @return
	 */
	boolean subscribe(String clientId, String topic,int qos);
	/**
	 * 描述：订阅主题消息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 15:59
	 * @param clientIdList
	 * @param topic
	 * @param qos
	 * @return
	 */
	boolean subscribe(List<String> clientIdList, String topic,int qos);
	/**
	 * 描述：取消订阅主题消息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 16:00
	 * @param clientId
	 * @param topic
	 * @return
	 */
	boolean unSubscribe(String clientId, String topic);
	/**
	 * 描述：取消订阅主题消息
	 * @author 490485964@qq.com
	 * @date 2018/4/20 16:00
	 * @param clientIdList
	 * @param topic
	 * @return
	 */
	boolean unSubscribe(List<String> clientIdList, String topic);
	/**
	 * 描述：断开mqtt连接
	 * @author 490485964@qq.com
	 * @date 2018/4/25 9:28
	 * @param
	 * @return
	 */
	public boolean disconnect(String clientId);

	/**
	 * 描述： 注册mq与mqtt 回调
	 * @author 490485964@qq.com
	 * @date 2018/6/20 17:06
	 * @param callBackKey mq队列名或mqtt服务名
	 * @param callBackProcessor 回调实例
	 * @param function B：注册mqtt回调 C：rabbitmq回调
	 * @return
	 */
	boolean addCompatibleCallBack(String callBackKey, CallBackProcessor callBackProcessor,String function);

}
