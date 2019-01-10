package com.iot.smarthome.controller;

import com.iot.smarthome.api.DeviceClassifyApi;
import com.iot.smarthome.model.DeviceClassify;
import com.iot.smarthome.service.impl.DeviceClassifyServiceImpl;
import com.iot.smarthome.util.BeanCopyUtil;
import com.iot.smarthome.vo.resp.DeviceClassifyResp;
import com.iot.smarthome.vo.resp.ThirdPartyInfoResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/12/13 9:39
 * @Modify by:
 */

@Slf4j
@RestController
public class DeviceClassifyController implements DeviceClassifyApi {
    @Autowired
    private DeviceClassifyServiceImpl deviceClassifyService;

    @Override
    public DeviceClassifyResp getDeviceClassifyByProductId(@RequestParam("productId") Long productId) {
        DeviceClassifyResp deviceClassifyResp = null;
        DeviceClassify deviceClassify = deviceClassifyService.getDeviceClassifyByProductId(productId);
        if (deviceClassify != null) {
            deviceClassifyResp = new DeviceClassifyResp();
            BeanCopyUtil.copyDeviceClassify(deviceClassifyResp, deviceClassify);
        }

        return deviceClassifyResp;
    }

    @Override
    public DeviceClassifyResp getByTypeCode(String typeCode) {
        DeviceClassifyResp deviceClassifyResp = null;
        DeviceClassify deviceClassify = deviceClassifyService.getByTypeCode(typeCode);
        if (deviceClassify != null) {
            deviceClassifyResp = new DeviceClassifyResp();
            BeanCopyUtil.copyDeviceClassify(deviceClassifyResp, deviceClassify);
        }

        return deviceClassifyResp;
    }
}
