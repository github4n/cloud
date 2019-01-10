package com.iot.shcs.ifttt;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.helper.Page;
import com.iot.control.ifttt.api.IftttApi;
import com.iot.control.ifttt.vo.ActuatorVo;
import com.iot.control.ifttt.vo.SensorVo;
import com.iot.control.ifttt.vo.req.RuleListReq;
import com.iot.control.ifttt.vo.res.RuleResp;
import com.iot.shcs.security.domain.Security;
import com.iot.shcs.security.mapper.SecurityRuleMapper;
import com.iot.shcs.security.service.SecurityRuleService;
import com.iot.shcs.security.service.SecurityService;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.security.vo.SecurityRule;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class SecurityTest {
    @Autowired
    private SecurityRuleService securityRuleService;
    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityMqttService securityMqttService;

    @Autowired
    private IftttApi iftttApi;

    @Test
    public void moveData(){

        System.out.println("======迁移开始:");
        //取出旧ifttt数据
        RuleListReq req = new RuleListReq();
        //安防类型为1
        req.setRuleType((byte) 1);
        req.setTemplateFlag((byte) 0);
        Page<RuleResp> page =iftttApi.list(req);
        List<RuleResp> oldList = page.getResult();
        int x=0;
        for(RuleResp vo : oldList){

            System.out.println("vovovo******************:vo"+vo.toString());

            Map<String, Object> thenMap = Maps.newHashMap();
            Map<String, Object> ifMap = Maps.newHashMap();
            List<Map<String, Object>> triggerList = Lists.newArrayList();
            List<Map<String, Object>> thenList = Lists.newArrayList();

            RuleResp ruleResp=iftttApi.get(vo.getId());
            System.out.println("ruleResp******************:"+ruleResp.toString());
            //设置if
            List<SensorVo> sensorList = ruleResp.getSensors();
            if (CollectionUtils.isNotEmpty(sensorList)) {
                for (SensorVo sensor : sensorList) {
                    String properties = sensor.getProperties();
                    Map<String, Object> triggerMap = JSON.parseObject(properties, Map.class);
                    triggerList.add(triggerMap);
                }
            }
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
                securityRuleService.saveSecurityRule(securityRule);
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
                securityRuleService.saveSecurityRule(securityRule);
            }

        }

        System.out.println("======迁移完rule:");
    }


}
