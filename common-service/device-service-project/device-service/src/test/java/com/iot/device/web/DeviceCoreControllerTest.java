package com.iot.device.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.iot.device.BaseTest;
import com.iot.device.vo.req.DevTypePageReq;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.req.device.ListDeviceInfoReq;
import com.iot.device.vo.req.device.PageDeviceInfoReq;
import org.junit.Test;

import java.util.Map;

/**
 * @Author: lucky
 * @Descrpiton:
 * @Date: 8:50 2018/10/29
 * @Modify by:
 */
public class DeviceCoreControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/deviceCore/";
    }

    @Test
    public void getDeviceTypeByDeviceId() {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("deviceId", "8b39e845b90a45dfb4726bca07cb675e");
        mockGet("getDeviceTypeByDeviceId", paramMap);
    }

    @Test
    public void listDevices() {

        mockPost(
                "listDevices",
                JSON.parseObject("{\"deviceIds\":[\"5c44f2f9968f443688e23323d1370b77\",\"e9ff2ea0cbfdfc4df62d83c212f064f4\"\n"
                        + ",\"c8d63bc365089cc414b1a7fc1ba24720\",\"d940b75496fb702859ff856546790f28\",\"5092d18162c3afa94871bacb728b87b5\",\"9ceebbb6f3644cde9cdf43bd5db2bd77\",\"3ea6a032c1fa0829f098f6f0f3010460\"],\"batchSize\":null}", ListDeviceInfoReq.class));
    }


    @Test
    public void get() {
    }

    @Test
    public void saveOrUpdate() {
    }

    @Test
    public void updateByCondition() {
    }

    @Test
    public void saveOrUpdateBatch() {
    }

    @Test
    public void listDevicesByParentId() {
    }

    @Test
    public void deleteByDeviceId() {
    }

    @Test
    public void deleteBatchByDeviceIds() {
    }

    @Test
    public void getProductByDeviceId() {
    }


    @Test
    public void pageDeviceInfoByParams() {
        PageDeviceInfoReq params = PageDeviceInfoReq.builder()
                .tenantId(1L)
                .orgId(10L)
                .build();
        mockPost("pageDeviceInfoByParams", params);
    }

    @Test
    public void pageDeviceByDeviceTypeList() {
        DevTypePageReq pageReq = DevTypePageReq.builder()
                .pageNum(1)
                .pageSize(100)
                .tenantId(1L)
                .orgId(10L)
                .build();

        mockPost("pageDeviceByDeviceTypeList", pageReq);
    }

    @Test
    public void listDeviceByParams() {
        ListDeviceByParamsReq params = ListDeviceByParamsReq.builder()
                .tenantId(1L)
                .orgId(10L)
                .build();
        mockPost("listDeviceByParams", params);
    }

    @Test
    public void findDevRelationListByDeviceIds() {
    }
}