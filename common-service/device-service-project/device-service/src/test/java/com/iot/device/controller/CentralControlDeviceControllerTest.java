package com.iot.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.iot.device.BaseTest;
import com.iot.device.model.Device;
import com.iot.device.model.DeviceType;
import com.iot.device.model.Product;
import com.iot.device.service.IDeviceService;
import com.iot.device.service.IDeviceTypeService;
import com.iot.device.service.IProductService;
import com.iot.device.vo.req.DeviceReq;
import com.iot.device.vo.rsp.DeviceTypeResp;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xfz
 * @Descrpiton:
 * @Date: 8:33 2018/4/23
 * @Modify by:
 */
public class CentralControlDeviceControllerTest extends BaseTest {

    @Override
    public String getBaseUrl() {
        return "/device-central/";
    }

    @Test
    public void saveDevice() throws Exception{

        DeviceReq device = new DeviceReq();
        mockPost("saveDevice", device);
    }

    @Test
    public void getDeviceByDeviceId() {
        Map<String, String> params = new HashMap<>();
        params.put("deviceId", "00e647dae2cb8d403c168fce7e4628e0");
        mockGet("getDeviceByDeviceId", params);
    }

    @Test
    public void findUserDeviceListByUserIdAndDeviceIds() {
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add("00e647dae2cb8d403c168fce7e4628e0");

        mockPost("findUserDeviceListByUserIdAndDeviceIds?userId=1232", deviceIds);
    }

    @Test
    public void findDirectDeviceListByVenderCode() {

        Map<String, String> params = new HashMap<>();
        params.put("venderCode", "00e647dae2cb8d403c168fce7e4628e0");
        mockGet("findDirectDeviceListByVenderCode", params);
    }

    @Test
    public void deleteDeviceByDeviceId() {

        Map<String, String> params = new HashMap<>();
        params.put("deviceId", "2434534");
        mockDelete("deleteDeviceByDeviceId", params);
    }

    @Test
    public void updateDevice() {
        DeviceReq device = new DeviceReq();
        mockPost("updateDevice", device);
    }

    @Test
    public void findUnDirectDeviceListByParentDeviceId() {

        Map<String, String> params = new HashMap<>();
        params.put("parentDeviceId", "00e647dae2cb8d403c168fce7e4628e0");
        mockGet("findUnDirectDeviceListByParentDeviceId", params);
    }

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IDeviceTypeService deviceTypeService;
    @Test
    public void getDeviceTypeByDeviceId() {

        DeviceTypeResp deviceTypeResp = null;
        EntityWrapper<Device> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", "00e647dae2cb8d403c168fce7e4628e0");
        Device device = deviceService.selectOne(wrapper);
        if (device != null) {
            if (device.getProductId() != null && device.getProductId() > 0) {
                Product product = productService.selectById(device.getProductId());
                if (product != null) {
                    DeviceType deviceType = deviceTypeService.selectById(product.getDeviceTypeId());
                    if (deviceType != null) {
                        deviceTypeResp = new DeviceTypeResp();
                        BeanUtils.copyProperties(deviceType, deviceTypeResp);

                    }
                }
            }
        }
    }

    @Test
    public void findBusinessTypeList() {

        mockGet("findBusinessTypeList", null);
    }

}