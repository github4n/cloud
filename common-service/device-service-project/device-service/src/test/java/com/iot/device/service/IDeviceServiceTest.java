package com.iot.device.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.iot.device.BaseTest;
import com.iot.device.comm.utils.exceltools.FastExcel;
import com.iot.device.model.Device;
import com.iot.device.utils.DeviceImportDataVo;
import com.iot.device.vo.req.device.ListDeviceByParamsReq;
import com.iot.device.vo.rsp.DevicePropertyInfoResp;
import com.iot.device.vo.rsp.DeviceResp;
import com.iot.device.vo.rsp.device.ListDeviceByParamsRespVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 10:58 2018/4/23
 * @Modify by:
 */
public class IDeviceServiceTest extends BaseTest {

    @Autowired
    private IDeviceService deviceService;

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Test
    public void findDirectDeviceListByVenderCode() {

        deviceService.findDirectDeviceListByVenderCode(11L, 1L,"MultiProtocolGatetay",1);
    }

    @Test
    public void findDeviceListByIsDirectDeviceAndUserId() {

//        deviceService.findDeviceListByIsDirectDeviceAndUserId(0L, 1, 175L);
    }

    @Test
    public void findDeviceListByDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add("00e647dae2cb8d403c168fce7e4628e0");
        List<DevicePropertyInfoResp> respList = deviceService.findDeviceListByDeviceIds(deviceIds);
        System.out.println(respList);
    }

    @Test
    public void findPage() {
        Page<Device> page = new Page<>(1,100);

        deviceService.selectPage(page);
    }

    @Test
    public void findDevListByDeviceIds() {

    }


    @Test
    public void findDevPageByProductId() {

        com.iot.common.helper.Page<DeviceResp> respPage = deviceService.findDevPageByProductId(1, 25, 1L);
    }

    @Test
    public void deviceDataImport() throws Exception {
        InputStream is = IDeviceServiceTest.class.getClassLoader().getResourceAsStream("device.xls");
        FastExcel fastExcel = new FastExcel(is);
        fastExcel.setStartRow(1);
        List<DeviceImportDataVo> dataVos = fastExcel.parse(DeviceImportDataVo.class);

        if (!CollectionUtils.isEmpty(dataVos)) {
            for (DeviceImportDataVo vo : dataVos
                    ) {
                Device device = new Device();
                device.setUuid(vo.getDeviceId());
                if (!StringUtils.isEmpty(vo.getProductId())) {
                    device.setProductId(Long.parseLong(vo.getProductId()));
                } else {
                    continue;
                }
                device.setIsDirectDevice(Integer.parseInt(vo.getIsDirectDevice()));

                try {
                    deviceService.insert(device);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    @Test
    public void listDeviceByParams() {
        ListDeviceByParamsReq params =
                ListDeviceByParamsReq.builder()
//                        .deviceType("a")
//                        .productId(1324L)
//                        .deviceTypeId(23432L)
                        .deviceIds(Lists.newArrayList("8cb5e4d553924ff2bad967523cdd6b70", "a6cb2641297211b18c3e0c32cc1ddbd6"))
                        .build();

        List<ListDeviceByParamsRespVo> resultDataList = deviceService.listByParams(params);
        System.out.println(JSON.toJSONString(resultDataList));

    }

}