package com.iot.mqttsdk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：死信回调接口
 * 创建人： maochengyuan
 * 创建时间：2018年11月16日 15:34
 * 修改人： maochengyuan
 * 修改时间：2018年11月16日 15:34
 */
public interface DeadBackProcessor {

   Logger logger = LoggerFactory.getLogger(DeadBackProcessor.class);

   /**  
    * 描述：监听消息接口
    * @author maochengyuan
    * @date 2018年11月16日 15:34
    * @param message 消息
    * @return   
    */ 
   void onMessage(String message);

   /**
    * 描述：监听连接接口
    * @author maochengyuan
    * @date 2018年11月16日 15:34
    * @return
    */
   default void onConnected(){
      logger.info("connect success!");
   }

}
