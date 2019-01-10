package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IServiceToEventService;
import com.iot.device.vo.rsp.ServiceToEventRsp;
import com.iot.saas.SaaSContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ServiceToEventServiceTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    IServiceToEventService serviceToEventService;

    @Test
    public void listByModuleIdTest(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(2515L);
        List<ServiceToEventRsp> list = serviceToEventService.listByServiceModuleId(ids);
        Assert.assertNotNull(list);
    }
    @Test
    public void delete(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(2222L);
        serviceToEventService.delete(ids);
    }
}
