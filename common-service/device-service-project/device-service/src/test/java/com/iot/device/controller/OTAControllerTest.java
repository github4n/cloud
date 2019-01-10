package com.iot.device.controller;

import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 17:38 2018/5/4
 * @Modify by:
 */
public class OTAControllerTest extends BaseTest {
    @Override
    public String getBaseUrl() {
        return "/ota/";
    }

    @Test
    public void addOtaVersion() {
    }

    @Test
    public void findOtaVersionPage() {
    }

    @Test
    public void getOtaInfoByUserIdAndDeviceId() {
    }

    @Test
    public void addOrUpdateOtaLog() {
    }

    @Test
    public void getOtaNewInfoByProductId() {
    }

    @Test
    public void getOtaInfoLog() {
    }

    @Test
    public void getOtaInfoById() {
    }

    @Test
    public void getOtaNewInfoByFwType() {
    }

    @Test
    public void getOtaNewInfoByFwTypeAndProductId() {
    }

    @Test
    public void findOtaNewInfoListByProductId() {
        Map<String, String> params = new HashMap<>();
        params.put("productId", "1");
        mockGet("findOtaNewInfoListByProductId", params);

    }
}