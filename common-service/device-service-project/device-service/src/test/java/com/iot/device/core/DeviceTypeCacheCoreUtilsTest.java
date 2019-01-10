package com.iot.device.core;

import com.iot.device.BaseTest;
import com.iot.device.comm.cache.VersionEnum;
import com.iot.device.model.DeviceType;
import org.junit.Test;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:58 2018/6/22
 * @Modify by:
 */
public class DeviceTypeCacheCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getCacheDeviceTypeByDeviceTypeId() {
        DeviceTypeCacheCoreUtils.getCacheData(1001L, VersionEnum.V1);
    }

    @Test
    public void updateCacheDeviceType() {
        DeviceType deviceType = new DeviceType();
        deviceType.setType("1111");
        deviceType.setId(1001L);
        deviceType.setName("deviceType_test");
        deviceType.setTenantId(0L);
        DeviceTypeCacheCoreUtils.cacheData(1001L, deviceType, VersionEnum.V1);
    }
}