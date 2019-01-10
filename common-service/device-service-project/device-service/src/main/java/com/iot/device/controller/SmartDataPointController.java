package com.iot.device.controller;

import com.iot.device.api.SmartDataPointApi;
import com.iot.device.service.ISmartDataPointService;
import com.iot.device.vo.rsp.voicebox.SmartDataPointResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descrpiton:
 * @Author: yuChangXing
 * @Date: 2018/10/29 11:11
 * @Modify by:
 */

@RestController
public class SmartDataPointController implements SmartDataPointApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartDataPointController.class);

    @Autowired
    private ISmartDataPointService iSmartDataPointService;


    @Override
    public SmartDataPointResp getSmartDataPoint(@RequestParam(value = "tenantId") Long tenantId,
                                                @RequestParam(value = "propertyId") Long propertyId,
                                                @RequestParam(value = "smart") Integer smart) {
        return iSmartDataPointService.getSmartDataPoint(tenantId, propertyId, smart);
    }
}
