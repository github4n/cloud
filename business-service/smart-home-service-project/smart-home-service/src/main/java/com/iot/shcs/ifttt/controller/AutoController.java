package com.iot.shcs.ifttt.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.Page;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.vo.ActuatorVo;
import com.iot.control.ifttt.vo.RelationVo;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.RuleListReq;
import com.iot.control.ifttt.vo.req.RuleReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.ifttt.common.IftttServiceEnum;
import com.iot.ifttt.vo.AppletItemVo;
import com.iot.ifttt.vo.AppletThatVo;
import com.iot.ifttt.vo.AppletThisVo;
import com.iot.ifttt.vo.AppletVo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.ifttt.api.AutoApi;
import com.iot.shcs.ifttt.constant.AutoConstants;
import com.iot.shcs.ifttt.entity.Automation;
import com.iot.shcs.ifttt.entity.AutomationItem;
import com.iot.shcs.ifttt.mapper.AutomationMapper;
import com.iot.shcs.ifttt.service.IAutoService;
import com.iot.shcs.ifttt.service.IAutomationItemService;
import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import com.iot.shcs.ifttt.vo.req.AutoListReq;
import com.iot.shcs.ifttt.vo.req.SaveAutoReq;
import com.iot.shcs.ifttt.vo.res.AutoListResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class AutoController implements AutoApi {
    private final Logger logger = LoggerFactory.getLogger(AutoController.class);

    @Autowired
    private IAutoService autoService;

    @Autowired
    private IftttApi iftttApi;

    @Autowired
    private AutomationMapper automationMapper;

    @Autowired
    private IAutomationItemService automationItemService;

    @Override
    public List<AutoListResp> list(@RequestBody AutoListReq req) {
        return autoService.list(req);
    }

    @Override
    public Long saveAuto(@RequestBody SaveAutoReq req) {
        return autoService.saveAuto(req);
    }

    @Override
    public List<Map<String, Object>> getSceneListByDevId(String devId) {
        Long tenantId = SaaSContextHolder.currentTenantId();
        List<AutomationItem> items = automationItemService.getItemByParam(AutoConstants.IFTTT_THEN_DEVICE, devId, tenantId);

        List<Map<String, Object>> res = Lists.newArrayList();
        if (CollectionUtils.isEmpty(items)) {
            return res;
        }

        for (AutomationItem item : items) {
            Long autoId = autoService.getAutoIdByAppletId(item.getAppletId());
            Map<String, Object> payload = autoService.getPayloadById(autoId);
            res.add(payload);
        }
        return res;
    }

    @Override
    public void moveData(@RequestParam("key") String key) {
        if (!"move666".equals(key)) {
            logger.info("暗号不对，不处理！");
            return;
        }
        //取出旧ifttt数据
        RuleListReq req = new RuleListReq();
        req.setRuleType((byte) 0);
        req.setTemplateFlag((byte) 0);

        Page<RuleResp> page = iftttApi.list(req);
        List<RuleResp> oldList = page.getResult();

        //拼接
        for (RuleResp vo : oldList) {
            if (!"_2B".equals(vo.getIftttType()) && !StringUtils.isEmpty(vo.getType())) {
                //判断是否已迁移了
                if (checkAuto(vo.getId())) {
                    //跳过
                    continue;
                }
                //保存automation
                Automation automation = new Automation();
                automation.setName(vo.getName());
                automation.setIcon(vo.getIcon());
                automation.setStatus(vo.getStatus().intValue());
                automation.setUserId(vo.getUserId());
                automation.setSpaceId(vo.getSpaceId());
                automation.setTenantId(vo.getTenantId());
                automation.setIsMulti(vo.getIsMulti().intValue());
                automation.setDirectId(vo.getDirectId());
                automation.setTriggerType(vo.getType());
                automation.setRuleId(vo.getId());
                automation.setDelay(vo.getDelay());

                //保存auto
                MqttMsg message = new MqttMsg();
                Map<String, Object> payload = getPayloadById(vo.getId());
                automation.setTimeJson(getTimeJson(payload));
                automationMapper.insert(automation);
                //保存规则
                Long autoId = automation.getId();
                payload.put("autoId", autoId);
                message.setPayload(payload);
                SaaSContextHolder.setCurrentTenantId(vo.getTenantId());
                Boolean enable = false;
                if (vo.getIsMulti() == 1) {
                    enable = true;
                }
                autoService.saveAutoRule(message, enable);

                System.out.println("======迁移完rule:" + vo.getId());
            }
        }
    }

    @Override
    public void clearDirtyData(@RequestParam("tenantId") Long tenantId, @RequestParam("key") String key) {
        if (!"clear888".equals(key)) {
            logger.info("暗号不对，不处理！");
            return;
        }
        SaaSContextHolder.setCurrentTenantId(tenantId);
        AutoListReq req = new AutoListReq();
        req.setTenantId(tenantId);
        List<AutoListResp> list = autoService.list(req);
        for (AutoListResp vo : list) {
            autoService.delete(vo.getId());
            logger.debug("======删除完auto:" + vo.getId());
        }
    }

    @Override
    public void delBleAuto(@RequestBody SaveAutoReq req) {
        autoService.delBleAuto(req);
    }

    @Override
    public Map<String, Object> getAutoDetail(@RequestBody SaveAutoReq saveAutoReq) {
        return autoService.getAutoDetail(saveAutoReq);
    }

    @Override
    public void addAutoRule(@RequestBody AddAutoRuleReq req) {
        autoService.addAutoRule(req);
    }

    @Override
    public void editAutoRule(@RequestBody AddAutoRuleReq req) {
        autoService.editAutoRule(req);
    }

    @Override
    public void setAutoEnable(@RequestBody AddAutoRuleReq req) {
        autoService.setAutoEnable(req);
    }
    //////////////////////////////////////////////////////////////////////////////

    public boolean checkAuto(Long ruleId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("rule_id", ruleId);
        List<Automation> list = automationMapper.selectByMap(params);
        if (list.size() > 0) {
            return true;
        } else {
            return false;
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
        Map<String, Object> valid = (Map<String, Object>) payload.get("valid");
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

    /**
     * 2C转换方法
     *
     * @param req
     * @return
     */
    public static AppletVo getAppletBean(RuleResp req) {
        Long tenantId = req.getTenantId();

        //applet
        AppletVo appletVo = new AppletVo();
        appletVo.setName(req.getName());
        appletVo.setCreateBy(req.getUserId());
        if (req.getStatus() == 1) {
            appletVo.setStatus("on");
        } else {
            appletVo.setStatus("off");
        }

        //this
        appletVo.setThisList(getAppletThis(req.getSensors()));

        //that
        appletVo.setThatList(getAppletThat(req.getActuators(), tenantId));

        return appletVo;
    }

    /**
     * 获取that
     *
     * @param actuatorVos
     * @param tenantId
     * @return
     */
    public static List<AppletThatVo> getAppletThat(List<ActuatorVo> actuatorVos, Long tenantId) {
        List<AppletThatVo> appletThatVos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(actuatorVos)) {
            AppletThatVo appletThatVo = new AppletThatVo();
            appletThatVo.setServiceCode(IftttServiceEnum.MQ.getCode());

            List<AppletItemVo> items = Lists.newArrayList();
            for (ActuatorVo vo : actuatorVos) {
                AppletItemVo item = new AppletItemVo();
                /*{
                    tenantId:
                    msg:{}
                }*/
                String json = "{\"tenantId\":" + tenantId + ",\"msg\":" + vo.getProperties() + "}";
                item.setJson(json);
                items.add(item);
            }
            appletThatVo.setItems(items);
            appletThatVos.add(appletThatVo);
        }
        return appletThatVos;
    }

    /**
     * 获取this
     *
     * @param sensorVos
     * @return
     */
    public static List<AppletThisVo> getAppletThis(List<SensorVo> sensorVos) {
        if (CollectionUtils.isEmpty(sensorVos)) {
            //没有sensor，不赋值
            return null;
        }

        String ruleType = sensorVos.get(0).getType();
        String timing = sensorVos.get(0).getTiming();

        List<AppletThisVo> appletThisVos = Lists.newArrayList();
        if (ruleType.equals("timer")) {
            //定时this
            String properties = sensorVos.get(0).getProperties();
            appletThisVos.add(getTimerThis(properties));
        }
        if (ruleType.equals("sunset")) {
            //日落
            String properties = sensorVos.get(0).getProperties();
            appletThisVos.add(getAstroThis(properties));
        }
        if (ruleType.equals("sunrise")) {
            //日出
            String properties = sensorVos.get(0).getProperties();
            appletThisVos.add(getAstroThis(properties));
        } else {
            //时间范围this
            appletThisVos.add(getTimeRangeThis(timing));
            //设备状态this
            appletThisVos.add(getDevThis(sensorVos));
        }
        return appletThisVos;
    }

    /**
     * 获取设备this
     *
     * @param sensorVos
     * @return
     */
    public static AppletThisVo getDevThis(List<SensorVo> sensorVos) {
        //设备状态触发
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.DEV_STATUS.getCode());

        //多个dev item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        for (SensorVo vo : sensorVos) {
            appletItemVos.add(getDevItem(vo.getProperties()));
        }
        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }

    /**
     * 获取定时this
     *
     * @param properties
     * @return
     */
    public static AppletThisVo getTimerThis(String properties) {
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.TIMER.getCode());

        //1 timer-item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        //{"at":"09:30","repeat":[0,1,2,3,4,5,6],"trigType":"timer","idx":0}
        Map maps = (Map) JSON.parse(properties);
        String at = (String) maps.get("at");
        String repeat = maps.get("repeat").toString();
        String json = "{\"at\":\"" + at + "\",\"repeat\":" + repeat + "}";
        appletItemVo.setJson(json);
        appletItemVos.add(appletItemVo);
        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }


    /**
     * 获取Astro this
     *
     * @param properties
     * @return
     */
    public static AppletThisVo getAstroThis(String properties) {
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.ASTRONOMICAL.getCode());

        //1 timer-item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        //{
        //	"idx": 2,
        //	"trigType": "sunset",
        //	"longitude": "113.211",
        //	"latitude": "40.14924",
        //	"timeZone": "GMT+8",
        //	"intervalType": 0,
        //	"intervalTime": "3600",
        //	"repeat": [0, 1, 2, 3, 4, 5, 6]
        //}
        Map maps = (Map) JSON.parse(properties);
        String trigType = (String) maps.get("trigType");
        String longitude = maps.get("longitude").toString();
        String latitude = maps.get("latitude").toString();
        String timeZone = maps.get("timeZone").toString();
        String intervalType = maps.get("intervalType").toString();
        String intervalTime = maps.get("intervalTime").toString();
        String repeat = maps.get("repeat").toString();

        String json = "{\"trigType\":\"" + trigType
                + "\",\"longitude\":\"" + longitude
                + "\",\"latitude\":\"" + latitude
                + "\",\"timeZone\":\"" + timeZone
                + "\",\"intervalType\":\"" + intervalType
                + "\",\"intervalTime\":\"" + intervalTime
                + ",\"repeat\":" + repeat + "}";

        appletItemVo.setJson(json);
        appletItemVos.add(appletItemVo);
        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }

    /**
     * 获取时间范围this
     *
     * @param timing
     * @return
     */
    public static AppletThisVo getTimeRangeThis(String timing) {
        AppletThisVo appletThisVo = new AppletThisVo();
        appletThisVo.setLogic("or");
        appletThisVo.setServiceCode(IftttServiceEnum.TIME_RANGE.getCode());

        //timeRange item
        List<AppletItemVo> appletItemVos = Lists.newArrayList();
        AppletItemVo appletItemVo = new AppletItemVo();
        //{"at":"09:30","repeat":[0,1,2,3,4,5,6],"trigType":"timer","idx":0}
        Map<String, Object> map = JSON.parseObject(timing, Map.class);
        String begin = String.valueOf(map.get("begin"));
        String end = String.valueOf(map.get("end"));
        String repeat = String.valueOf(map.get("week"));
        String json = "{\"begin\":\"" + begin + "\",\"end\":\"" + end + "\",\"repeat\":" + repeat + "}";
        appletItemVo.setJson(json);
        appletItemVos.add(appletItemVo);

        appletThisVo.setItems(appletItemVos);
        return appletThisVo;
    }

    /**
     * 获取设备子项
     *
     * @param properties
     * @return
     */
    public static AppletItemVo getDevItem(String properties) {
        AppletItemVo appletItemVo = new AppletItemVo();
        //{"devId":"97003f06bee103e5fb694e01ee0f6312","trigType":"dev","idx":0,"attr":"occupancy ","value":"1","compOp":"== "}
        Map<String, Object> map = JSON.parseObject(properties, Map.class);
        String attr = String.valueOf(map.get("attr"));
        String compOp = String.valueOf(map.get("compOp"));
        String value = String.valueOf(map.get("value"));
        String devId = String.valueOf(map.get("devId"));

        String json = "{\"devId\":\"" + devId + "\",\"field\": \"" + attr + "\",\"sign\": \"" + compOp + "\",\"value\":\"" + value + "\",\"type\":2}";  //json格式
        appletItemVo.setJson(json);
        return appletItemVo;
    }

    /**
     * 根据ruleId获取payload
     *
     * @param ruleId
     * @return
     */
    public Map<String, Object> getPayloadById(Long ruleId) {
        RuleResp req = iftttApi.get(ruleId);
        return getPayload(req);
    }

    /**
     * 获取payload
     *
     * @param req
     * @return
     */
    public Map<String, Object> getPayload(RuleResp req) {
        //if
        Map<String, Object> ifMap = new HashMap<>();
        Map<String, Object> validMap = new HashMap<>();
        List<Map<String, Object>> triggerList = new ArrayList<>();
        List<Map<String, Object>> thenList = new ArrayList<>();
        int enableDelay = 0;

        List<SensorVo> sensorList = req.getSensors();
        if (CollectionUtils.isNotEmpty(sensorList)) {
            //valid
            String valid = sensorList.get(0).getTiming();
            if (sensorList.get(0).getDelay() == null) {
                enableDelay = 0;
            } else {
                enableDelay = sensorList.get(0).getDelay();
            }
            validMap = JSON.parseObject(valid, Map.class);

            //trigger
            for (SensorVo sensor : sensorList) {
                String properties = sensor.getProperties();
                Map<String, Object> triggerMap = JSON.parseObject(properties, Map.class);
                triggerList.add(triggerMap);
            }
        }

        ifMap.put("valid", validMap);
        ifMap.put("trigger", triggerList);

        //logic
        List<RelationVo> relationList = req.getRelations();
        if (relationList != null && relationList.size() > 0) {
            ifMap.put("logic", relationList.get(0).getType());
        } else {
            ifMap.put("logic", "");
        }

        //then
        List<ActuatorVo> actuatorList = req.getActuators();
        if (actuatorList != null && actuatorList.size() > 0) {
            for (ActuatorVo actuator : actuatorList) {
                String properties = actuator.getProperties();
                Map<String, Object> propertiesMap = JSON.parseObject(properties, Map.class);
                thenList.add(propertiesMap);
            }
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("then", thenList);
        payload.put("if", ifMap);
        payload.put("autoId", String.valueOf(req.getId()));
        payload.put("enableDelay", enableDelay);
        return payload;
    }
}
