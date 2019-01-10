package com.iot.device.core.service;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 19:30 2018/6/27
 * @Modify by:
 */
public class DeviceStatusServiceCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void findDeviceStatusListByDeviceIds() {
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add("feb2444d2443486b9e5e46dada719cd8");
        deviceIds.add("ef4e94876c07490ba209b0c46b6e1104");
        deviceIds.add("1001");
        DeviceStatusServiceCoreUtils.findDeviceStatusListByDeviceIds(0L, deviceIds);
    }
}