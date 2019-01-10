package com.iot.device.core;

import com.iot.device.BaseTest;
import org.junit.Test;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:49 2018/6/22
 * @Modify by:
 */
public class DeviceStateCacheCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getCacheDeviceStateByDeviceId() {
        DeviceStateCacheCoreUtils.getCacheDeviceStateByDeviceId("1001");
    }

    @Test
    public void updateCacheDeviceState() {
        DeviceStateCacheCoreUtils.updateCacheDeviceState("1001", "CCT", "1000");
    }

    @Test
    public void recoveryCacheDeviceState() {
        DeviceStateCacheCoreUtils.recoveryCacheDeviceState("1001");

    }
}