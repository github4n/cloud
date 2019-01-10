package com.iot.device.core.service;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import com.iot.device.model.Device;
import org.junit.Test;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 16:19 2018/6/22
 * @Modify by:
 */
public class DeviceServiceCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getDeviceInfoByDeviceId() {

        DeviceServiceCoreUtils.getDeviceInfoByDeviceId("07f7d869d38b44c2be0226ff7ff3bab3");
    }

    @Test
    public void findDeviceListByDeviceIds() {
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add("7a86c1f079944e0caeb5a2a9fa4c865d");
        deviceIds.add("ef4e94876c07490ba209b0c46b6e1104");
        deviceIds.add("10012");
        List<Device> deviceList = DeviceServiceCoreUtils.findDeviceListByDeviceIds(deviceIds);
        System.out.println(deviceList);
    }

    @Test
    public void findChildDeviceListByParentDeviceId() {
        DeviceServiceCoreUtils.findChildDeviceListByParentDeviceId("0e9c5f69a1d5431b88d09e55f052f6c3");
    }

    @Test
    public void cacheDevices() {
    }

    @Test
    public void cacheDevice() {
        Device device = new Device();
        device.setUuid("1001");
        device.setParentId("001001");
        device.setName("xfz_test");
        device.setProductId(10001L);
        device.setIcon("default_icon");
        DeviceServiceCoreUtils.cacheDevice(device);
    }

    @Test
    public void cacheChildDeviceByParentDeviceId() {
    }

    @Test
    public void removeCacheChildDeviceByParentDeviceId() {

    }

    @Test
    public void findCacheDevicesByUserId() {

    }
}