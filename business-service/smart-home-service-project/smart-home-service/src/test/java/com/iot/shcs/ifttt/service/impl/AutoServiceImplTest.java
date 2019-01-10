package com.iot.shcs.ifttt.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoServiceImplTest {

    @Autowired
    private AutoServiceImpl autoService;

    @Test
    public void delBleAuto() {
    }

    @Test
    public void getAutoDetail() {
    }

    @Test
    public void addAutoRule() {
        SaaSContextHolder.setCurrentTenantId(2L);
        String json = "{\"payload\": {\"autoId\": \"2057\", \"enable\": true, \"enableDelay\": 0," +
                "\"if\": {\"logic\":\"or\",\"valid\":{\"begin\": \"00:00\",\"end\": \"00:00\",\" week\": [0, 1, 2, 3, 4, 5, 6]}," +
                "    \"trigger\": [{\"idx\": 0,\"trigType\": \"timer\",\"at\": \"11:05\",\"repeat\": [0, 1, 2, 3, 4, 5, 6]}]}," +
                "        \"then\": [{\"idx\": 0,\"thenType\": \"dev\",\"parentId\": \"\",\"id\": \"9d08f865fc364c8eb950b207432fd9e3\"," +
                "            \"attr\": {\"OnOff\": 0}}]" +
                "    }" +
                "}";
        AddAutoRuleReq req = JSON.parseObject(json, AddAutoRuleReq.class);
        req.setAutoId("2057");
        autoService.addAutoRule(req);
        System.out.println();

    }

    @Test
    public void editAutoRule() {
        SaaSContextHolder.setCurrentTenantId(2L);
        String json = "{\"payload\": {\"autoId\": \"2057\", \"enable\": 0, \"enableDelay\": 0," +
                "\"if\": {\"logic\":\"or\",\"valid\":{\"begin\": \"00:00\",\"end\": \"00:00\",\" week\": [0, 1, 2, 3, 4, 5, 6]}," +
                "    \"trigger\": [{\"idx\": 0,\"trigType\": \"timer\",\"at\": \"17:05\",\"repeat\": [0, 1, 2, 3, 4, 5, 6]}]}," +
                "        \"then\": [{\"idx\": 0,\"thenType\": \"dev\",\"parentId\": \"\",\"id\": \"9d08f865fc364c8eb950b207432fd9e3\"," +
                "            \"attr\": {\"OnOff\": 1}}]" +
                "    }" +
                "}";
        AddAutoRuleReq req = JSON.parseObject(json, AddAutoRuleReq.class);
        req.setAutoId("2057");
        req.setTenantId(2l);
        req.setUserId(1197l);
        autoService.editAutoRule(req);
        System.out.println();
    }

    @Test
    public void setAutoEnable() {
        SaaSContextHolder.setCurrentTenantId(2L);
        String json = "{\"payload\": {\"autoId\": \"2057\", \"enable\": 0, \"enableDelay\": 0," +
                "\"if\": {\"logic\":\"or\",\"valid\":{\"begin\": \"00:00\",\"end\": \"00:00\",\" week\": [0, 1, 2, 3, 4, 5, 6]}," +
                "    \"trigger\": [{\"idx\": 0,\"trigType\": \"timer\",\"at\": \"17:05\",\"repeat\": [0, 1, 2, 3, 4, 5, 6]}]}," +
                "        \"then\": [{\"idx\": 0,\"thenType\": \"dev\",\"parentId\": \"\",\"id\": \"9d08f865fc364c8eb950b207432fd9e3\"," +
                "            \"attr\": {\"OnOff\": 0}}]" +
                "    }" +
                "}";
        AddAutoRuleReq req = JSON.parseObject(json, AddAutoRuleReq.class);
        req.setAutoId("2057");
        req.setTenantId(2l);
        req.setUserId(1197l);
        autoService.setAutoEnable(req);
        System.out.println();
    }
}