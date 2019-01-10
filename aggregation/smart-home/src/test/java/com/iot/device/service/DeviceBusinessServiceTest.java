package com.iot.device.service;

import com.iot.saas.SaaSContextHolder;
import com.iot.smarthome.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:11 2018/11/14
 * @Modify by:
 */
public class DeviceBusinessServiceTest extends BaseTest {

    @Autowired
    private DeviceBusinessService deviceBusinessService;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getUnBindPlanDeviceList() {
        //1154986580@qq.com
        SaaSContextHolder.setCurrentTenantId(2L);
        SaaSContextHolder.setCurrentUserId(1474L);
        deviceBusinessService.getUnBindPlanDeviceList();

    }
}