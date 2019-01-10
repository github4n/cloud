package com.iot.shcs.ifttt.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.ifttt.constant.IftttConstants;
import com.iot.ifttt.api.IftttAccessApi;
import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.common.IftttStatusEnum;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.common.ServiceEnum;
import com.iot.ifttt.vo.*;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.device.service.impl.DeviceService;
import com.iot.shcs.ifttt.constant.AutoConstants;
import com.iot.shcs.ifttt.entity.Automation;
import com.iot.shcs.ifttt.entity.AutomationItem;
import com.iot.shcs.ifttt.exception.AutoExceptionEnum;
import com.iot.shcs.ifttt.mapper.AutomationMapper;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.ifttt.service.IAutomationItemService;
import com.iot.shcs.ifttt.util.IftttBeanUtil;
import com.iot.shcs.ifttt.util.RedisKeyUtil;
import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import com.iot.shcs.ifttt.vo.req.AutoListReq;
import com.iot.shcs.ifttt.vo.req.SaveAutoReq;
import com.iot.shcs.ifttt.vo.res.AutoListResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 描述：联动服务实现类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/30 14:21
 */
@Service
@Transactional
public class AutoServiceImpl implements IAutoService {

    private final Logger logger = LoggerFactory.getLogger(AutoServiceImpl.class);

    @Autowired
    private AutomationMapper automationMapper;

    @Autowired
    private IAutomationItemService automationItemService;

    @Autowired
    private IftttApi iftttApi;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AutoMQTTServiceImpl autoMQTTService;

    @Autowired
    private IftttAccessApi iftttAccessApi;

    @Override
    public List<AutoListResp> list(AutoListReq req) {
        logger.info("=== receive auto list request ===" + req.toString());
        // 目标页记录 列表
        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        List<AutoListResp> resultList = Lists.newArrayList();
        try {
            // 目标页记录 的id列表
            Automation entity = new Automation();
            entity.setUserId(req.getUserId());
            entity.setSpaceId(req.getSpaceId());
            entity.setTenantId(req.getTenantId());
            List<Automation> tempList = automationMapper.findSimpleList(entity);
            List<Long> autoIds = buildRuleIdList(tempList);
            if (CollectionUtils.isNotEmpty(autoIds)) {
                logger.debug("list(), 从数据库查询出的 tempList.size()={}", autoIds.size());
                List<Automation> list = getlistCache(autoIds);
                for (Automation automation : list) {
                    AutoListResp resp = new AutoListResp();
                    BeanUtils.copyProperties(automation, resp);
                    resp.setType(automation.getTriggerType());
                    resultList.add(resp);
                }

                //排序
                Collections.sort(resultList, new Comparator<AutoListResp>() {
                    @Override
                    public int compare(AutoListResp b1, AutoListResp b2) {
                        return b2.getCreateTime().compareTo(b1.getCreateTime());
                    }
                });
            } else {
                logger.debug("list(), 从数据库查询出的 tempList is empty.");
            }
        } catch (Exception e) {
            //分页获取IFTTT规则失败
            logger.error("get ifttt list error", e);
            throw new BusinessException(AutoExceptionEnum.GET_IFTTT_LIST_ERROR, e);
        }
        return resultList;
    }

    @Override
    public Long saveAuto(SaveAutoReq req) {
        logger.info("收到保存auto请求：" + req.toString());
        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        Long autoId = -1l;

        //校验名称是否重复
        if (req.getVisiable() == null || req.getVisiable().intValue() == 1) {
            int flag = automationMapper.checkAutoName(req.getUserId(), req.getName());
            if (flag > 0) {
                //抛异常
                logger.warn("save auto error, name is exist");
                throw new BusinessException(AutoExceptionEnum.AUTO_IS_EXIST);
            }
        }

        try {
            Automation automation = new Automation();
            BeanUtils.copyProperties(req, automation);
            if (req.getId() != null) {
                //横向越权检测
                checkOverPower(req.getId());

                automationMapper.updateById(automation);
                //删除缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttAutoKey(req.getId()));
            } else {
                automation.setCreateTime(new Date());
                automationMapper.insert(automation);
            }
            autoId = automation.getId();
        } catch (Exception e) {
            logger.error("save auto error", e);
            throw new BusinessException(AutoExceptionEnum.SAVE_RULE_ERROR, e);
        }
        return autoId;
    }

