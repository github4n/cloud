package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IServiceModuleEventService;
import com.iot.device.vo.rsp.ServiceModuleEventResp;
import com.iot.saas.SaaSContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceModuleEventTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private IServiceModuleEventService serviceModuleEventService;

    @Test
    public void listByModuleIdTest(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        Long id = 2515L;
        List<ServiceModuleEventResp> list = serviceModuleEventService.listByServiceModuleId(id);
        Assert.assertNotNull(list);
    }
}
