package com.iot.center.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.airswitch.vo.req.SwitchElectricityStatisticsReq;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsHeadResp;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsTopResp;
import com.iot.airswitch.vo.resp.SwtichElectricityStatisticsChartResp;
import com.iot.center.service.AirSwitchService;
import com.iot.common.beans.CommonResponse;
import com.iot.saas.SaaSContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/11/6
 * @Description: *
 */
@RestController
@RequestMapping("air/switch/")
public class AirSwitchController {

    @Autowired
    private AirSwitchService airSwitchService;

    @RequestMapping("getStatisticsHeadInfo")
    public CommonResponse<SwitchElectricityStatisticsHeadResp> getStatisticsHeadInfo(SwitchElectricityStatisticsReq req) {
        return airSwitchService.getStatisticsHeadInfo(req, getTenantId());
    }

    @RequestMapping("getStatisticsChartInfo")
    public CommonResponse<SwtichElectricityStatisticsChartResp> getStatisticsChartInfo(SwitchElectricityStatisticsReq req) {
        return airSwitchService.getStatisticsChartInfo(req, getTenantId());
    }

    @RequestMapping("getStatisticsTopInfo")
    public CommonResponse<SwitchElectricityStatisticsTopResp> getStatisticsTopInfo(SwitchElectricityStatisticsReq req) {
        return airSwitchService.getStatisticsTopInfo(req, getTenantId());
    }

    @RequestMapping("getEventChartInfo")
    public CommonResponse<SwtichElectricityStatisticsChartResp> getEventChartInfo(SwitchElectricityStatisticsReq req) {
        return airSwitchService.getEventChartInfo(req, getTenantId());
    }

    @RequestMapping("getEventTopInfo")
    public CommonResponse<SwitchElectricityStatisticsTopResp> getEventTopInfo(SwitchElectricityStatisticsReq req) {
        return airSwitchService.getEventTopInfo(req, getTenantId());
    }

    @RequestMapping("getBusinessElectricityInfo")
    public CommonResponse<List> getBusinessElectricityInfo() {
        return airSwitchService.getBusinessElectricityInfo(getTenantId());
    }

    @RequestMapping("setServerAddress")
    public CommonResponse setServerAddress(String deviceId, String serverAddress) {
        return airSwitchService.setServerAddress(getTenantId(), deviceId, serverAddress);
    }

    @RequestMapping("leakageTest")
    public CommonResponse leakageTest(String deviceId) {
        return airSwitchService.leakageTest(deviceId, getTenantId());
    }

    @RequestMapping("switchOn")
    public CommonResponse switchOn(String deviceId) {
        return airSwitchService.switchOn(deviceId, getTenantId());
    }

    @RequestMapping("switchOff")
    public CommonResponse switchOff(String deviceId) {
        return airSwitchService.switchOff(deviceId, getTenantId());
    }

    @RequestMapping("setRTVI")
    public CommonResponse setRTVI(String deviceId, Integer interval) {
        return airSwitchService.setRTVI(deviceId, interval, getTenantId());
    }

    @RequestMapping("test")
    public CommonResponse test() {
        return airSwitchService.test();
    }

    @RequestMapping("syncEle")
    public CommonResponse syncEle(String date, Long tenantId) {
        return airSwitchService.syncEle(date, tenantId);
    }

    public Long getTenantId() {
//        return 11L;
        return SaaSContextHolder.currentTenantId();
    }

}
