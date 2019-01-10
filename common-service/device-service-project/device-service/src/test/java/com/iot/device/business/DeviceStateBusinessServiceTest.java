package com.iot.device.business;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:10 2018/10/22
 * @Modify by:
 */
@Slf4j
public class DeviceStateBusinessServiceTest extends BaseTest {

    @Autowired
    private DeviceStateBusinessService deviceStateBusinessService;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void listBatchDeviceStates() {
        Map<String, Map<String, Object>> resultDataMap = deviceStateBusinessService.listBatchDeviceStates(1L, Lists.newArrayList("c8798d508dc2fa16d36708fec8db5801"));
        log.info(JSON.toJSONString(resultDataMap) + "========");
    }

    @Test
    public void listBatchDeviceStates1() {
    }
}