    @Override
    public void saveAutoRule(MqttMsg message, Boolean enable) {
        try {
            Map<String, Object> payload = (Map<String, Object>) message.getPayload();
            //取autoId
            Object autoIdObj = payload.get("autoId");
            if (autoIdObj == null) {
                logger.info("格式不正确，不保存！");
                return;
            }
            Long autoId = Long.parseLong(autoIdObj.toString());

            //取出auto
            Automation auto = getCache(autoId);
            Long appletId = auto.getAppletId();

            AppletVo appletVo = getAppletBean(message, auto);
            if (appletId != null) {
                appletVo.setId(appletId);
            }
            //状态设置
            Integer status = auto.getStatus();
            appletVo.setStatus(IftttStatusEnum.OFF.getCode());
            if (status == 1 && enable) {
                //启用且为跨直连
                appletVo.setStatus(IftttStatusEnum.ON.getCode());
            }
            appletId = iftttApi.save(appletVo);

            //更新auto
            auto.setAppletId(appletId);
            //delay
            int enableDelay = 0;
            if (payload.containsKey("enableDelay")) {
                Object delay = payload.get("enableDelay");
                enableDelay = delay != null ? Integer.parseInt(delay.toString()) : 0;
            }
            auto.setDelay(enableDelay);
            //时间json
            String timeJson = getTimeJson(payload);
            auto.setTimeJson(timeJson);
            //时间类型 timer/sunset/sunrise/dev/devEvent
            String type = IftttConstants.IFTTT_TRIGGER_DEVICE;
            if (!StringUtils.isEmpty(timeJson)) {
                Map<String, Object> timeMap = JSON.parseObject(timeJson, Map.class);
                if (timeMap.get("trigType") != null) {
                    type = (String) timeMap.get("trigType");
                }
            }

            if (type.equals(IftttConstants.IFTTT_TRIGGER_DEVICE)) {
                //区分dev /devEvent
                type = getTriggerType(message);
            }

            auto.setTriggerType(type);

            automationMapper.updateById(auto);
            //删除缓存
            RedisCacheUtil.delete(RedisKeyUtil.getIftttAutoKey(autoId));

            //更新item与scene和设备的关系
            updateItemRelation(appletId, auto.getTenantId());
        } catch (Exception e) {
            logger.error("save auto rule error", e);
            throw new BusinessException(AutoExceptionEnum.SAVE_IFTTT_ERROR, e);
        }
    }

    /**
     * 获取triggerType
     *
     * @param message
     * @return
     */
    private String getTriggerType(MqttMsg message) {
        String type = IftttConstants.IFTTT_TRIGGER_DEVICE;
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        Map<String, Object> ifMap = (Map<String, Object>) payload.get("if");
        List<Map<String, Object>> triggerList = new ArrayList<>();
        if (ifMap != null && ifMap.containsKey("trigger")) {
            triggerList = (List<Map<String, Object>>) ifMap.get("trigger");
        }

        for (Map<String, Object> prop : triggerList) {
            if (prop.get("event") != null) {
                type = "devEvent";
                break;
            }
        }

        return type;
    }

    @Override
    public void setEnable(Long autoId, Integer status) {
        try {
            Automation automation = new Automation();
            automation.setId(autoId);
            automation.setStatus(status);
            automationMapper.updateById(automation);
            //删除缓存
            RedisCacheUtil.delete(RedisKeyUtil.getIftttAutoKey(autoId));

            //更新applet
            Automation auto = getCache(autoId);
            SetEnableReq setEnableReq = new SetEnableReq();
            if (auto.getAppletId() != null) {
                setEnableReq.setId(auto.getAppletId());
                if (status == 0) {
                    setEnableReq.setStatus(IftttStatusEnum.OFF.getCode());
                } else {
                    setEnableReq.setStatus(IftttStatusEnum.ON.getCode());
                }
                if (auto.getIsMulti() == 1) {
                    iftttApi.setEnable(setEnableReq);
                }
            }
        } catch (Exception e) {
            logger.error("set auto enable error", e);
            throw new BusinessException(AutoExceptionEnum.SET_IFTTT_ENABLE_ERROR, e);
        }
    }

    @Override
    public void delete(Long autoId) {
        try {
            //删除applet
            Automation auto = getCache(autoId);
            automationMapper.deleteById(autoId);

            if (auto.getAppletId() != null) {
                iftttApi.delete(auto.getAppletId());
                //删除规则子项
                automationItemService.delItemByAppletId(auto.getAppletId(), auto.getTenantId());
            }

            //删除缓存
            RedisCacheUtil.delete(RedisKeyUtil.getIftttAutoKey(autoId));
        } catch (Exception e) {
            logger.error("delete auto error", e);
            throw new BusinessException(AutoExceptionEnum.DELETE_IFTTT_ERROR, e);
        }
    }

