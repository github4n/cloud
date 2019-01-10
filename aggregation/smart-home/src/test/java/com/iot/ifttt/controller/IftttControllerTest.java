package com.iot.ifttt.controller;

import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.ifttt.vo.req.AddAutoRuleReq;
import com.iot.smarthome.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Ignore
public class IftttControllerTest extends BaseTest{
    @Autowired
    private IftttController iftttController;

    @Test
    public void setAutoEnable() {
        SaaSContextHolder.setCurrentTenantId(2l);
        SaaSContextHolder.setCurrentUserId(1324l);
        SaaSContextHolder.setCurrentUserUuid("c2b81ad6b7cb43b2aa157ccc11d80661");
        AddAutoRuleReq req = new AddAutoRuleReq();
        req.setAutoId("1485");
        req.setEnable(0);

        iftttController.setAutoEnable(req);

    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}