package com.iot.device.queue.process;

import com.alibaba.fastjson.JSON;
import com.iot.device.BaseTest;
import com.iot.device.queue.bean.DeviceStateMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 16:18 2018/10/22
 * @Modify by:
 */
@Slf4j
public class DeviceStateMessageProcessTest extends BaseTest {

    @Autowired
    private DeviceStateMessageProcess deviceStateMessageProcess;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void processMessage() {
        DeviceStateMessage stateMessage =
                JSON.parseObject(
                        "{\"createTime\":1540195508922,\"params\":[{\"deviceId\":\"c8798d508dc2fa16d36708fec8db5801\",\"stateList\":[{\"propertyName\":\"OnOff\",\"propertyValue\":\"0\"}],\"tenantId\":1}]}",
                        DeviceStateMessage.class);
        deviceStateMessageProcess.processMessage(stateMessage);

    }
}