package com.iot.shcs.ipc.callback;

import com.alibaba.fastjson.JSON;
import com.iot.common.util.JsonUtil;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.ipc.service.IpcMQTTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：回调
 * 功能描述：回调IPC接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月25日 9:59
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月25日 9:59
 */
@Service("ipcManage")
public class IpcCallBackProcessor implements CallBackProcessor {
    /**日志*/
    private static final Logger logger = LoggerFactory.getLogger(IpcCallBackProcessor.class);

    @Autowired
    private IpcMQTTService ipcMQTTService;
    @Override
    public void onMessage(MqttMsg mqttMsg) {
        try {
            String jsonMessage = JSON.toJSONString(mqttMsg);
            Map<String,Object> bodyMap = JsonUtil.fromJson(jsonMessage, Map.class);
            String methodName=mqttMsg.getMethod();
            logger.info("methodName->"+methodName);
            Method method = this.ipcMQTTService.getClass().getMethod(methodName, Map.class);
            method.invoke(this.ipcMQTTService, bodyMap);
        } catch (Exception e) {
            logger.error("error->",e);
        }
    }
}
