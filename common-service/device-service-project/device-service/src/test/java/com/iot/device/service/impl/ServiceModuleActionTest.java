package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IServiceModuleActionService;
import com.iot.device.vo.rsp.ServiceModuleActionResp;
import com.iot.saas.SaaSContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ServiceModuleActionTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private IServiceModuleActionService serviceModuleActionService;

    @Test
    public void listByModuleIdTest(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        Long id = 2515L;
        List<ServiceModuleActionResp> list = serviceModuleActionService.listByServiceModuleId(id);
        Assert.assertNotNull(list);
    }

    @Test
    public void delete(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1143L);
        serviceModuleActionService.delete(ids);
    }
}
