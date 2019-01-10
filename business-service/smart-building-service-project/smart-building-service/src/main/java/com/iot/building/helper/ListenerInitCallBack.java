package com.iot.building.helper;

import com.iot.building.callback.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iot.building.listener.SystembufferInit;


/**
 * 设备状态上报监听
 * @author fenglijian
 *
 */
//@Component
public class ListenerInitCallBack{
	
	private static final Logger logger = LoggerFactory.getLogger(ListenerInitCallBack.class);

	public static void intitCallBack() {
		logger.info("Listener.contextInitialized");
//		SystembufferInit.addListerner(Constants.DEVICE_REPORT_CALLBACK_KEY_PREFIX, new DeviceReportCallback());
		SystembufferInit.addListerner(Constants.DEVICE_LISTENER_KEY, new DeviceCallback());
		//SystembufferInit.addListerner(Constants.CALLBACK_KEY_PREFIX, new IftttCallback());重构前
		//重构后
		SystembufferInit.addListerner(Constants.CALLBACK_KEY_PREFIX, new IftttCallbackTob());
		SystembufferInit.addListerner(Constants.SPACE_LISTENER_KEY, new SpaceCallback());
		SystembufferInit.addListerner(Constants.SCENE_LISTENER_KEY, new WarningCallback());
//		SystembufferInit.addListerner(Constants.REMOTE_CONTROL_LISTENER_KEY, new NewDeviceRemoteControlCallBack());
		//旧的遥控器配置 暂时不需要
		//SystembufferInit.addListerner(Constants.REMOTE_CONTROL_LISTENER_KEY, new DeviceRemoteControlCallBack());
		logger.info("初始化完成,size:"+SystembufferInit.getListenerMap().size());
	}

}
