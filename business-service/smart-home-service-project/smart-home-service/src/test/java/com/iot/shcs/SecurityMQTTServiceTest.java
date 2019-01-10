package com.iot.shcs;

import com.alibaba.fastjson.JSON;
import com.iot.device.vo.rsp.device.GetDeviceInfoRespVo;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.security.service.impl.SecurityMqttService;
import com.iot.shcs.security.vo.SecurityRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class SecurityMQTTServiceTest {

    @Autowired
    private SecurityMqttService securityMqttService;

    @Test
    public void testGetSecurityRuleReq() {
        SaaSContextHolder.setCurrentTenantId(1L);
        String topic = "iot/v1/s/b1979c1e5afc4d5096adb03079f0f2c6/security/getSecurityRuleReq";
        String json = "{\"method\":\"getSecurityRuleReq\",\"payload\":{\"securityType\":\"away\",\"homeId\":\"749\"},\"seq\":\"269588068\",\"service\":\"security\",\"srcAddr\":\"0.150e7d75d9ec44f691b222a410e1a748\"}";
        MqttMsg msg = JSON.parseObject(json, MqttMsg.class);
        securityMqttService.getSecurityRuleReq(msg, topic);
    }

    @Test
    public void testDeleteSubSecurityInfo(){
        securityMqttService.deleteSubDevSecurityInfo(2L, "a4eab429f5f037c4ac0439cd04a713c5");
    }
}
