package com.iot.shcs.ifttt.service;

import com.iot.mqttsdk.common.MqttMsg;
import com.iot.shcs.ifttt.entity.Automation;
import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import com.iot.shcs.ifttt.vo.req.AutoListReq;
import com.iot.shcs.ifttt.vo.req.SaveAutoReq;
import com.iot.shcs.ifttt.vo.res.AutoListResp;

import java.util.List;
import java.util.Map;

/**
 * 描述：联动服务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 14:16
 */
public interface IAutoService {

    /**
     * 获取联动列表
     *
     * @param req
     * @return
     */
    List<AutoListResp> list(AutoListReq req);

    /**
     * 保存联动记录
     *
     * @param req
     * @return
     */
    Long saveAuto(SaveAutoReq req);

    /**
     * 保存联动规则
     *
     * @param message
     */
    void saveAutoRule(MqttMsg message, Boolean enable);


    /**
     * 设置状态
     *
     * @param autoId
     * @param status
     */
    void setEnable(Long autoId, Integer status);


    /**
     * 删除联动
     *
     * @param autoId
     */
    void delete(Long autoId);

    /**
     * 获取缓存
     *
     * @param autoId
     * @return
     */
    Automation getCache(Long autoId);


    /**
     * 获取payload
     *
     * @param autoId
     * @return
     */
    Map<String, Object> getPayloadById(Long autoId);


    /**
     * 设备上报判断触发
     *
     * @param deviceId
     * @param attrMap
     */
    void checkByDevice(String deviceId, Map<String, Object> attrMap);

    /**
     * 删除设备 关联操作
     *
     * @param devId
     */
    void delByDeviceId(String devId, Long tenantId);

    void delByBatchDeviceIds(Long tenantId, List<String> devIds);

    /**
     * 删除scene 关联操作
     *
     * @param sceneId
     */
    void delBySceneId(Long sceneId, Long tenantId);

    /**
     * 根据直连设备删除联动
     *
     * @param devId
     */
    void delByDirectDeviceId(String devId, Long tenantId);

    void delByBatchDirectDeviceIds(Long tenantId,List<String> devIds);

    /**
     * 根据appletId获取AutoId
     *
     * @param appletId
     */
    Long getAutoIdByAppletId(Long appletId);

    ////////////////////////////////////////////////////////////////////////////////////

    void delBleAuto(SaveAutoReq req);

    Map<String, Object> getAutoDetail(SaveAutoReq saveAutoReq);

    void addAutoRule(AddAutoRuleReq req);

    void editAutoRule(AddAutoRuleReq req);

    void setAutoEnable(AddAutoRuleReq req);
}
