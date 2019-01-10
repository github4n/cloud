package com.iot.callback;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;

@Service("user")
public class UserCallBackService implements CallBackProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserCallBackService.class);

    public static final int QOS = 1;
    
    private final static String DISCONNECT="disconnect";


    @Override
    public void onMessage(MqttMsg mqttMsg) {
    	String clientId=mqttMsg.getClientId();
    	String uuid=clientId.split("-")[1];
    	String mapKey=clientId.split("-")[0];
    	String key=uuid+":pwd";
    	try {
           if(mqttMsg.getMethod().equals(DISCONNECT)){
        	   Map<String,String> map=RedisCacheUtil.hashGetAll(key, String.class, false);
        	   if(map.containsKey(mapKey)) {
        		   RedisCacheUtil.hashRemove(key, mapKey);
        		   LOGGER.info(" ******************** remove key :"+mapKey+"success ********************");
        		   return;
        	   }
        	   LOGGER.info(" xxxxxxxxxxxx not contains key :"+mapKey+"remove fail xxxxxxxxx");
           }
    	}catch (Exception e) {
			e.printStackTrace();
		}

    }
}