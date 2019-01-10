package com.iot.mqttsdk.common;

import java.util.Arrays;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：常量
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:34
 */
public class ModuleConstants {


	/** 错误的用户名或密码 */
	public static final String MQTT_CONNECT_PASS_ERROR = "错误的用户名或密码";

	/** 选择MQTT */
	public static final String MQTT_SELECT = "B";

	/** 选择RABBITMQ */
	public static final String RABBITMQ_SELECT = "C";

	public static final int BLOCKING_QUEUE_SIZE = 1;

	public static final int THREAD_NUM_MIN = 3;

	public static final int THREAD_NUM_LARGE = 30;

	/**
	 *  交换机
	 */
	public static final String DEFAULT_EXCHANGE = "moqutteExchange";

	/**
	 * 需要特殊处理的队列
	 */
	public static final List<String> specialQueueArray = Arrays.asList("device","IPC");
}
