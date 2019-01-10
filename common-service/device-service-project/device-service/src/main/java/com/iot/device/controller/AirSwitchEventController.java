package com.iot.device.controller;

import com.iot.device.api.AirSwitchEventApi;
import com.iot.device.vo.req.AirSwitchDailyEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.service.IAirSwitchEventService;
import com.iot.device.vo.req.AirSwitchHourEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchMonthEventStatisticsReq;
import com.iot.device.vo.rsp.AirSwitchDailyEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import com.iot.device.vo.rsp.AirSwitchHourEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchMonthEventStatisticsResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
@RestController
public class AirSwitchEventController implements AirSwitchEventApi {

    @Autowired
    private IAirSwitchEventService airSwitchEventService;

    @Override
    public void saveEventBatch(@RequestBody List<AirSwitchEventReq> list) {
        airSwitchEventService.insertOrUpdateBatch(list);
    }

    @Override
    public List<AirSwitchEventRsp> queryList(@RequestBody AirSwitchEventReq req) {
        return airSwitchEventService.queryList(req);
    }

    @Override
    public void saveAndUpdateHourEventBatch(@RequestBody List<AirSwitchHourEventStatisticsReq> list) {
        airSwitchEventService.insertOrUpdateHourEventBatch(list);
    }

    @Override
    public List<AirSwitchHourEventStatisticsResp> queryHourEventList(@RequestBody AirSwitchHourEventStatisticsReq req) {
        return airSwitchEventService.queryHourEventList(req);
    }

    @Override
    public List<AirSwitchDailyEventStatisticsResp> queryDailyEventList(@RequestBody AirSwitchDailyEventStatisticsReq req) {
        return airSwitchEventService.queryDailyEventList(req);
    }

    @Override
    public List<AirSwitchMonthEventStatisticsResp> queryMonthEventList(@RequestBody AirSwitchMonthEventStatisticsReq req) {
        return airSwitchEventService.queryMonthEventList(req);
    }

    @Override
    public void saveAndUpdateDailyEventBatch(@RequestBody List<AirSwitchDailyEventStatisticsReq> list) {
        airSwitchEventService.insertOrUpdateDailyEventBatch(list);
    }

    @Override
    public void saveAndUpdateMonthEventBatch(@RequestBody List<AirSwitchMonthEventStatisticsReq> list) {
        airSwitchEventService.insertOrUpdateMonthEventBatch(list);
    }
}
