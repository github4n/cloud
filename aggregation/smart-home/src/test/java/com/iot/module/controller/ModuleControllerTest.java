package com.iot.module.controller;

import com.google.common.collect.Maps;
import com.iot.saas.SaaSContextHolder;
import com.iot.smarthome.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 13:56 2018/10/23
 * @Modify by:
 */
@Slf4j
public class ModuleControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/module/";
    }

    @Test
    public void findServiceModuleListByProductId() {
        SaaSContextHolder.setCurrentTenantId(2L);
        Map<String, String> params = Maps.newHashMap();
        params.put("productId", "1090210500");
        mockGet("v1/findModuleListByProductId", params);

    }
}