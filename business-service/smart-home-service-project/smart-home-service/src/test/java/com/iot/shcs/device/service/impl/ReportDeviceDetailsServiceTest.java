package com.iot.shcs.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.mqttsdk.common.MqttMsg;
import com.iot.saas.SaaSContextHolder;
import com.iot.shcs.BaseTest;
import com.iot.shcs.device.service.ReportDeviceDetailsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportDeviceDetailsServiceTest extends BaseTest {

    @Autowired
    private ReportDeviceDetailsService reportDeviceDetailsService;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void doUpdateDevDetails() {
        SaaSContextHolder.setCurrentTenantId(1L);

        String topic = "iot/v1/s/d73b92f7c8df46f28613913d0263bc40/device/updateDevDetails";
        String json = "{\"topic\":\"iot/v1/s/d73b92f7c8df46f28613913d0263bc40/device/updateDevDetails\",\"service\":\"device\",\"method\":\"updateDevDetails\",\"seq\":\"b4b7b2ffc8ba46d9bee0bf3b334c5baf\",\"srcAddr\":\"1.d73b92f7c8df46f28613913d0263bc40\",\"payload\":{\"subDev\":[ { \"devId\": \"513f68f6e2fdae74e479c850b2b3ffb0\", \"productId\": \"lds.magnet.zbdwsensors0000\", \"devType\": \"lds.magnet.zbdwsensors0000\", \"online\": 1, \"attr\": { } }, { \"devId\": \"f1d5d3734a90dcde9ac4e67d59fe5103\", \"productId\": \"lds.magnet.zbdwsensors0000\", \"devType\": \"lds.magnet.zbdwsensors0000\", \"online\": 1, \"attr\": { } }, { \"devId\": \"0f30abc4a4e7f9069a6021c191e35573\", \"productId\": \"lds.light.zbtdimmablelight\", \"devType\": \"lds.light.zbtdimmablelight\", \"online\": 0, \"attr\": { } }, { \"devId\": \"042d2fecd31d22f55a50777af7d75acc\", \"productId\": \"lds.motion.zbmotionsensors0000\", \"devType\": \"lds.motion.zbmotionsensors0000\", \"online\": 0, \"attr\": { } }, { \"devId\": \"d91e54091dafa9de4951b9cbb4ecea6d\", \"productId\": \"lds.light.zllcolortemperature\", \"devType\": \"lds.light.zllcolortemperature\", \"online\": 0, \"attr\": { } }, { \"devId\": \"f6d2421c9258748228219f6931e1bb93\", \"productId\": \"lds.light.zhadimmablelight\", \"devType\": \"lds.light.zhadimmablelight\", \"online\": 0, \"attr\": { } }, { \"devId\": \"1295a58b068aaf13a05aa1e710bea3f3\", \"productId\": \"lds.light.zbtextendedcolor\", \"devType\": \"lds.light.zbtextendedcolor\", \"online\": 0, \"attr\": { } } ],\"timestamp\":\"2018-12-24T15:04:44+0800\"},\"ack\":{\"code\":200,\"desc\":\"OK\"}}";
        MqttMsg reqMqttMsg = JSON.parseObject(json, MqttMsg.class);
        reportDeviceDetailsService.doUpdateDevDetails(reqMqttMsg, topic);
    }
}