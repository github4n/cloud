package com.iot.device.controller;

import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 8:33 2018/4/23
 * @Modify by:
 */

public class DeviceTypeControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/devType/";
    }

    @Test
    public void getDeviceTypeById() {
        Map<String, String> params = new HashMap<>();
        params.put("deviceTypeId", "-1019");
        mockGet("getDeviceTypeById", params);


    }
}