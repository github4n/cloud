package com.iot.mqttsdk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：回调接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月17日 19:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月17日 19:34
 */
public interface CallBackProcessor {

   Logger logger = LoggerFactory.getLogger(CallBackProcessor.class);

   /**  
    * 描述：监听消息接口
    * @author 490485964@qq.com  
    * @date 2018/4/20 15:45  
    * @param mqttMsg 消息体
    * @return   
    */ 
   void onMessage(MqttMsg mqttMsg);

   /**
    * 描述：监听连接接口
    * @author 490485964@qq.com
    * @date 2018/4/20 15:45
    * @return
    */
   default void onConnected(){
      logger.info("connect success!");
   }

}
