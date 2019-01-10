package com.iot.device.service.core;

import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import org.junit.Test;

import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 15:12 2018/6/25
 * @Modify by:
 */
public class DeviceCoreServiceTest extends BaseTest {


    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void delDevByUserIdAndDeviceId() {
    }

    @Test
    public void findDeviceListByUserId() {
        Long userId = 147L;
        List<String> deviceIds = Lists.newArrayList();
        deviceIds.add("c501d3f53a2f4f2bab8fb2fbd95c5744");
        deviceIds.add("13640897c264400dad382d1e7c35498e");
        deviceIds.add("b45b9125f04940ed9112ccdb5da25ee3");
//
//        deviceIds.add("78d0ee6821bc2433e0f80d55939d1946");
    }
}