    @Override
    public Automation getCache(Long autoId) {
        Automation auto = null;
        try {
            //取缓存
            String key = RedisKeyUtil.getIftttAutoKey(autoId);
            auto = RedisCacheUtil.valueObjGet(key, Automation.class);
            if (auto == null) {
                auto = automationMapper.selectById(autoId);
                if (auto != null) {
                    RedisCacheUtil.valueObjSet(key, auto, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
                }
            }
        } catch (Exception e) {
            logger.error("get auto cache error", e);
            throw new BusinessException(AutoExceptionEnum.GET_IFTTT_ERROR, e);
        }
        return auto;
    }

    @Override
    public Map<String, Object> getPayloadById(Long autoId) {
        Map<String, Object> payload = new HashMap<>();
        try {
            //获取auto
            Automation auto = getCache(autoId);
            if (auto == null) {
                throw new BusinessException(AutoExceptionEnum.RULE_ID_IS_NULL);
            }
            String triggerType = auto.getTriggerType();

            //获取applet
            AppletVo appletVo;
            if (auto.getAppletId() == null) {
                appletVo = new AppletVo();
            } else {
                appletVo = iftttApi.get(auto.getAppletId());
            }

            //if
            Map<String, Object> ifMap = new HashMap<>();
            Map<String, Object> validMap = new HashMap<>();
            List<Map<String, Object>> triggerList = new ArrayList<>();
            List<Map<String, Object>> thenList = new ArrayList<>();
            int enableDelay = 0;
            if (auto.getDelay() != null) {
                enableDelay = auto.getDelay();
            }

            //valid
            String valid = "{\"end\":\"23:59\",\"begin\":\"00:00\",\"week\":[0,1,2,3,4,5,6]}";
            List<AppletThisVo> thisVos = appletVo.getThisList();

            String logic = "or";
            if (CollectionUtils.isNotEmpty(thisVos)) {
                if (triggerType.equals("dev")) {
                    for (AppletThisVo thisVo : thisVos) {
                        if (thisVo.getServiceCode().equals(IftttServiceEnum.TIME_RANGE.getCode())) {
                            valid = IftttBeanUtil.getTimeRangeJson(thisVo);
                        } else {
                            //设备触发条件
                            logic = thisVo.getLogic();
                            //获取trigger
                            triggerList.addAll(IftttBeanUtil.getDevTriggerList(thisVo));
                        }
                    }
                } else if (triggerType.equals("devEvent")) {
                    //设备事件
                    triggerList.addAll(IftttBeanUtil.getDevEventTriggerMap(thisVos));
                } else if (triggerType.equals("timer")) {
                    triggerList.add(IftttBeanUtil.getTimerTriggerMap(thisVos));
                } else if (triggerType.equals("sunset")) {
                    triggerList.add(IftttBeanUtil.getAstroTriggerMap(thisVos));
                } else if (triggerType.equals("sunrise")) {
                    triggerList.add(IftttBeanUtil.getAstroTriggerMap(thisVos));
                }
            }


            validMap = JSON.parseObject(valid, Map.class);
            ifMap.put("valid", validMap);
            ifMap.put("trigger", triggerList);

            //logic
            ifMap.put("logic", logic);

            //then
            List<AppletThatVo> thatVos = appletVo.getThatList();
            if (CollectionUtils.isNotEmpty(thatVos)) {
                AppletThatVo appletThatVo = thatVos.get(0);
                List<AppletItemVo> itemVos = appletThatVo.getItems();
                if (CollectionUtils.isNotEmpty(itemVos)) {
                    for (AppletItemVo vo : itemVos) {
                        //{
                        //tenantId:
                        //msg:{}
                        //}
                        String json = vo.getJson();
                        Map<String, Object> jsonMap = JSON.parseObject(json, Map.class);
                        String properties = jsonMap.get("msg").toString();
                        Map<String, Object> propertiesMap = JSON.parseObject(properties, Map.class);
                        thenList.add(propertiesMap);
                    }
                }
            }

            payload.put("then", thenList);
            payload.put("if", ifMap);
            payload.put("autoId", String.valueOf(autoId));
            payload.put("enableDelay", enableDelay);
            if (auto.getStatus() == 0) {
                //禁用
                payload.put("enable", false);
            } else {
                payload.put("enable", true);
            }
        } catch (Exception e) {
            logger.error("get auto cache error", e);
        }

        return payload;
    }

    @Override
    public void checkByDevice(String deviceId, Map<String, Object> attrMap) {
        logger.debug("收到设备触发联动检测,devId=" + deviceId + ",属性=" + attrMap.toString());
        try {
            //字段名称
            String field = "";
            for (String vo : attrMap.keySet()) {
                field = vo;
                break;
            }

            if (StringUtil.isEmpty(field)) {
                return;
            }
            CheckAppletReq checkAppletReq = new CheckAppletReq();
            checkAppletReq.setType(IftttTypeEnum.DEV_STATUS.getType());
            String msg = "{\"devId\":\"" + deviceId + "\",\"field\":\"" + field + "\"}";
            checkAppletReq.setMsg(msg);
            iftttApi.check(checkAppletReq);

            //ifttt.com 检测
            CheckNotifyReq checkNotifyReq = new CheckNotifyReq();
            checkNotifyReq.setType(ServiceEnum.DEVICE_STATUS.getCode());
            Map<String, Object> fields = Maps.newHashMap();
            fields.put("deviceId", deviceId);
            fields.put("attr", attrMap);
            checkNotifyReq.setFields(fields);
            iftttAccessApi.checkNotify(checkNotifyReq);
        } catch (Exception e) {
            logger.error("check device status error", e);
        }
    }

    @Override
    public void delByDeviceId(String devId, Long tenantId) {
        //删除和设备相关的item
        delItemByObject(AutoConstants.IFTTT_THEN_DEVICE, devId, tenantId);
    }

    @Override
    public void delByBatchDeviceIds(Long tenantId, List<String> devIds) {
        if (CollectionUtils.isEmpty(devIds)) {
            return;
        }
        //TODO 批量删除
        devIds.forEach(devId -> {
            this.delByDeviceId(devId, tenantId);
        });
    }

    @Override
    public void delBySceneId(Long sceneId, Long tenantId) {
        //删除和情景相关的item
        String sceneIdStr = String.valueOf(sceneId);
        delItemByObject(AutoConstants.IFTTT_THEN_SCENE, sceneIdStr, tenantId);
    }

    @Override
    public void delByDirectDeviceId(String devId, Long tenantId) {
        //删除直连设备相关的auto
        List<Long> autoIds = automationMapper.getByDirectId(devId);
        if (CollectionUtils.isNotEmpty(autoIds)) {
            for (Long id : autoIds) {
                this.delete(id);
            }
        }
    }

    @Override
    public void delByBatchDirectDeviceIds(Long tenantId, List<String> devIds) {
        if (CollectionUtils.isEmpty(devIds)) {
            return;
        }
        //TODO 批量删除
        devIds.forEach(devId -> {
            this.delByDirectDeviceId(devId, tenantId);
        });
    }

    @Override
    public Long getAutoIdByAppletId(Long appletId) {
        //TODO 缓存优化
        Map<String, Object> params = Maps.newHashMap();
        params.put("applet_id", appletId);
        List<Automation> list = automationMapper.selectByMap(params);
        Long autoId = null;
        if (CollectionUtils.isNotEmpty(list)) {
            autoId = list.get(0).getId();
        }
        return autoId;
    }

    @Override
    public void delBleAuto(SaveAutoReq req) {
        logger.debug("delBuleAuto req is {}", JSON.toJSONString(req));
        Long autoId = Long.valueOf(req.getId());
        Long tenantId = req.getTenantId();
        Long userId = req.getUserId();
        //检查auto与用户关系
        this.checkAutoRelation(tenantId, userId, autoId);
        //Ble跨直连设备，云端删除，直接响应APP
        this.delete(autoId);
    }

    @Override
    public Map<String, Object> getAutoDetail(SaveAutoReq req) {
        logger.debug("getAutoDetail req is {}", JSON.toJSONString(req));
        Long tenantId = req.getTenantId();
        Long userId = req.getUserId();
        Long autoId = Long.valueOf(req.getId());
        //检查auto与用户关系
        this.checkAutoRelation(tenantId, userId, autoId);
        return this.getPayloadById(autoId);
    }

    //目前只针对ble设备
    @Override
    public void addAutoRule(AddAutoRuleReq req) {
        logger.debug("addAutoRule req is {}", JSON.toJSONString(req));
        MqttMsg message = new MqttMsg();
        message.setPayload(req.getPayload());
//        MqttMsg res = null;

        //兼容网关设备，需要设置OnOff属性，在设置其他
        message = autoMQTTService.sortMsg(message);
//            res = message;

        //判断是否跨直连
        Map<String, Object> flagMap = autoMQTTService.checkMulti(message);
        Boolean flag = (Boolean) flagMap.get("flag");
        String deviceId = (String) flagMap.get("directId");

        //更新rule直连关系信息
        Long autoId = Long.valueOf(req.getAutoId());
        autoMQTTService.updateRule(flag, autoId, deviceId);

        //ble设备目前无论是否跨直连由设备自己执行
        this.saveAutoRule(message, false);
//        if (flag) {
        //若跨直连，则云端保存，直接返回app
//            this.saveAutoRule(message, false);
//                String userUuid = req.getUserUuid();
//                res = MqttKeyUtil.getAddAutoRuleRespMsg(res, userUuid);
//        } else {
        //不跨直连，则转发给网关
//                res = MqttKeyUtil.getAddAutoRuleReqMsg(res, deviceId);
//        }
    }

    @Override
    //目前auto暂不支持跨协议编辑
    public void editAutoRule(AddAutoRuleReq req) {
        logger.debug("editAutoRule req is {}", JSON.toJSONString(req));
        MqttMsg message = new MqttMsg();
        message.setPayload(req.getPayload());
        Long tenantId = req.getTenantId();
        Long userId = req.getUserId();
        MqttMsg res = null;

        //兼容网关设备，需要设置OnOff属性，在设置其他
        message = autoMQTTService.sortMsg(message);
        res = message;

        //判断是否跨直连
        Map<String, Object> flagMap = autoMQTTService.checkMulti(message);
        Boolean flag = (Boolean) flagMap.get("flag");
        String directId = (String) flagMap.get("directId");

        //更新rule直连关系信息
        Long autoId = Long.valueOf(req.getAutoId());

        // --------------------- 跨直连变化逻辑 -------------------------
        //编辑转新增标识
        Boolean addFlag = false;
        //旧rule数据
        Automation auto = this.getCache(autoId);
        //检查auto与用户关系
        this.checkAutoRelation(tenantId, userId, autoId);

        Byte isMulti = auto.getIsMulti().byteValue();
        logger.info("rule:" + autoId + ",跨直连判断：旧" + isMulti + ",新：" + flag);

        //直连->跨直连 删除网关ifttt规则
//        if (isMulti == AutoConstants.IFTTT_MULTI_FALSE && flag) {
//            MqttMsg delMsg = MqttKeyUtil.getDelAutoReqMsg(null, auto.getDirectId());
//            //payload
//            Map<String, Object> respPayload = new HashMap<>();
//            respPayload.put("autoId", String.valueOf(autoId));
//            delMsg.setPayload(respPayload);
//            delMsg.setSeq(req.getSeq());
//            autoMQTTService.sendMqttMsg(delMsg);
//            //添加缓存
//            String key = RedisKeyUtil.getIftttDelKey(autoId);
//            RedisCacheUtil.valueSet(key, "del", RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_1);
//            logger.debug("直连->跨直连");
//
//            //更新规则
//            this.saveAutoRule(message, true);
//            autoMQTTService.updateRule(flag, autoId, directId);
//            return;
//        }
//
//        //跨直连->直连 给网关下发新增ifttt请求
//        if (isMulti == AutoConstants.IFTTT_MULTI_TRUE && !flag) {
//            logger.debug("跨直连->直连");
//            addFlag = true;
//            //添加缓存
//            String key = RedisKeyUtil.getIftttEditKey(autoId);
//            RedisCacheUtil.valueSet(key, "edit", RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_1);
//        }
        // ----------------------- 跨直连变化逻辑 ----------------------

        autoMQTTService.updateRule(flag, autoId, directId);
        //ble设备目前无论是否跨直连由设备自己执行
        this.saveAutoRule(message, false);

//        if (flag) {
//            //云端保存
//            this.saveAutoRule(message, flag);
//            //应答给app
//            String uuid = ToolUtils.getClientId(topic);
//            res = MqttKeyUtil.getEditAutoRuleRespMsg(res, uuid);
//        } else {
//            //转发网关
//            if (addFlag) {
//                res = MqttKeyUtil.getAddAutoRuleReqMsg(res, directId);
//            } else {
//                res = MqttKeyUtil.getEditAutoRuleReqMsg(res, directId);
//            }
//        }
    }

    public void setAutoEnable(AddAutoRuleReq req) {
        logger.info("setAutoEnable req:{}", req);
        Long autoId = Long.valueOf(req.getAutoId());
        Long userId = req.getUserId();
        Long tenantId = req.getTenantId();
        MqttMsg message = new MqttMsg();
        message.setPayload(req.getPayload());
        Automation auto = this.getCache(autoId);

        //检查auto与用户关系
        this.checkAutoRelation(tenantId, userId, autoId);
        //判断是否跨直连
        int isMulti = auto.getIsMulti();
        String directId = auto.getDirectId();
        //ble设备目前无论是否跨直连由设备自己执行
        autoMQTTService.updateEnable(message, autoId);

//        if (isMulti == AutoConstants.IFTTT_MULTI_TRUE || StringUtils.isEmpty(directId)) {
//            //跨直连，云端更新状态
//            autoMQTTService.updateEnable(message, autoId);

//            String UserUuid = req.getUserUuid();
//            res = MqttKeyUtil.getSetAutoEnableRespMsg(res, UserUuid);
//        } else if (isMulti == AutoConstants.IFTTT_MULTI_FALSE) {
        //单直连且直连设备ID不为空，转发网关
//            res = MqttKeyUtil.getSetAutoEnableReqMsg(res, directId);
//        }
    }

    private void checkAutoRelation(Long tenantId, Long userId, Long autoId) {
        Automation auto = this.getCache(autoId);
        if (auto == null) {
            throw new BusinessException(AutoExceptionEnum.RULE_ID_IS_NULL);
        }
        //判断当前auto是否属于当前用户
        if (tenantId.compareTo(auto.getTenantId()) != 0) {
            throw new BusinessException(AutoExceptionEnum.RULE_IS_NULL);
        }
        if (userId.compareTo(auto.getUserId()) != 0) {
            throw new BusinessException(AutoExceptionEnum.RULE_IS_NULL);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 构建 automation.id 的列表
     *
     * @param list
     * @return
     */
    public List<Long> buildRuleIdList(List<Automation> list) {
        List<Long> idList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Automation vo : list) {
                idList.add(vo.getId());
            }
        }
        return idList;
    }

    /**
     * 获取数据缓存
     *
     * @param tempList
     * @return
     */
    private List<Automation> getlistCache(List<Long> tempList) {
        if (CollectionUtils.isEmpty(tempList)) {
            return null;
        }
        List<String> autoIdKeys = Lists.newArrayList();
        tempList.forEach(autoId -> {
            autoIdKeys.add(RedisKeyUtil.getIftttAutoKey(autoId));
        });

        //批量取缓存
        List<Automation> cacheList = RedisCacheUtil.mget(autoIdKeys, Automation.class);

        if (cacheList == null || cacheList.size() != tempList.size()) {
            //logger.debug("auto getlistCache(), 从缓存取出的 cacheList is null OR 数量不等于 dbIds(dbIds.size={}).", tempList.size());

            // 未命中id
            List<Long> unHitIdList = tempList;
            if (cacheList == null) {
                cacheList = Lists.newArrayList();
            } else {
                List<Long> hitIdList = Lists.newArrayList();
                cacheList.forEach(cacheBean -> {
                    hitIdList.add(cacheBean.getId());
                });
                //取差集
                unHitIdList.removeAll(hitIdList);
            }
            //数据库取
            List<Automation> autoList = automationMapper.listByIds(unHitIdList);
            //批量map
            Map<String, Object> multiSet = Maps.newHashMap();
            // 未命中 结果记录
            List<Automation> unHitAutoList = Lists.newArrayList();

            if (CollectionUtils.isNotEmpty(autoList)) {
                autoList.forEach(auto -> {
                    unHitAutoList.add(auto);
                    multiSet.put(RedisKeyUtil.getIftttAutoKey(auto.getId()), auto);
                });

                RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT_7);
                cacheList.addAll(unHitAutoList);
            }

        } else {
            logger.debug("auto getlistCache(), 直接使用从缓存查询出的 cacheList." + cacheList.size());
        }

        return cacheList;
    }

