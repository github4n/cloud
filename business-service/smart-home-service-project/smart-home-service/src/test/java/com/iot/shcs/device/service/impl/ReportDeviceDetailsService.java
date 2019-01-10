package com.iot.shcs.device.service.impl;

import com.iot.shcs.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 15:53 2018/12/19
 * @Modify by:
 */
public class ReportDeviceDetailsService extends BaseTest{

  @Autowired
  private com.iot.shcs.device.service.ReportDeviceDetailsService reportDeviceDetailsService;

  @Override
  public String getBaseUrl() {
    return null;
  }

  @Test
  public void doUpdateDevDetails() {
    String content = "{\"topic\":\"iot/v1/s/a447405e70234250b2bcfd10d5f5afc2/device/updateDevDetails\",\"service\":\"device\",\"method\":\"updateDevDetails\",\"seq\":\"ebe0af6386234e0e9e66bb9382b4959d\",\"srcAddr\":\"1.a447405e70234250b2bcfd10d5f5afc2\",\"payload\":{\"subDev\":[ { \"devId\": \"f0baa11f6be0d92db462b7dbe7f09b40\", \"productId\": \"lds.keypad.zbkeypadallv2\", \"devType\": \"lds.keypad.zbkeypadallv2\", \"online\": 1, \"attr\": { } }, { \"devId\": \"973d42b35cf2b7852a8889181882ba5a\", \"productId\": \"lds.keypad.zbkeypadallv2\", \"devType\": \"lds.keypad.zbkeypadallv2\", \"online\": 1, \"attr\": { } }, { \"devId\": \"69a9fd599023540036610dfd1ff6f50a\", \"productId\": \"lds.keypad.zbkeypadallv2\", \"devType\": \"lds.keypad.zbkeypadallv2\", \"online\": 1, \"attr\": { } } ],\"timestamp\":\"2018-12-19T15:11:16+0800\"},\"ack\":{\"code\":200,\"desc\":\"OK\"}}";
    String topic = "iot/v1/s/a447405e70234250b2bcfd10d5f5afc2/device/updateDevDetails";
    //reportDeviceDetailsService.doUpdateDevDetails(JSON.parseObject(content, MqttMsg.class), topic);
  }
}