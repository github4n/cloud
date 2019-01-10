package com.iot.device.core.service;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import com.iot.device.model.DeviceType;
import org.junit.Test;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:56 2018/6/22
 * @Modify by:
 */
public class DeviceTypeServiceCoreUtilsTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void getDeviceTypeByDeviceTypeId() {
        DeviceTypeServiceCoreUtils.getDeviceTypeByDeviceTypeId(1L);
    }

    @Test
    public void cacheDeviceTypeList() {
        List<DeviceType> deviceTypeList = Lists.newArrayList();
        DeviceType deviceType = new DeviceType();
        deviceType.setType("1111");
        deviceType.setId(1001L);
        deviceType.setName("deviceType_test");
        deviceType.setTenantId(0L);
        deviceTypeList.add(deviceType);
        DeviceTypeServiceCoreUtils.cacheDeviceTypeList(deviceTypeList);
    }
}