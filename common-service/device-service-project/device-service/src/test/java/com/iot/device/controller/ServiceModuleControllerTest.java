package com.iot.device.controller;

import com.iot.device.BaseTest;
import com.iot.saas.SaaSContextHolder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ServiceModuleControllerTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return "/module/action/";
    }

    @Test
    public void findActionListByServiceModuleIdTest() {
        SaaSContextHolder.setCurrentTenantId(-1L);
        Map<String, String> params = new HashMap<>();
        params.put("serviceModuleId", "2515");
        mockGet("findActionListByServiceModuleId", params);

    }
}
