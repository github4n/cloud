package com.iot.shcs.scene.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.control.device.vo.resp.ListUserDeviceInfoRespVo;
import com.iot.control.scene.exception.SceneExceptionEnum;
import com.iot.device.api.CentralControlDeviceApi;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.locale.LocaleMessageSourceService;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.CallBackProcessor;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.mqttsdk.common.MqttMsgAck;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.common.exception.BusinessExceptionEnum;
import com.iot.shcs.common.util.MQTTUtils;
import com.iot.shcs.contants.GatewayErrorCodeEnum;
import com.iot.shcs.device.service.impl.DeviceCoreService;
import com.iot.shcs.helper.Constants;
import com.iot.shcs.helper.DispatcherRouteHelper;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.listener.MQTTClientListener;
import com.iot.shcs.scene.queue.bean.ExcSceneMessage;
import com.iot.shcs.scene.queue.sender.ExcSceneRepSender;
import com.iot.shcs.scene.queue.sender.ExcSceneReqSender;
import com.iot.shcs.scene.service.SceneDetailService;
import com.iot.shcs.scene.service.SceneService;
import com.iot.shcs.scene.util.RedisKeyUtil;
import com.iot.shcs.scene.vo.req.SceneDetailReq;
import com.iot.shcs.scene.vo.rsp.SceneDetailResp;
import com.iot.shcs.scene.vo.rsp.SceneResp;
import com.iot.shcs.voicebox.queue.bean.SceneMessage;
import com.iot.shcs.voicebox.queue.sender.DeleteSceneSender;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 项目名称: IOT云平台
 * 模块名称：
 * 功能描述：
 * 创建人: yuChangXing
 * 创建时间: 2018/4/24 10:28
 * 修改人:
 * 修改时间：
 */

