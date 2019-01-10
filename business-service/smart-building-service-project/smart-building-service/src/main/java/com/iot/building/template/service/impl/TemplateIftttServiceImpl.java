package com.iot.building.template.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.iot.building.device.api.CentralControlDeviceApi;
import com.iot.building.device.vo.DeviceParamReq;
import com.iot.building.ifttt.api.IftttApi;
import com.iot.building.ifttt.constant.IftttConstants;
import com.iot.building.ifttt.exception.IftttExceptionEnum;
import com.iot.building.ifttt.service.*;
import com.iot.building.ifttt.vo.ActuatorVo;
import com.iot.building.ifttt.vo.RelationVo;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.TriggerVo;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.req.SaveRuleReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.listener.MQTTClientListener;
import com.iot.building.space.api.SpaceApi;
import com.iot.building.space.domain.SpaceDevice;
import com.iot.building.space.enums.SpaceEnum;
import com.iot.building.space.mapper.SpaceDeviceMapper;
import com.iot.building.space.service.IBuildingSpaceService;
import com.iot.building.template.domain.Template;
import com.iot.building.template.domain.TemplateIfttt;
import com.iot.building.template.mapper.TemplateIftttMapper;
import com.iot.building.template.mapper.TemplateMapper;
import com.iot.building.template.service.TemplateIftttService;
import com.iot.building.template.vo.req.BuildIftttReq;
import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.common.exception.BusinessException;
import com.iot.common.util.StringUtil;
import com.iot.control.scene.api.SceneApi;
import com.iot.control.scene.vo.req.SceneReq;
import com.iot.control.scene.vo.rsp.SceneResp;
import com.iot.control.space.api.SpaceDeviceApi;
import com.iot.control.space.vo.SpaceAndSpaceDeviceVo;
import com.iot.control.space.vo.SpaceDeviceReq;
import com.iot.control.space.vo.SpaceDeviceResp;
import com.iot.control.space.vo.SpaceResp;
import com.iot.device.api.DeviceApi;
import com.iot.device.api.DeviceCoreApi;
import com.iot.device.vo.rsp.DeviceParamResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.MqttSdkService;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TemplateIftttServiceImpl implements TemplateIftttService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TemplateIftttServiceImpl.class);

    @Autowired
    private SceneApi sceneApi;
    @Autowired
    private IftttApi iftttApi;
    @Autowired
    private IBuildingSpaceService buildingSpaceService;
    @Autowired
    private DeviceCoreApi deviceApi;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private IftttService iftttService;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private TriggerService triggerService;
    @Autowired
    private MqttSdkService mqttSdkService;
    @Autowired
    private TemplateMapper templateMapper;
    @Autowired
    private ActuatorService actuatorService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private SpaceDeviceMapper spaceDeviceMapper;
    @Autowired
    private TemplateIftttMapper templateIftttMapper;
    @Autowired
    private CentralControlDeviceApi centralControlDeviceApi;
    @Autowired
    private SpaceDeviceApi spaceDeviceApi;
    /**
     * 获取租户id
     *
     * @return
     */
    private Long getTenantId() {
        return SaaSContextHolder.currentTenantId();
    }
    
    /**
     * 获取租户id
     *
     * @return
     */
    private Long getOrgId() {
        return SaaSContextHolder.getCurrentOrgId();
    }


    @Override
    public void saveIftttTemplate(SaveIftttTemplateReq req) {
        LOGGER.info("save ifttt template {}, time is {}.", JSON.toJSON(req), new Date());
        try {
            // delete old ifttt template data
            if (req.getId() != null && req.getId() != 0) {
                deleteOldData(req.getId());
            }
            // save ifttt_template
            SaveIftttReq saveIftttReq = new SaveIftttReq();
            BeanUtils.copyProperties(req, saveIftttReq);
            saveIftttReq.setTemplateFlag(IftttConstants.IFTTT_TEMPLATE_TRUE);
//            Rule rule = BeanCopyUtil.getRule(saveIftttReq);
//            Long templateId = ruleService.save(rule);
            Long templateId = iftttService.save(saveIftttReq);

            // set template id
            req.setTemplateId(templateId);
//            saveIftttReq.setTemplateId(templateId);
            // set ifttt template is false
            req.setTemplateFlag(IftttConstants.IFTTT_TEMPLATE_FALSE);
//            saveIftttReq.setTemplateFlag(IftttConstants.IFTTT_TEMPLATE_FALSE);
            // set ifttt template id null
            saveIftttReq.setId(null);

            // get sensor device type
            if (CollectionUtils.isEmpty(saveIftttReq.getSensors())) {
                return;
            }

            // get sensor device business type set
            Set<Long> businessTypeSet = createBusinessTypeSet(saveIftttReq.getSensors());

            if (CollectionUtils.isEmpty(businessTypeSet)) {
                return;
            }
            // check actuator (scene template or useage)
            boolean isSceneTemplateFlag = false;
            for (ActuatorVo actuatorVo : saveIftttReq.getActuators()) {
                if (actuatorVo.getType().equals("sence")) {
                    isSceneTemplateFlag = true;
                }
            }
            // when actuator is scene template
            if (isSceneTemplateFlag) {
                ActuatorVo actuatorVo = saveIftttReq.getActuators().get(0);
                if (Strings.isNullOrEmpty(actuatorVo.getDeviceId())) {
                    return;
                }
                // get all scene in scene template
                Long sceneTemplateId = Long.parseLong(actuatorVo.getDeviceId());
                //SceneDetailReq sceneDetail = new SceneDetailReq();
                SceneReq sceneReq = new SceneReq();
                sceneReq.setTemplateId(sceneTemplateId);
                //sceneDetail.setTemplateId(sceneTemplateId);
                //List<SceneResp> sceneList = sceneApi.querySceneList(sceneDetail);
                List<SceneResp> sceneList = sceneApi.sceneByParamDoing(sceneReq);//TODO 通过spaceID过滤
                // count scene in same space
                Multimap<Long,SceneResp> spaceRuleMap = ArrayListMultimap.create();
                for (SceneResp sr : sceneList) {
                    spaceRuleMap.put(sr.getSpaceId(), sr);
                }
                // loop room
                for (Long sId : spaceRuleMap.keySet()) {
                    List<SceneResp> sList = (List<SceneResp>) spaceRuleMap.get(sId);
                    // get sensor in space
                    //重构前List<SpaceDevice> spaceDevices = spaceDeviceMapper.findListBySpaceId(sId);
                    //重构后
                    SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                    spaceDeviceReq.setSpaceId(sId);
                    spaceDeviceReq.setTenantId(req.getTenantId());
                    List<SpaceDeviceResp> spaceDevices = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                    List<String> sensorDeviceIdList = Lists.newArrayList();
                    for (SpaceDeviceResp sd : spaceDevices) {
                        // check device business type exist in sensor business type
                        if (businessTypeSet.contains(sd.getBusinessTypeId())) {
                            sensorDeviceIdList.add(sd.getDeviceId());
                        }
                    }
                    if (CollectionUtils.isEmpty(sensorDeviceIdList)) {
                        continue;
                    }
                    // find device list in space
                    DeviceParamReq paramReq = new DeviceParamReq();
                    paramReq.setDeviceIdList(sensorDeviceIdList);
                    paramReq.setCheckUserNotNull(false);
                    DeviceParamResp deviceParamResp = new DeviceParamResp();
                    BeanUtils.copyProperties(centralControlDeviceApi.findDeviceListByDeviceIds(paramReq),deviceParamResp);

                    // create ifttt info
                    SaveIftttReq saveIfttt = createSaveIftttByScene(req, sList, deviceParamResp.getDeviceResps());
                    // change ifttt rule name
                    saveIfttt.setName(modifyIftttRuleName(saveIfttt.getName(), sId));
                    Long iftttId = iftttApi.save(saveIfttt);
                    LOGGER.info("save ifttt id = {}.", iftttId);
                    // save template and ifttt relate
                    createTemplateIfttt(templateId, iftttId, req.getTenantId(), req.getUserId());
                }
            } else {
                // find space in tenantId
                List<SpaceResp> spaceRespList =  buildingSpaceService.findSpaceByTenantId(saveIftttReq.getOrgId(),saveIftttReq.getTenantId());//TODO 通过spaceID过滤
                Set<Long> actuatorTypeIdSet = Sets.newHashSet();
                for (ActuatorVo actuatorVo : saveIftttReq.getActuators()) {
                    actuatorTypeIdSet.add(Long.parseLong(actuatorVo.getDeviceId()));
                }
                Set<Long> sensorTypeIdSet = Sets.newHashSet();
                for (SensorVo sensorVo : saveIftttReq.getSensors()) {
                    sensorTypeIdSet.add(Long.parseLong(sensorVo.getDeviceId()));
                }
                // loop all space (room)
                for (SpaceResp space : spaceRespList) {
                    // check space is room
                    if (!space.getType().equals(SpaceEnum.ROOM.getCode())) {
                        continue;
                    }
                    // get sensor in space
                    //List<SpaceDevice> spaceDevices = spaceDeviceMapper.findListBySpaceId(space.getId());
                    //重构后
                    SpaceDeviceReq spaceDeviceReq = new SpaceDeviceReq();
                    spaceDeviceReq.setSpaceId(space.getId());
                    spaceDeviceReq.setTenantId(req.getTenantId());
                    List<SpaceDeviceResp> spaceDevices = spaceDeviceApi.findSpaceDeviceByCondition(spaceDeviceReq);
                    if (CollectionUtils.isEmpty(spaceDevices)) {
                        return;
                    }
                    List<String> actuatorIdList = Lists.newArrayList();
                    List<String> sensorIdList = Lists.newArrayList();
                    // get actuator id list and sensor id list
                    for (SpaceDeviceResp spaceDevice : spaceDevices) {
                        if (actuatorTypeIdSet.contains(spaceDevice.getBusinessTypeId())) {
                            actuatorIdList.add(spaceDevice.getDeviceId());
                        }
                        if (sensorTypeIdSet.contains(spaceDevice.getBusinessTypeId())) {
                            sensorIdList.add(spaceDevice.getDeviceId());
                        }
                    }
                    if (CollectionUtils.isEmpty(actuatorIdList)) {
                        continue;
                    }
                    if (CollectionUtils.isEmpty(sensorIdList)) {
                        continue;
                    }
                    // find actuator device list in space
                    DeviceParamReq paramReq = new DeviceParamReq();
                    paramReq.setDeviceIdList(actuatorIdList);
                    paramReq.setCheckUserNotNull(false);
                    DeviceParamResp actuatorResp = new DeviceParamResp();
                    BeanUtils.copyProperties(centralControlDeviceApi.findDeviceListByDeviceIds(paramReq),actuatorResp);
                    //DeviceParamResp actuatorResp = centralControlDeviceApi.findDeviceListByDeviceIds(paramReq);

                    // find sensor device list in space
                    DeviceParamReq sensorReq = new DeviceParamReq();
                    sensorReq.setDeviceIdList(sensorIdList);
                    sensorReq.setCheckUserNotNull(false);
                    DeviceParamResp sensorResp = new DeviceParamResp();
                    BeanUtils.copyProperties(centralControlDeviceApi.findDeviceListByDeviceIds(paramReq),sensorResp);
                    //DeviceParamResp sensorResp = centralControlDeviceApi.findDeviceListByDeviceIds(sensorReq);

                    // create ifttt info
                    SaveIftttReq saveIfttt = createSaveIftttByDevice(req, actuatorResp.getDeviceResps(), sensorResp.getDeviceResps());
                    saveIfttt.setName(modifyIftttRuleName(saveIfttt.getName(), space.getId()));
                    Long iftttId = iftttApi.save(saveIfttt);
                    LOGGER.info("save ifttt id = {}.", iftttId);
                    // save template and ifttt relate
                    createTemplateIfttt(templateId, iftttId, req.getTenantId(), req.getUserId());
                }
            }
            // delete old template info
            if (checkLong(req.getTemplateIftttId())) {
                templateIftttMapper.deleteByPrimaryKey(req.getTemplateIftttId());
            }
        } catch (BusinessException e) {
            //保存IFTTT规则失败
            LOGGER.error("save ifttt template error", e);
            throw new BusinessException(IftttExceptionEnum.SAVE_IFTTT_TEMPLATE_ERROR, e);
        }
    }

    // ifttt name = name-spaceId-ifttt
    private String modifyIftttRuleName(String name, Long id) {
        StringBuffer sb = new StringBuffer();
        sb.append(name).append("-")
                .append(id).append("-ifttt");
        return sb.toString();
    }

    // get business type id set from business type
    private Set<Long> createBusinessTypeSet(List<SensorVo> sensors) {
        Set<Long> set = Sets.newHashSet();
        for (SensorVo s : sensors) {
            if (Strings.isNullOrEmpty(s.getDeviceId())) {
                continue;
            }
            set.add(Long.parseLong(s.getDeviceId()));
//            String deviceType = s.getType();
//            if (Strings.isNullOrEmpty(deviceType)) {
//                continue;
//            }
//            DeviceBusinessType businessType = deviceBusinessTypeApi.getBusinessTypeIdByType(deviceType);
//            if (businessType == null) {
//                continue;
//            }
//            set.add(businessType.getId());
        }
        return set;
    }

    // 保存ifttt和模板关系表
    private void createTemplateIfttt(Long templateId, Long iftttId, Long tenantId, Long userId) {
        TemplateIfttt templateIfttt = new TemplateIfttt();
        templateIfttt.setRuleId(iftttId);
        templateIfttt.setCreateBy(userId);
        templateIfttt.setTenantId(tenantId);
        templateIfttt.setTemplateId(templateId);
        templateIfttt.setCreateTime(new Date());
        templateIfttt.setUpdateTime(new Date());
        templateIftttMapper.insertSelective(templateIfttt);
    }

    // create ifttt when sensor touch device
    private SaveIftttReq createSaveIftttByDevice(SaveIftttTemplateReq req, List<DeviceResp> actuatorDeviceList, List<DeviceResp> sensorDeviceList) {

        int index = 1;
        List<SensorVo> sensorList = transformDeviceToSensor(sensorDeviceList, req.getSensors(), index);
        index += sensorDeviceList.size();
        List<ActuatorVo> actuatorList = transformDeviceToActuatorVo(actuatorDeviceList, req.getActuators(), index);
        List<RelationVo> relationVoList = transformRelate(req.getRelations(), sensorList);
        List<TriggerVo> triggerVoList = transformTrigger(relationVoList, sensorList, actuatorList);

        req.setSensors(sensorList);
        req.setActuators(actuatorList);
        req.setTriggers(triggerVoList);
        req.setRelations(relationVoList);
        req.setId(null);

        return req;
    }

    // assemble sensor touch scene
    private SaveIftttReq createSaveIftttByScene(SaveIftttTemplateReq req, List<SceneResp> sceneList, List<DeviceResp> deviceList) {

        int index = 1;
        List<SensorVo> sensorList = transformDeviceToSensor(deviceList, req.getSensors(), index);
        index += deviceList.size();
        List<ActuatorVo> actuatorList = transformSceneToActuator(sceneList, index);
        List<RelationVo> relationVoList = transformRelate(req.getRelations(), sensorList);
        List<TriggerVo> triggerVoList = transformTrigger(relationVoList, sensorList, actuatorList);

        req.setSensors(sensorList);
        req.setActuators(actuatorList);
        req.setTriggers(triggerVoList);
        req.setRelations(relationVoList);
        req.setId(null);

        return req;
    }

    // transform trigger
    private List<TriggerVo> transformTrigger(List<RelationVo> relationVoList, List<SensorVo> sensorList, List<ActuatorVo> actuatorList) {
        List<TriggerVo> triggerVoList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(relationVoList)) {
            triggerVoList = assembleTriggerWithRelate(relationVoList.get(0), sensorList, actuatorList);
        } else {
            triggerVoList = assembleTrigger(sensorList, actuatorList);
        }
        return triggerVoList;
    }

    // transform relate with sensor
    private List<RelationVo> transformRelate(List<RelationVo> relations, List<SensorVo> sensorList) {
        List<RelationVo> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(relations)) {
            return list;
        }
        // current only consider one relate
        RelationVo relationVo = relations.get(0);
        String[] pLabel = new String[sensorList.size()];
        for (int j=0; j<sensorList.size(); j++) {
            pLabel[j] = sensorList.get(j).getName();
        }
        relationVo.setParentLabels(pLabel);
        list.add(relationVo);
        return list;
    }

    // assemble trigger with relate
    private List<TriggerVo> assembleTriggerWithRelate(RelationVo relationVo, List<SensorVo> sensorList, List<ActuatorVo> actuatorList) {
        List<TriggerVo> triggerVoList = Lists.newArrayList();
        // relate sensor with related
        for (SensorVo sv : sensorList) {
            TriggerVo triggerVo = new TriggerVo();
            triggerVo.setStart(sv.getPosition()[2]);
            triggerVo.setSourceLabel(sv.getName());
            triggerVo.setEnd(relationVo.getPosition()[2]);
            triggerVo.setDestinationLabel(relationVo.getLabel());
            triggerVo.setLineId("icon_" + (int)Math.random()*100);
            triggerVoList.add(triggerVo);
        }
        // relate actuator with related
        for (ActuatorVo av : actuatorList) {
            TriggerVo triggerVo = new TriggerVo();
            triggerVo.setStart(relationVo.getPosition()[2]);
            triggerVo.setSourceLabel(relationVo.getLabel());
            triggerVo.setEnd(av.getPosition()[2]);
            triggerVo.setDestinationLabel(av.getName());
            triggerVo.setLineId("icon_" + (int)Math.random()*100);
            triggerVoList.add(triggerVo);
        }
        return triggerVoList;
    }

    // assemble trigger without relate
    private List<TriggerVo> assembleTrigger(List<SensorVo> sensorList, List<ActuatorVo> actuatorList) {
        List<TriggerVo> triggerList = Lists.newArrayList();
        if (sensorList.size() == 1) {
            SensorVo sensorVo = sensorList.get(0);
            List<TriggerVo> list = createTriggerList(actuatorList, sensorVo);
            triggerList.addAll(list);
        } else {
            // assign actuator to sensor
            Multimap<Integer, ActuatorVo> multimap = assignActuatorToSensor(sensorList, actuatorList);
            for (int i=0; i<sensorList.size(); i++) {
                List<ActuatorVo> actList = (List<ActuatorVo>) multimap.get(i);
                List<TriggerVo> list = createTriggerList(actList, sensorList.get(i));
                triggerList.addAll(list);
            }
        }
        return triggerList;
    }

    // assign actuator to sensor
    private Multimap<Integer, ActuatorVo> assignActuatorToSensor(List<SensorVo> sensorList, List<ActuatorVo> actuatorList) {
        Multimap<Integer, ActuatorVo> multimap = ArrayListMultimap.create();
        if (CollectionUtils.isEmpty(sensorList)) {
            return multimap;
        }
        int total = sensorList.size();
        for (int i=0; i<actuatorList.size(); i++) {
            int index = i % total;
            multimap.put(index, actuatorList.get(i));
        }
        return multimap;
    }

    private List<TriggerVo> createTriggerList(List<ActuatorVo> actuatorList, SensorVo sensorVo) {
        List<TriggerVo> list = Lists.newArrayList();
        for (ActuatorVo actuator : actuatorList) {
            TriggerVo trigger = new TriggerVo();
            trigger.setStart(sensorVo.getPosition()[2]);
            trigger.setSourceLabel(sensorVo.getName());
            trigger.setEnd(actuator.getPosition()[2]);
            trigger.setDestinationLabel(actuator.getName());
            trigger.setLineId("icon_" + (int)Math.random()*100);
            list.add(trigger);
        }
        return list;
    }

    // transform device to actuator
    private List<ActuatorVo> transformDeviceToActuatorVo(List<DeviceResp> actuatorDeviceList, List<ActuatorVo> actuators, int index) {
        List<ActuatorVo> list = Lists.newArrayList();
        Map<String, String> actuatorMap = Maps.newHashMap();
        for (ActuatorVo a : actuators) {
            actuatorMap.put(a.getDeviceId(), a.getProperties());
        }
        for (DeviceResp device : actuatorDeviceList) {
            ActuatorVo temp = new ActuatorVo();
            temp.setName(device.getName());
            temp.setLabel(device.getName());
            temp.setDeviceId(device.getDeviceId());
            temp.setProductId(device.getProductId());
            temp.setType(device.getBusinessType());
            temp.setProperties(actuatorMap.get(String.valueOf(device.getBusinessTypeId())));
            Long[] position = createPosition(index);
            temp.setPosition(position);

            list.add(temp);
            index += 1;
        }
        return list;
    }

    // transform device to sensor
    private List<SensorVo> transformDeviceToSensor(List<DeviceResp> deviceList, List<SensorVo> sensorVos, Integer index) {
        List<SensorVo> sensorList = Lists.newArrayList();
        Map<String, String> typeMap = Maps.newHashMap();
        for (SensorVo s : sensorVos) {
            typeMap.put(s.getDeviceId(), s.getProperties());
        }
        for (DeviceResp device : deviceList) {
            SensorVo sensor = new SensorVo();
            sensor.setDeviceId(device.getDeviceId());
            sensor.setLabel(device.getName());
            sensor.setName(device.getName());
            sensor.setProductId(device.getProductId());
            Long[] position = createPosition(index);
            sensor.setPosition(position);
            sensor.setProperties(typeMap.get(String.valueOf(device.getBusinessTypeId())));
//            sensor.setType(device.getBusinessType());
            sensor.setType(device.getBusinessType() + IftttConstants.IFTTT_2B_FLAG);
            index += 1;

            sensorList.add(sensor);
        }
        return sensorList;
    }

    // transform scene to actuator
    private List<ActuatorVo> transformSceneToActuator(List<SceneResp> sceneList, Integer index) {
        List<ActuatorVo> list = Lists.newArrayList();
        for (SceneResp scene : sceneList) {
            ActuatorVo vo = new ActuatorVo();
            vo.setDeviceId(String.valueOf(scene.getId()));
            vo.setLabel(scene.getSceneName());
            vo.setName(scene.getSceneName());
            vo.setType("scene");
            Long[] position = createPosition(index);
            vo.setPosition(position);
            list.add(vo);
            index += 1;
        }
        return list;
    }

    // assemble position
    private Long[] createPosition(Integer index) {
        Long[] position = new Long[3];
        // set position random
        position[0] = Long.parseLong(String.valueOf((int)(Math.random()*400+100)));
        position[1] = Long.parseLong(String.valueOf((int)(Math.random()*400+100)));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, index);
        position[2] = c.getTimeInMillis();
        return position;
    }

    private void deleteOldData(Long id) {
        LOGGER.info("delete ifttt templatem, id = {} ", id);
        try {
            List<Long> ruleIds = ruleService.selectRuleIdByTemplateId(id);
            LOGGER.info("find ifttt in template (id = {}), count is {}", id, ruleIds.size());
            List<Integer> idxList = new ArrayList<>();
            for (Long ruleId : ruleIds) {
                //删除relation
                relationService.deleteByRuleId(ruleId);
                //删除trigger
                triggerService.deleteByRuleId(ruleId);
                // 删除 actuator
                actuatorService.deleteByRuleId(ruleId, idxList);
                //删除sensor
                sensorService.deleteByRuleId(ruleId, idxList);
                //删除rule
                ruleService.delete(ruleId);
            }
        } catch (BusinessException e) {
            // 删除IFTTT规则失败
            LOGGER.error("delete ifttt template error", e);
            throw new BusinessException(IftttExceptionEnum.DELETE_IFTTT_TEMPLATE_ERROR, e);
        }
    }

    /**
     * 判断long值
     *
     * @param value
     * @return
     */
    public Boolean checkLong(Long value) {
        Boolean flag = false;
        if (value != null && value != 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void deleteIftttTemplate(Long templateId) {
        List<TemplateIfttt> ifttts = templateIftttMapper.queryByTemplateId(templateId);
        if(ifttts.size() == 0){
            // delete ifttt-rule
            iftttService.delete(templateId, true);
        }else {
            for (TemplateIfttt template : ifttts) {
                templateIftttMapper.deleteByPrimaryKey(template.getId());
                // delete ifttt-rule
                iftttService.delete(template.getRuleId(), true);
                // delete ifttt-template-rule
                iftttService.delete(template.getTemplateId(), true);
            }
        }
    }

    @Override
    public void deleteByTemplateId(Long templateId) {
        List<TemplateIfttt> list = templateIftttMapper.selectByTemplateId(templateId, getTenantId(),getOrgId());
        for (TemplateIfttt vo : list) {
            deleteIftttTemplate(vo.getId());
        }
    }

    @Override
    public void buildIfttt2C(BuildIftttReq req) {
        //校验参数
        checkParam(req);

        //获取模板修改
        Template template = templateMapper.getTemplateById(req.getTemplateId());
        if (template == null) {
            throw new BusinessException(IftttExceptionEnum.TEMPLATE_IS_NULL);
        }

        //获取模板关系
        List<TemplateIfttt> templateIftttList = templateIftttMapper.selectByTemplateId(template.getId(), getTenantId(),req.getOrgId());

        if (templateIftttList.isEmpty()) {
            throw new BusinessException(IftttExceptionEnum.IFTTT_TEMPLATE_IS_NULL);
        }

        List<Long> autoIds = Lists.newArrayList();
        for (TemplateIfttt templateIfttt : templateIftttList) {
            Long ruleId = saveIfttt(templateIfttt.getRuleId(), req, true);
            autoIds.add(ruleId);
        }

        //插入数据
        //return autoIds;
    }

    @Override
    public Long buildIfttt2B(BuildIftttReq req) {
        //不下发网关
        return saveIfttt(req.getTemplateId(), req, false);
    }

    public Long saveIfttt(Long ruleId, BuildIftttReq req, Boolean downFlag) {
        RuleResp ruleResp = iftttService.get(ruleId);
        if (ruleResp == null) {
            throw new BusinessException(IftttExceptionEnum.IFTTT_TEMPLATE_IS_NULL);
        }

        //根据设备主键获取产品主键
        Map<Long, String> product2devMap = Maps.newConcurrentMap();
        Map<Long, Boolean> product2sameMap = Maps.newConcurrentMap();
        Map<Long, Queue<String>> product2ListMap = Maps.newConcurrentMap();
        for (String uuid : req.getDeviceIds()) {
            //根据设备主键获取产品ID
            GetDeviceInfoRespVo deviceResp = deviceApi.get(uuid);
            Integer type = req.getType();
            Long productId = deviceResp.getProductId();
            if (type != null && type.intValue() == 1) {
                productId = deviceResp.getBusinessTypeId();
            }

            //取相同产品
            if (product2devMap.get(productId) != null) {
                product2sameMap.put(productId, true);
                Queue<String> devList = product2ListMap.get(productId);
                if (devList == null) {
                    devList = new LinkedList<String>();
                    devList.offer(product2devMap.get(productId));
                }
                devList.offer(uuid);
                product2ListMap.put(productId, devList);
            } else {
                product2sameMap.put(productId, false);
                product2devMap.put(productId, uuid);
            }
        }
        LOGGER.info("***** kit --> saveIfttt() --> product2devMap={}", product2devMap);

        //生成ifttt_rule  //生成具体我的IFTTT
        SaveIftttReq saveIftttReq = new SaveIftttReq();
        BeanUtils.copyProperties(ruleResp, saveIftttReq);
        saveIftttReq.setId(null);
        saveIftttReq.setName(ruleResp.getName());
        saveIftttReq.setIcon(ruleResp.getIcon());
        saveIftttReq.setType(ruleResp.getType());
        saveIftttReq.setStatus(ruleResp.getStatus());
        saveIftttReq.setIsMulti(ruleResp.getIsMulti());
        saveIftttReq.setSecurityType(ruleResp.getSecurityType());
        saveIftttReq.setDelay(ruleResp.getDelay());
        saveIftttReq.setTemplateFlag(IftttConstants.IFTTT_TEMPLATE_FALSE);
        saveIftttReq.setDirectId(saveIftttReq.getProductId()!=null?product2devMap.get(saveIftttReq.getProductId()):null);
        saveIftttReq.setProductId(null);
        saveIftttReq.setSpaceId(req.getSpaceId());
        saveIftttReq.setTemplateId(ruleId);  // 生成的具体IFTTT添加模板ID
        //设置用户主键和租户主键
        if (req.getUserId() != null) {
            saveIftttReq.setUserId(req.getUserId());
        } else {
            saveIftttReq.setUserId(SaaSContextHolder.getCurrentUserId());
        }
        if (req.getTenantId() != null) {
            saveIftttReq.setTenantId(req.getTenantId());
        } else {
            saveIftttReq.setTenantId(SaaSContextHolder.currentTenantId());
        }

        String iftttType = ruleResp.getIftttType();
        //创建sensor
        List<SensorVo> resultSensorList = Lists.newArrayList();
        List<SensorVo> sensors = ruleResp.getSensors();
        if (CollectionUtils.isEmpty(sensors)) {
            return null;
        }
        for (SensorVo vo : sensors) {
            if (vo.getProductId() == null) {
                continue;
            }
            String deviceId = product2devMap.get(vo.getProductId());
            if (StringUtil.isBlank(deviceId)) {
                LOGGER.info("kit vo.id={} and vo.productId={} -----> is not in product2devMap={}", vo.getId(), vo.getProductId(), product2devMap);
                continue;
            }

            //相同产品处理
            Boolean flag = product2sameMap.get(vo.getProductId());
            if (flag == null) {
                continue;
            }
            if (flag) {
                Queue<String> queue = product2ListMap.get(vo.getProductId());
                deviceId = queue.poll();
            }
            vo.setDeviceId(deviceId);
            if (vo.getProductId() != null) {
                String pro = vo.getProperties();
                if(!IftttConstants.IFTTT_2B_FLAG.equals(iftttType)){
                    pro = replaceDev(pro, vo.getProductId().toString(), deviceId, "devId");
                }
                vo.setProperties(pro);
            }
            vo.setId(null);
            vo.setRuleId(null);
            vo.setProductId(null);

            resultSensorList.add(vo);
        }
        saveIftttReq.setSensors(resultSensorList);

        //创建actuator
        List<ActuatorVo> resultActuatorList = Lists.newArrayList();
        List<ActuatorVo> actuators = ruleResp.getActuators();
        if (CollectionUtils.isEmpty(actuators)) {
            return null;
        }
        for (ActuatorVo vo : actuators) {
            if (vo.getProductId() == null) {
                continue;
            }
            String deviceId = product2devMap.get(vo.getProductId());
            if (StringUtil.isBlank(deviceId)) {
                LOGGER.info("kit vo.id={} and vo.productId={} -----> is not in product2devMap={}", vo.getId(), vo.getProductId(), product2devMap);
                continue;
            }

            //相同产品处理
            Boolean flag = product2sameMap.get(vo.getProductId());
            if (flag == null) {
                continue;
            }
            if (flag) {
                Queue<String> queue = product2ListMap.get(vo.getProductId());
                deviceId = queue.poll();
            }
            vo.setDeviceId(deviceId);
            if (vo.getProductId() != null) {
                String pro = vo.getProperties();
                if(!IftttConstants.IFTTT_2B_FLAG.equals(iftttType)){
                    pro = replaceDev(pro, vo.getProductId().toString(), deviceId, "id");
                }
                vo.setProperties(pro);
            }
            vo.setId(null);
            vo.setRuleId(null);
            vo.setProductId(null);
            resultActuatorList.add(vo);
        }
        saveIftttReq.setActuators(resultActuatorList);

        //创建relation
        List<RelationVo> relations = ruleResp.getRelations();
        for (RelationVo vo : relations) {
            vo.setId(null);
            vo.setRuleId(null);
        }
        saveIftttReq.setRelations(relations);

        //创建trigger
        List<TriggerVo> triggers = ruleResp.getTriggers();
        for (TriggerVo vo : triggers) {
            vo.setId(null);
            vo.setRuleId(null);
        }
        saveIftttReq.setTriggers(triggers);

        //如果不跨网关，则下发直连设备（网关）网关自行处理
        Long autoId = 0l;
        //云端保存 2B
        iftttService.save(saveIftttReq);
        autoId = saveIftttReq.getId();
        return autoId;
    }

    public static String replaceDev(String pro, String proId, String devId, String str) {
        String s1 = str + "\" : \"" + proId + "\"";
        String[] strs = pro.split(s1);
        String s2 = str + "\" : \"" + devId + "\"";
        return strs[0] + s2 + strs[1];
    }

    @Override
    public List<RuleResp> getIftttTemplateList(Long templateId) {
        return templateIftttMapper.getRuleByTemplateId(templateId, getTenantId(),getOrgId());
    }

    @Override
    public RuleResp getIftttTemplateByRuleId(Long ruleId) {
        return iftttService.get(ruleId);
    }

    /**
     * 校验参数
     *
     * @param req
     */
    public void checkParam(BuildIftttReq req) {
        if (req.getTemplateId() == null) {
            throw new BusinessException(IftttExceptionEnum.IFTTT_TEMPLATEID_IS_NULL);
        }

        if (req.getDeviceIds().isEmpty()) {
            throw new BusinessException(IftttExceptionEnum.IFTTT_TEMPLATEID_DEVICEIDS_IS_NULL);
        }
    }

}
