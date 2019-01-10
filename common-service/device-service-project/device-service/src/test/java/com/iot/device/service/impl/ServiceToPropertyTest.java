package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IServiceToPropertyService;
import com.iot.device.vo.rsp.ServiceToPropertyRsp;
import com.iot.saas.SaaSContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ServiceToPropertyTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private IServiceToPropertyService serviceToPropertyService;

    @Test
    public void listByModuleIdTest(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(2515L);
        List<ServiceToPropertyRsp> list = serviceToPropertyService.listByServiceModuleId(ids);
        Assert.assertNotNull(list);
    }

    @Test
    public void delete(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(4864L);
        serviceToPropertyService.delete(ids);
    }
}
