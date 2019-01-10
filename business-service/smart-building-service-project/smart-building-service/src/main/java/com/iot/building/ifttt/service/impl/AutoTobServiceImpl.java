package com.iot.building.ifttt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.building.device.api.DeviceBusinessTypeApi;
import com.iot.building.device.vo.DeviceBusinessTypeResp;
import com.iot.building.gateway.MultiProtocolGatewayHepler;
import com.iot.building.helper.Constants;
import com.iot.building.ifttt.entity.Relation;
import com.iot.building.ifttt.mapper.RelationTobMapper;
import com.iot.building.ifttt.vo.RelationVo;
import com.iot.building.scene.api.SceneControlApi;
import com.iot.building.scene.service.SceneService;
import com.iot.building.utils.ValueUtils;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneDetailReq;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.api.DeviceTypeApi;
import com.iot.device.api.ProductCoreApi;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import com.iot.ifttt.api.IftttApi;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.building.ifttt.entity.Rule;
import com.iot.building.ifttt.entity.Trigger;
import com.iot.building.ifttt.mapper.RuleTobMapper;
import com.iot.building.ifttt.mapper.TriggerTobMapper;
import com.iot.building.ifttt.service.IAutoTobService;
import com.iot.building.ifttt.util.RedisKeyUtil;
import com.iot.ifttt.common.IftttTypeEnum;
import com.iot.ifttt.vo.*;
import com.iot.redis.RedisCacheUtil;
import com.iot.saas.SaaSContextHolder;
import com.iot.user.api.UserApi;
import com.iot.building.ifttt.constant.IftttConstants;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.TriggerVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import springfox.documentation.swagger.web.UiConfiguration;

/**
 * 描述：联动服务实现类 ToB
 * 创建人： huangxu
 * 创建时间： 2018/10/12
 */
@Service
public class AutoTobServiceImpl implements IAutoTobService {

    private final Logger logger = LoggerFactory.getLogger(AutoTobServiceImpl.class);

    @Autowired
    private SceneService sceneService;
    @Autowired
    private RuleTobMapper ruleTobMapper;

    @Autowired
    private IftttApi iftttApi;//ifttt项目的服务

    @Autowired
    private TriggerTobMapper triggerTobMapper;

    @Autowired
    private UserApi userApi;

    @Autowired
    private RelationTobMapper relationTobMapper;

    @Autowired
    private SceneControlApi sceneControlApi;

    @Autowired
    private SceneApi sceneApi;

    @Autowired
    private DeviceBusinessTypeApi deviceBusinessTypeApi;

    @Autowired
    private DeviceCoreApi deviceCoreApi;

    @Autowired
    private SpaceDeviceApi spaceDeviceApi;

    @Autowired
    private DeviceTypeApi deviceTypeApi;
    /**
     * 设备上报判断触发
     * @param deviceId
     * @param attrMap
     */
    @Override
    public void checkByDevice(String deviceId, Map<String, Object> attrMap) {
        //字段名称
        String field = null;
        String msg = null;
        CheckAppletReq checkAppletReq = new CheckAppletReq();
        try{
            if(StringUtil.isEmpty(deviceId)){
                return;
            }
            if(MapUtils.isEmpty(attrMap)){
                return;
            }
            field = attrMap.toString();
            if(field.contains("button")){
                //墙控
                String s2 = "button";
                String recheck = "0";
                msg = "{\"devId\":\"" + deviceId + "\",\"field\":\"" + s2 + "\",\"recheck\":\""+recheck+"\"}";
                logger.info("==============msg================"+msg);
            }else {
                //String field = "{\"Door\":1}";
                Set<String> set = attrMap.keySet(); //取出所有的key值
                List<String> list = Lists.newArrayList();
                for (String key:set) {
                    list.add(key);
                }
                String s2 = list.get(0);
                //String s1 = field.replace("{","").replace("}","");
                //String s2 = s1.replace("\"","").split(":")[0];
                msg = "{\"devId\":\"" + deviceId + "\",\"field\":\"" + s2 + "\"}";
            }
            checkAppletReq.setType(IftttTypeEnum.DEV_STATUS.getType());
            checkAppletReq.setMsg(msg);
            iftttApi.check(checkAppletReq);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 保存/修改联动信息（sensor、actuator、or或and）
     * @param ruleVO
     * @return
     */
    @Override
    public Long saveAuto(SaveIftttReq ruleVO) {
        AppletVo appletVo = getDevAuto(ruleVO);
        Long appletId = iftttApi.save(appletVo);
        return appletId;
    }

    /**
     * 保存/修改ifttt记录（保存/修改build_tob_rule表）
     * @param ruleVO
     * @return
     */
    @Override
    public Long saveBuildTobRule(SaveIftttReq ruleVO) {
        SaaSContextHolder.setCurrentTenantId(ruleVO.getTenantId());
        String type = ruleVO.getType();
        Rule rule = getRule(ruleVO);
        if(StringUtil.isNotBlank(rule.getType()) && rule.getType().equals("template")){
            rule.setType(null);
            rule.setTemplateFlag(IftttConstants.IFTTT_TEMPLATE_FALSE);
            rule.setId(null);
        }
        logger.error("=======saveBuildTobRule====process==============");
        try {
            rule.setTenantId(getTenantId());

            if (rule.getId() != null && rule.getId() != 0) {
                //修改
                logger.error("=======修改===Build_Tob_Rule====start==============");
                rule.setTenantId(getTenantId());
                rule.setUpdateTime(new Date());
                ruleTobMapper.updateByPrimaryKeySelective(rule);
                logger.error("=======修改===Build_Tob_Rule====end==============");
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleKey(rule.getId()));
                logger.error("=======修改===Build_Tob_Rule====redis===end==============");
            } else {
                logger.error("=======新增===Build_Tob_Rule======start=============");
                rule.setTenantId(getTenantId());
                rule.setCreateTime(new Date());
                rule.setUpdateTime(new Date());
                if(StringUtil.isNotBlank(type) && type.equals("template")){
                    rule.setTemplateId(ruleVO.getId());
                }
                logger.error("=======新增===Build_Tob_Rule====end==============");
                ruleTobMapper.insertSelective(rule);
                logger.error("=======新增===Build_Tob_Rule====redis===end==============");
            }

            // 删除用户的ifttt-rule列表缓存
            RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleForListIdKey(rule.getId()));
        } catch (Exception e) {
            //保存Rule失败
            logger.error("save rule error", e);
            throw new BusinessException(IftttExceptionEnum.SAVE_RULE_ERROR, e);
        }
        return rule.getId();
    }

    @Override
    public List<RuleResp> getRuleList(SaveIftttReq ruleVO) {
        List<RuleResp> list = ruleTobMapper.getRuleList(ruleVO);
        return list;
    }

    /**
     * 通过名字查找build_tob_rule集合
     * @param ruleVO
     * @return
     */
    @Override
    public List<RuleResp> getRuleListByName(SaveIftttReq ruleVO) {
        List<RuleResp> list = ruleTobMapper.getRuleListByName(ruleVO);
        return list;
    }

