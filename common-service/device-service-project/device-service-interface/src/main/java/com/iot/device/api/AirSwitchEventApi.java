package com.iot.device.api;

import com.iot.device.api.fallback.AirSwitchEventApiFallbackFactory;
import com.iot.device.vo.req.AirSwitchDailyEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchHourEventStatisticsReq;
import com.iot.device.vo.req.AirSwitchMonthEventStatisticsReq;
import com.iot.device.vo.rsp.AirSwitchDailyEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchHourEventStatisticsResp;
import com.iot.device.vo.rsp.AirSwitchMonthEventStatisticsResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
@Api(tags = "云端设备接口")
@FeignClient(value = "device-service" , fallbackFactory = AirSwitchEventApiFallbackFactory.class)
@RequestMapping("/air/switch/event")
public interface AirSwitchEventApi {

    @ApiOperation("批量保存事件")
    @RequestMapping(value = "saveEventBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveEventBatch(@RequestBody List<AirSwitchEventReq> list);

    @ApiOperation("批量小时保存事件")
    @RequestMapping(value = "saveAndUpdateHourEventBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAndUpdateHourEventBatch(@RequestBody List<AirSwitchHourEventStatisticsReq> list);

    @ApiOperation("查询事件列表")
    @RequestMapping(value = "queryList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AirSwitchEventRsp> queryList(@RequestBody AirSwitchEventReq req);

    @ApiOperation("查询小时的事件列表")
    @RequestMapping(value = "queryHourEventList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AirSwitchHourEventStatisticsResp> queryHourEventList(@RequestBody AirSwitchHourEventStatisticsReq req);

    @ApiOperation("查询天的事件列表")
    @RequestMapping(value = "queryDailyEventList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AirSwitchDailyEventStatisticsResp> queryDailyEventList(@RequestBody AirSwitchDailyEventStatisticsReq req);

    @ApiOperation("查询月的事件列表")
    @RequestMapping(value = "queryMonthEventList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AirSwitchMonthEventStatisticsResp> queryMonthEventList(@RequestBody AirSwitchMonthEventStatisticsReq req);

    @ApiOperation("批量保存天事件")
    @RequestMapping(value = "saveAndUpdateDailyEventBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAndUpdateDailyEventBatch(@RequestBody List<AirSwitchDailyEventStatisticsReq> list);

    @ApiOperation("批量保存月事件")
    @RequestMapping(value = "saveAndUpdateMonthEventBatch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveAndUpdateMonthEventBatch(List<AirSwitchMonthEventStatisticsReq> list);
}
