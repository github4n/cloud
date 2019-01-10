package com.iot.building.scene.service.impl;

import com.iot.mqttsdk.common.MqttMsg;

public interface CommonSceneMQTTInterface {

	/**
     * 描述：添加情景规则请求
     *
     * @param msg
     * @param topic
     * @return
     * @author wanglei
     * @created 2018年3月1日 上午10:26:46
     */
    public void addSceneRuleReq(MqttMsg msg, String topic);
    
    /**
     * 描述：编辑情景规则请求
     *
     * @param msg
     * @param topic
     * @return
     * @author wanglei
     */
    public void editSceneRuleReq(MqttMsg msg, String topic);
    
    /**
     * 描述：添加情景规则响应
     *
     * @return
     * @author wanglei
     */
    public void addSceneRuleResp(MqttMsg msg, String topic);
    
    /**
     * 描述：编辑情景规则响应
     *
     * @param msg
     * @param topic
     * @return
     * @author wanglei
     */
    public void editSceneRuleResp(MqttMsg msg, String topic);
    
    /**
     * 描述：删除情景请求
     *
     *  app --> cloud
     *  cloud --> device
     *
     * @param topic
     * @return
     * @author wanglei
     * @created 2017年12月12日 下午4:22:3
     */
    public void delSceneReq(MqttMsg message, String topic);
    
    /**
     * 描述：获取情景规则请求
     *
     * @param topic
     * @return
     * @author wanglei
     * @created 2017年12月12日 下午4:22:33
     */
    public void getSceneRuleReq(MqttMsg message, String topic);
    
    /**
     * 描述：删除情景规则请求
     *
     * @param message
     * @param topic
     * @return
     * @author wanglei
     */
    public void delSceneRuleReq(MqttMsg message, String topic);
    
    /**
     * 描述：删除情景规则响应
     *
     * @param message
     * @param topic
     * @return
     * @author wanglei
     */
    public void delSceneRuleResp(MqttMsg message, String topic);
    
    /**
     * 描述：删除情景响应
     *
     *  device --> cloud
     *  cloud --> app
     *
     * @param topic
     * @return
     * @author wanglei
     * @created 2017年12月12日 下午4:22:33
     */
    public void delSceneResp(MqttMsg message, String topic);
    
    /**
     * 描述：执行情景请求
     *
     * @param message
     * @param topic
     * @return
     * @author wanglei
     */
    public void excSceneReq(MqttMsg message, String topic);
    
    /**
     * 描述：执行情景响应
     *
     * @param message
     * @param topic
     * @return
     * @author wanglei
     */
    public void excSceneResp(MqttMsg message, String topic);
    
}
