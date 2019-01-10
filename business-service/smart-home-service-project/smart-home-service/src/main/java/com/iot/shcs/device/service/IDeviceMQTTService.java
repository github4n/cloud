package com.iot.shcs.device.service;

import com.iot.mqttsdk.common.MqttMsg;

import java.util.Map;

public interface IDeviceMQTTService {
    /**
     * 描述：绑定用户和设备请求 设备发消息给云，云端业务处理后响应给设备，并且通知给app
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void devBindReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 解绑 流程 app--->发给云端 云端直接做删除等动作 并通知app 和 直连设备
     *
     * @param reqMqttMsg
     * @param reqTopic
     * @return
     * @author lucky
     * @date 2018/5/14 15:17
     */
    void devUnbindReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 解绑确认=====>>>>目前改成云端直接删除确认 并通知app 和直连设备(如果是IPC类型的还需要解绑计划)
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void devUnbindConfirm(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 描述：获取家庭设备列表响应
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void getDevListReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 上报设备基本信息 设备发消息给云，云端记录，然后同步到设备之上
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void updateDevBaseInfo(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设备状态通知(上报 子设备上线、离线 的消息)
     * <p>
     * <p>iot/v1/cb/[devId]/device/devStatusNotif
     *
     * @param reqMqttMsg
     * @param reqTopic
     */
    void devStatusNotif(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 上报设备详细信息 设备发送给云，云存储起来
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void updateDevDetails(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设置设备属性（单控）
     *
     * @param deviceId
     * @param attr
     */
    void setDevAttr(String deviceId, Map<String, Object> attr);

    /**
     * 设置设备属性通知 设备发出，云端接收到后进行数据存储处理
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void setDevAttrNotif(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 4.46	设备事件通知 设备发出，云端接收到后进行数据存储处理
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void devEventNotif(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 获取设备属性 app发消息给云，云转发给设备
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void getDevAttrReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 获取设备属性信息响应 设备响应给云，云再转发到app
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    MqttMsg getDevAttrResp(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 获取用户房间设备列表 直接从云端获取
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void getRoomDevListReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 添加子设备通知 设备广播，云端接收到
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void addDevNotif(MqttMsg reqMqttMsg, String reqTopic);

    void delDevReq(MqttMsg reqMqttMsg, String reqTopic);

    void delDevResp(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 删除子设备通知 设备广播，云端接收到
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void delDevNotif(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 获取设备信息
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void getDevInfoReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设置设备信息响应 设备响应给云，云处理完业务再响应给app
     *
     * @param reqMqttMsg
     * @param reqTopic
     */
    void setDevInfoNotif(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 4.10子设备上下线状态或名称改变上报
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void statusNotify(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设备上线
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void connect(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设备下线
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void disconnect(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 修改家庭设备排序
     *
     * @param reqMqttMsg
     * @param reqTopic
     */
    void updateDevOrder(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设置倒计时信息请求 app发消息给云，云转发给设备
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void setCountDownReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设置倒计时信息响应 设备响应给云，云再转发到app
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    MqttMsg setCountDownResp(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 4.56 设置倒计时使能请求 app发消息给云，云转发给设备
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void setCountDownEnableReq(
            MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 设置倒计时使能响应 设备响应给云，云再转发到app
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    MqttMsg setCountDownEnableResp(
            MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 上报电量信息请求 设备发给云，云保存数据，回响应
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void updateEnergyReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 上报运行时间请求 设备发给云，云保存数据，回响应
     *
     * @param reqMqttMsg 消息
     * @param reqTopic   主题
     */
    void updateRuntimeReq(MqttMsg reqMqttMsg, String reqTopic);

    /**
     * 4.46设备状态通知
     *
     * @param reqMqttMsg
     * @param reqTopic
     * @return
     * @author lucky
     * @date 2018/5/25 8:32
     */
    void devStautsNotif(MqttMsg reqMqttMsg, String reqTopic);

    void setDevAttrResp(MqttMsg reqMqttMsg, String reqTopic);


    /**
     * 设备方法调用请求
     *
     * @param reqMqttMsg
     * @param reqTopic
     * @return
     * @author lucky
     * @date 2018/10/23 11:43
     */
    void devActionReq(MqttMsg reqMqttMsg, String reqTopic);


    /**
     * 4.48设备方法调用响应
     *
     * @param reqMqttMsg
     * @param reqTopic
     * @return
     * @author lucky
     * @date 2018/10/23 11:50
     */
    void devActionResp(MqttMsg reqMqttMsg, String reqTopic);
}
