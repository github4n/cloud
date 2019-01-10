package com.iot.portal.web.controller;

import com.google.common.collect.Maps;
import com.iot.BaseTest;
import com.iot.saas.SaaSContextHolder;
import org.junit.Test;

import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:57 2018/7/4
 * @Modify by:
 */
public class PortalServiceModuleControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/portal/serviceModule/";
    }

    @Test
    public void findServiceModuleListByDeviceTypeId() {
    }

    @Test
    public void findServiceModuleListAll() {
    }

    @Test
    public void findServiceModuleListByProductId() {
        SaaSContextHolder.setCurrentTenantId(2L);
        Map<String, String> params = Maps.newHashMap();
        params.put("productId", "1090210874");
        mockGet("findServiceModuleListByProductId", params);
    }

    @Test
    public void findServiceModuleListByServiceModuleIds() {

    }

    @Test
    public void updateServiceModuleInfo() {
    }

}