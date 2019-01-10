package com.iot.device.service;

import com.iot.device.vo.req.AirSwitchDailyEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.req.AirSwitchHourEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchMonthEventStatisticsReq;
import com.iot.device.vo.rsp.AirSwitchDailyEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import com.iot.device.vo.rsp.AirSwitchHourEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchMonthEventStatisticsResp;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
public interface IAirSwitchEventService {

    Boolean insertOrUpdateBatch(List<AirSwitchEventReq> list);

    List<AirSwitchEventRsp> queryList(AirSwitchEventReq req);

    List<AirSwitchHourEventStatisticsResp> queryHourEventList(AirSwitchHourEventStatisticsReq req);

    List<AirSwitchDailyEventStatisticsResp> queryDailyEventList(AirSwitchDailyEventStatisticsReq req);

    List<AirSwitchMonthEventStatisticsResp> queryMonthEventList(AirSwitchMonthEventStatisticsReq req);

    Boolean insertOrUpdateHourEventBatch(List<AirSwitchHourEventStatisticsReq> list);

    Boolean insertOrUpdateDailyEventBatch(List<AirSwitchDailyEventStatisticsReq> list);

    Boolean insertOrUpdateMonthEventBatch(List<AirSwitchMonthEventStatisticsReq> list);
}
