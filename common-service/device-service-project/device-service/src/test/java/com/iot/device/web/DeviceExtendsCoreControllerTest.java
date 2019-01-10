package com.iot.device.web;

import com.iot.device.BaseTest;
import org.junit.Test;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 18:01 2018/10/31
 * @Modify by:
 */
public class DeviceExtendsCoreControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/deviceExtendsCore/";
    }

    @Test
    public void listDeviceExtends() {
    }

    @Test
    public void get() {
    }

    @Test
    public void saveOrUpdate() {
        String jsonStr = "{\"deviceId\":\"026261b830ae43dfa20e162e6be43a61\",\"tenantId\":2,\"batchNumId\":null,\"p2pId\":null,\"uuidValidityDays\":null\n" +
                ",\"deviceCipher\":null,\"firstUploadSubDev\":null,\"unbindFlag\":0,\"resetFlag\":0,\"batchNum\":null,\"area\":\"Asia/Shanghai\"}";

        mockPost("saveOrUpdate", jsonStr);
    }

    @Test
    public void saveOrUpdateBatch() {
    }
}