package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IServiceModulePropertyService;
import com.iot.device.vo.rsp.ServiceModulePropertyResp;
import com.iot.saas.SaaSContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceModulePropertyTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private IServiceModulePropertyService serviceModulePropertyService;

    @Test
    public void listByModuleIdTest(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        Long id = 2515L;
        List<ServiceModulePropertyResp> list = serviceModulePropertyService.listByServiceModuleId(id);
        Assert.assertNotNull(list);
    }
}