    /**
     * 删除子规则
     *
     * @param type
     * @param objectId
     */
    private void delItemByObject(String type, String objectId, Long tenantId) {
        try {
            List<AutomationItem> items = automationItemService.getItemByParam(type, objectId, tenantId);

            if (CollectionUtils.isEmpty(items)) {
                return;
            }

            for (AutomationItem item : items) {
                iftttApi.delItem(item.getItemId());
            }

            //批量删除
            automationItemService.delItemByParam(type, objectId, tenantId);
        } catch (Exception e) {
            logger.error("删除子项错误", e);
        }
    }

    /**
     * 更新item关联关系
     *
     * @param appletId
     */
    private void updateItemRelation(Long appletId, Long tenantId) {
        //先删除关系
        automationItemService.delItemByAppletId(appletId, tenantId);

        //新增子项关系
        List<AutomationItem> items = Lists.newArrayList();
        AppletVo appletVo = iftttApi.get(appletId);
        //this
        List<AppletThisVo> appletThisVos = appletVo.getThisList();
        if (CollectionUtils.isNotEmpty(appletThisVos)) {
            for (AppletThisVo appletThisVo : appletThisVos) {
                if (appletThisVo.getServiceCode().equals(IftttServiceEnum.DEV_STATUS.getCode())) {
                    List<AppletItemVo> appletItemVos = appletThisVo.getItems();
                    if (CollectionUtils.isNotEmpty(appletItemVos)) {
                        for (AppletItemVo vo : appletItemVos) {
                            AutomationItem item = new AutomationItem();
                            item.setAppletId(appletId);
                            item.setItemId(vo.getId());
                            String json = vo.getJson();
                            Map<String, Object> map = JSON.parseObject(json);
                            String devId = (String) map.get("devId");
                            item.setObjectId(devId);
                            item.setType(AutoConstants.IFTTT_THEN_DEVICE);
                            item.setTenantId(tenantId);
                            items.add(item);
                        }
                    }
                } else if (appletThisVo.getServiceCode().equals(IftttServiceEnum.DEV_EVENT.getCode())) {
                    List<AppletItemVo> appletItemVos = appletThisVo.getItems();
                    if (CollectionUtils.isNotEmpty(appletItemVos)) {
                        for (AppletItemVo vo : appletItemVos) {
                            AutomationItem item = new AutomationItem();
                            item.setAppletId(appletId);
                            item.setItemId(vo.getId());
                            String json = vo.getJson();
                            Map<String, Object> map = JSON.parseObject(json);
                            String devId = (String) map.get("devId");
                            item.setObjectId(devId);
                            item.setType(AutoConstants.IFTTT_THEN_DEVICE);
                            item.setTenantId(tenantId);
                            items.add(item);
                        }
                    }
                }
            }
        }

        //that
        List<AppletThatVo> appletThatVos = appletVo.getThatList();
        if (CollectionUtils.isNotEmpty(appletThatVos)) {
            for (AppletThatVo appletThatVo : appletThatVos) {
                if (appletThatVo.getServiceCode().equals(IftttServiceEnum.MQ.getCode())) {
                    List<AppletItemVo> appletItemVos = appletThatVo.getItems();
                    if (CollectionUtils.isNotEmpty(appletItemVos)) {
                        for (AppletItemVo vo : appletItemVos) {
                            AutomationItem item = new AutomationItem();
                            item.setAppletId(appletId);
                            item.setItemId(vo.getId());
                            String json = vo.getJson();
                            Map<String, Object> map = JSON.parseObject(json);
                            if (map.get("msg") == null) {
                                //logger.info("不正确的："+json);
                                continue;
                            }
                            Map<String, Object> msg = (Map<String, Object>) map.get("msg");
                            String thenType = (String) msg.get("thenType");
                            String objectId = String.valueOf(msg.get("id"));
                            item.setObjectId(objectId);
                            item.setType(thenType);
                            item.setTenantId(tenantId);
                            items.add(item);
                        }
                    }
                }
            }
        }

        //批量保存
        if (CollectionUtils.isNotEmpty(items)) {
            automationItemService.insertBatch(items);
        }
    }


