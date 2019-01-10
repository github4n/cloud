package com.iot.building.reservation.api;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.building.reservation.api.callback.ReservationApiFallbackFactory;
import com.iot.building.reservation.vo.ReservationReq;
import com.iot.building.reservation.vo.ReservationResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author: Xieby
 * @Date: 2018/9/3
 * @Description: *
 */
@Api("会议预约接口")
@FeignClient(value = "building-control-service" , fallbackFactory = ReservationApiFallbackFactory.class)
@RequestMapping("/reservaton")
public interface ReservationApi {

    @ApiOperation("添加预约")
    @RequestMapping(value = "/saveReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Boolean saveReservation(@RequestBody ReservationReq reservationReq);

    @ApiOperation("更新预约")
    @RequestMapping(value = "/updateReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateReservation(@RequestBody ReservationReq reservationReq);

    @ApiOperation("预约列表")
    @RequestMapping(value = "/findByReservationList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ReservationResp> findByReservationList(@RequestBody ReservationReq req);

    @ApiOperation("删除预约")
    @RequestMapping(value = "/delReservationById", method = RequestMethod.GET)
    public void delReservationById(@RequestParam("tenantId") Long tenantId, @RequestParam("id") Long id);

    @ApiOperation("查询最近的预约信息")
    @RequestMapping(value = "/findNearResercationByOpenId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResp findNearResercationByOpenId(@RequestBody ReservationReq req);

    @ApiOperation("当前预约状态")
    @RequestMapping(value = "/currentReservationStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResp currentReservationStatus(@RequestBody ReservationReq req);

    @ApiOperation("开始的预约")
    @RequestMapping(value = "/getStartReservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationResp getStartReservation(@RequestBody ReservationReq req);

}
