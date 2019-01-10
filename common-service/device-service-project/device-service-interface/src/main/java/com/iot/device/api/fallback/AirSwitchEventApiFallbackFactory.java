package com.iot.device.api.fallback;

import com.alibaba.fastjson.JSON;
import com.iot.device.api.AirSwitchEventApi;
import com.iot.device.vo.req.AirSwitchDailyEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.req.AirSwitchHourEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchMonthEventStatisticsReq;
import com.iot.device.vo.rsp.AirSwitchDailyEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import com.iot.device.vo.rsp.AirSwitchHourEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchMonthEventStatisticsResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/12
 * @Description: *
 */
@Component
public class AirSwitchEventApiFallbackFactory implements FallbackFactory<AirSwitchEventApi> {

    private Logger log = LoggerFactory.getLogger(AirSwitchEventApiFallbackFactory.class);

    @Override
    public AirSwitchEventApi create(Throwable throwable) {
        return new AirSwitchEventApi() {
            @Override
            public void saveEventBatch(List<AirSwitchEventReq> list) {
                log.info(JSON.toJSONString(list));
            }

            @Override
            public void saveAndUpdateHourEventBatch(List<AirSwitchHourEventStatisticsReq> list) {

            }

            @Override
            public List<AirSwitchEventRsp> queryList(AirSwitchEventReq req) {
                return null;
            }

            @Override
            public List<AirSwitchHourEventStatisticsResp> queryHourEventList(AirSwitchHourEventStatisticsReq req) {
                return null;
            }

            @Override
            public List<AirSwitchDailyEventStatisticsResp> queryDailyEventList(AirSwitchDailyEventStatisticsReq req) {
                return null;
            }

            @Override
            public List<AirSwitchMonthEventStatisticsResp> queryMonthEventList(AirSwitchMonthEventStatisticsReq req) {
                return null;
            }

            @Override
            public void saveAndUpdateDailyEventBatch(List<AirSwitchDailyEventStatisticsReq> list) {

            }

            @Override
            public void saveAndUpdateMonthEventBatch(List<AirSwitchMonthEventStatisticsReq> list) {

            }
        };
    }
}