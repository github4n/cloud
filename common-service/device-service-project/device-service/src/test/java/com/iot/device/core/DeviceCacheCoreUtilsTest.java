package com.iot.device.core;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import com.iot.device.model.Device;
import org.junit.Test;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:42 2018/6/22
 * @Modify by:
 */
public class DeviceCacheCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getCacheDeviceInfoByDeviceId() {
        Device device = DeviceCacheCoreUtils.getCacheDeviceInfoByDeviceId("ef4e94876c07490ba209b0c46b6e1104");
        System.out.println(device);
    }

    @Test
    public void getCacheDeviceInfoListByParentDeviceId() {

        //7a86c1f079944e0caeb5a2a9fa4c865d
        List<Device> deviceList = DeviceCacheCoreUtils.getCacheDeviceInfoListByParentDeviceId("001001");
        System.out.println(deviceList);
    }

    @Test
    public void getCacheDeviceInfoListByDeviceIds() {
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add("7a86c1f079944e0caeb5a2a9fa4c865d");
        deviceIds.add("ef4e94876c07490ba209b0c46b6e1104");
        deviceIds.add("1001");
        List<Device> deviceList = DeviceCacheCoreUtils.getCacheDeviceInfoListByDeviceIds(deviceIds);
        System.out.println(deviceList);
    }

    @Test
    public void updateCacheDeviceInfo() {
        Device device = new Device();
        device.setUuid("1001");
        device.setParentId("001001");
        device.setName("xfz_test");
        device.setProductId(10001L);
        device.setIcon("default_icon");
        DeviceCacheCoreUtils.updateCacheDeviceInfo("1001", device);
    }

    @Test
    public void addCacheChildDevice() {
        Device device = new Device();
        device.setUuid("1001");
        device.setParentId("001001");
        device.setName("xfz_test");
        device.setProductId(10001L);
        device.setIcon("default_icon");
        DeviceCacheCoreUtils.addCacheChildDevice("001001", "1001");
    }

    @Test
    public void removeCacheDeviceInfo() {
        DeviceCacheCoreUtils.removeCacheDeviceInfo("1001");
    }

    @Test
    public void removeCacheChildDevice() {
        DeviceCacheCoreUtils.removeCacheChildDevice("001001", "1001");
    }
}