    /**
     * 保存点、线，表为tob_trigger
     * @param ruleVO
     * @return
     */
    @Override
    public Long saveTobTrigger(SaveIftttReq ruleVO) {
        List<TriggerVo> triggerVos = ruleVO.getTriggers();
        List<SensorVo> sensorVos = ruleVO.getSensors();
        List<ActuatorVo> actuatorVos = ruleVO.getActuators();
        SensorVo sensorVo = new SensorVo();
        Long spaceId = ruleVO.getSpaceId();
        logger.info("===tobTrigger==tempalte=======spaceId====="+spaceId);
        ActuatorVo actuatorVo = new ActuatorVo();
        if(StringUtil.isNotBlank(ruleVO.getType()) && ruleVO.getType().equals("template")){
            logger.info("===tobTrigger==tempalte============");
            //ifttt模板
            // List<SensorVo> sensorVoStrList = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(triggerVos)) {
                //for (TriggerVo triggerVo : triggerVos) {
                for (SensorVo sensorVoStr : sensorVos) {
                    //通过deviceBusinessId 和spaceId 查询 deviceId
                    logger.info("===tobTrigger==tempalte=======sensor=====1");
                    SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                    spaceDeviceReq.setBusinessTypeId(Long.valueOf(sensorVoStr.getDeviceId()));
                    spaceDeviceReq.setSpaceId(spaceId);
                    spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                    List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                    for (int i =0;i<spaceDeviceRespList.size();i++) {
                        TriggerVo triggerVoOfSensor = new TriggerVo();
                        logger.info("===tobTrigger==tempalte=======sensor=====2");
                        SensorVo sensorVoCs = null;
                        GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(spaceDeviceRespList.get(i).getDeviceId());
                        String sensortType = deviceTypeApi.getDeviceTypeById(Long.valueOf(getDeviceInfoRespVo.getDeviceTypeId())).getType();
                        logger.info("===tobTrigger==tempalte=======sensortType=====" + sensortType);
                        String name = deviceCoreApi.get(spaceDeviceRespList.get(i).getDeviceId()).getName();
                        logger.info("===tobTrigger==tempalte=======name=====" + name);
                        sensorVoCs = new SensorVo(name, getDeviceInfoRespVo.getUuid(), sensortType, getDeviceInfoRespVo.getProductId(), sensorVoStr.getProperties());
                        triggerVoOfSensor.setSourceLabel(name);
                        triggerVoOfSensor.setDestinationLabel("OR");
                        Long index = Long.valueOf((i+1)+"");
                        triggerVoOfSensor.setEnd(Constants.OR_TOP);
                        //Long newStart = triggerVo.getStart()+index;
                        Long newStart = sensorVoStr.getPosition()[2]+index;
                        triggerVoOfSensor.setStart(newStart);
                        Long[] sensorPositon = new Long[]{sensorVoStr.getPosition()[0],sensorVoStr.getPosition()[1],newStart};
                        triggerVoOfSensor.setSensorPosition(ValueUtils.join(sensorPositon, ","));
                        triggerVoOfSensor.setSensorType(sensorVoCs.getType());
                        triggerVoOfSensor.setSensorProperties(sensorVoStr.getProperties());
                        triggerVoOfSensor.setSensorDeviceId(sensorVoCs.getDeviceId());
                        triggerVoOfSensor.setActuctorPosition(ValueUtils.join(Constants.OR, ","));
                        triggerVoOfSensor.setActuctorType("OR");
                        triggerVoOfSensor.setActuctorProperties(null);
                        triggerVoOfSensor.setActuctorDeviceId(null);
                        Trigger trigger = getTrigger(triggerVoOfSensor);
                        trigger.setId(null);
                        trigger.setRuleId(ruleVO.getId());
                        trigger.setTenantId(ruleVO.getTenantId());
                        int  res = triggerTobMapper.insertSelective(trigger);
                        if (res == 0) {
                            throw new BusinessException(IftttExceptionEnum.SAVE_TRIGGER_FAILED);
                        }
                    }
                }
                for (ActuatorVo actuatorVoStr : actuatorVos) {
                    if (actuatorVoStr.getType().equals("sence")) {
                        TriggerVo triggerVoOfActuator = new TriggerVo();
                        logger.info("===tobTrigger==tempalte=======actuator为sence的情况=====1");
                        triggerVoOfActuator.setSourceLabel("OR");
                        triggerVoOfActuator.setDestinationLabel(actuatorVoStr.getName());
                        triggerVoOfActuator.setStart(Constants.OR_TOP);
                        //triggerVoOfActuator.setEnd(triggerVo.getEnd());
                        triggerVoOfActuator.setEnd(actuatorVoStr.getPosition()[2]);
                        triggerVoOfActuator.setSensorPosition(ValueUtils.join(Constants.OR, ","));
                        triggerVoOfActuator.setSensorType("OR");
                        triggerVoOfActuator.setSensorProperties(null);
                        triggerVoOfActuator.setSensorDeviceId(null);
                        triggerVoOfActuator.setActuctorPosition(ValueUtils.join(actuatorVoStr.getPosition(), ","));
                        triggerVoOfActuator.setActuctorType(actuatorVoStr.getType());
                        triggerVoOfActuator.setActuctorProperties(actuatorVoStr.getProperties());
                        triggerVoOfActuator.setActuctorDeviceId(actuatorVoStr.getDeviceId());
                        Trigger trigger = getTrigger(triggerVoOfActuator);
                        trigger.setId(null);
                        trigger.setRuleId(ruleVO.getId());
                        trigger.setTenantId(ruleVO.getTenantId());
                        int  res = triggerTobMapper.insertSelective(trigger);
                        if (res == 0) {
                            throw new BusinessException(IftttExceptionEnum.SAVE_TRIGGER_FAILED);
                        }
                    } else {
                        //通过deviceBusinessId 和spaceId 查询 deviceId
                        logger.info("===tobTrigger==tempalte=======actuator为业务类型=====1");
                        SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                        spaceDeviceReq.setBusinessTypeId(Long.valueOf(actuatorVoStr.getDeviceId()));
                        spaceDeviceReq.setSpaceId(spaceId);
                        spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                        List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                        for (int i=0;i<spaceDeviceRespList.size();i++) {
                            TriggerVo triggerVoOfActuator = new TriggerVo();
                            logger.info("===tobTrigger==tempalte=======actuator为业务类型=====2");
                            ActuatorVo actuatorVoCs = null;
                            GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(spaceDeviceRespList.get(i).getDeviceId());
                            String actuatorType = deviceTypeApi.getDeviceTypeById(Long.valueOf(getDeviceInfoRespVo.getDeviceTypeId())).getType();
                            logger.info("===tobTrigger==tempalte=======actuatorType=====" + actuatorType);
                            String name = deviceCoreApi.get(spaceDeviceRespList.get(i).getDeviceId()).getName();
                            logger.info("===tobTrigger==tempalte=======name=====" + name);
                            actuatorVoCs = new ActuatorVo(name, getDeviceInfoRespVo.getUuid(), actuatorType, getDeviceInfoRespVo.getProductId(), actuatorVoStr.getProperties());
                            triggerVoOfActuator.setSourceLabel("OR");
                            triggerVoOfActuator.setDestinationLabel(name);
                            Long index = Long.valueOf((i+1)+"");
                            //Long newEnd = triggerVo.getEnd() + index;
                            Long newEnd = actuatorVoStr.getPosition()[2] + index;
                            triggerVoOfActuator.setStart(Constants.OR_TOP);
                            triggerVoOfActuator.setEnd(newEnd);
                            Long[] actuatorPositon = new Long[]{actuatorVoStr.getPosition()[0],actuatorVoStr.getPosition()[1],newEnd};
                            triggerVoOfActuator.setSensorPosition(ValueUtils.join(Constants.OR, ","));
                            triggerVoOfActuator.setSensorType("OR");
                            triggerVoOfActuator.setSensorProperties(null);
                            triggerVoOfActuator.setSensorDeviceId(null);
                            triggerVoOfActuator.setActuctorPosition(ValueUtils.join(actuatorPositon, ","));
                            triggerVoOfActuator.setActuctorType(actuatorVoCs.getType());
                            triggerVoOfActuator.setActuctorProperties(actuatorVoStr.getProperties());
                            triggerVoOfActuator.setActuctorDeviceId(actuatorVoCs.getDeviceId());
                            Trigger trigger = getTrigger(triggerVoOfActuator);
                            trigger.setId(null);
                            trigger.setRuleId(ruleVO.getId());
                            trigger.setTenantId(ruleVO.getTenantId());
                            int  res = triggerTobMapper.insertSelective(trigger);
                            if (res == 0) {
                                throw new BusinessException(IftttExceptionEnum.SAVE_TRIGGER_FAILED);
                            }
                        }
                    }
                }
                //}
            }
        }else {
            if (CollectionUtils.isNotEmpty(triggerVos)) {
                for (TriggerVo triggerVo : triggerVos) {
                    //获取sensor的名字
                    String sensorName = triggerVo.getSourceLabel();
                    logger.info("===sensorName===="+sensorName);
                    //模板生成的ifttt，sensor为or的情况
                    if(sensorName.equals("OR") || sensorName.equals("AND")){
                        if(sensorName.equals("OR")){
                            sensorVo.setType("OR");
                        }else if(sensorName.equals("AND")){
                            sensorVo.setType("AND");
                        }
                        //Long[] sensorPositon = triggerVo.getPosition();
                        String[] cc = triggerVo.getSensorPosition().split(",");
                        Long[] sensorPositon = new Long[cc.length];
                        for(int i=0;i<cc.length;i++){
                            sensorPositon[i] = Long.valueOf(cc[i]);
                        }
                        sensorVo.setPosition(sensorPositon);
                        logger.info("=======triggerVo.getPosition()======="+sensorPositon);
                        sensorVo.setProperties(null);
                        sensorVo.setDeviceId(null);
                    }
                    for (SensorVo sensorVoStr : sensorVos) {
                        //获取相应的sensor，普通ifttt与模板生成的sensor不为or的情况
                        if (sensorName.equals(sensorVoStr.getName())) {
                            sensorVo = sensorVoStr;
                            logger.info("=======sensorVo.getProperties()======="+sensorVo.getPosition());
                            logger.info("=======sensorVo.getDeviceId()======="+sensorVo.getDeviceId());
                            continue;
                        }
                    }
                    //获取actuctor的名字
                    String actuatorName = triggerVo.getDestinationLabel();
                    logger.info("===actuatorName===="+actuatorName);
                    ///模板生成的ifttt，actuator为or的情况
                    if(actuatorName.equals("OR") || actuatorName.equals("AND")){
                        if(actuatorName.equals("OR")){
                            actuatorVo.setType("OR");
                        }else if(actuatorName.equals("AND")){
                            actuatorVo.setType("AND");
                        }
                        String[] cc = triggerVo.getActuctorPosition().split(",");
                        Long[] actuctorPositon = new Long[cc.length];
                        for(int i=0;i<cc.length;i++){
                            actuctorPositon[i] = Long.valueOf(cc[i]);
                        }
                        logger.info("=======triggerVo.getProperties()======="+actuctorPositon);
                        actuatorVo.setPosition(actuctorPositon);
                        actuatorVo.setProperties(null);
                        actuatorVo.setDeviceId(null);
                    }
                    for (ActuatorVo actuatorVoStr : actuatorVos) {
                        //获取相应的actuctor,普通ifttt与模板生成的actuctor不为or的情况
                        if (actuatorName.equals(actuatorVoStr.getName())) {
                            actuatorVo = actuatorVoStr;
                            logger.info("=======actuatorVo.getProperties()======="+actuatorVo.getPosition());
                            logger.info("=======actuatorVo.getDeviceId()======="+actuatorVo.getDeviceId());
                            continue;
                        }
                    }
                    logger.info("=======sensorVo.getProperties()======="+sensorVo.getPosition());
                    logger.info("=======actuatorVo.getProperties()======="+actuatorVo.getPosition());
                    //设置 Trigger 中的sensor，actuctor的属性
                    triggerVo.setSensorPosition(ValueUtils.join(sensorVo.getPosition(), ","));
                    triggerVo.setSensorType(sensorVo.getType());
                    triggerVo.setSensorProperties(sensorVo.getProperties());
                    triggerVo.setSensorDeviceId(sensorVo.getDeviceId());
                    triggerVo.setActuctorPosition(ValueUtils.join(actuatorVo.getPosition(), ","));
                    triggerVo.setActuctorType(actuatorVo.getType());
                    triggerVo.setActuctorProperties(actuatorVo.getProperties());
                    triggerVo.setActuctorDeviceId(actuatorVo.getDeviceId());
                    Trigger trigger = getTrigger(triggerVo);
                    trigger.setId(null);
                    trigger.setRuleId(ruleVO.getId());
                    trigger.setTenantId(ruleVO.getTenantId());
                    int  res = triggerTobMapper.insertSelective(trigger);
                    if (res == 0) {
                        throw new BusinessException(IftttExceptionEnum.SAVE_TRIGGER_FAILED);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 删除ifttt
     * @param id
     * @return
     */
    @Override
    public List<Integer> delete(Long tenantId,Long orgId,Long id,boolean flag) {
        logger.info("=== receive delete ifttt request ===" + id);
        List<Integer> idxList = new ArrayList<>();
        try {
            //获取单个 build_tob_rule 的详情
            RuleResp ruleResp = get(tenantId,orgId,id);
            SaaSContextHolder.setCurrentTenantId(tenantId);
            Long appletId = ruleResp.getAppletId();
            GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(ruleResp.getTriggers().get(0).getSensorDeviceId());
            String clientId = getDeviceInfoRespVo.getParentId();
            //删除 点、线，表为tob_trigger
            triggerTobMapper.deleteByRuleId(id, tenantId);
            RedisCacheUtil.delete(RedisKeyUtil.getIftttTriggerKey(id));
            logger.debug("delete rule's triggers success");

            //删除relation
            relationTobMapper.deleteByRuleId(id,tenantId);
            RedisCacheUtil.delete(RedisKeyUtil.getIftttRelationKey(id));
            logger.debug("delete rule's relations success");

            if(flag){
                //删除 联动信息（sensor、actuator、or或and）通过appletId
                iftttApi.delete(appletId);
                logger.debug("delete====联动信息（sensor、actuator、or或and)====success");
                //删除ifttt记录 （build_tob_rule表）
                ruleTobMapper.deleteByPrimaryKey(id, tenantId);
                logger.debug("删除ifttt记录====success");
                //删除rule对象缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleKey(id));
                logger.debug("删除rule对象缓存====success");
                //删除rule-user关系缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttUserKey(id));
                logger.debug("删除rule-user关系缓存====success");
                // 删除用户的ifttt-rule列表缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleForListIdKey(id));
                logger.debug("删除用户的ifttt-rule列表缓存====success");
            }
            if(StringUtil.isNotBlank(ruleResp.getUploadStatus()) && ruleResp.getUploadStatus().equals("1")){
                //同一个网关，删除网关下的ifttt
                deleteLowerHair(id,clientId);
            }

        }catch (Exception e) {
            //删除IFTTT规则失败
            logger.error("delete ifttt error", e);
            throw new BusinessException(IftttExceptionEnum.DELETE_IFTTT_ERROR, e);
        }
        return idxList;
    }


    @Override
    public void deleteAll(Long orgId,Long appletId, Long buildToRuleId, Long tenantId) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        try {
            if(appletId !=null){
                //删除 联动信息（sensor、actuator、or或and）通过appletId
                iftttApi.delete(appletId);
                logger.debug("delete====联动信息（sensor、actuator、or或and)====success");
            }
            if(buildToRuleId !=null){
                //删除ifttt记录 （build_tob_rule表）
                ruleTobMapper.deleteByPrimaryKey(buildToRuleId, tenantId);
                logger.debug("删除ifttt记录====success");
                //删除 点、线，表为tob_trigger
                triggerTobMapper.deleteByRuleId(buildToRuleId, tenantId);
                RedisCacheUtil.delete(RedisKeyUtil.getIftttTriggerKey(buildToRuleId));
                logger.debug("delete rule's triggers success");

                //删除relation
                relationTobMapper.deleteByRuleId(buildToRuleId,tenantId);
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRelationKey(buildToRuleId));
                logger.debug("delete rule's relations success");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 列表
     * @param req
     * @return
     */
    @Override
    public Page<RuleResp> list(RuleListReq req) {
        logger.info("=== receive rule list request ===" + req.toString());
        Page<RuleResp> page = new Page<RuleResp>();
        try{
            Integer pageNum = req.getPageNum();
            Integer pageSize = req.getPageSize();
            if (pageNum == null) {
                pageNum = 1;
            }
            if (pageSize == null) {
                pageSize = 100;
            }

            // 目标页记录 列表
            List<RuleResp> resultList = Lists.newArrayList();

            // 目标页记录 的id列表
            com.github.pagehelper.Page<Rule> page1 = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
            List<Rule> tempList = ruleTobMapper.findSimpleList(req);
            if (CollectionUtils.isNotEmpty(tempList)) {
                logger.debug("list(), 从数据库查询出的 tempList.size()={}", tempList.size());
                if (SaaSContextHolder.currentContextMap().get(SaaSContextHolder.KEY_TENANT_ID) == null) {
                    logger.debug("set SaaSContextHolder tenantId is {}.", tempList.get(0).getTenantId());
                    SaaSContextHolder.setCurrentTenantId(tempList.get(0).getTenantId());
                }
                List<Long> ruleIdList = buildRuleIdList(tempList);
                List<Rule> ruleList = listRuleRespByIds(ruleIdList);
                //LOGGER.debug("list(), 从缓存中查询出的 cacheList={}", ruleList.toString());

                if (CollectionUtils.isNotEmpty(ruleList)) {
                    for(Rule rule:ruleList){
                        RuleResp resp = getRuleResp(rule);
                        //RuleResp resp = fillRuleRespOtherInfo(getRuleResp(rule),req.getShowTime());

                        if (rule.getUserId() != null && userApi.getUser(rule.getUserId()) != null) {
                            resp.setUserName(userApi.getUser(rule.getUserId()).getUserName());
                        }
                        resultList.add(resp);
                    }

                    // 排序
                    Collections.sort(resultList, new Comparator<RuleResp>() {
                        @Override
                        public int compare(RuleResp b1, RuleResp b2) {
                            //return b2.getCreateTime().compareTo(b1.getCreateTime());
                            return Collator.getInstance(Locale.CHINESE).compare(b1.getName(),b2.getName());
                        }
                    });
                    //LOGGER.debug("list(), 排序后数据 resultList={}", resultList.toString());
                }
            } else {
                logger.info("list(), 从数据库查询出的 tempList is empty.");
            }

            page.setResult(resultList);
            page.setTotal(page1.getTotal());
        }catch (Exception e) {
            e.printStackTrace();
            //分页获取IFTTT规则失败
            logger.error("get ifttt list error", e);
            throw new BusinessException(IftttExceptionEnum.GET_IFTTT_LIST_ERROR, e);
        }
        return page;
    }

    /**
     * 启用或停止联动监听
     * @param id
     * @param start
     * @return
     */
    @Override
    public boolean run(Long id, Boolean start) {
        logger.info("=== receive update rule status request ===" + "{" + id + "，" + start + "}");
        try {
            Rule rule = getCache(id);
            if (rule == null) {
                throw new BusinessException(IftttExceptionEnum.RULE_IS_NULL);
            }
            Byte status = start ? IftttConstants.RUNNING : IftttConstants.STOP;
            if (status != rule.getStatus()) {
                rule.setStatus(status);
                int result = ruleTobMapper.updateByPrimaryKeySelective(rule);
                if (result != 1) {
                    throw new BusinessException(IftttExceptionEnum.UPDATE_RULE_STATUS_FAILED);
                }
                //清除缓存 下次从数据库取最新数据
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleKey(id));

                // 删除用户的ifttt-rule列表缓存
                RedisCacheUtil.delete(RedisKeyUtil.getIftttRuleForListIdKey(id));
            }
        } catch (Exception e) {
            //修改规则执行状态
            logger.error("update rule status error", e);
            throw new BusinessException(IftttExceptionEnum.UPDATE_RULE_STATUS_FAILED, e);
        }
        return true;
    }

    public String getParentLabels(List<String> sensorNameList){
        String ss = "";
        for(int i=0;i< sensorNameList.size();i++){
            if(i==0){
                if(sensorNameList.size()-1==0){
                    ss +="["+ "\"" +sensorNameList.get(i)+ "\"";
                }else {
                    ss +="["+ "\"" +sensorNameList.get(i)+ "\"" + ",";
                }
            }else if(i==sensorNameList.size()-1){
                ss+= "\"" +sensorNameList.get(i)+ "\"" + "]";
            }else {
                ss+= "\"" +sensorNameList.get(i)+ "\"" + ",";
            }
        }
        return ss;
    }
    /**
     * 保存relation
     * @param ruleVO
     * @return
     */

    @Override
    public void saveTobRelation(SaveIftttReq ruleVO) {
        List<RelationVo> relationVos = ruleVO.getRelations();
        Long spaceId = ruleVO.getSpaceId();
        List<String> sensorNameList = Lists.newArrayList();
        if(relationVos !=null && relationVos.size() ==0){
            if(StringUtil.isNotBlank(ruleVO.getType())){
                if(ruleVO.getType().equals("template")){
                    //为ifttt模板
                    for(SensorVo sensorVoStr : ruleVO.getSensors()){
                        //通过deviceBusinessId 和spaceId 查询 deviceId
                        logger.info("===tobTrigger==tempalte=======sensor=====1");
                        SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                        spaceDeviceReq.setBusinessTypeId(Long.valueOf(sensorVoStr.getDeviceId()));
                        spaceDeviceReq.setSpaceId(spaceId);
                        spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                        List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                        for(SpaceDeviceResp spaceDeviceResp : spaceDeviceRespList){
                            GetDeviceInfoRespVo getDeviceInfoRespVo = deviceCoreApi.get(spaceDeviceResp.getDeviceId());
                            sensorNameList.add(getDeviceInfoRespVo.getName());
                        }
                    }
                    Relation relation = new Relation();
                    relation.setLabel("OR");
                    relation.setType("OR");
                    relation.setPosition("460,2,1542251540000");
                    //String s = "[\"四合一_4\", \"四合一_7\"]";
                    String parentLabels = getParentLabels(sensorNameList);
                    relation.setParentLabels(parentLabels);
                    relation.setRuleId(ruleVO.getId());
                    relation.setTenantId(ruleVO.getTenantId());
                    int res = relationTobMapper.insertSelective(relation);
                    if (res == 0) {
                        throw new BusinessException(IftttExceptionEnum.SAVE_RELATION_FAILED);
                    }
                }
            }
        }else {
                    //普通的ifttt
                    for (RelationVo relationVo : relationVos) {
                            Relation relation = getRelation(relationVo);
                            relation.setRuleId(ruleVO.getId());
                            relation.setTenantId(ruleVO.getTenantId());
                            int res = relationTobMapper.insertSelective(relation);
                            if (res == 0) {
                                throw new BusinessException(IftttExceptionEnum.SAVE_RELATION_FAILED);
                            }
                    }
        }
    }

    public String setButton(SensorVo sensorVo){
        String properties = null;
        Map<String,Object> map = Maps.newHashMap();
        JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
        String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
        String propertyType = String.valueOf(((JSONObject) jsonArray.get(0)).get("propertyType"));
        String triggerSign = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerSign"));
        if(!triggerValue.contains("[") || !triggerValue.contains("]")){
            triggerValue = "["+triggerValue+"]";
        }
        properties = "[{\"propertyName\":\"button\",\"triggerValue\":\""+ triggerValue +"\",\"propertyType\":\""+propertyType+"\",\"triggerSign\":\""+triggerSign+"\"}]";
        return properties;
    }

    /**
     * 下发的
     * @param ruleVO
     */
    @Override
    public void saveLowerHair(SaveIftttReq ruleVO) {
        //判断是同网关还是跨网关
        boolean isTemplateFlag = false;
        if(StringUtil.isNotBlank(ruleVO.getType()) && ruleVO.getType().equals("template")){
            logger.info("=======isTemplateFlag============="+isTemplateFlag);
            isTemplateFlag = true;
        }
        List<String> deviceIds = Lists.newArrayList();
        List<Long> sceneIds = Lists.newArrayList();
        boolean isSingleGatewayFlag = false;
        if(isTemplateFlag) {//模板生成具体的ifttt
            //ifttt模板:设备类型----情景
            List<SensorVo> sensorSingle = Lists.newArrayList();
            for(SensorVo sensorVo : ruleVO.getSensors()){
                //为button类型
                if(sensorVo.getProperties().contains("button")) {
                    String propertiess = setButton(sensorVo);
                    sensorVo.setProperties(propertiess);
                }
                Long deviceBusinessId = Long.valueOf(sensorVo.getDeviceId());
                SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                spaceDeviceReq.setBusinessTypeId(Long.valueOf(deviceBusinessId));
                spaceDeviceReq.setSpaceId(ruleVO.getSpaceId());
                spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                for(SpaceDeviceResp listDeviceByParamsRespVo : spaceDeviceRespList){
                    SensorVo sensorStr = new SensorVo();
                    sensorStr.setDeviceId(listDeviceByParamsRespVo.getDeviceId());
                    sensorStr.setProperties(sensorVo.getProperties());
                    logger.info("====Sensor=====DeviceTypeId=========="+listDeviceByParamsRespVo.getDeviceTypeId());
                    String type = deviceTypeApi.getDeviceTypeById(listDeviceByParamsRespVo.getDeviceTypeId()).getType();
                    logger.info("====Sensor=====type=========="+type);
                    sensorStr.setType(type);
                    sensorSingle.add(sensorStr);
                    deviceIds.add(listDeviceByParamsRespVo.getDeviceId());
                }
            }
            ruleVO.setSensorSingle(sensorSingle);
            List<ActuatorVo> actuatorSingle = Lists.newArrayList();
            for (ActuatorVo actuatorVo : ruleVO.getActuators()) {
                if(StringUtil.isNotBlank(actuatorVo.getType())){
                    if(actuatorVo.getType().equals("sence")){//ifttt模板:设备类型----情景
                        //通过templateId 和 spaceId查找 sceneId
                        ActuatorVo actuatorStr = new ActuatorVo();
                        SceneReq sceneReq = new SceneReq();
                        sceneReq.setSpaceId(ruleVO.getSpaceId());
                        sceneReq.setTemplateId(Long.valueOf(actuatorVo.getDeviceId()));
                        List<SceneResp> list = sceneApi.sceneByParamDoing(sceneReq);
                        if(CollectionUtils.isNotEmpty(list)){
                            Long sceneId = list.get(0).getId();
                            sceneIds.add(sceneId);
                            actuatorStr.setDeviceId(sceneId+"");
                            actuatorStr.setType("sence");
                            actuatorSingle.add(actuatorStr);
                            actuatorVo.setType("sence");
                            actuatorVo.setDeviceId(sceneId+"");
                        }
                    }else {//ifttt模板:设备类型----设备类型
                        Long deviceBusinessId = Long.valueOf(actuatorVo.getDeviceId());
                        SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                        spaceDeviceReq.setBusinessTypeId(Long.valueOf(deviceBusinessId));
                        spaceDeviceReq.setSpaceId(ruleVO.getSpaceId());
                        spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                        List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                        for(SpaceDeviceResp listDeviceByParamsRespVo : spaceDeviceRespList){
                            ActuatorVo actuatorStr = new ActuatorVo();
                            actuatorStr.setDeviceId(listDeviceByParamsRespVo.getDeviceId());
                            actuatorStr.setProperties(actuatorVo.getProperties());
                            logger.info("===Actuator======DeviceTypeId=========="+listDeviceByParamsRespVo.getDeviceTypeId());
                            String type = deviceTypeApi.getDeviceTypeById(listDeviceByParamsRespVo.getDeviceTypeId()).getType();
                            logger.info("===Actuator======type=========="+type);
                            actuatorStr.setType(type);
                            actuatorSingle.add(actuatorStr);
                            deviceIds.add(listDeviceByParamsRespVo.getDeviceId());
                        }
                    }
                }
            }
            ruleVO.setActuatorSingle(actuatorSingle);
            isSingleGatewayFlag = sceneService.isSingleGateway(deviceIds,sceneIds,ruleVO.getTenantId());
        }
        if(isSingleGatewayFlag){//单网关
            ruleVO.setUploadStatus("1");
            //第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
            Long appletId = saveAuto(ruleVO);
            //第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
            ruleVO.setAppletId(appletId);
            Long buildToRuleId = saveBuildTobRule(ruleVO);
            //第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
            ruleVO.setId(buildToRuleId);
            saveTobTrigger(ruleVO);
            //第四步保存tob_relation,相当于之前的ifttt_relation
            //if(ruleVO.getRelations() !=null && ruleVO.getRelations().size() !=0){
            saveTobRelation(ruleVO);

            //获取网关id
            String client = getClientId(ruleVO);
            //创建下发给网关
            ruleVO.setClientId(client);
            ruleVO.setId(buildToRuleId);
            createLowerHair(ruleVO);
        }else {
            ruleVO.setUploadStatus("0");//跨网关
            //第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
            Long appletId = saveAuto(ruleVO);
            //第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
            ruleVO.setAppletId(appletId);
            Long buildToRuleId = saveBuildTobRule(ruleVO);
            //第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
            ruleVO.setId(buildToRuleId);
            saveTobTrigger(ruleVO);
            //第四步保存tob_relation,相当于之前的ifttt_relation
            //if(ruleVO.getRelations() !=null && ruleVO.getRelations().size() !=0){
            saveTobRelation(ruleVO);
        }
    }

    public String getClientId(SaveIftttReq ruleVO){
        String clientId = null;
        if(StringUtil.isNotBlank(ruleVO.getActuatorSingle().get(0).getType())){
            if(ruleVO.getActuatorSingle().get(0).getType().equals("sence")){
                SceneDetailReq sceneDetailReq = new SceneDetailReq();
                sceneDetailReq.setSceneId(Long.valueOf(ruleVO.getActuatorSingle().get(0).getDeviceId()));
                sceneDetailReq.setTenantId(ruleVO.getTenantId());
                sceneDetailReq.setSpaceId(ruleVO.getSpaceId());
                String deviceId = sceneApi.sceneDetailByParam(sceneDetailReq).get(0).getDeviceId();
                clientId =  deviceCoreApi.get(deviceId).getParentId();
            }else {
                clientId =  deviceCoreApi.get(ruleVO.getActuatorSingle().get(0).getDeviceId()).getParentId();
            }
        }else {
            clientId = deviceCoreApi.get(ruleVO.getActuatorSingle().get(0).getDeviceId()).getParentId();
        }
        return clientId;
    }

    @Override
    public List<TriggerVo> getTriggerTobListByDeviceId(SaveIftttReq ruleVO) {
        List<TriggerVo> list = triggerTobMapper.getTriggerTobListByDeviceId(ruleVO);
        return list;
    }

    /**
     * 创建下发到网关
     * @param ruleVO
     */
    @Override
    public void createLowerHair(SaveIftttReq ruleVO) {
        MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
        try {
            multiProtocolGatewayHepler.createIfttt(ruleVO.getClientId(),ruleVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除下发到网关
     * @param id
     */
    @Override
    public void deleteLowerHair(Long id,String clientId) {
        MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
        try {
            multiProtocolGatewayHepler.deleteIfttt(clientId,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网关开启/关闭联动
     * @param id
     * @param clientId
     * @param start
     */
    @Override
    public void runLowerHair(Long id, String clientId, Boolean start) {
        MultiProtocolGatewayHepler multiProtocolGatewayHepler = new MultiProtocolGatewayHepler();
        try {
            multiProtocolGatewayHepler.runIfttt(clientId,id,start);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setIftttOnOff(Boolean flag, RuleResp rule) {
        logger.info("============setIftttOnOff===========rule.getTenantId()"+rule.getTenantId());
        SaaSContextHolder.setCurrentTenantId(rule.getTenantId());
        String clientId = null;
        if(rule.getUploadStatus().equals("1")){//单网关
            //改变build_tob_rule
            SetEnableReq req = new SetEnableReq();
            req.setId(rule.getId());
            if(flag == true){
                req.setStatus("on");
            }else if(flag == false){
                req.setStatus("off");
            }
            //改变网关
            if(rule.getTemplateId() ==null){
                //非模板
                String sensorDeviceId = triggerTobMapper.selectByRuleId(rule.getId(),rule.getTenantId()).get(0).getSensorDeviceId();
                clientId = deviceCoreApi.get(sensorDeviceId).getParentId();
            }else {
                //模板
                String sensorDeviceId = triggerTobMapper.selectByRuleId(rule.getId(),rule.getTenantId()).get(0).getSensorDeviceId();
                clientId = deviceCoreApi.get(sensorDeviceId).getParentId();
            }
            runLowerHair(rule.getId(),clientId,flag);
        }else if(rule.getUploadStatus().equals("0")){//跨网关
            SetEnableReq req = new SetEnableReq();
            if(flag == true){
                req.setStatus("on");
            }else if(flag == false){
                req.setStatus("off");
            }
            Rule rule1 = ruleTobMapper.selectByPrimaryKey(rule.getId());
            req.setId(rule1.getAppletId());
            //改变applet
            iftttApi.setEnable(req);
            //改变build_tob_rule
            run(rule.getId(),flag);
        }

    }

    /**
     * 获取List<SensorVo>
     * @param appletVo
     * @param triggers
     * @param trigger
     * @param id
     * @return
     */
    public List<SensorVo> getSensorVOList(AppletVo appletVo,List<TriggerVo> triggers,TriggerVo trigger,Long id){
        List<SensorVo> sensorVos = Lists.newArrayList();
        //SensorVo sensorVo = new SensorVo();
        List<AppletThisVo> thisList = appletVo.getThisList();
        List<AppletItemVo> appletItemVoSensor = Lists.newArrayList();
        String jsonSensor = null;
            /*json的格式
                {"devId":"11111111111111","field": "Sunny","sign": "4","value": "100","type": "4"}
            }*/
        for (AppletThisVo appletThisVo : thisList){
            appletItemVoSensor = appletThisVo.getItems();
            //设置sensor
            for(AppletItemVo appletItemVo : appletItemVoSensor){
                SensorVo sensorVo = new SensorVo();
                jsonSensor = appletItemVo.getJson();
                JSONObject jsonObjectStr = JSON.parseObject(jsonSensor);
                String devId = (String) jsonObjectStr.get("devId");
                for(TriggerVo triggerStr : triggers){
                    if((triggerStr.getSensorDeviceId()+"").equals(devId)){
                        trigger = triggerStr;
                        continue;
                    }
                }
                sensorVo.setDeviceId(devId);
                sensorVo.setLabel(trigger.getSourceLabel());
                sensorVo.setName(trigger.getSourceLabel());
                logger.info("=========trigger.getSensorPosition()=========="+trigger.getSensorPosition());
                if(StringUtil.isNotBlank(trigger.getSensorPosition())){
                    sensorVo.setPosition(ValueUtils.exPosition(trigger.getSensorPosition()));
                }else {
                    sensorVo.setPosition(null);
                }
                if(StringUtil.isNotBlank(trigger.getSensorType()) && trigger.getSensorType().equals("key_remote13")){//墙控
                    JSONArray jsonArray = JSONObject.parseArray(trigger.getSensorProperties());
                    String triggerValue = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerValue"));
                    String propertyType = String.valueOf(((JSONObject) jsonArray.get(0)).get("propertyType"));
                    String triggerSign = String.valueOf(((JSONObject) jsonArray.get(0)).get("triggerSign"));
                    triggerValue = triggerValue.substring(1,triggerValue.length()-1);
                    String propertiess = "[{\"propertyName\":\"button\",\"triggerValue\":\""+ triggerValue +"\",\"propertyType\":\""+propertyType+"\",\"triggerSign\":\""+triggerSign+"\"}]";
                    sensorVo.setProperties(propertiess);
                }else {
                    sensorVo.setProperties(trigger.getSensorProperties());
                }
                logger.info("=========trigger.getSensorType()1=========="+trigger.getSensorType());
                sensorVo.setType(trigger.getSensorType());
                sensorVo.setRuleId(id);
                sensorVos.add(sensorVo);
            }
        }
        return sensorVos;
    }

    /**
     * 获取 List<ActuatorVo>
     * @param appletVo
     * @param triggers
     * @param trigger
     * @param id
     * @return
     */
    public List<ActuatorVo> getActuatorVoList(AppletVo appletVo,List<TriggerVo> triggers,TriggerVo trigger,Long id){
        String jsonActuctor = null;
        List<ActuatorVo> actuatorVos = Lists.newArrayList();
        //ActuatorVo actuatorVo = new ActuatorVo();
        List<AppletThatVo> thatList = appletVo.getThatList();
        List<AppletItemVo> appletItemVoActuctor = Lists.newArrayList();
        //{"tenantId":222,"msg":{"deviceId":111,"OnOff":null,"Blink":null,"Dimming":null,"Duration":null,"CCT":null,"RGBW":null}}
        for(AppletThatVo appletThatVo : thatList){
            appletItemVoActuctor = appletThatVo.getItems();
            //设置actuctor
            for(AppletItemVo appletItemVo : appletItemVoActuctor){
                ActuatorVo actuatorVo = new ActuatorVo();
                jsonActuctor = appletItemVo.getJson();
                Map map = ValueUtils.jsonStringToMap(jsonActuctor);
                String msg = map.get("msg").toString();
                logger.info("=====设置actuctor=========msg=========="+msg);
                Map maps = (Map)JSON.parse(msg);
                //通过type去拼装actuctor的数据
                String type = String.valueOf(maps.get("type"));
                logger.info("=====设置actuctor=========type=========="+type);
                String devId = null;
                String devIdStr = null;
                if((StringUtil.isNotBlank(type) && type.equals("sence") || (StringUtil.isNotBlank(type) && type.equals("template-scene")))){//设备
                    logger.info("=====设置actuctor===================sence");
                    devId = String.valueOf(maps.get("sceneId"));
                    logger.info("=====设置actuctor===================devId"+devId);
                    devIdStr = String.valueOf(maps.get("templateId"));
                    logger.info("=====设置actuctor===================devIdStr"+devIdStr);
                    logger.info("=====设置actuctor===================triggers.size()"+triggers.size());
                    for(TriggerVo triggerStr : triggers){
                        if((StringUtil.isNotBlank(devId) && (triggerStr.getActuctorDeviceId()+"").equals(devId))  ||  (StringUtil.isNotBlank(devIdStr) && (triggerStr.getActuctorDeviceId()+"").equals(devIdStr))){
                            trigger = triggerStr;
                            continue;
                        }
                    }
                }else if(StringUtil.isNotBlank(type) && type.equals("space")){
                    logger.info("=====设置actuctor===================space");
                    devId = String.valueOf(maps.get("spaceId"));
                    for(TriggerVo triggerStr : triggers){
                        logger.info("=====设置actuctor===================triggerStr.getActuctorDeviceId()"+triggerStr.getActuctorDeviceId());
                        if((triggerStr.getActuctorDeviceId()+"").equals(devId)){
                            trigger = triggerStr;
                            continue;
                        }
                    }
                }else {
                    logger.info("=====设置actuctor===================dev");
                    devId = String.valueOf(maps.get("deviceId"));
                    for(TriggerVo triggerStr : triggers){
                        logger.info("=====设置actuctor===================triggerStr.getActuctorDeviceId()"+triggerStr.getActuctorDeviceId());
                        if((triggerStr.getActuctorDeviceId()+"").equals(devId)){
                            trigger = triggerStr;
                            continue;
                        }
                    }
                }

                actuatorVo.setDeviceId(devId);
                actuatorVo.setLabel(trigger.getDestinationLabel());
                actuatorVo.setName(trigger.getDestinationLabel());
                logger.info("=========trigger.getActuctorPosition()=========="+trigger.getActuctorPosition());
                if(StringUtil.isNotBlank(trigger.getActuctorPosition())){
                    actuatorVo.setPosition(ValueUtils.exPosition(trigger.getActuctorPosition()));
                }else {
                    actuatorVo.setPosition(null);
                }
                actuatorVo.setProperties(trigger.getActuctorProperties());
                actuatorVo.setType(trigger.getActuctorType());
                actuatorVo.setRuleId(id);
                actuatorVos.add(actuatorVo);
            }
        }
        return actuatorVos;
    }
    /**
     * 获取单个 build_tob_rule 的详情
     * @param id
     * @return
     */
    @Override
    public RuleResp get(Long tenantId,Long orgId,Long id) {
        logger.info("=== receive get ifttt request ===" + id);
        RuleResp ruleResp = new RuleResp();
        SaaSContextHolder.setCurrentTenantId(tenantId);
        try {
            Rule rule = getCache(id);
            //获取联动详细信息
            AppletVo appletVo = iftttApi.get(rule.getAppletId());
            String name = appletVo.getName();
            String status = appletVo.getStatus();
            //获取Trigger,包含 sensor actuctor的name，type，properties
            logger.info("================get==Trigger==start=====");
            List<TriggerVo> triggers = getTriggerByRuleId(id);
            logger.info("================get==Trigger==success=====");
            TriggerVo trigger = new TriggerVo();
            //获取relation
            logger.info("===============get====relation====start====");
            List<RelationVo> relations = getRelationByRuleId(id);
            logger.info("===============get====relation====end====");
            //获取sensor
            logger.info("================get==sensor==start=====");
            List<SensorVo> sensorVos = Lists.newArrayList();
            sensorVos = getSensorVOList(appletVo,triggers,trigger,id);
            logger.info("================get==sensor==success=====");
            //获取actuator
            logger.info("================get==actuator==start=====");
            List<ActuatorVo> actuatorVos = Lists.newArrayList();
            actuatorVos = getActuatorVoList(appletVo,triggers,trigger,id);
            logger.info("================get==actuator==success=====");
            //拼装RuleResp 给前端
            ruleResp.setTenantId(getTenantId());
            ruleResp.setName(name);
            /*if(status.equals("on")){
                ruleResp.setStatus(Byte.valueOf("1"));
            }else if(status.equals("off")){
                ruleResp.setStatus(Byte.valueOf("0"));
            }*/
            ruleResp.setStatus(rule.getStatus());
            ruleResp.setSpaceId(rule.getSpaceId());
            ruleResp.setActuators(actuatorVos);
            ruleResp.setSensors(sensorVos);
            ruleResp.setRelations(relations);
            ruleResp.setTriggers(triggers);
            ruleResp.setAppletId(rule.getAppletId());
            ruleResp.setUploadStatus(rule.getUploadStatus());
            ruleResp.setId(rule.getId());
            logger.debug("get rule success");
        }catch (Exception e) {
            //获取单条规则失败
            logger.error("get ifttt error", e);
            throw new BusinessException(IftttExceptionEnum.GET_IFTTT_ERROR, e);
        }
        return ruleResp;
    }

    /**
     * 获取relation 通过 ruleId
     * @param id
     * @return
     */
    private List<RelationVo> getRelationByRuleId(Long id) {
        List<RelationVo> relationVOs = new ArrayList<>();
        Long tenantId = SaaSContextHolder.currentTenantId();
        String key = RedisKeyUtil.getIftttRelationKey(id);
        List<Relation> relations = RedisCacheUtil.listGetAll(key, Relation.class);
        if (CollectionUtils.isEmpty(relations)) {
            relations = relationTobMapper.selectByRuleId(id, tenantId);
            //添加缓存
            RedisCacheUtil.listSet(key, relations);
        }
        if (CollectionUtils.isNotEmpty(relations)) {
            for (Relation relation : relations) {
                RelationVo relationVO = getRelationVo(relation);
                relationVOs.add(relationVO);
            }
        }
        return relationVOs;
    }

    /**
     * 获取trigger 通过ruleId
     * @param id
     * @return
     */
    private List<TriggerVo> getTriggerByRuleId(Long id) {
        List<TriggerVo> triggerVOs = new ArrayList<>();
        List<Trigger> triggerCaches;
        Long tenantId = SaaSContextHolder.currentTenantId();
        String key = RedisKeyUtil.getIftttTriggerKey(id);
        triggerCaches = RedisCacheUtil.listGetAll(key, Trigger.class);
        if (CollectionUtils.isEmpty(triggerCaches)) {
            List<TriggerVo> triggers = triggerTobMapper.selectByRuleId(id, tenantId);
            RedisCacheUtil.listSet(key,triggers);
            return  triggers;
        } else {
            for (Trigger trigger : triggerCaches) {
                triggerVOs.add(getTriggerVo(trigger));
            }
        }
        return triggerVOs;
        //List<TriggerVo> list = triggerMapper.selectByRuleId(id,getTenantId());
    }

    public static RelationVo getRelationVo(Relation source) {
        RelationVo target = new RelationVo();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLabel(source.getLabel());
        target.setType(source.getType());
        target.setRuleId(source.getRuleId());
        if (source.getPosition() != null) {
            Long[] iArr = ValueUtils.exPositionRelation(source.getPosition());
            target.setPosition(iArr);
        }
        if (source.getParentLabels() != null) {
            target.setParentLabels(source.getParentLabels().split(","));
        }
        return target;
    }

    public static TriggerVo getTriggerVo(Trigger source) {
        TriggerVo target = new TriggerVo();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLineId(source.getLineId());
        target.setSourceLabel(source.getSourceLabel());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        target.setDestinationLabel(source.getDestinationLabel());
        target.setInvocationPolicy(source.getInvocationPolicy());
        target.setStatusTrigger(source.getStatusTrigger());
        target.setRuleId(source.getRuleId());
        target.setSensorPosition(source.getSensorPosition());
        target.setSensorType(source.getSensorType());
        target.setSensorProperties(source.getSensorProperties());
        target.setSensorDeviceId(source.getSensorDeviceId());
        target.setActuctorPosition(source.getActuctorPosition());
        target.setActuctorType(source.getActuctorType());
        target.setActuctorProperties(source.getActuctorProperties());
        target.setActuctorDeviceId(source.getActuctorDeviceId());
        return target;
    }

    public static Relation getRelation(RelationVo source) {
        Relation target = new Relation();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLabel(source.getLabel());
        target.setType(source.getType());
        target.setRuleId(source.getRuleId());
        String[] parentLabels = source.getParentLabels();
        if (parentLabels != null && parentLabels.length > 0) {
            target.setParentLabels(String.join(",", source.getParentLabels()));
        }
        Long[] position = source.getPosition();
        if (position != null && position.length > 0) {
            target.setPosition(ValueUtils.join(source.getPosition(), ","));
        }
        return target;
    }

    /**
     * 构建 rule.id 的列表
     *
     * @param ruleList
     * @return
     */
    public List<Long> buildRuleIdList(List<Rule> ruleList) {
        List<Long> idList = Lists.newArrayList();
        for (Rule rule : ruleList) {
            idList.add(rule.getId());
        }

        return idList;
    }

    /**
     * 根据 ids 获取列表
     *
     * @param dbIds
     * @return
     */
    public List<Rule> listRuleRespByIds(List<Long> dbIds) {
        if (CollectionUtils.isEmpty(dbIds)) {
            return null;
        }

        // 返回的 结果记录
        List<Rule> returnList = Lists.newArrayList();
        List<String> ruleIdKeysList = Lists.newArrayList();
        dbIds.forEach(ruleId -> {
            ruleIdKeysList.add(RedisKeyUtil.getIftttRuleForListIdKey(ruleId));
        });
        logger.info("rule id list = " + JSON.toJSONString(ruleIdKeysList));
        // 已经命中的 id
        Map<String, Long> cacheIdMap = Maps.newHashMap();
        List<Rule> cacheList = RedisCacheUtil.mget(ruleIdKeysList, Rule.class);
        if (cacheList == null || cacheList.size() != dbIds.size()) {
            logger.debug("listRuleRespByIds(), 从缓存取出的 cacheList is null OR 数量不等于 dbIds(dbIds.size={}).", dbIds.size());
            if (cacheList == null) {
                cacheList = Lists.newArrayList();
            }
            for(Rule cacheBean : cacheList){
                cacheIdMap.put(cacheBean.getId() + "", cacheBean.getId());
            }
            // 未命中id
            List<String> unHitIdList = Lists.newArrayList();
            dbIds.forEach(dbId -> {
                Long tempId = cacheIdMap.get(dbId + "");
                if (tempId == null) {
                    unHitIdList.add(dbId + "");
                }
            });
            // 加入到缓存
            Map<String, Object> multiSet = Maps.newHashMap();
            // 未命中 结果记录
            List<Rule> unHitRuleList = Lists.newArrayList();
            List<Rule> ruleList = ruleTobMapper.listByIds(unHitIdList);
            if (CollectionUtils.isNotEmpty(ruleList)) {
                for(Rule rule:ruleList){
                    unHitRuleList.add(rule);
                    multiSet.put(RedisKeyUtil.getIftttRuleForListIdKey(rule.getId()), rule);
                }
                RedisCacheUtil.multiSet(multiSet, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
                returnList.addAll(unHitRuleList);
            }
            returnList.addAll(cacheList);
        } else {
            logger.debug("listRuleRespByIds(), 直接使用从缓存查询出的 cacheList." + JSON.toJSONString(cacheList));
            returnList = cacheList;
        }
        return returnList;
    }


    public static RuleResp getRuleResp(Rule source) {
        RuleResp target = new RuleResp();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setIcon(source.getIcon());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setIsMulti(source.getIsMulti());
        target.setLocationId(source.getLocationId());
        target.setSpaceId(source.getSpaceId());
        target.setTenantId(source.getTenantId());
        target.setUserId(source.getUserId());
        target.setDirectId(source.getDirectId());
        target.setTemplateFlag(source.getTemplateFlag());
        target.setProductId(source.getProductId());
        target.setRuleType(source.getRuleType());
        /*target.setSecurityType(source.getSecurityType());
        target.setDelay(source.getDelay());*/
        target.setIftttType(source.getIftttType());
        target.setTemplateId(source.getTemplateId());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        target.setAppletId(source.getAppletId());
        return target;
    }

    /**
     * 填充 ruleResp其它信息
     *
     * @param isShow
     * @return
     */
    /*public RuleResp fillRuleRespOtherInfo(RuleResp ruleResp, Boolean isShow) {
        if (ruleResp != null &&
                (isShow != null && isShow)) {
            List<SensorVo> sensors = sensorService.selectByRuleId(ruleResp.getId());
            if (!sensors.isEmpty()) {
                SensorVo sensor = sensors.get(0);
                if (sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_DEVICE)) {
                    //4:20 AM-3:20 PM
                    //Mon,Tue,Wed,Thu,Fri,Sat,Sun
                    String valid = sensor.getTiming();
                    if (StringUtil.isEmpty(valid) || "null".equals(valid)) {
                        return ruleResp;
                    }
                    Map validMap = JSON.parseObject(valid, Map.class);
                    String begin = (String) validMap.get("begin");
                    String end = (String) validMap.get("end");
                    List<Integer> week = (List<Integer>) validMap.get("week");
                    begin = TimeUtil.getTime(begin);
                    end = TimeUtil.getTime(end);
                    String time = begin + "-" + end;
                    ruleResp.setExecuteTime(time);
                    ruleResp.setExecuteDate(TimeUtil.getWeeks(week));
                } else if (sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_TIMER)) {
                    //定时任务，只有一个sensor
                    String properties = sensor.getProperties();
                    Map map = JSON.parseObject(properties, Map.class);
                    String at = (String) map.get("at");
                    List<Integer> week = (List<Integer>) map.get("repeat");
                    ruleResp.setExecuteTime(TimeUtil.getTime(at));
                    ruleResp.setExecuteDate(TimeUtil.getWeeks(week));
                } else if (sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_SUNRISE) ||
                        sensor.getType().equals(IftttConstants.IFTTT_TRIGGER_SUNSET)) {
                    //sunrise sunset,只有一个sensor
                    String properties = sensor.getProperties();
                    Map map = JSON.parseObject(properties, Map.class);
                    String trigType = (String) map.get("trigType");
                    Integer intervalType = (Integer) map.get("intervalType");
                    String intervalTimeStr = (String) map.get("intervalTime");
                    Integer intervalTime = Integer.parseInt(intervalTimeStr);
                    List<Integer> week = (List<Integer>) map.get("repeat");
                    //1 min before sunrise
                    String time = trigType;
                    if (intervalType == 1 && intervalTime > 60) {
                        //before
                        intervalTime = intervalTime / 60;
                        time = intervalTime + " min before " + trigType;
                    } else if (intervalType == 2 && intervalTime > 60) {
                        //after
                        intervalTime = intervalTime / 60;
                        time = intervalTime + " min after " + trigType;
                    }
                    ruleResp.setExecuteTime(time);
                    ruleResp.setExecuteDate(TimeUtil.getWeeks(week));
                }
            }
        }
        return ruleResp;
    }*/

    /**
     * 获取缓存对象
     *
     * @param id
     * @return
     */
    public Rule getCache(Long id) {
        String key = RedisKeyUtil.getIftttRuleKey(id);
        Rule rule = RedisCacheUtil.valueObjGet(key, Rule.class);
        if (rule == null) {
            rule = ruleTobMapper.selectByPrimaryKey(id);
            if (rule == null) {
                return null;
                //throw new BusinessException(IftttExceptionEnum.RULE_IS_NULL);
            }
            RedisCacheUtil.valueObjSet(key, rule, RedisKeyUtil.DEFAULT_EXPIRE_TIME_OUT);
        }
        return rule;
    }


    public static Trigger getTrigger(TriggerVo source) {
        Trigger target = new Trigger();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setLineId(source.getLineId());
        target.setSourceLabel(source.getSourceLabel());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        target.setDestinationLabel(source.getDestinationLabel());
        target.setInvocationPolicy(source.getInvocationPolicy());
        target.setStatusTrigger(source.getStatusTrigger());
        target.setRuleId(source.getRuleId());
        target.setAppletId(source.getAppletId());
        target.setSensorPosition(source.getSensorPosition());
        target.setSensorType(source.getSensorType());
        target.setSensorProperties(source.getSensorProperties());
        target.setSensorDeviceId(source.getSensorDeviceId());
        target.setActuctorPosition(source.getActuctorPosition());
        target.setActuctorType(source.getActuctorType());
        target.setActuctorProperties(source.getActuctorProperties());
        target.setActuctorDeviceId(source.getActuctorDeviceId());
        return target;
    }

    /**
     * 获取AppletThisVo
     * @param ruleVO
     * @return
     */
    public AppletThisVo getAppletThisVo(SaveIftttReq ruleVO){
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setServiceCode(IftttServiceEnum.DEV_STATUS.getCode());//设置服务类型，TO B 目前就用到DEV_STATUS 和TIMER
        if(ruleVO.getRelations() !=null && ruleVO.getRelations().size() !=0){
            String type = ruleVO.getRelations().get(0).getType();
            if(StringUtil.isNotEmpty(type)){
                if(type.equals("AND")){
                    appletThisVo.setLogic("and");//按照 TO C 的说法，不是and 就 默认为 or
                }else {
                    appletThisVo.setLogic("or");
                }
            }else {
                appletThisVo.setLogic("or");
            }
        }else {
            appletThisVo.setLogic("or");//按照 TO C 的说法，不是and 就 默认为 or
        }
        return appletThisVo;
    }

    /**
     * 获取List<AppletItemVo>  this
     * @param ruleVO
     * @param isTemplateFlag
     * @param spaceId
     * @return
     */
    public List<AppletItemVo> getAppletItemVoThisList(SaveIftttReq ruleVO,Boolean isTemplateFlag,Long spaceId){
        List<AppletItemVo> thisItems = Lists.newArrayList();
        List<SensorVo> sensorVoList = ruleVO.getSensors();
        List<String> stringStr = Lists.newArrayList();
        String sensorType = null;
        SensorVo sensorVo = new SensorVo();
        for (int i = 0;i< sensorVoList.size();i++) {
            AppletItemVo appletItemVo = new AppletItemVo();
            //json 格式固定为这样
            /*{
                "devId": "b6f7c7f3e7a14a74be155d6a0f7f38e1",
                    "field": "occupancy ",
                    "sign": "== ",
                    "value":  "1",
                    "type":"0"
            }*/

            sensorVo = sensorVoList.get(i);
            JSONArray jsonArray = JSONObject.parseArray(sensorVo.getProperties());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String propertyName = (String) jsonObject.get("propertyName");//属性名，如：门
            if(i==sensorVoList.size()-1){
                sensorType = propertyName;
            }else {
                sensorType = propertyName + ",";
            }
            int propertyType = Integer.valueOf(jsonObject.get("propertyType")+"");//整型，浮点型，字符串  0整型 1浮点型 2字符串
            int triggerSign = Integer.valueOf(jsonObject.get("triggerSign")+"");//条件判断（大于等于的那些）
            String triggerSignStr = null;
            if(triggerSign == 0){
                triggerSignStr = ">";
            }else if(triggerSign == 1){
                triggerSignStr = ">=";
            }else if(triggerSign == 2){
                triggerSignStr = "<";
            }else if(triggerSign == 3){
                triggerSignStr = "<=";
            }else if(triggerSign == 4){
                triggerSignStr = "==";
            }else if(triggerSign == 5){
                triggerSignStr = "!=";
            }
            String triggerValue = String.valueOf(jsonObject.get("triggerValue"));//满足的值
            //{"devId":"11111111111111","field": "Sunny","sign": "4","value": "100","type": "4"}

            if(isTemplateFlag){
                //为ifttt模板
                logger.info("========this==is====ifttt_template=========");
                //通过device_business_id 查找 productId ,再通过productId查找 设备id
                Long deviceBusinessId = Long.valueOf(sensorVo.getDeviceId());
                DeviceBusinessTypeResp deviceBusinessTypeResp = deviceBusinessTypeApi.findById(ruleVO.getOrgId(),ruleVO.getTenantId(),deviceBusinessId);
                Long productId = deviceBusinessTypeResp.getProductId();
                logger.info("==========ifttt模板:设备类型----设备类型=====sensor===productId:====================="+productId);
                //ListDeviceByParamsReq params = new ListDeviceByParamsReq();
                SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                spaceDeviceReq.setBusinessTypeId(Long.valueOf(deviceBusinessId));
                spaceDeviceReq.setSpaceId(spaceId);
                spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);

                //params.setProductId(productId);
                //List<ListDeviceByParamsRespVo> listDeviceByParamsRespVos = deviceCoreApi.listDeviceByParams(params);
                for(SpaceDeviceResp listDeviceByParamsRespVo : spaceDeviceRespList){
                    String jsonThis = "{\"devId\":\"" + listDeviceByParamsRespVo.getDeviceId() + "\",\"field\": \"" + propertyName + "\",\"sign\": \"" + triggerSignStr + "\",\"value\": \"" + triggerValue + "\",\"type\": \"" + propertyType + "\"}";  //json格式
                    AppletItemVo appletItemVo2 = new AppletItemVo();
                    appletItemVo2.setJson(jsonThis);
                    thisItems.add(appletItemVo2);
                }
            }else {
                String jsonThis = "{\"devId\":\"" + sensorVo.getDeviceId() + "\",\"field\": \"" + propertyName + "\",\"sign\": \"" + triggerSignStr + "\",\"value\": \"" + triggerValue + "\",\"type\": \"" + propertyType + "\"}";  //json格式
                appletItemVo.setJson(jsonThis);
                thisItems.add(appletItemVo);

            }
            //String param = "{\"label\":\"" + sensorVo.getLabel() + "\",\"name\":\"" + sensorVo.getName() + "\",\"position\":\"" + sensorVo.getPosition() + "\",\"productId\":\"" + sensorVo.getProductId() + "\",\"properties\":\"" + sensorVo.getProperties() + "\",\"type\":\"" + sensorVo.getType() + "\"}";
        }
        return thisItems;
    }

    /**
     * 获取AppletThatVo
     * @return
     */
    public AppletThatVo getAppletThatVo(){
        AppletThatVo appletThatVo = new AppletThatVo();
        appletThatVo.setServiceCode(IftttServiceEnum.MQ.getCode()); //MQ消息服务
        appletThatVo.setCode("TOB_CODE"); //事件码，TO B 的为空
        return appletThatVo;
    }

    /**
     * 获取 List<AppletItemVo> that
     * @param ruleVO
     * @param isTemplateFlag
     * @param spaceId
     * @param sensorType
     * @return
     */
    public List<AppletItemVo> getAppletItemVoThatList(SaveIftttReq ruleVO,Boolean isTemplateFlag,Long spaceId,String sensorType){
        List<AppletItemVo> thatItems = Lists.newArrayList();
        List<ActuatorVo> actuatorVoList = ruleVO.getActuators();
        Map<String,Object> map = new HashMap<String,Object>();
        List<String> listStr = Lists.newArrayList();
        String msg = null;
        String jsonThat = null;
        Long appletId = ruleVO.getAppletId();
        for (ActuatorVo actuatorVo : actuatorVoList) {
            AppletItemVo item = new AppletItemVo();
            //type 目前为scene,space,和设备的类型
            String type = actuatorVo.getType();
            logger.info("==========type的值为："+type+"======================================sence=====");
            //ToC 那边规定的 json 格式固定为这样
            /* {
                tenantId:
                msg:{}
            }*/
            // type.equals("template")
            if(StringUtil.isNotBlank(type) && type.equals("sence") && isTemplateFlag == false){
                logger.info("==========ifttt情景===========================================");
                //ifttt 情景
                msg = "{\"sceneId\":" + actuatorVo.getDeviceId()+",\"type\":\"" + type + "\",\"appletId\":" + appletId + ",\"sensorType\":\""+sensorType+"\"}";

            }else if(StringUtil.isNotBlank(type) && type.equals("space") && isTemplateFlag == false){
                //ifttt 房间
                logger.info("==========ifttt房间===========================================");
                map = ValueUtils.jsonStringToMap(actuatorVo.getProperties());
                Map<String,Object> map1 = new HashMap<String,Object>();
                map1 = (Map<String, Object>) map.get("device");
                map1.put("spaceId",actuatorVo.getDeviceId());
                map1.put("sceneId",map.get("senceId").toString());
                map1.put("type",type);
                map1.put("sensorType",sensorType);
                msg = map1.toString();
                //msg = "\"spaceId\":" + actuatorVo.getDeviceId() + "\",\"type\":" + type + "\",\"OnOff\":" + map.get("OnOff") + ",\"Blink\":" + map.get("Blink") + ",\"Dimming\":" +map.get("Dimming") + ",\"Duration\":" + map.get("Duration") + "\"";
            }else if(StringUtil.isNotBlank(type) && type.equals("sence")&& isTemplateFlag){
                logger.info("==========ifttt模板:设备类型----情景===========================================");
                //ifttt 模板： 设备类型----情景
                //通过templateId 和 spaceId查找 sceneId
                SceneReq sceneReq = new SceneReq();
                sceneReq.setSpaceId(spaceId);
                logger.info("===spaceId===="+spaceId);
                logger.info("===actuatorVo.getDeviceId===="+actuatorVo.getDeviceId());
                sceneReq.setId(Long.valueOf(actuatorVo.getDeviceId()));
                //sceneReq.setTemplateId(Long.valueOf(actuatorVo.getDeviceId()));
                List<SceneResp> list = sceneApi.sceneByParamDoing(sceneReq);
                String sceneId = list.get(0).getId()+"";
                String templateId = actuatorVo.getDeviceId();
                logger.info("==========ifttt模板:设备类型----情景====sceneId:======================"+sceneId);
                Map<String,Object> map1 = new HashMap<String,Object>();
                msg = "{\"sceneId\":" + sceneId+",\"type\":\"" + "template-scene" + "\",\"appletId\":" + appletId + ",\"sensorType\":\""+sensorType+"\",\"templateId\":\""+templateId+"\"}";
                logger.info("==========ifttt模板:设备类型----情景====================msg======================="+msg);
            }else if(StringUtil.isNotBlank(type) && !type.equals("sence") && isTemplateFlag){
                //ifttt 模板： 设备类型----设备类型
                logger.info("==========ifttt模板:设备类型----设备类型===========================================");
                //通过device_business_id 查找 productId ,再通过productId查找 设备id
                Long deviceBusinessId = Long.valueOf(actuatorVo.getDeviceId());
                logger.info("==========ifttt模板:设备类型----设备类型=====actuator=deviceBusinessId:====================="+deviceBusinessId);
                DeviceBusinessTypeResp deviceBusinessTypeResp = deviceBusinessTypeApi.findById(ruleVO.getOrgId(),ruleVO.getTenantId(),deviceBusinessId);
                Long productId = deviceBusinessTypeResp.getProductId();
                logger.info("==========ifttt模板:设备类型----设备类型=====actuator=productId:====================="+productId);
                //ListDeviceByParamsReq params = new ListDeviceByParamsReq();
                //params.setProductId(productId);
                //List<ListDeviceByParamsRespVo> listDeviceByParamsRespVos = deviceCoreApi.listDeviceByParams(params);
                SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                spaceDeviceReq.setBusinessTypeId(Long.valueOf(deviceBusinessId));
                spaceDeviceReq.setSpaceId(spaceId);
                spaceDeviceReq.setTenantId(ruleVO.getTenantId());
                List<SpaceDeviceResp> spaceDeviceRespList = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                for (SpaceDeviceResp listDeviceByParamsRespVo : spaceDeviceRespList){
                    Map<String,Object> mapStr = new HashMap<String,Object>();
                    String msgStr = null;
                    mapStr = ValueUtils.jsonStringToMap(actuatorVo.getProperties());
                    Map<String,Object> map2 = new HashMap<String,Object>();
                    map2 = (Map<String, Object>) mapStr.get("device");
                    String uuid = listDeviceByParamsRespVo.getDeviceId();
                    logger.info("==========ifttt模板:设备类型----设备类型=====actuator=uuid:====================="+uuid);
                    map2.put("deviceId",uuid);
                    map2.put("type","template-business-type");
                    map2.put("sensorType",sensorType);
                    map2.put("appletId",appletId);
                    msgStr = map2.toString();
                    listStr.add(msgStr);
                }
            }else {
                //ifttt 设备
                //{"device":{"OnOff":1,"Blink":5,"Dimming":20,"Duration":3},"senceId":null}
                map = ValueUtils.jsonStringToMap(actuatorVo.getProperties());
                Map<String,Object> map1 = new HashMap<String,Object>();
                map1 = (Map<String, Object>) map.get("device");
                map1.put("deviceId",actuatorVo.getDeviceId());
                map1.put("type","dev");
                map1.put("sensorType",sensorType);
                map1.put("appletId",appletId);
                msg = map1.toString();
            }
            if(CollectionUtils.isEmpty(listStr)){
                logger.info("====================非ifttt模板"+msg);
                jsonThat = "{\"route\":\"2B\",\"tenantId\":" + ruleVO.getTenantId() + ",\"msg\":" + msg + "}";
                item.setJson(jsonThat);
                thatItems.add(item);
            }
        }
        if(CollectionUtils.isNotEmpty(listStr)){
            logger.info("==========ifttt模板:设备类型----设备类型===拼接消息体=================listStr======================="+listStr.size());
            logger.info("==========ifttt模板:设备类型----设备类型===拼接消息体========================================");
            for (String str : listStr){
                AppletItemVo item2 = new AppletItemVo();
                logger.info("==========ifttt模板:设备类型----设备类型===拼接消息体============="+str);
                String jsonThatStr = "{\"route\":\"2B\",\"tenantId\":" + ruleVO.getTenantId() + ",\"msg\":" + str + "}";
                item2.setJson(jsonThatStr);
                thatItems.add(item2);
            }
        }
        return thatItems;
    }
    /**
     * 获取AppletVo
     * @return
     */
    public AppletVo getDevAuto(SaveIftttReq ruleVO) {
        AppletVo appletVo = new AppletVo();
        logger.info("=======getDevAuto========applet=============");
        //applet 相当于重构前的ifttt_rule
        appletVo.setName(ruleVO.getName());
        appletVo.setCreateBy(ruleVO.getUserId());
        if(ruleVO.getUploadStatus().equals("1")){//单网关
            appletVo.setStatus("off");//启用状态 0=running 1=stop
        }else {//跨网关
            appletVo.setStatus("on");//启用状态 0=running 1=stop
        }
        appletVo.setId(ruleVO.getAppletId());
        Boolean isTemplateFlag = false;
        String sensorType = null;
        Long spaceId = ruleVO.getSpaceId();
        for (int i = 0;i< ruleVO.getSensors().size();i++) {
            JSONArray jsonArray = JSONObject.parseArray(ruleVO.getSensors().get(i).getProperties());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String propertyName = (String) jsonObject.get("propertyName");//属性名，如：门
            if (i == ruleVO.getSensors().size() - 1) {
                sensorType = propertyName;
            } else {
                sensorType = propertyName + ",";
            }
        }
        if(StringUtil.isNotBlank(ruleVO.getType()) && ruleVO.getType().equals("template")){
            logger.info("=======isTemplateFlag============="+isTemplateFlag);
            isTemplateFlag = true;
        }
        logger.info("=======getDevAuto========this=======start=========");
        //this
        List<AppletThisVo> thisList = Lists.newArrayList();
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo = getAppletThisVo(ruleVO);
        logger.info("=======getDevAuto========this=======end=========");
        logger.info("=======getDevAuto========this_item======start=======");
        //this_item  相当于重构前的Sensor传感器
        List<AppletItemVo> thisItems = Lists.newArrayList();
        thisItems = getAppletItemVoThisList(ruleVO,isTemplateFlag,spaceId);
        appletThisVo.setItems(thisItems);
        thisList.add(appletThisVo);
        appletVo.setThisList(thisList);
        logger.info("=======getDevAuto========this_item======end=======");
        logger.info("=======getDevAuto========that======start=======");
        //that
        List<AppletThatVo> thatList = Lists.newArrayList();
        AppletThatVo appletThatVo = new AppletThatVo();
        appletThatVo = getAppletThatVo();
        logger.info("=======getDevAuto========that======end=======");
        logger.info("=======getDevAuto========that_item======start=======");
        //that_item  相当于重构前的actuator执行器
        List<AppletItemVo> thatItems = Lists.newArrayList();
        thatItems = getAppletItemVoThatList(ruleVO,isTemplateFlag,spaceId,sensorType);
        logger.info("=======getDevAuto========that_item======end=======");
        for(AppletItemVo appletItemVo:thatItems){
            logger.info("===================thatItems======================"+thatItems.size()+"======"+appletItemVo.getJson());
        }
        appletThatVo.setItems(thatItems);
        thatList.add(appletThatVo);
        appletVo.setThatList(thatList);
        return appletVo;
    }


    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }


    public static Rule getRule(SaveIftttReq source) {
        Rule target = new Rule();
        if (source == null) {
            return target;
        }
        target.setId(source.getId());
        target.setName(source.getName());
        target.setIcon(source.getIcon());
        target.setType(source.getType());
        target.setStatus(source.getStatus());
        target.setIsMulti(source.getIsMulti());
        target.setLocationId(source.getLocationId());
        target.setSpaceId(source.getSpaceId());
        target.setTenantId(source.getTenantId());
        target.setUserId(source.getUserId());
        target.setDirectId(source.getDirectId());
        target.setTemplateFlag(source.getTemplateFlag());
        target.setProductId(source.getProductId());
        target.setRuleType(source.getRuleType());
        //target.setSecurityType(source.getSecurityType());
        //target.setDelay(source.getDelay());
        target.setAppletId(source.getAppletId());
        target.setIftttType(source.getIftttType());
        target.setTemplateId(source.getTemplateId());
        target.setUploadStatus(source.getUploadStatus());
        return target;
    }

    public void addOrUpdateIfttt(boolean isSingleGatewayFlag,SaveIftttReq ruleVO){
        if(isSingleGatewayFlag){//单网关
            logger.info("================replaceIfttt==============process2.1=======单网关=====");
            String client = deviceCoreApi.get(ruleVO.getSensors().get(0).getDeviceId()).getParentId();
            if(ruleVO.getId() !=null){
                //修改，要删除网关里的ifttt
                deleteLowerHair(ruleVO.getId(),client);
            }
            ruleVO.setUploadStatus("1");
            //重构后，分四步
            //第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
            Long appletId = saveAuto(ruleVO);
            //第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
            ruleVO.setAppletId(appletId);
            Long buildToRuleId = saveBuildTobRule(ruleVO);
            //第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
            ruleVO.setId(buildToRuleId);
            saveTobTrigger(ruleVO);
            //第四步保存tob_relation,相当于之前的ifttt_relation
            saveTobRelation(ruleVO);
            //创建下发给网关
            ruleVO.setClientId(client);
            ruleVO.setId(buildToRuleId);
            createLowerHair(ruleVO);
        }else {//多网关
            logger.info("================replaceIfttt==============process2.1=======多网关=====");
            ruleVO.setUploadStatus("0");//跨网关//重构后，分四步
            //第一步先保存联动信息（sensor、actuator、or或and）,表：applet,applet_this,applet_that,applet_item
            Long appletId = saveAuto(ruleVO);
            //第二步保存/修改ifttt记录（保存/修改build_tob_rule表）,相当于之前的ifttt_rule
            ruleVO.setAppletId(appletId);
            Long buildToRuleId = saveBuildTobRule(ruleVO);
            //第三步保存点、线、sensor的位置、类型、属性，actuctor的位置、类型、属性  表为tob_trigger,相当于之前的ifttt_trigger
            ruleVO.setId(buildToRuleId);
            saveTobTrigger(ruleVO);
            //第四步保存tob_relation,相当于之前的ifttt_relation
            saveTobRelation(ruleVO);
        }
    }

}
