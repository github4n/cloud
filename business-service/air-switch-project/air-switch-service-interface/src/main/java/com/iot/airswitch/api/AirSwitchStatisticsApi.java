package com.iot.airswitch.api;

import com.iot.airswitch.vo.req.SwitchElectricityStatisticsReq;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsHeadResp;
import com.iot.airswitch.vo.resp.SwitchElectricityStatisticsTopResp;
import com.iot.airswitch.vo.resp.SwtichElectricityStatisticsChartResp;
import com.iot.device.vo.req.AirSwitchEventReq;
import com.iot.device.vo.rsp.AirSwitchEventRsp;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: Xieby
 * @Date: 2018/11/8
 * @Description: *
 */
@Api("空开统计接口")
@FeignClient(value = "air-switch-service")
@RequestMapping("/air/switch/statistics")
public interface AirSwitchStatisticsApi {

    @RequestMapping(value = "getStatisticsHeadInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SwitchElectricityStatisticsHeadResp getStatisticsHeadInfo(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "getStatisticsChartInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SwtichElectricityStatisticsChartResp getStatisticsChartInfo(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "getStatisticsTopInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SwitchElectricityStatisticsTopResp getStatisticsTopInfo(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "countHourEvent", method = RequestMethod.POST)
    void countHourEvent(@RequestParam("tenantId") Long tenantId, @RequestParam("day") String day, @RequestParam("hour") String hour);

    @RequestMapping(value = "getEventList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<AirSwitchEventRsp> getEventList(@RequestBody AirSwitchEventReq req);

    @RequestMapping(value = "getEventStatisticsInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SwtichElectricityStatisticsChartResp getEventStatisticsInfo(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "getEventTopInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    SwitchElectricityStatisticsTopResp getEventTopInfo(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "countElectricity", method = RequestMethod.POST)
    void countElectricity(@RequestParam("tenantId") Long tenantId);

    @RequestMapping(value = "countAirSwitchEvent", method = RequestMethod.POST)
    void countAirSwitchEvent(@RequestParam("tenantId") Long tenantId);

    @RequestMapping(value = "getBusinessElectricityInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<DailyElectricityStatisticsResp> getBusinessElectricityInfo(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "getTotalElectricityList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<Map> getTotalElectricityList(@RequestBody SwitchElectricityStatisticsReq req);

    @RequestMapping(value = "syncElectricity", method = RequestMethod.POST)
    void syncElectricity(@RequestParam("date") String date, @RequestParam("tenantId") Long tenantId);

}