    public AppletVo getAppletBean(MqttMsg message, Automation auto) {
        Map<String, Object> payload = (Map<String, Object>) message.getPayload();
        AppletVo appletVo = new AppletVo();
        if (auto.getStatus() == 0) {
            appletVo.setStatus(IftttStatusEnum.OFF.getCode());
        } else {
            appletVo.setStatus(IftttStatusEnum.ON.getCode());
        }
        appletVo.setName(auto.getName());
        appletVo.setCreateBy(auto.getUserId());

        //if
        if (payload.containsKey("if")) {
            Map<String, Object> ifMap = (Map<String, Object>) payload.get("if");
            appletVo = saveIf(ifMap, appletVo, auto.getUserId());
        }
        //then
        if (payload.containsKey("then")) {
            List<Map<String, Object>> thenList = (List<Map<String, Object>>) payload.get("then");
            if (CollectionUtils.isNotEmpty(thenList)) {
                appletVo = saveThen(thenList, appletVo, auto.getTenantId());
            }
        }
        return appletVo;
    }

    /**
     * 保存if
     *
     * @param ifMap
     * @param appletVo
     * @return
     */
    private AppletVo saveIf(Map<String, Object> ifMap, AppletVo appletVo, Long userId) {
        AppletVo res = appletVo;

        //this
        List<Map<String, Object>> triggerList = (List<Map<String, Object>>) ifMap.get("trigger");
        if (CollectionUtils.isNotEmpty(triggerList)) {
            List<AppletThisVo> appletThisVos = Lists.newArrayList();
            AppletThisVo appletThisVo = new AppletThisVo();
            List<AppletItemVo> itemVos = Lists.newArrayList();
            //时区值
            String area = deviceService.getAreaByUserId(userId);
            for (Map<String, Object> trigger : triggerList) {
                String trigType = String.valueOf(trigger.get("trigType"));
                if (AutoConstants.IFTTT_TRIGGER_TIMER.equals(trigType)) {
                    appletThisVo = IftttBeanUtil.getTimerThis(JSON.toJSONString(trigger), area);
                } else if (AutoConstants.IFTTT_TRIGGER_SUNRISE.equals(trigType)) {
                    appletThisVo = IftttBeanUtil.getAstroThis(JSON.toJSONString(trigger), area);
                } else if (AutoConstants.IFTTT_TRIGGER_SUNSET.equals(trigType)) {
                    appletThisVo = IftttBeanUtil.getAstroThis(JSON.toJSONString(trigger), area);
                } else {
                    AppletItemVo appletItemVo = IftttBeanUtil.getDevItem(JSON.toJSONString(trigger));
                    itemVos.add(appletItemVo);

                    if (trigger.get("event") != null) {
                        //事件
                        String logic = (String) ifMap.get("logic");
                        appletThisVo.setLogic(logic);
                        appletThisVo.setItems(itemVos);
                        appletThisVo.setServiceCode(IftttServiceEnum.DEV_EVENT.getCode());
                    } else {
                        //设备属性
                        Map<String, Object> valid = (Map<String, Object>) ifMap.get("valid");
                        String timing = JSON.toJSONString(valid);
                        appletThisVos.add(IftttBeanUtil.getTimeRangeThis(timing, area));

                        //dev
                        String logic = (String) ifMap.get("logic");
                        appletThisVo.setLogic(logic);
                        appletThisVo.setItems(itemVos);
                        appletThisVo.setServiceCode(IftttServiceEnum.DEV_STATUS.getCode());
                    }
                }
            }

            appletThisVos.add(appletThisVo);
            res.setThisList(appletThisVos);
        }
        return res;
    }

