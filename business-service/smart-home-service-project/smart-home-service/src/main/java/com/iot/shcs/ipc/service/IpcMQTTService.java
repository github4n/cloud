package com.iot.shcs.ipc.service;

import com.iot.mqttsdk.common.MqttMsg;

import java.util.Map;

/**
 * 项目名称：IOT云平台
 * 模块名称：IPC操作接口
 * 功能描述：IPC操作接口
 * 创建人： 490485964@qq.com
 * 创建时间：2018年04月24日 11:34
 * 修改人： 490485964@qq.com
 * 修改时间：2018年04月24日 11:34
 */
public interface IpcMQTTService {
	
    /**
     * 描述：获取文件服务put预签名url
     * @author 490485964@qq.com
     * @date 2018/4/8 9:03
     * @param
     * @return
     */
    void getfsurlReq(MqttMsg mqttMsgParam, String topicParam);
    
    /**
     * 
     * 描述：获取文件服务put预签名url
     * @author 李帅
     * @created 2018年7月16日 下午8:06:40
     * @since 
     * @param deviceId
     * @param planId
     * @param fileType
     * @return
     */
    Map<String, Object> getFilePreSignUrls(String deviceId, String planId, String fileType);
    
    /**
     * 描述：文件信息上报
     * @author 490485964@qq.com
     * @date 2018/4/9 11:47
     * @param
     * @return
     */
    void uploadFileInfo(MqttMsg mqttMsgParam, String topicParam);

    /**
     * @despriction：上报事件
     * @author  yeshiyuan
     * @created 2018/4/9 15:26
     * @param mqttMsgParam
     * @param topicParam
     * @return
     */
    void uploadEvent(MqttMsg mqttMsgParam, String topicParam);
    
    /**
     * 描述：通知设备录影配置
     * @author 490485964@qq.com
     * @date 2018/4/25 9:53
     * @param
     * @return
     */
    void notifyDeviceRecordConfig(String planId, String deviceId);


    void getEventNotifReq(MqttMsg mqttMsgParam, String topicParam);

    void setEventNotifReq(MqttMsg mqttMsgParam, String topicParam);
}
