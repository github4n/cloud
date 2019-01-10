package com.iot.control.device.controller;

import com.google.common.collect.Lists;
import com.iot.control.BaseTest;
import com.iot.control.device.vo.req.GetUserDeviceInfoReq;
import com.iot.control.device.vo.req.ListUserDeviceInfoReq;
import org.junit.Test;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 17:06 2018/11/6
 * @Modify by:
 */
public class UserDeviceControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/control/userDeviceCore/";
    }

    @Test
    public void saveOrUpdate() {
    }

    @Test
    public void saveOrUpdateBatch() {
    }

    @Test
    public void listUserDevice() {
        ListUserDeviceInfoReq userDeviceInfoReq = ListUserDeviceInfoReq.builder()
                .tenantId(2L)
//                .userId(1360L)
                .deviceId("2cdfe16898eb42659d168ca266a445f0")
                .build();
        mockPost("listUserDevice", userDeviceInfoReq);
    }

    @Test
    public void listBatchUserDevice() {

        GetUserDeviceInfoReq userDeviceInfoReq = GetUserDeviceInfoReq.builder()
                .tenantId(2L)
//                .userId(1360L)
                .deviceIds(Lists.newArrayList("2cdfe16898eb42659d168ca266a445f0"))
                .build();
        mockPost("listBatchUserDevice", userDeviceInfoReq);
    }

    @Test
    public void delUserDevice() {
    }

    @Test
    public void pageUserDevice() {
    }
}