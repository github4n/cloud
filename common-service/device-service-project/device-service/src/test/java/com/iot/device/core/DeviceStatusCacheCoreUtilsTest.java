package com.iot.device.core;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import com.iot.device.model.DeviceStatus;
import org.junit.Test;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:52 2018/6/22
 * @Modify by:
 */
public class DeviceStatusCacheCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getCacheDeviceStatusByDeviceId() {
        DeviceStatus deviceStatus = DeviceStatusCacheCoreUtils.getCacheDeviceStatusByDeviceId(0L, "1001");
        System.out.println(deviceStatus);
    }

    @Test
    public void getCacheDeviceStatusListByDeviceIds() {
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add("1001");
        List<DeviceStatus> deviceStatusList = DeviceStatusCacheCoreUtils.getCacheDeviceStatusListByDeviceIds(deviceIds);
        System.out.println(deviceStatusList);
    }

    @Test
    public void updateCacheDeviceStatus() {
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setDeviceId("1001");
        deviceStatus.setActiveStatus(0);
        DeviceStatusCacheCoreUtils.updateCacheDeviceStatus(0L, "1001", deviceStatus);
    }
}