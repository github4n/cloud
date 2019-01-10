package com.iot.ifttt.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.common.util.StringUtil;
import com.iot.ifttt.api.IftttAccessApi;
import com.iot.ifttt.common.ServiceEnum;
import com.iot.ifttt.entity.IftttApi;
import com.iot.ifttt.entity.IftttDevice;
import com.iot.ifttt.service.IIftttApiService;
import com.iot.ifttt.service.IIftttDeviceService;
import com.iot.ifttt.service.MQSender;
import com.iot.ifttt.util.RedisKeyUtil;
import com.iot.ifttt.util.SendUtil;
import com.google.common.collect.Lists;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceStateCoreApi;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;

import com.iot.ifttt.vo.*;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.shcs.space.api.SmarthomeSpaceApi;
import com.iot.shcs.space.vo.SpaceResp;
import com.iot.shcs.voicebox.api.VoiceBoxApi;
import com.iot.shcs.voicebox.vo.VoiceBoxMqttMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：IFTTT接入控制器
 * 创建人： LaiGuiMing
 * 创建时间： 2018/11/29 11:27
 */

@Slf4j
@RestController
public class IftttAccessController implements IftttAccessApi {

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private DeviceStateCoreApi deviceStateCoreApi;

    @Autowired
    private IIftttApiService iftttApiService;

    @Autowired
    private IIftttDeviceService iftttDeviceService;

    @Autowired
    private SmarthomeSpaceApi smarthomeSpaceApi;

    @Autowired
    private VoiceBoxApi voiceBoxApi;

    @Autowired
    private MQSender mqSender;

    private String iftttKey; //IFTTT秘钥

    @Override
    public int checkApplet(@RequestBody CheckTriggerReq req) {
        log.debug("收到触发校验请求：" + req.toString());
        int rc = 0;
        String type = req.getType();
        IftttApi iftttApi = iftttApiService.getByName(type);
        if (iftttApi == null) {
            log.info("未配置服务，校验失败！" + req.toString());
            return 2;
        }

        ServiceEnum triggerType = ServiceEnum.getEnum(iftttApi.getCode());
        if (triggerType == null) {
            log.info("服务配置不正确，校验失败！");
            return 2;
        }
        switch (triggerType) {
            case DEVICE_STATUS:
                //设备状态触发
                rc = checkDevice(req, iftttApi.getField());
                break;
            case SCENE:
                //情景检测通知
                rc = checkScene(req);
                break;
            case SECURITY:
                //安防报警检测通知
                rc = checkSecurity(req);
                break;
            case DEVICE_RANGE:
                //安防报警检测通知
                rc = checkDeviceRange(req, iftttApi.getField());
                break;
            case SECURITY_MODE:
                //安防类型设置触发
                rc = checkSecurityType(req);
                break;
            default:
                break;
        }

        return rc;
    }

    @Override
    public void checkNotify(@RequestBody CheckNotifyReq req) {
        log.debug("收到执行通知校验请求：" + req.toString());
        String type = req.getType();
        Map<String, Object> fields = req.getFields();
        ServiceEnum triggerType = ServiceEnum.getEnum(type);
        String redisKey = null;
        switch (triggerType) {
            case DEVICE_STATUS:
                //设备状态检测通知
                String deviceId = (String) fields.get("deviceId");
                Map<String, Object> attrMap = (Map<String, Object>) fields.get("attr");
                String field = "";
                if (attrMap != null) {
                    for (String vo : attrMap.keySet()) {
                        field = vo;
                    }
                    redisKey = RedisKeyUtil.getIftttDeviceKey(deviceId, field);
                }
                break;
            case SCENE:
                //情景检测通知
                String sceneId = (String) fields.get("sceneId");
                redisKey = RedisKeyUtil.getIftttSceneKey(sceneId);
                break;
            case SECURITY:
                //安防报警检测通知
                String userId = (String) fields.get("userId");
                redisKey = RedisKeyUtil.getIftttSecurityKey(userId);
                break;
            case SECURITY_MODE:
                //安防类型设置通知
                String uuid = (String) fields.get("userId");
                String status = (String) fields.get("status");
                redisKey = RedisKeyUtil.getIftttSecurityTypeKey(uuid, status);
                break;
            default:
                break;
        }
        if (redisKey == null) {
            return;
        }

        log.debug("关联applet 缓存key:" + redisKey);
        Set<String> triggerSet = RedisCacheUtil.setGetAll(redisKey, String.class, false);
        List<String> identities = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(triggerSet)) {
            log.debug("关联applet 缓存:" + triggerSet.toString());
            for (String vo : triggerSet) {
                String identityKey = RedisKeyUtil.getIftttTriggerKey(vo);
                String time = RedisCacheUtil.valueGet(identityKey);
                if (time != null) {
                    identities.add(vo);
                }
            }
        }

