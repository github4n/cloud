package com.iot.building.ifttt.controller;

import java.util.List;
import java.util.Map;

import com.iot.building.template.vo.req.SaveIftttTemplateReq;
import com.iot.saas.SaaSContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.building.ifttt.api.IftttApi;
import com.iot.building.ifttt.service.ActuatorService;
import com.iot.building.ifttt.service.AstroClockService;
import com.iot.building.ifttt.service.IftttService;
import com.iot.building.ifttt.service.RelationService;
import com.iot.building.ifttt.service.RuleService;
import com.iot.building.ifttt.service.SensorService;
import com.iot.building.ifttt.service.TriggerService;
import com.iot.building.ifttt.vo.SensorVo;
import com.iot.building.ifttt.vo.req.ExecIftttReq;
import com.iot.building.ifttt.vo.req.GetSensorReq;
import com.iot.building.ifttt.vo.req.RuleListReq;
import com.iot.building.ifttt.vo.req.SaveIftttReq;
import com.iot.building.ifttt.vo.req.SaveRuleReq;
import com.iot.building.ifttt.vo.res.RuleResp;
import com.iot.building.ifttt.vo.res.SensorResp;
import com.iot.common.enums.APIType;
import com.iot.common.helper.Page;
import com.iot.schedule.api.ScheduleApi;

@RestController
public class IftttController implements IftttApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(IftttController.class);
    @Autowired
    private IftttService iftttService;
    @Autowired
    private ScheduleApi scheduleApi;


    @Autowired
    private RuleService ruleService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private ActuatorService actuatorService;

    @Autowired
    private RelationService relationService;

    @Autowired
    private TriggerService triggerService;

    @Autowired
    private AstroClockService astroClockService;

    @Override
    public Page<RuleResp> list(@RequestBody RuleListReq req) {
        return ruleService.list(req);
    }

    @Override
    public List<RuleResp> listNoPage(@RequestBody RuleListReq req) {
        return ruleService.listNoPage(req);
    }

    @Override
    public Long save(@RequestBody SaveIftttReq req) {
        SaaSContextHolder.setCurrentUserId(req.getUserId());
        SaaSContextHolder.setCurrentOrgId(req.getOrgId());
        SaaSContextHolder.setCurrentTenantId(req.getTenantId());
        return iftttService.save(req);
    }


    @Override
    public Long saveRule(@RequestBody SaveRuleReq req) {
        return iftttService.saveRule(req);
    }

    @Override
    public RuleResp get(Long tenantId,@PathVariable("id") Long id) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        return iftttService.get(id);
    }

    @Override
    public List<Integer> delete(Long tenantId,@PathVariable("id") Long id) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        return iftttService.delete(id, true);
    }

    @Override
    public List<SensorResp> getSensorByParams(@RequestBody GetSensorReq req) {
        return sensorService.getSensorByParams(req);
    }

    @Override
    public Boolean run(Long tenantId,@PathVariable("id") Long id,
                       @RequestParam("start") Boolean start) {
        SaaSContextHolder.setCurrentTenantId(tenantId);
        return ruleService.updateStatus(id, start);
    }

    @Override
    public void execCronIftttByRule(@RequestParam("ruleId") Long ruleId,
                                    @RequestParam("tenantId") Long tenantId,
                                    @RequestParam("type") String type) {
        iftttService.execCronIftttByRule(ruleId, tenantId, type);
    }

    @Override
    public Boolean checkName(Long tenantId,@RequestParam(value = "ruleId", defaultValue = "0") Long ruleId,
                             @RequestParam("ruleName") String ruleName,
                             @RequestParam("userId") Long userId) {
        return ruleService.checkName(ruleId, ruleName, userId);
    }

    /**
     * 初始化定时任务
     * 添加到定时任务服务器中
     *//*
    @RequestMapping(value = "/initQuartzJob", method = RequestMethod.GET)
    public void initQuartzJob() {
        GetSensorReq req = new GetSensorReq();
        req.setType(IftttConstants.IFTTT_TRIGGER_TIMER);
        List<SensorResp> sensorList = getSensorByParams(req);

        if (sensorList.isEmpty()) {
            return;
        }

        for (SensorResp sensor : sensorList) {
            Sensor entity = new Sensor();
            BeanUtils.copyProperties(sensor, entity);
            sensorService.addCronJob(entity);
        }
    }*/

    /*@Override
    public void doActuator(@RequestParam("objectId") String objectId, @RequestBody ExecIftttReq execIftttReq) {
        RuleResp ruleResp = execIftttReq.getRuleResp();
        SensorVo sensor = execIftttReq.getSensor();
        Map<String, Object> callbackMap = execIftttReq.getCallbackMap();
        APIType apiType = execIftttReq.getApiType();
        iftttCallback.doActuator(objectId, ruleResp, sensor, callbackMap, apiType);
    }*/

   /* @Override
    public void addAstroClockJob(Long tenantId) {
        astroClockService.addAstroClockJob(tenantId);
    }*/

    @Override
    public List<RuleResp> getRuleListByName(@RequestBody SaveIftttReq ruleVO) {
        List<RuleResp> list = iftttService.getRuleListByName(ruleVO);
        return list;
    }

    @Override
    public boolean findTemplateListByName(@RequestBody SaveIftttTemplateReq iftttReq) {
        return iftttService.findTemplateListByName(iftttReq);
    }
}