    /**
     * 保存then
     *
     * @param thenList
     * @param appletVo
     * @return
     */
    private AppletVo saveThen(List<Map<String, Object>> thenList, AppletVo appletVo, Long tenantId) {
        AppletVo res = appletVo;

        List<AppletThatVo> appletThatVos = IftttBeanUtil.getAppletThatByMap(thenList, tenantId);
        res.setThatList(appletThatVos);

        return res;
    }


    /**
     * 横向越权检测
     *
     * @param id
     */
    private void checkOverPower(Long id) {
        //横向越权处理
        Long userId = SaaSContextHolder.getCurrentUserId();
        Automation entity = automationMapper.selectById(id);
        Long realUserId = entity.getUserId();
        if (userId != realUserId) {
            logger.warn("lateral ultra vires!");
            //throw new BusinessException(IftttExceptionEnum.LATERAL_ULTRA_VIRES);
        }
    }

    /**
     * 获取timeJson
     *
     * @param payload
     * @return
     */
    private String getTimeJson(Map<String, Object> payload) {
        Map<String, Object> ifMap = (Map<String, Object>) payload.get("if");
        Map<String, Object> valid = (Map<String, Object>) ifMap.get("valid");
        List<Map<String, Object>> triggerList = (List<Map<String, Object>>) ifMap.get("trigger");

        String timeJson = "";
        if (CollectionUtils.isNotEmpty(triggerList)) {
            for (Map<String, Object> vo : triggerList) {
                String type = (String) vo.get("trigType");
                if ("dev".equals(type)) {
                    timeJson = JSON.toJSONString(valid);
                } else {
                    timeJson = JSON.toJSONString(vo);
                }
            }
        }

        return timeJson;
    }
}
