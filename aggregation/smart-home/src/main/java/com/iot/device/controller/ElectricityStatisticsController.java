package com.iot.device.controller;

import com.iot.common.beans.CommonResponse;
import com.iot.device.api.ElectricityStatisticsApi;
import com.iot.device.vo.req.EnergyReq;
import com.iot.device.vo.req.RuntimeReq2Runtime;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import com.iot.util.AssertUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/device")
public class ElectricityStatisticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricityStatisticsController.class);
    @Autowired
    ElectricityStatisticsApi electricityStatisticsApi;
    @Autowired
    private UserApi userApi;

    @ApiOperation(value = "电量报表获取")
    @RequestMapping(value = "/getEnergy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Map<String, Object>> getEnergy(@RequestBody EnergyReq energyReq) {
        LOGGER.debug("getEnergy({})", energyReq);

        String userId = energyReq.getCookieUserId();
        FetchUserResp user = userApi.getUserByUuid(userId);
        AssertUtils.notEmpty(user, "user.notnull");
        energyReq.setUserId(user.getId());

        // 从redis缓存中获取电量报表信息
        Map<String, Object> dataMap = electricityStatisticsApi.getEnergyTab(energyReq);
        LOGGER.debug("getEnergyTab({})", dataMap);
        return CommonResponse.success(dataMap);
    }

    @ApiOperation(value = "运行时间获取")
    @RequestMapping(value = "/getRuntime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<Map<String, Object>> getRuntime(@RequestBody RuntimeReq2Runtime runtimeReq) {
        LOGGER.debug("getRuntime({})", runtimeReq);

        String userId = runtimeReq.getCookieUserId();
        FetchUserResp user = userApi.getUserByUuid(userId);
        AssertUtils.notEmpty(user, "user.notnull");
        runtimeReq.setUserId(user.getId());

        // 从redis缓存中获取运行时间报表信息
        Map<String, Object> dataMap = electricityStatisticsApi.getRuntimeTab(runtimeReq);
        LOGGER.debug("getRuntimeTab({})", dataMap);
        return CommonResponse.success(dataMap);

    }

}
