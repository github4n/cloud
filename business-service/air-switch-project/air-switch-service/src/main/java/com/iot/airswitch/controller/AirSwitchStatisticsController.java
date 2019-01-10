package com.iot.airswitch.controller;

import com.iot.airswitch.api.AirSwitchStatisticsApi;
import com.iot.airswitch.service.AirSwitchStatisticsService;
import com.iot.airswitch.vo.req.SwitchElectricityStatisticsReq;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsHeadResp;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsTopResp;
import com.iot.airswitch.vo.resp.SwtichElectricityStatisticsChartResp;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
@RestController
public class AirSwitchStatisticsController implements AirSwitchStatisticsApi {

    @Autowired
    private AirSwitchStatisticsService airSwitchStatisticsService;

    @Override
    public SwitchElectricityStatisticsHeadResp getStatisticsHeadInfo(@RequestBody  SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getStatisticsHeadInfo(req);
    }

    @Override
    public SwtichElectricityStatisticsChartResp getStatisticsChartInfo(@RequestBody SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getStatisticsChartInfo(req);
    }

    @Override
    public SwitchElectricityStatisticsTopResp getStatisticsTopInfo(@RequestBody SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getStatisticsTopInfo(req);
    }

    @Override
    public void countHourEvent(@RequestParam("tenantId") Long tenantId, @RequestParam("day") String day, @RequestParam("hour") String hour) {
        airSwitchStatisticsService.countHourEvent(tenantId, day);
    }

    @Override
    public List<AirSwitchEventRsp> getEventList(@RequestBody AirSwitchEventReq req) {
        return airSwitchStatisticsService.getEventList(req);
    }

    @Override
    public SwtichElectricityStatisticsChartResp getEventStatisticsInfo(@RequestBody SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getEventStatisticsInfo(req);
    }

    @Override
    public SwitchElectricityStatisticsTopResp getEventTopInfo(@RequestBody SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getEventTopInfo(req);
    }

    @Override
    public void countElectricity(@RequestParam("tenantId") Long tenantId) {
        airSwitchStatisticsService.countElectricity(tenantId);
    }

    @Override
    public void countAirSwitchEvent(@RequestParam("tenantId") Long tenantId) {
        airSwitchStatisticsService.countAirSwitchEvent(tenantId);
    }

    @Override
    public List<DailyElectricityStatisticsResp> getBusinessElectricityInfo(@RequestBody SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getBusinessElectricityInfo(req);
    }

    @Override
    public List<Map> getTotalElectricityList(@RequestBody SwitchElectricityStatisticsReq req) {
        return airSwitchStatisticsService.getTotalElectricityList(req);
    }

    @Override
    public void syncElectricity(@RequestParam("date") String date,@RequestParam("tenantId") Long tenantId) {
        airSwitchStatisticsService.syncElectricity(date, tenantId);
    }
}
