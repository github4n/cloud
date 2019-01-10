package com.iot.device.service.impl;

import com.iot.device.BaseTest;
import com.iot.device.service.IServiceToActionService;
import com.iot.device.vo.rsp.ServiceToActionRsp;
import com.iot.saas.SaaSContextHolder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ServiceToActionServiceTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return null;
    }

    @Autowired
    private IServiceToActionService serviceToActionService;

    @Test
    public void listByServiceModuleIdTest(){
        SaaSContextHolder.setCurrentTenantId(60L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1931L);
        List<ServiceToActionRsp> list = serviceToActionService.listByServiceModuleId(ids);
        Assert.assertNotNull(list);
    }

    @Test
    public void delete(){
        SaaSContextHolder.setCurrentTenantId(-1L);
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(1598L);
        serviceToActionService.delete(ids);
    }


}
