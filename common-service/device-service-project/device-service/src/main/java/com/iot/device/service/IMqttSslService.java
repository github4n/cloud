package com.iot.device.service;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.iot.common.exception.BusinessException;

public interface IMqttSslService {
	
    /**
     * 
     * 描述：证书校验
     * @author 李帅
     * @created 2018年5月31日 下午3:58:33
     * @since 
     * @param devUUID
     * @param password
     * @param tempFileName
     * @return
     * @throws BusinessException
     */
    public MqttClient createConnect(String devUUID, String password, String tempFileName) throws Exception;
}