@Service("scene")
public class SceneMQTTService implements CallBackProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SceneMQTTService.class);
    private static final int QOS = 1;
    private final String SCENE_ID = "sceneId";
    private final String PAYLOAD_THEN = "then";
    private final String PAYLOAD_ACTION = "action";
    private final String PAYLOAD_ACTION_UUID = "uuid";


    @Autowired
    private DeviceCoreService deviceCoreService;

    @Autowired
    private CentralControlDeviceApi centralControlDeviceApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private SceneDetailService sceneDetailService;

    @Autowired
    private MqttSdkService mqttSdkService;

    @Autowired
    private LocaleMessageSourceService localeMessageSourceService;

    @Autowired
    private IAutoService iAutoService;

    @Override
    public void onMessage(MqttMsg mqttMsg) {
        if (mqttMsg == null) {
            return;
        }
        DispatcherRouteHelper.dispatch(this, mqttMsg);
    }

    /**
     * 删除场景请求2c
     * <p>
     * iot/v1/s/[userId]/scene/delSceneReq     (app --> cloud)
     * iot/v1/c/[devId]/scene/delSceneReq      (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    public void delSceneReq(MqttMsg mqttMsg, String topic) {
        logger.info("***** 接受app请求删除scene ***** delSceneReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String userUuid = MQTTUtils.parseReqTopic(topic);
        List<String> directDeviceUuidList = null;
        boolean sendToDevice = true;
        Long sceneId = null;

        try {
            sceneId = MQTTUtils.getMustLong(payload, SCENE_ID);
            directDeviceUuidList = sceneService.findSceneDirectDeviceUuidListBySceneId(sceneId,SaaSContextHolder.currentTenantId());

//            SceneResp scene = sceneService.getSceneById(sceneId, SaaSContextHolder.currentTenantId());
            FetchUserResp user = userApi.getUserByUuid(userUuid);
            if (user == null) {
                logger.error("delSceneReq-system-error, user is null.");
                ack.setCode(MqttMsgAck.ERROR);
                ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
                return;
            }
            if (CollectionUtils.isEmpty(directDeviceUuidList)) {
                sendToDevice = false;
                logger.info("**** not found any direct device, will delete sceneId={},tenantId={},userId={}", sceneId,SaaSContextHolder.currentTenantId(),SaaSContextHolder.getCurrentUserId());
                deleteSceneAndSceneDetail(SaaSContextHolder.currentTenantId(), sceneId,user.getId());
            }

        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("delSceneReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (sendToDevice && ack.getCode() == MqttMsgAck.SUCCESS) {

                logger.info("*** delSceneReq(), will notify directDevice={} to delete sceneId={}", directDeviceUuidList, sceneId);
                // 缓存该情景存在的直连设备
                RedisCacheUtil.valueSet(RedisKeyUtil.getSceneWillDeleteIdKey(sceneId), JSON.toJSONString(directDeviceUuidList), 60L);

                //success notify device
                mqttMsg.setAck(null);

                directDeviceUuidList.forEach(deviceUuid -> {
                    mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
                    mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
                });
            } else {
                //error notify user
                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/scene/delSceneResp");
                mqttMsg.setMethod("delSceneResp");
                mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
            }
        }
    }

    /**
     * 删除场景响应2c
     * <p>
     * iot/v1/s/[devId]/scene/delSceneResp     (dev --> cloud)
     * iot/v1/c/[userId]/scene/delSceneResp      (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void delSceneResp(MqttMsg mqttMsg, String topic) {
        logger.info("***** 收到直连设备删除scene响应 ***** delSceneResp({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String directDeviceUuid = MQTTUtils.parseReqTopic(topic);
        String userUuid = null;
        boolean sendMsgToUser = true;

        try {
            Long sceneId = MQTTUtils.getMustLong(payload, SCENE_ID);
            SceneResp scene = sceneService.getSceneById(sceneId, SaaSContextHolder.currentTenantId());
            userUuid = userApi.getUuid(scene.getCreateBy());

            if (mqttMsg.getAck().getCode() == MqttMsgAck.SUCCESS
                    || mqttMsg.getAck().getCode() == GatewayErrorCodeEnum.INVALID_SCENE_ID.getCodeValue()) {

                // delSceneReq() 里存入的, 标记向哪几个directDeviceUuid发了删除scene请求, 要收到 全部directDeviceUuid 的delSceneResp()响应 才真正删除数据库中的scene记录
                String jsonVal = RedisCacheUtil.valueGet(RedisKeyUtil.getSceneWillDeleteIdKey(sceneId));
                logger.info("delSceneResp() -- jsonVal 删除数据前 --->{}", jsonVal);

                if (StringUtil.isNotBlank(jsonVal)) {
                    List<String> directDeviceUuidList = JSON.parseObject(jsonVal, List.class);
                    logger.info("delSceneResp() -- 删除数据前 数量={}, directDeviceUuidList={}", directDeviceUuidList.size(), directDeviceUuidList);

                    // 更新到缓存
                    directDeviceUuidList.remove(directDeviceUuid);
                    logger.info("delSceneResp() -- 删除数据后 数量={}, directDeviceUuidList ={}", directDeviceUuidList.size(), directDeviceUuidList);
                    RedisCacheUtil.valueSet(RedisKeyUtil.getSceneWillDeleteIdKey(sceneId), JSON.toJSONString(directDeviceUuidList), 60L);

                    // 删除sceneDetail
                    deleteSceneDetail(sceneId, directDeviceUuid);

                    if (directDeviceUuidList.size() == 0) {
                        deleteSceneAndSceneDetail(SaaSContextHolder.currentTenantId(), sceneId,scene.getCreateBy());

                        RedisCacheUtil.delete(RedisKeyUtil.getSceneWillDeleteIdKey(sceneId));
                        logger.info("delSceneResp() -- delete scene success!");
                    } else {
                        sendMsgToUser = false;
                    }

                } else {
                    sendMsgToUser = false;
                    logger.error("delSceneResp() -- gateWay return value timeout!");
                }
            }else{
                ack.setCode(mqttMsg.getAck().getCode());
                ack.setDesc(mqttMsg.getAck().getDesc());
            }
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("delSceneResp-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            logger.info("delSceneResp() -- sendMsgToUser={}", sendMsgToUser);
            if (sendMsgToUser) {
                //响应给app用户
                mqttMsg.setAck(ack);
                mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
                mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
            }
        }
    }

    /**
     * 添加场景项请求(组控)(scene_detail)2c
     * <p>
     * iot/v1/s/[userId]/scene/addActionReq     (app --> cloud)
     * iot/v1/c/[devId]/scene/addActionReq      (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    public void addActionReq(MqttMsg mqttMsg, String topic) {
        logger.info("addActionReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String deviceUuid = null;

        try {
            // 校验 参数action
            Map<String, Object> action = checkPayloadAction(payload);

            String actionUuid = String.valueOf(action.get(PAYLOAD_ACTION_UUID));
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(actionUuid);
            if (deviceResp.getIsDirectDevice() != null && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                deviceUuid = deviceResp.getUuid();
            }else{
                deviceUuid = deviceResp.getParentId();
            }

            if (StringUtil.isEmpty(deviceUuid)) {
                throw new BusinessException(SceneExceptionEnum.SCENE_UNBOUND_DIRECT_DEVICE);
            }
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("addActionReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);
                mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
            } else {
                //error notify user
                String userUuid = MQTTUtils.parseReqTopic(topic);
                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/scene/addActionResp");
                mqttMsg.setMethod("addActionResp");
            }

            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 添加场景项响应(组控)(scene_detail)2c
     * <p>
     * iot/v1/s/[devId]/scene/addActionResp     (dev --> cloud)
     * iot/v1/c/[userId]/scene/addActionResp    (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void addActionResp(MqttMsg mqttMsg, String topic) {
        logger.info("addActionResp({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck resultAck = MqttMsgAck.successAck();
        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        String userUuid = null;

        try {
            GetDeviceInfoRespVo deviceInfoRespVo =   deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
            FetchUserResp userResp = getUserByDeviceUuid(deviceInfoRespVo.getTenantId(),deviceUuid);

            userUuid = userResp.getUuid();

            MqttMsgAck deviceAck = mqttMsg.getAck();
            if (deviceAck.getCode() == 200) {
                insertOrUpdateSceneDetail(payload, userResp);
            }else{
                resultAck.setCode(deviceAck.getCode());
                resultAck.setDesc(deviceAck.getDesc());
            }
        } catch (BusinessException e) {
            resultAck.setCode(e.getCode());
            resultAck.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("addActionResp-system-error", e);
            resultAck.setCode(MqttMsgAck.ERROR);
            resultAck.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            //响应给app用户
            mqttMsg.setAck(resultAck);
            mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 删除场景项请求(组控)(scene_detail)2c
     * <p>
     * iot/v1/s/[userId]/scene/delActionReq     (app --> cloud)
     * iot/v1/c/[devId]/scene/delActionReq      (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    public void delActionReq(MqttMsg mqttMsg, String topic) {
        logger.info("delActionReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String deviceUuid = null;

        try {
            Map<String, Object> action = (Map<String, Object>) payload.get(PAYLOAD_ACTION);
            if (action == null) {
                throw new BusinessException(SceneExceptionEnum.PAYLOAD_ACTION_ERROR);
            }

            String actionUuid = String.valueOf(action.get(PAYLOAD_ACTION_UUID));
            if (actionUuid == null) {
                throw new BusinessException(SceneExceptionEnum.PAYLOAD_ACTION_ERROR);
            }

            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(actionUuid);
            if (deviceResp.getIsDirectDevice() != null && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                deviceUuid = deviceResp.getUuid();
            }else{
                deviceUuid = deviceResp.getParentId();
            }

            if (StringUtil.isEmpty(deviceUuid)) {
                throw new BusinessException(SceneExceptionEnum.SCENE_UNBOUND_DIRECT_DEVICE);
            }
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("delActionReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);
                mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
            } else {
                //error notify user
                String userUuid = MQTTUtils.parseReqTopic(topic);
                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/scene/delActionResp");
                mqttMsg.setMethod("delActionResp");
            }

            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 删除场景项响应(组控)(scene_detail)2c
     * <p>
     * iot/v1/s/[devId]/scene/delActionResp       (dev --> cloud)
     * iot/v1/c/[userId]/scene/delActionResp      (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void delActionResp(MqttMsg mqttMsg, String topic) {
        logger.info("delActionResp({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        String userUuid = null;

        try {
            GetDeviceInfoRespVo deviceInfoRespVo =   deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);

            FetchUserResp userResp = getUserByDeviceUuid(deviceInfoRespVo.getTenantId(),deviceUuid);
            userUuid = userResp.getUuid();

            MqttMsgAck deviceAck = mqttMsg.getAck();
            if (deviceAck.getCode() == 200) {
                Long sceneId = MQTTUtils.getMustLong(payload, SCENE_ID);
                Map<String, Object> action = (Map<String, Object>) payload.get(PAYLOAD_ACTION);
                if (action != null) {
                    //String type = String.valueOf(action.get("type"));
                    String subDeviceUuid = String.valueOf(action.get("uuid"));
                    sceneDetailService.delSceneDetailBySceneIdAndDeviceId(sceneId, subDeviceUuid, SaaSContextHolder.currentTenantId());
                }
            }else{
                ack.setCode(deviceAck.getCode());
                ack.setDesc(deviceAck.getDesc());
            }
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("delActionResp-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            //响应给app用户
            mqttMsg.setAck(ack);
            mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 修改场景项请求(组控)(scene_detail)2c
     * <p>
     * iot/v1/s/[userId]/scene/editActionReq     (app --> cloud)
     * iot/v1/c/[devId]/scene/editActionReq      (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    public void editActionReq(MqttMsg mqttMsg, String topic) {
        logger.info("editActionReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String deviceUuid = null;

        try {
            // 校验 参数action
            Map<String, Object> action = checkPayloadAction(payload);
            String actionUuid = String.valueOf(action.get(PAYLOAD_ACTION_UUID));
            GetDeviceInfoRespVo deviceResp = deviceCoreService.getDeviceInfoByDeviceId(actionUuid);
            if (deviceResp.getIsDirectDevice() != null && deviceResp.getIsDirectDevice().intValue() == Constants.IS_DIRECT_DEVICE) {
                deviceUuid = deviceResp.getUuid();
            }else{
                deviceUuid = deviceResp.getParentId();
            }

            if (StringUtil.isEmpty(deviceUuid)) {
                throw new BusinessException(SceneExceptionEnum.SCENE_UNBOUND_DIRECT_DEVICE);
            }
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("editActionReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            if (ack.getCode() == MqttMsgAck.SUCCESS) {
                //success notify device
                mqttMsg.setAck(null);
                mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), deviceUuid));
            } else {
                //error notify user
                String userUuid = MQTTUtils.parseReqTopic(topic);
                mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/scene/editActionResp");
                mqttMsg.setMethod("editActionResp");
            }

            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 修改场景项响应(组控)(scene_detail)2c
     * <p>
     * iot/v1/s/[devId]/scene/editActionResp     (dev --> cloud)
     * iot/v1/c/[userId]/scene/editActionResp    (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void editActionResp(MqttMsg mqttMsg, String topic) {
        logger.info("editActionResp({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String deviceUuid = MQTTUtils.parseReqTopic(topic);
        String userUuid = null;

        try {
            GetDeviceInfoRespVo deviceInfoRespVo =  deviceCoreService.getDeviceInfoByDeviceId(deviceUuid);
            FetchUserResp userResp = getUserByDeviceUuid(deviceInfoRespVo.getTenantId(),deviceUuid);
            userUuid = userResp.getUuid();

            MqttMsgAck deviceAck = mqttMsg.getAck();
            if (deviceAck.getCode() == 200) {
                insertOrUpdateSceneDetail(payload, userResp);
            }else{
                ack.setCode(deviceAck.getCode());
                ack.setDesc(deviceAck.getDesc());
            }
        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("editActionResp-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            //响应给app用户
            mqttMsg.setAck(ack);
            mqttMsg.setTopic(buildClientSceneTopic(MQTTUtils.getMethodFromTopic(topic), userUuid));
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 查询 场景项列表 请求(scene_detail)2c
     * <p>
     * iot/v1/s/[userId]/scene/getThenReq     (app --> cloud)
     *
     * @param mqttMsg
     * @param topic
     */
    public void getThenReq(MqttMsg mqttMsg, String topic) {
        logger.info("getThenReq({}, {})", mqttMsg, topic);

        Map<String, Object> payload = (Map<String, Object>) mqttMsg.getPayload();
        MqttMsgAck ack = MqttMsgAck.successAck();
        String userUuid = MQTTUtils.parseReqTopic(topic);

        try {
            Long sceneId = MQTTUtils.getMustLong(payload, SCENE_ID);

            List<Map<String, Object>> thenList = Lists.newArrayList();
            List<SceneDetailResp> sceneDetailList = sceneDetailService.findSceneDetailsBySceneId(sceneId,SaaSContextHolder.currentTenantId());
            if (CollectionUtils.isNotEmpty(sceneDetailList)) {
                sceneDetailList.forEach(sceneDetail -> {
                    Map<String, Object> attrMap = null;
                    if (StringUtils.isNotBlank(sceneDetail.getTargetValue())) {
                        attrMap = JSON.parseObject(sceneDetail.getTargetValue(), Map.class);
                    }

                    Map<String, Object> thenMap = Maps.newHashMap();
                    thenMap.put(Constants.SCENE_THEN_TYPE, "device");
                    thenMap.put(Constants.SCENE_THEN_UUID, sceneDetail.getDeviceId());
                    thenMap.put(Constants.SCENE_THEN_METHOD, sceneDetail.getMethod());
                    thenMap.put(Constants.SCENE_THEN_PARAMS, attrMap);
                    thenList.add(thenMap);
                });
            }
            payload.put(PAYLOAD_THEN, thenList);

        } catch (BusinessException e) {
            ack.setCode(e.getCode());
            ack.setDesc(localeMessageSourceService.getMessage(e.getMessage()));
        } catch (Exception e) {
            logger.error("getThenReq-system-error", e);
            ack.setCode(MqttMsgAck.ERROR);
            ack.setDesc(BusinessExceptionEnum.COMMOMN_EXCEPTION.getMessageKey());
        } finally {
            mqttMsg.setAck(ack);
            mqttMsg.setTopic(Constants.TOPIC_CLIENT_PREFIX + userUuid + "/scene/getThenResp");
            mqttMsg.setMethod("getThenResp");
            mqttMsg.setPayload(payload);
            mqttSdkService.sendMessage(MQTTClientListener.getMqttClientId(), mqttMsg, QOS);
        }
    }

    /**
     * 场景操作请求(执行场景请求)2c
     * <p>
     * iot/v1/s/[userId]/scene/excSceneReq     (app --> cloud)
     * iot/v1/c/[devId]/scene/excSceneReq     (cloud --> dev)
     *
     * @param mqttMsg
     * @param topic
     */
    public void excSceneReq(MqttMsg mqttMsg, String topic) {
        logger.info("excSceneReq({}, {})", mqttMsg, topic);
        Long tenantId = SaaSContextHolder.currentTenantId();
        logger.info("excSceneReq tenantId={}",tenantId);
        ExcSceneMessage excSceneMessage = ExcSceneMessage.builder().data(mqttMsg).topic(topic).build();
        excSceneMessage.setTenantId(tenantId);
        ApplicationContextHelper.getBean(ExcSceneReqSender.class).send(excSceneMessage);
    }

    /**
     * 场景操作响应(执行场景响应)2c
     * <p>
     * iot/v1/s/[devId]/scene/excSceneResp     (dev --> cloud)
     * iot/v1/s/[devId]/scene/excSceneResp     (cloud --> app)
     *
     * @param mqttMsg
     * @param topic
     */
    public void excSceneResp(MqttMsg mqttMsg, String topic) {
        logger.info("excSceneResp({}, {})", mqttMsg, topic);
        Long tenantId = SaaSContextHolder.currentTenantId();
        logger.info("excSceneResp tenantId={}",tenantId);
        ExcSceneMessage excSceneMessage = ExcSceneMessage.builder().data(mqttMsg).topic(topic).build();
        excSceneMessage.setTenantId(tenantId);
        ApplicationContextHelper.getBean(ExcSceneRepSender.class).send(excSceneMessage);
    }


    /**
     * 构建 scene模块的 topic
     *
     * @param method  方法名
     * @param objUUid 对象uuid(userUuid、deviceUuid)
     * @return
     */
    private String buildClientSceneTopic(String method, String objUUid) {
        return Constants.TOPIC_CLIENT_PREFIX + objUUid + "/" + "scene" + "/" + method;
    }

    /**
     * 获取 用户
     *
     * @param deviceUuid
     * @return
     */
    private FetchUserResp getUserByDeviceUuid(Long tenantId,String deviceUuid) {
        List<ListUserDeviceInfoRespVo> userDeviceInfoRespList = deviceCoreService.listUserDevices(tenantId,deviceUuid);
        if (CollectionUtils.isEmpty(userDeviceInfoRespList)) {
            logger.error("***** getUserByDeviceUuid() error! userDeviceInfoRespList is empty");
            throw new BusinessException(SceneExceptionEnum.USER_UNBOUND_GATEWAY);
        }

        return userApi.getUser(userDeviceInfoRespList.get(0).getUserId());
    }

    /**
     * 校验 参数action
     *
     * @param payload
     */
    private Map<String, Object> checkPayloadAction(Map<String, Object> payload) {
        Map<String, Object> action = (Map<String, Object>) payload.get(PAYLOAD_ACTION);
        if (action == null) {
            throw new BusinessException(SceneExceptionEnum.PAYLOAD_ACTION_ERROR);
        }
        if (action.get("uuid") == null) {
            throw new BusinessException(SceneExceptionEnum.PAYLOAD_ACTION_ERROR);
        }
        if (action.get("method") == null) {
            throw new BusinessException(SceneExceptionEnum.PAYLOAD_ACTION_ERROR);
        }

        return action;
    }

    /**
     * 插入or更新 sceneDetail
     *
     * @param payload
     * @param userResp
     */
    private void insertOrUpdateSceneDetail(Map<String, Object> payload, FetchUserResp userResp) {
        logger.info("insertOrUpdateSceneDetail({}, {})", payload, userResp);
        Long sceneId = MQTTUtils.getMustLong(payload, SCENE_ID);
        SceneResp scene = sceneService.getSceneById(sceneId,SaaSContextHolder.currentTenantId());
        if (scene == null) {
            throw new BusinessException(SceneExceptionEnum.SCENE_NOT_EXIST);
        }

        Map<String, Object> action = (Map<String, Object>) payload.get(PAYLOAD_ACTION);
        if (action != null) {
            //String type = String.valueOf(action.get("type"));
            String subDeviceUuid = String.valueOf(action.get("uuid"));
            String method = String.valueOf(action.get("method"));
            String params = String.valueOf(action.get("params"));

            SceneDetailReq sceneDetailReq = new SceneDetailReq();
            sceneDetailReq.setSceneId(sceneId);
            sceneDetailReq.setDeviceId(subDeviceUuid);
            sceneDetailReq.setSpaceId(scene.getSpaceId());
            sceneDetailReq.setMethod(method);
            sceneDetailReq.setTargetValue(params);
            sceneDetailReq.setCreateBy(userResp.getId());
            sceneDetailReq.setUpdateBy(userResp.getId());
            sceneDetailReq.setTenantId(userResp.getTenantId());
            logger.info("insertOrUpdateSceneDetail {}", JSONObject.toJSONString(sceneDetailReq));
            sceneDetailService.insertOrUpdateSceneDetail(sceneDetailReq);
        }
    }

    /**
     *  删除 scene 和 sceneDetail
     * @param sceneId
     */
    private void deleteSceneAndSceneDetail(Long tenantId, Long sceneId ,Long userId) {
        if (sceneId != null) {
            SceneResp sceneResp = sceneService.getSceneById(sceneId,tenantId);
            sceneDetailService.delSceneDetailBySceneId(sceneId, tenantId,userId);
            sceneService.delSceneBySceneId(sceneId,tenantId,userId);
            iAutoService.delBySceneId(sceneId,tenantId);
            if (sceneResp != null) {
                ApplicationContextHelper.getBean(DeleteSceneSender.class).send(SceneMessage.builder()
                        .userId(sceneResp.getCreateBy())
                        .sceneId(sceneResp.getId()).build());
            }
        }
    }

    /**
     * 删除 directDeviceId 对应下的 sceneDetail
     *
     * @param sceneId
     * @param directDeviceId
     */
    private void deleteSceneDetail(Long sceneId, String directDeviceId) {
        List<String> willDeleteIdList = Lists.newArrayList();

        // directDeviceId 下的 子设备
        List<DeviceResp> deviceRespList = centralControlDeviceApi.findUnDirectDeviceListByParentDeviceId(directDeviceId);
        if (CollectionUtils.isEmpty(deviceRespList)) {
            logger.info("deleteSceneDetail() --> 将要删除的 sceneDetail.deviceUuid={} 为直连设备", directDeviceId);
            willDeleteIdList.add(directDeviceId);
        }else{
            deviceRespList.forEach(deviceResp -> {
                willDeleteIdList.add(deviceResp.getDeviceId());
            });
        }

        logger.info("deleteSceneDetail() --> 将要删除的 willDeleteIdList={}", willDeleteIdList);

        if(willDeleteIdList.size() > 0){
            willDeleteIdList.forEach(deviceId -> {
                try {
                    sceneDetailService.delSceneDetailBySceneIdAndDeviceId(sceneId, deviceId, SaaSContextHolder.currentTenantId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
