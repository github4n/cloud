package com.iot.shcs.ifttt.service.impl;

import com.iot.common.util.StringUtil;
import com.iot.device.vo.rsp.device.ListDeviceInfoRespVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.ifttt.constant.AutoConstants;
import com.iot.shcs.ifttt.entity.Automation;
import com.iot.shcs.ifttt.service.IAutoMQTTService;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.ifttt.util.MqttKeyUtil;
import com.iot.shcs.ifttt.util.RedisKeyUtil;
import com.iot.shcs.ifttt.vo.req.SaveAutoReq;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.user.api.UserApi;
import com.iot.util.ToolUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 描述：联动MQTT处理服务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 15:14
 */
@Service("automation")
public class AutoMQTTServiceImpl implements IAutoMQTTService, CallBackProcessor {

    private final static Logger logger = LoggerFactory.getLogger(AutoMQTTServiceImpl.class);


    @Autowired
    private DeviceCoreService deviceCoreService;


    @Autowired
    private MqttSdkService mqttSdkService;

    @Autowired
    private UserApi userApi;

    @Autowired
    private IAutoService autoService;

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }

    /**
     * 7.4 删除Automation请求
     *
     * @param message
     * @param topic
     */
    public void delAutoReq(MqttMsg message, String topic) {
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            //取出 rule
            Long autoId = getAutoId(message);
            Automation auto = autoService.getCache(autoId);
            String directId = auto.getDirectId();
            //判断是否跨直连
            Boolean isMulti = checkMulti(auto.getIsMulti(), directId);
            if (isMulti) {
                //跨直连设备，云端删除，直接响应APP
                String uuid = userApi.getUuid(auto.getUserId());
                res = MqttKeyUtil.getDelAutoRespMsg(message, uuid);
                //payload
                autoService.delete(autoId);
                Map<String, Object> respPayload = new HashMap<>();
                respPayload.put("autoId", String.valueOf(autoId));
                res.setPayload(respPayload);
            } else if (auto.getIsMulti() == AutoConstants.IFTTT_MULTI_FALSE.intValue()) {//单直连
                //直连设备,转发给网关
                res = MqttKeyUtil.getDelAutoReqMsg(message, directId);
            }
            res.setAck(MqttMsgAck.successAck());
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("del auto req error.", e);
            //错误，直接应答给app
            String uuid = ToolUtils.getClientId(topic);
            res = MqttKeyUtil.getDelAutoRespMsg(res, uuid);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
        } finally {
            //应答
            sendMqttMsg(res);
        }
    }

    /**
     * 7.5 删除Automation应答
     *
     * @param message
     * @param topic
     */
    public void delAutoResp(MqttMsg message, String topic) {
        MqttMsg res = message;
        Long autoId = getAutoId(message);
        String uuid = null;
        try {
            /********* 业务逻辑 start *********/
            int code = message.getAck().getCode();
            uuid = getUuid(message);
            if (code == MqttMsgAck.SUCCESS || code == AutoConstants.NOT_EXIST || code == AutoConstants.INVALID_IFTTT_ID) {
                String key = RedisKeyUtil.getIftttDelKey(autoId);
                String value = RedisCacheUtil.valueGet(key);
                if (!StringUtils.isEmpty(value)) {
                    logger.debug("直连转跨直连,删除");
                    //空删除
                    RedisCacheUtil.delete(key);
                    //应答给app 编辑应答
                    res = MqttKeyUtil.getEditAutoRuleRespMsg(res, uuid);
                    res.setAck(MqttMsgAck.successAck());
                    sendMqttMsg(res);
                    return;
                }

                //网关删除成功，云端也删除
                autoService.delete(autoId);
                //payload
                Map<String, Object> respPayload = new HashMap<>();
                respPayload.put("autoId", String.valueOf(autoId));
                //respPayload.put("idx", idxList);
                res.setPayload(respPayload);
                res.setAck(MqttMsgAck.successAck());
            }
            // 封装应答给app
            res = MqttKeyUtil.getDelAutoRespMsg(res, uuid);
            //发送
            sendMqttMsg(res);
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("del auto resp error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
            // 封装应答给app
            res = MqttKeyUtil.getDelAutoRespMsg(res, uuid);
            //发送
            sendMqttMsg(res);
        }
    }

    /**
     * 7.6 获取Automation规则请求
     *
     * @param message
     * @param topic
     */
    public void getAutoRuleReq(MqttMsg message, String topic) {
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            Long autoId = getAutoId(message);
            //从数据库中取规则数据
            res.setPayload(autoService.getPayloadById(autoId));
            res.setAck(MqttMsgAck.successAck());
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("get auto rule req error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
        } finally {
            //直接应该给app
            String uuid = ToolUtils.getClientId(topic);
            res = MqttKeyUtil.getGetAutoRuleRespMsg(res, uuid);
            //应答
            sendMqttMsg(res);
        }
    }

    /**
     * 7.8 添加Automation规则请求
     *
     * @param message
     * @param topic
     */
    public void addAutoRuleReq(MqttMsg message, String topic) {
        logger.info("addAutoRuleReq(), message={}, topic={}", message, topic);

        MqttMsg res = null;
        try {
            message = sortMsg(message);
            res = message;

            /********* 业务逻辑 start *********/
            //判断是否跨直连
            Map<String, Object> flagMap = checkMulti(message);
            Boolean flag = (Boolean) flagMap.get("flag");
            String deviceId = (String) flagMap.get("directId");

            //TODO 取出visiable字段
            Map<String, Object> payload = (Map<String, Object>) message.getPayload();
            if (payload.get("visiable") != null) {
                payload.remove("visiable");
                res.setPayload(payload);
            }

            //更新rule直连关系信息
            Long autoId = getAutoId(message);
            updateRule(flag, autoId, deviceId);

            if (flag) {
                //若跨直连，则云端保存，直接返回app
                autoService.saveAutoRule(message, flag);
                String uuid = ToolUtils.getClientId(topic);
                res = MqttKeyUtil.getAddAutoRuleRespMsg(res, uuid);
            } else {
                //不跨直连，则转发给网关
                res = MqttKeyUtil.getAddAutoRuleReqMsg(res, deviceId);
            }
            res.setAck(MqttMsgAck.successAck());
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("add auto rule req error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
            String uuid = ToolUtils.getClientId(topic);
            res = MqttKeyUtil.getAddAutoRuleRespMsg(res, uuid);
        } finally {
            //应答
            sendMqttMsg(res);
        }
    }

    /**
     * 7.9 添加Automation规则响应
     *
     * @param message
     * @param topic
     */
    public void addAutoRuleResp(MqttMsg message, String topic) {
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            MqttMsgAck ack = message.getAck();
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //网关保存成功，则云端保存
                autoService.saveAutoRule(message, false);
            }

            //新增转编辑判断
            Long autoId = getAutoId(message);
            String key = RedisKeyUtil.getIftttEditKey(autoId);
            String value = RedisCacheUtil.valueGet(key);
            String uuid = getUuid(message);
            if (!StringUtils.isEmpty(value)) {
                //转编辑
                logger.debug("跨直连转直连，应答edit");
                res = MqttKeyUtil.getEditAutoRuleRespMsg(res, uuid);
                RedisCacheUtil.delete(key);
            } else {
                res = MqttKeyUtil.getAddAutoRuleRespMsg(res, uuid);
            }

            res.setAck(ack);
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("add auto rule resp error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
        } finally {
            //应答给app
            sendMqttMsg(res);
        }
    }

    /**
     * 7.10 编辑Automation规则请求
     *
     * @param message
     * @param topic
     */
    public void editAutoRuleReq(MqttMsg message, String topic) {
        message = sortMsg(message);
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            Map<String, Object> flagMap = checkMulti(message);
            Boolean flag = (Boolean) flagMap.get("flag");
            String directId = (String) flagMap.get("directId");

            //TODO 取出visiable字段
            Map<String, Object> payload = (Map<String, Object>) message.getPayload();
            if (payload.get("visiable") != null) {
                payload.remove("visiable");
                res.setPayload(payload);
            }

            //更新rule直连关系信息
            Long autoId = getAutoId(message);

            // --------------------- 跨直连变化逻辑 -------------------------
            //编辑转新增标识
            Boolean addFlag = false;
            //旧rule数据
            Automation auto = autoService.getCache(autoId);
            Byte isMulti = auto.getIsMulti().byteValue();
            logger.info("rule:" + autoId + ",跨直连判断：旧" + isMulti + ",新：" + flag);

            //直连->跨直连 删除网关ifttt规则
            if (isMulti == AutoConstants.IFTTT_MULTI_FALSE && flag) {
                MqttMsg delMsg = MqttKeyUtil.getDelAutoReqMsg(null, auto.getDirectId());
                //payload
                Map<String, Object> respPayload = new HashMap<>();
                respPayload.put("autoId", String.valueOf(autoId));
                delMsg.setPayload(respPayload);
                delMsg.setSeq(res.getSeq());
                delMsg.setSrcAddr(res.getSrcAddr());
                sendMqttMsg(delMsg);
                //添加缓存
                String key = RedisKeyUtil.getIftttDelKey(autoId);
                RedisCacheUtil.valueSet(key, "del", RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_1);
                logger.debug("直连->跨直连");

                //更新规则
                autoService.saveAutoRule(message, true);
                updateRule(flag, autoId, directId);
                return;
            }

            //跨直连->直连 给网关下发新增ifttt请求
            if (isMulti == AutoConstants.IFTTT_MULTI_TRUE && !flag) {
                logger.debug("跨直连->直连");
                addFlag = true;
                //添加缓存
                String key = RedisKeyUtil.getIftttEditKey(autoId);
                RedisCacheUtil.valueSet(key, "edit", RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_1);
            }
            // ----------------------- 跨直连变化逻辑 ----------------------

            updateRule(flag, autoId, directId);

            if (flag) {
                //云端保存
                autoService.saveAutoRule(message, flag);
                //应答给app
                String uuid = ToolUtils.getClientId(topic);
                res = MqttKeyUtil.getEditAutoRuleRespMsg(res, uuid);
            } else {
                //TODO(laiguiming) 去除enable
                payload.remove("enable");
                res.setPayload(payload);

                //转发网关
                if (addFlag) {
                    res = MqttKeyUtil.getAddAutoRuleReqMsg(res, directId);
                } else {
                    res = MqttKeyUtil.getEditAutoRuleReqMsg(res, directId);
                }
            }
            res.setAck(MqttMsgAck.successAck());
            sendMqttMsg(res);
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("edit auto rule req.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
            //应答给app
            String uuid = ToolUtils.getClientId(topic);
            res = MqttKeyUtil.getEditAutoRuleRespMsg(res, uuid);
            sendMqttMsg(res);
        }
    }

    /**
     * 7.11 编辑Automation规则响应
     *
     * @param message
     * @param topic
     */
    public void editAutoRuleResp(MqttMsg message, String topic) {
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            MqttMsgAck ack = message.getAck();
            //网关保存成功，则云端保存
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                autoService.saveAutoRule(message, false);
                ack = MqttMsgAck.successAck();
            }
            res.setAck(ack);
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("edit auto rule resp error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
        } finally {
            //应答给app
            String uuid = getUuid(message);
            res = MqttKeyUtil.getEditAutoRuleRespMsg(res, uuid);
            sendMqttMsg(res);
        }
    }

    /**
     * 7.12 设置Automation使能（开关）请求
     *
     * @param message
     * @param topic
     */
    public void setAutoEnableReq(MqttMsg message, String topic) {
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            //取出rule
            Long autoId = getAutoId(message);
            Automation auto = autoService.getCache(autoId);

            //直接应答app
            if (auto == null) {
                logger.error("【setAutoEnableReq】 rule is not exist!");
                String uuid = ToolUtils.getClientId(topic);
                res = MqttKeyUtil.getSetAutoEnableRespMsg(res, uuid);
                res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. rule is null!"));
                return;
            }
            //判断是否跨直连
            int isMulti = auto.getIsMulti();
            String directId = auto.getDirectId();
            if (isMulti == AutoConstants.IFTTT_MULTI_TRUE || StringUtils.isEmpty(directId)) {
                //跨直连，云端更新状态
                updateEnable(message, autoId);
                MqttMsgAck ack = MqttMsgAck.successAck();
                res.setAck(ack);
                String uuid = userApi.getUuid(auto.getUserId());
                res = MqttKeyUtil.getSetAutoEnableRespMsg(res, uuid);
            } else if (isMulti == AutoConstants.IFTTT_MULTI_FALSE) {
                //单直连且直连设备ID不为空，转发网关
                res = MqttKeyUtil.getSetAutoEnableReqMsg(res, directId);
            }
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("set auto enable req error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
            String uuid = ToolUtils.getClientId(topic);
            res = MqttKeyUtil.getSetAutoEnableRespMsg(res, uuid);
        } finally {
            //应答
            sendMqttMsg(res);
        }
    }

    /**
     * 7.13 设置Automation使能（开关）响应
     *
     * @param message
     * @param topic
     */
    public void setAutoEnableResp(MqttMsg message, String topic) {
        MqttMsg res = message;
        try {
            /********* 业务逻辑 start *********/
            if (message.getAck().getCode() == MqttMsgAck.SUCCESS) {
                updateEnable(message, getAutoId(message));
                res.setAck(MqttMsgAck.successAck());
            }
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("set auto enable resp error.", e);
            res.setAck(MqttMsgAck.failureAck(MqttMsgAck.BUSINESS_ERROR, "Fail. " + e.getMessage()));
        } finally {
            String uuid = getUuid(message);
            res = MqttKeyUtil.getSetAutoEnableRespMsg(res, uuid);
            //应答
            sendMqttMsg(res);
        }
    }

    /**
     * 7.16 执行Automation通知
     *
     * @param message
     * @param topic
     */
    public void excAutoNotif(MqttMsg message, String topic) {
        try {
            /********* 业务逻辑 start *********/
            Long autoId = getAutoId(message);
            //保存执行记录
            saveActivityRecord(autoId, message.getAck().getCode());
            /*********** 业务逻辑 end ***********/
        } catch (Exception e) {
            logger.error("exe auto notify error.", e);
        }
    }

    ////////////////////////////抽取方法////////////////////////////////////////////////////

    /**
     * 保存执行记录
     *
     * @param autoId
     * @param code
     */
    public void saveActivityRecord(Long autoId, int code) {
        Automation auto = autoService.getCache(autoId);
        if (auto == null) {
            logger.error("***** failed to save record, rule id({}) is null ", autoId);
            return;
        }
        //保存日志
        /*ActivityRecordReq activityRecordReq = new ActivityRecordReq();
        activityRecordReq.setForeignId(auto.getId().toString());
        activityRecordReq.setType(Constants.ACTIVITY_RECORD_AUTO);
        activityRecordReq.setIcon(auto.getIcon());
        activityRecordReq.setCreateBy(auto.getUserId());
        //TODO(laiguiming) 取执行的设备名称，暂不处理，只有中控需要

        String value;
        if (code == 200) {
            activityRecordReq.setResult(0);
            value = DeviceUtils.changeValue(auto.getName(), "auto", "is enable");
        } else {
            activityRecordReq.setResult(1);
            value = DeviceUtils.changeValue(auto.getName(), "auto", "is disabled");
        }
        activityRecordReq.setActivity(value);
        List<ActivityRecordReq> list = new ArrayList<>();
        list.add(activityRecordReq);
        activityRecordService.saveActivityRecord(list);*/
    }

    /**
     * 判断是否跨直连
     *
     * @param isMulti
     * @param derictId
     * @return
     */
    private boolean checkMulti(int isMulti, String derictId) {
        if (isMulti == AutoConstants.IFTTT_MULTI_TRUE ||
                (isMulti == AutoConstants.IFTTT_MULTI_FALSE && derictId == null) ||
                (isMulti == AutoConstants.IFTTT_MULTI_FALSE && "".equals(derictId))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取用户uuid
     *
     * @param message
     * @return
     */
    private String getUuid(MqttMsg message) {
        Long autoId = getAutoId(message);
        //先从缓存中取
        String key = RedisKeyUtil.getIftttUserKey(autoId);
        String uuid = RedisCacheUtil.valueGet(key);
        if (uuid == null) {
            Automation auto = autoService.getCache(autoId);
            uuid = userApi.getUuid(auto.getUserId());
            RedisCacheUtil.valueSet(key, uuid);
        }
        return uuid;
    }

    /**
     * 判断是否跨网关
     *
     * @param message
     * @return
     */
    public Map<String, Object> checkMulti(MqttMsg message) {
        boolean onlyDelay = checkPayload(message);
        boolean flag = false;
        String directId = "";
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();

        if (payload.get("visiable") != null) {
            //TODO 事件判断 暂时都为直连，下发网关
            Map<String, Object> flagMap = new HashMap<>();
            //取出trigger中的deviceId
            HashSet<String> deviceIdsSet = getDeviceIdsFromIf(message);
            //如果没有设备ID，则返回返回非跨直连
            if (CollectionUtils.isEmpty(deviceIdsSet)) {
                logger.error("error rule,have not deviceId!");
                flagMap.put("flag", flag);
                return flagMap;
            }
            //取出父设备主键列表
            List<String> directList = getParentIdList(deviceIdsSet);

            flagMap.put("flag", flag);
            flagMap.put("directId", directList.get(0));
            return flagMap;
        }

        if (onlyDelay) {
            return checkMultiMap(message);
        } else {
            Long autoId = getAutoId(message);
            Automation auto = autoService.getCache(autoId);
            int isMulti = auto.getIsMulti();
            if (isMulti == AutoConstants.IFTTT_MULTI_TRUE) {
                flag = true;
            } else {
                directId = auto.getDirectId();
            }

            Map<String, Object> flagMap = new HashMap<>();
            flagMap.put("flag", flag);
            flagMap.put("directId", directId);
            return flagMap;
        }
    }

    /**
     * 更新使能状态
     *
     * @param message
     * @param autoId
     */
    public void updateEnable(MqttMsg message, Long autoId) {
        Map<String, Object> param = (Map<String, Object>) message.getPayload();
        Integer enable = Integer.valueOf(param.get("enable").toString());
        autoService.setEnable(autoId, enable);
    }

    /**
     * 判断是否跨直连
     *
     * @param message
     * @return
     */
    private Map<String, Object> checkMultiMap(MqttMsg message) {
        Map<String, Object> flagMap = new HashMap<>();
        Boolean flag = false;

        //判断scene是否跨直连,包含scene都为跨直连
        List<Map<String, Object>> thenList = getThenList(message);
        for (Map<String, Object> prop : thenList) {
            String thenType = String.valueOf(prop.get("thenType"));
            if (AutoConstants.IFTTT_THEN_SCENE.equals(thenType)) {
                //跨直连
                flag = true;
                flagMap.put("flag", flag);
                return flagMap;
            }
        }

        //取出trigger中的deviceId
        HashSet<String> deviceIdsSet = getDeviceIdsFromIf(message);
        //取出then中的deviceId
        deviceIdsSet = getDeviceIdsFromThen(message, deviceIdsSet);
        //如果没有设备ID，则返回返回非跨直连
        if (CollectionUtils.isEmpty(deviceIdsSet)) {
            logger.error("error rule,have not deviceId!");
            flagMap.put("flag", flag);
            return flagMap;
        }
        //取出父设备主键列表
        List<String> directList = getParentIdList(deviceIdsSet);
        //判断是否跨直连,多个直连设备，则为跨直连
        String directId = "";
        if (directList.size() > 1) {
            //多个直连设备
            flag = true;
        } else {
            //单个直连设备
            directId = directList.get(0);
        }

        //应答
        flagMap.put("flag", flag);
        flagMap.put("directId", directId);
        return flagMap;
    }

    /**
     * 从then获取设备主键
     *
     * @param message
     * @param deviceIdsSet
     * @return
     */
    private HashSet<String> getDeviceIdsFromThen(MqttMsg message, HashSet<String> deviceIdsSet) {
        HashSet<String> res = deviceIdsSet;
        List<Map<String, Object>> thenList = getThenList(message);
        for (Map<String, Object> prop : thenList) {
            String id = String.valueOf(prop.get("id"));
            String thenType = String.valueOf(prop.get("thenType"));
            if (AutoConstants.IFTTT_THEN_DEVICE.equals(thenType)) {
                res.add(id);
            }
        }
        return res;
    }

    /**
     * 从if获取设备主键
     *
     * @param message
     * @return
     */
    private HashSet<String> getDeviceIdsFromIf(MqttMsg message) {
        HashSet<String> deviceIdsSet = new HashSet<>();
        List<Map<String, Object>> triggerList = getTriggerList(message);
        for (Map<String, Object> prop : triggerList) {
            String trigType = String.valueOf(prop.get("trigType"));
            if (AutoConstants.IFTTT_TRIGGER_DEVICE.equals(trigType)) {
                String deviceId = String.valueOf(prop.get("devId"));
                deviceIdsSet.add(deviceId);
            }
        }
        return deviceIdsSet;
    }

    /**
     * 更新跨直连关系信息
     *
     * @param flag
     * @param autoId
     * @param directId
     */
    public void updateRule(Boolean flag, Long autoId, String directId) {
        Byte isMulti = AutoConstants.IFTTT_MULTI_FALSE;
        if (flag) {
            isMulti = AutoConstants.IFTTT_MULTI_TRUE;
        }
        SaveAutoReq saveAutoReq = new SaveAutoReq();
        saveAutoReq.setIsMulti(isMulti.intValue());
        saveAutoReq.setDirectId(directId);
        Automation automation = autoService.getCache(autoId);
        saveAutoReq.setId(autoId);
        saveAutoReq.setTenantId(automation.getTenantId());
        autoService.saveAuto(saveAutoReq);
    }

    /**
     * 取父设备主键列表
     *
     * @param deviceIdsSet
     * @return
     */
    private List<String> getParentIdList(HashSet<String> deviceIdsSet) {
        List<String> deviceIds = new ArrayList<>(deviceIdsSet);

        List<ListDeviceInfoRespVo> deviceList = deviceCoreService.listDevicesByDeviceIds(deviceIds);
        logger.debug("======传入设备ID:" + deviceIds.toString() + ",取出设备ID" + deviceList.toString());
        //取出设备列表
        //取出父设备主键
        HashSet<String> directSet = new HashSet<>(); //唯一性
        for (ListDeviceInfoRespVo device : deviceList) {
            String directId;
            // 是否直连设备
            Integer isDirectDevice = device.getIsDirectDevice();
            if (isDirectDevice == 0) {//非
                //非直连
                directId = device.getParentId();
                if (StringUtil.isEmpty(directId)) {
                    //父节点为空，则为wifi plug直连设备
                    directId = device.getUuid();
                }
            } else {
                directId = device.getUuid();
            }
            directSet.add(directId);
        }

        return new ArrayList<>(directSet);
    }

    /**
     * 获取trigger集
     *
     * @param message
     * @return
     */
    private List<Map<String, Object>> getTriggerList(MqttMsg message) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        Map<String, Object> ifMap = (Map<String, Object>) payload.get("if");
        List<Map<String, Object>> triggerList = new ArrayList<>();
        if (ifMap != null && ifMap.containsKey("trigger")) {
            triggerList = (List<Map<String, Object>>) ifMap.get("trigger");
        }
        return triggerList;
    }

    /**
     * 获取then集
     *
     * @param message
     * @return
     */
    private List<Map<String, Object>> getThenList(MqttMsg message) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        List<Map<String, Object>> thenList = new ArrayList<>();
        if (payload.containsKey("then")) {
            thenList = (List<Map<String, Object>>) payload.get("then");
        }
        return thenList;
    }

    /**
     * 判断是否if和then
     *
     * @param message
     * @return
     */
    private boolean checkPayload(MqttMsg message) {
        boolean flag = false;
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        if (payload.containsKey("if")) {
            flag = true;
        }
        if (payload.containsKey("then")) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取ifttt主键
     *
     * @param message
     * @return
     */
    private Long getAutoId(MqttMsg message) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        if (StringUtil.isBlank(String.valueOf(payload.get("autoId")))) {
            return null;
        }
        return Long.parseLong(payload.get("autoId").toString());
    }

    /**
     * 发送mqtt消息
     *
     * @param message
     */
    public void sendMqttMsg(MqttMsg message) {
        logger.error("******* service send msg topic: ");
        String uuid = ToolUtils.getClientId(message.getTopic());
        if (StringUtil.isNotEmpty(uuid)) {
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), message, 1);
        }
    }

    /**
     * 属性排序
     *
     * @param message
     * @return
     */
    public MqttMsg sortMsg(MqttMsg message) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        List<Map<String, Object>> then = (List<Map<String, Object>>) payload.get("then");

        if (CollectionUtils.isNotEmpty(then)) {
            for (Map<String, Object> thenItem : then) {
                Map<String, Object> attr = (Map<String, Object>) thenItem.get("attr");

                HashMap<String, Object> attrMap = new LinkedHashMap();
                if (attr != null) {
                    if (attr.containsKey("OnOff")) {
                        //有OnOff,重新排序
                        attrMap.put("OnOff", attr.get("OnOff"));
                        attr.remove("OnOff");
                    }
                    for (Map.Entry<String, Object> entry : attr.entrySet()) {
                        attrMap.put(entry.getKey(), entry.getValue());
                    }
                }
                thenItem.put("attr", attrMap);
            }
        }

        payload.put("then", then);
        message.setPayload(payload);

        return message;
    }

}
