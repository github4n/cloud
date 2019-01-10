package com.iot.shcs.security.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.exception.BusinessException;
import com.iot.common.helper.Page;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.vo.ActuatorVo;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.RuleListReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.shcs.device.vo.ControlReq;
import com.iot.shcs.security.api.SecurityApi;
import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.service.SecurityRuleService;
import com.iot.shcs.security.service.SecurityService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.security.vo.SecurityRule;
import com.iot.shcs.security.vo.rsp.IntentResultResp;
import com.iot.shcs.security.vo.rsp.SecurityResp;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SecurityController implements SecurityApi {


    @Autowired
    private SecurityRuleService securityRuleService;
    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityMqttService securityMqttService;

    @Autowired
    private IftttApi iftttApi;

    @Override
    public SecurityResp getBySpaceId(Long spaceId) {
        return null;
    }

    @Override
    public IntentResultResp arm(ControlReq controlVo) throws BusinessException {
        return null;
    }

    @Override
    public IntentResultResp panic(ControlReq controlVo) throws BusinessException {
        return null;
    }

    @Override
    public IntentResultResp statusQuery(ControlReq controlVo) throws BusinessException {
        return null;
    }

    @Override
    public String doMoveData(@RequestParam("isMove") int doMove) {

        if(doMove!=1){
            return "暗号不对，不迁移";
        }

        //取出旧ifttt数据
        RuleListReq req = new RuleListReq();
        //安防类型为1
        req.setRuleType((byte) 1);
        req.setTemplateFlag((byte) 0);
        Page<RuleResp> page =iftttApi.list(req);
        List<RuleResp> oldList = page.getResult();
        for(RuleResp vo : oldList){
            Map<String, Object> thenMap = Maps.newHashMap();
            Map<String, Object> ifMap = Maps.newHashMap();
            List<Map<String, Object>> triggerList = Lists.newArrayList();
            List<Map<String, Object>> thenList = Lists.newArrayList();

            RuleResp ruleResp=iftttApi.get(vo.getId());

            //设置if
            List<SensorVo> sensorList = ruleResp.getSensors();
            if (CollectionUtils.isNotEmpty(sensorList)) {
                for (SensorVo sensor : sensorList) {
                    String properties = sensor.getProperties();
                    Map<String, Object> triggerMap = JSON.parseObject(properties, Map.class);
                    triggerList.add(triggerMap);
                }
            }

            //排除掉脏数据
            if(CollectionUtils.isEmpty(triggerList)){
                continue;
            }
            ifMap.put("trigger", triggerList);

            Integer delay=0;
            //设置then
            List<ActuatorVo> actuatorList = ruleResp.getActuators();
            if (CollectionUtils.isNotEmpty(actuatorList)) {
                for (ActuatorVo actuator : actuatorList) {
                    String properties = actuator.getProperties();
                    Map<String, Object> propertiesMap = JSON.parseObject(properties, Map.class);
                    thenList.add(propertiesMap);
                    delay=actuator.getDelay();
                }
            }

            //排除掉脏数据
            if(CollectionUtils.isEmpty(actuatorList)){
                continue;
            }

            Long spaceId=vo.getSpaceId();
            Long tenantId=vo.getTenantId();
            Security security=securityService.getBySpaceId(spaceId,tenantId);
            Long securityId=security.getId();
            SecurityRule securityRule=securityRuleService.selectBySecurityIdAndType(tenantId,securityId,vo.getSecurityType());
            if(securityRule!=null){
                securityRule.setTenantId(vo.getTenantId());
                securityRule.setSecurityId(securityId);
                securityRule.setDefer(vo.getDelay());
                securityRule.setEnabled(vo.getStatus().intValue());
                securityRule.setType(vo.getSecurityType());
                securityRule.setDelay(delay);
                securityRule.setThenCondition(JSON.toJSONString(thenList));
                securityRule.setIfCondition(JSON.toJSONString(ifMap));
                securityRule.setCreateTime(vo.getCreateTime());
                securityRule.setUpdateTime(vo.getUpdateTime());
                securityRuleService.saveSecurityRuleForMoveData(securityRule,false);
            }else {
                securityRule=new SecurityRule();
                securityRule.setTenantId(vo.getTenantId());
                securityRule.setSecurityId(securityId);
                securityRule.setDefer(vo.getDelay());
                securityRule.setEnabled(vo.getStatus().intValue());
                securityRule.setType(vo.getSecurityType());
                securityRule.setDelay(delay);
                securityRule.setThenCondition(JSON.toJSONString(thenList));
                securityRule.setIfCondition(JSON.toJSONString(ifMap));
                securityRule.setUpdateTime(vo.getUpdateTime());
                securityRuleService.saveSecurityRuleForMoveData(securityRule,true);
            }
        }
        return "数据迁移完成";
    }
}
