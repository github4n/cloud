package com.iot.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.iot.device.BaseTest;
import com.iot.device.model.DeviceState;
import com.iot.device.repository.DeviceStateRepository;
import com.iot.device.service.IDeviceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 14:07 2018/6/26
 * @Modify by:
 */
public class DeviceServiceImplTest extends BaseTest {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private DeviceStateRepository deviceStateRepository;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void findDeviceListByIsDirectDeviceAndUserId() {
//        deviceService.findDeviceListByIsDirectDeviceAndUserId(0L, null, 27L);
    }

    @Test
    public void testInsert() {
        DeviceState log = new DeviceState();
        log.setDeviceId("a945bed270e17e8865197d81cc2a055d");
        log.setGroupId(1L);
        log.setLogDate(new Date());
        log.setProductId(1090210078L);
        log.setPropertyDesc("OnOff");
        log.setPropertyName("OnOff");
        log.setPropertyValue("1");
        deviceStateRepository.insert(log);
    }

    @Test
    public void testFindByPropertyName() {
        List<DeviceState> deviceStateLogs = deviceStateRepository.findByPropertyName("OnOff");
        System.out.printf(JSON.toJSONString(deviceStateLogs));
    }
}