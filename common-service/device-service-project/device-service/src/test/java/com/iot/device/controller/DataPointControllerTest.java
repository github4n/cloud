package com.iot.device.controller;

import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 19:01 2018/4/23
 * @Modify by:
 */
public class DataPointControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/dataPoint/";
    }

    @Test
    public void findDataPointListByDeviceId() {
        Map<String, String> params = new HashMap<>();
        params.put("deviceId", "af6a637582de8c1a1e332f55b1547c");

        mockGet("findDataPointListByDeviceId", params);
    }
}