        //更新trigger缓存
        if (CollectionUtils.isNotEmpty(identities)) {
            //批量发送实时API
            SendUtil.sendNotify(identities, getIftttKey());

            //更新
            Set<String> set = new HashSet<>(identities);
            RedisCacheUtil.setAdd(redisKey, set, false);
        } else {
            //删除
            RedisCacheUtil.delete(redisKey);
        }
    }

    @Override
    public List<Long> getProductList(@RequestBody GetProductReq req) {
        log.debug("收到获取产品列表请求：" + req.toString());
        IftttApi iftttApi = iftttApiService.getByName(req.getType());
        List<Long> resList = Lists.newArrayList();
        if (iftttApi != null) {
            List<IftttDevice> list = iftttDeviceService.getListByApi(iftttApi.getId(), req.getTenantId());
            if (CollectionUtils.isNotEmpty(list)) {
                for (IftttDevice vo : list) {
                    resList.add(vo.getProductId());
                }
            }
        }
        return resList;
    }

    @Override
    public void doAction(@RequestBody ActionReq req) {
        log.debug("收到控制设备请求：" + req.toString());
        String type = req.getType();
        IftttApi iftttApi = iftttApiService.getByName(type);
        if (iftttApi == null) {
            log.debug("未配置服务，执行失败！" + req.toString());
            return;
        }

        ServiceEnum actionType = ServiceEnum.getEnum(iftttApi.getCode());
        if (actionType == null) {
            log.debug("服务配置不正确，校验失败！");
            return;
        }
        switch (actionType) {
            case CONTROL_DEVICE:
                //控制设备
                controlDevice(iftttApi.getField(), req.getActionFields());
                break;
            case SET_SECURITY:
                //设置安防类型
                setSecurity(iftttApi.getField(), req);
                break;
            default:
                log.debug("未知服务，执行失败！" + req.toString());
                break;
        }
    }

    @Override
    public Map<String, Object> getConfig() {
        Map<String, Object> map = Maps.newHashMap();
        //获取秘钥配置
        map.put("key", getIftttKey());
        return map;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setSecurity(String field, ActionReq req) {
        // 填充mqtt数据
        String seq = "r_" + StringUtil.getRandomString(8);

        MqttMsg mqttMsgReq = new MqttMsg();
        mqttMsgReq.setService("security");
        mqttMsgReq.setMethod("setArmModeReq");
        mqttMsgReq.setSeq(seq);
        mqttMsgReq.setSrcAddr("0." + req.getUuid());

        Map<String, Object> payload = Maps.newHashMap();
        SpaceResp spaceResp = smarthomeSpaceApi.findUserDefaultSpace(req.getUserId(), req.getTenantId());
        if (spaceResp == null) {
            log.debug("家数据为空，设置安防类型失败！");
            return;
        }
        payload.put("homeId", spaceResp.getId());
        Map<String, Object> actionFields = req.getActionFields();
        String mode = (String) actionFields.get(field);
        payload.put("armMode", mode);
        mqttMsgReq.setPayload(payload);

        String jsonStr = JSON.toJSONString(mqttMsgReq);

        // 包装到 VoiceBoxMqttMsg
        VoiceBoxMqttMsg voiceBoxMqttMsg = new VoiceBoxMqttMsg(jsonStr, req.getUuid());
        log.debug("发送设置安防模式请求：" + jsonStr);
        voiceBoxApi.setArmModeReq(voiceBoxMqttMsg);
    }

    /**
     * 控制设备
     *
     * @param fields
     * @param actionFields
     */
    private void controlDevice(String fields, Map<String, Object> actionFields) {
        //发送MQ消息
        ActionMessage action = new ActionMessage();
        action.setRoute("2C");
        //执行控灯
        if (fields == null) {
            log.info("控制字段未配置，不执行！");
            return;
        }
        String[] fieldArray = fields.split(",");
        String attr = "";
        for (String vo : fieldArray) {
            Object obj = actionFields.get(vo);
            if (obj != null) {
                //TODO 暂时都为整型
                attr += "\"" + vo + "\":" + obj.toString() + ",";
            }
        }

        if ("".equals(attr)) {
            log.info("控制为空，不执行！");
            return;
        }
        //取出末尾逗号
        attr = attr.substring(0, attr.length() - 1);
        //发送控制设备消息
        String objectId = (String) actionFields.get("objectId");
        String msg = "{\"thenType\":\"dev\",\"id\":\"" + objectId + "\",\"idx\":0,\"attr\":{" + attr + "}}";
        action.setMessage(msg);
        mqSender.send(action);
        log.info("发送MQ消息：msg=" + msg);
    }

    private String getIftttKey() {
        if (StringUtils.isEmpty(iftttKey)) {
            Environment environment = ApplicationContextHelper.getBean(Environment.class);
            //获取环境标识
            iftttKey = environment.getProperty("ifttt-api-key");
        }
        return iftttKey;
    }

    /**
     * 设备范围值检测
     *
     * @param req
     * @return
     */
    private int checkDeviceRange(CheckTriggerReq req, String field) {
        int rc = 2;
        if (!checkField(req, "objectId", "min", "max")) {
            //参数错误
            log.info("参数错误，校验失败");
            return 1;
        }
        Map<String, Object> triggerFields = req.getTriggerFields();
        String uuid = (String) triggerFields.get("objectId");
        float min = Float.parseFloat(triggerFields.get("min").toString());
        float max = Float.parseFloat(triggerFields.get("max").toString());
        String identity = req.getIdentity();
        //更新缓存
        String redisKey = RedisKeyUtil.getIftttDeviceKey(uuid, field);
        RedisCacheUtil.setPush(redisKey, req.getIdentity(), false);
        String identityKey = RedisKeyUtil.getIftttTriggerKey(req.getIdentity());
        String time = String.valueOf(System.currentTimeMillis());
        RedisCacheUtil.valueSet(identityKey, time, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_2);

        //判断是否是实时API
        String checkKey = RedisKeyUtil.getIftttCheckKey(identity);
        String value = RedisCacheUtil.valueGet(checkKey);

        if (value == null) {
            log.debug("没有实时变化值，不做判断");
            return rc;
        }

        //删除缓存
        RedisCacheUtil.delete(checkKey);

        Long tenantId = getTenantId(uuid);
        Map<String, Object> redisProperties = deviceStateCoreApi.get(tenantId, uuid);
        if (redisProperties == null) {
            log.warn("设备缓存状态为空，不执行！");
            return rc;
        }
        log.info("设备状态缓存：" + redisProperties.toString());
        String fieldRedis = (String) redisProperties.get(field);
        float redisValue = Float.parseFloat(fieldRedis);
        log.debug("设备状态范围校验，redis值：" + redisValue + ",规则min：" + min + ",max:" + max);
        if (redisValue >= min && redisValue <= max) {
            log.debug("【已】达到触发条件！");
            rc = 0;
        } else {
            log.debug("【未】达到触发条件！");
        }

        return rc;
    }

    /**
     * 设备状态检测
     *
     * @param req
     * @return
     */
    private int checkDevice(CheckTriggerReq req, String field) {
        log.debug("设备状态校验" + req.toString());
        int rc = 2;
        if (!checkField(req, "objectId", "status")) {
            //参数错误
            log.info("参数错误，校验失败");
            return 1;
        }
        Map<String, Object> triggerFields = req.getTriggerFields();
        String uuid = (String) triggerFields.get("objectId");
        String status = String.valueOf(triggerFields.get("status"));
        String identity = req.getIdentity();
        //更新缓存
        String redisKey = RedisKeyUtil.getIftttDeviceKey(uuid, field);
        RedisCacheUtil.setPush(redisKey, req.getIdentity(), false);
        String identityKey = RedisKeyUtil.getIftttTriggerKey(req.getIdentity());
        String time = String.valueOf(System.currentTimeMillis());
        RedisCacheUtil.valueSet(identityKey, time, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_2);

        //判断是否是实时API
        String checkKey = RedisKeyUtil.getIftttCheckKey(identity);
        String value = RedisCacheUtil.valueGet(checkKey);

        if (value == null) {
            log.debug("没有实时API消息，校验失败！");
            return rc;
        }

        //删除缓存
        RedisCacheUtil.delete(checkKey);

        Long tenantId = getTenantId(uuid);
        Map<String, Object> redisProperties = deviceStateCoreApi.get(tenantId, uuid);
        if (redisProperties == null) {
            log.warn("设备缓存状态为空，不执行！");
            return rc;
        }
        log.info("设备状态缓存：" + redisProperties.toString());
        String fieldRedis = (String) redisProperties.get(field);
        Integer redisValue = Integer.valueOf(fieldRedis);
        Integer statusValue = Integer.valueOf(status);
        log.debug("设备状态校验，redis值：" + redisValue + ",规则值：" + statusValue);
        if (redisValue.intValue() == statusValue.intValue()) {
            rc = 0;
        }
        log.debug("设备状态校验结果：" + rc);
        return rc;
    }

    /**
     * 情景检测
     *
     * @param req
     * @return
     */
    private int checkScene(CheckTriggerReq req) {
        int rc = 2;
        if (!checkField(req, "objectId")) {
            //参数错误
            return 1;
        }
        Map<String, Object> triggerFields = req.getTriggerFields();
        String objectId = (String) triggerFields.get("objectId");
        String identity = req.getIdentity();
        //更新缓存
        String redisKey = RedisKeyUtil.getIftttSceneKey(objectId);
        RedisCacheUtil.setPush(redisKey, identity, false);
        String identityKey = RedisKeyUtil.getIftttTriggerKey(identity);
        String time = String.valueOf(System.currentTimeMillis());
        RedisCacheUtil.valueSet(identityKey, time, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_2);

        //判断是否执行
        String checkKey = RedisKeyUtil.getIftttCheckKey(identity);
        String value = RedisCacheUtil.valueGet(checkKey);
        if (value != null) {
            //有事件缓存
            rc = 0;
            //删除缓存
            RedisCacheUtil.delete(checkKey);
        }
        return rc;
    }

    /**
     * 安防类型设置检测
     *
     * @param req
     * @return
     */
    private int checkSecurityType(CheckTriggerReq req) {
        int rc = 2;
        if (!checkField(req, "status")) {
            //参数错误
            return 1;
        }
        Map<String, Object> triggerFields = req.getTriggerFields();
        String status = (String) triggerFields.get("status");
        String identity = req.getIdentity();
        //更新缓存
        String redisKey = RedisKeyUtil.getIftttSecurityTypeKey(String.valueOf(req.getUserId()),status);
        RedisCacheUtil.setPush(redisKey, identity, false);
        String identityKey = RedisKeyUtil.getIftttTriggerKey(identity);
        String time = String.valueOf(System.currentTimeMillis());
        RedisCacheUtil.valueSet(identityKey, time, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_2);

        //判断是否执行
        String checkKey = RedisKeyUtil.getIftttCheckKey(identity);
        String value = RedisCacheUtil.valueGet(checkKey);
        if (value != null) {
            //有事件缓存
            rc = 0;
            //删除缓存
            RedisCacheUtil.delete(checkKey);
        }
        return rc;
    }

    /**
     * 安防检测
     *
     * @param req
     * @return
     */
    private int checkSecurity(CheckTriggerReq req) {
        int rc = 2;
        if (!checkField(req, "type")) {
            //参数错误
            return 1;
        }
        String userId = req.getUserId().toString();
        String identity = req.getIdentity();
        //更新缓存
        String redisKey = RedisKeyUtil.getIftttSecurityKey(userId);
        RedisCacheUtil.setPush(redisKey, identity, false);
        String identityKey = RedisKeyUtil.getIftttTriggerKey(identity);
        String time = String.valueOf(System.currentTimeMillis());
        RedisCacheUtil.valueSet(identityKey, time, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_2);

        //判断是否执行
        String checkKey = RedisKeyUtil.getIftttCheckKey(identity);
        String value = RedisCacheUtil.valueGet(checkKey);
        if (value != null) {
            //有事件缓存
            rc = 0;
            //删除缓存
            RedisCacheUtil.delete(checkKey);
        }
        return rc;
    }

    /**
     * 获取租户主键
     *
     * @param subDevId
     * @return
     */
    private Long getTenantId(String subDevId) {
        Long tenantId;
        GetDeviceInfoRespVo subDeviceInfo = deviceCoreApi.get(subDevId);
        if (subDeviceInfo.getParentId() != null) {
            GetDeviceInfoRespVo deviceInfo = deviceCoreApi.get(subDeviceInfo.getParentId());
            tenantId = deviceInfo.getTenantId();
        } else {
            tenantId = subDeviceInfo.getTenantId();
        }

        return tenantId;
    }

    /**
     * 校验参数方法
     *
     * @param req
     * @param field
     * @return
     */
    protected boolean checkField(CheckTriggerReq req, String... field) {
        Map<String, Object> triggerFields = req.getTriggerFields();
        boolean flag = true;
        if (triggerFields == null || triggerFields.size() == 0) {
            flag = false;
        } else {
            for (int i = 0; i < field.length; i++) {
                String key = field[i];
                String value = (String) triggerFields.get(key);
                if (value == null) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
}
