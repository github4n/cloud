package com.iot.mqttsdk.common;

/**
 * 项目名称：IOT云平台
 * 模块名称：mqsdk
 * 功能描述：死信回调接口
 * 创建人： maochengyuan
 * 创建时间：2018年11月16日 15:34
 * 修改人： maochengyuan
 * 修改时间：2018年11月16日 15:34
 */
public class DefaultDeadBackProcessor implements DeadBackProcessor{

   @Override
   public void onMessage(String message) {
      logger.info("DefaultDeadBackProcessor processor :"+message);
   }

}
