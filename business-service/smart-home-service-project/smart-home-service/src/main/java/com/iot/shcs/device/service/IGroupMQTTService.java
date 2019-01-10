package com.iot.shcs.device.service;

import com.iot.mqttsdk.common.MqttMsg;

public interface IGroupMQTTService {
   /**
   * 方法描述 删除组请求
   */
    public void delGroupReq(MqttMsg reqMqttMsg, String reqTopic);
    /**
    * 方法描述 删除组响应
    */
    public void delGroupResp(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 方法描述 添加组成员请求
     */
    public void delGroupMemberReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
    * 方法描述 添加组成员响应
    */
    public void addGroupMemberResp(MqttMsg reqMqttMsg, String reqTopic);

    /**
    * 方法描述 获得组成员请求
    */
    public void getGroupMemberReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 方法描述 获得组成员响应
     */

    public void getGroupMemberResp(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 方法描述 删除组成员请求
     */
    public void addGroupMemberReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
    * 方法描述 删除组成员响应
    */
    public void delGroupMemberResp(MqttMsg reqMqttMsg, String reqTopic);


    /**
     * 方法描述 执行组请求
     */
    public void excGroupReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
    * 方法描述 执行组响应
    */
    public void excGroupResp(MqttMsg reqMqttMsg, String reqTopic);

}
