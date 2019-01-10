package com.iot.building.helper;

import java.util.Map;

import com.iot.building.callback.ListenerCallback;
import com.iot.building.callback.impl.IftttCallbackTob;
import com.iot.building.listener.SystembufferInit;
import com.iot.common.enums.APIType;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapCallBack {
	private static final Logger LOGGER = LoggerFactory.getLogger(MapCallBack.class);
	public static void mapCallBack(GetDeviceInfoRespVo device,Map<String, Object>map,APIType apiType){
		LOGGER.info("=============MapCallBack=========================");
		Map<String, ListenerCallback>listenerMap=SystembufferInit.getListenerMap();
		LOGGER.info("=============MapCallBack=========listenerMap================"+listenerMap);
		try{
			for (String key : listenerMap.keySet()) {
				ThreadPoolUtil.instance().execute(new Runnable() {
					@Override
					public void run() {
						LOGGER.info("=============MapCallBack=========================start");
						ListenerCallback listenerCallback= listenerMap.get(key);
						listenerCallback.callback(device, map,apiType);
					}
				});
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
