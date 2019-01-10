package com.iot.device.api;

import com.iot.device.api.fallback.ElectricityStatisticsApiFallbackFactory;
import com.iot.device.vo.req.*;
import com.iot.device.vo.rsp.DailyElectricityStatisticsResp;
import com.iot.device.vo.rsp.ElectricityStatisticsRsp;
import com.iot.device.vo.rsp.MonthlyElectricityStatisticsResp;
import com.iot.device.vo.rsp.RuntimeRsp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Api(tags = "电量上报接口")
@FeignClient(value = "device-service", fallbackFactory = ElectricityStatisticsApiFallbackFactory.class)
@RequestMapping("/ElectricityStatistics")
public interface ElectricityStatisticsApi {

	@ApiOperation("上报设备电量")
    @RequestMapping(value = "/insertElectricityStatistics", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean insertElectricityStatistics(@RequestBody ElectricityStatisticsReq electrictyStatistics);

	@ApiOperation("更新设备电量")
    @RequestMapping(value = "/insertOrUpdateElectricityStatistics", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertOrUpdateElectricityStatistics();

	@ApiOperation("获取设备当天电量")
    @RequestMapping(value = "/selectElectricityRspByDeviceAndUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ElectricityStatisticsRsp selectElectricityRspByDeviceAndUser(@RequestBody EnergyReq energyReq);

	@ApiOperation("上报设备运行时间")
    @RequestMapping(value = "/insertRuntime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean insertRuntime(@RequestBody RuntimeReq runtimeReq);

	@ApiOperation("更新设备运行时间")
    @RequestMapping(value = "/insertOrUpdateRuntime", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void insertOrUpdateRuntime();

	@ApiOperation("获取设备当天运行时间")
    @RequestMapping(value = "/selectRuntimeRspByDeviceAndUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public RuntimeRsp selectRuntimeRspByDeviceAndUser(@RequestBody RuntimeReq2Runtime runtimeReq);

	@ApiOperation("测试Job")
    @RequestMapping(value = "/testJob", method = RequestMethod.GET)
	public void testJob();

	@ApiOperation("获取设备电量报表")
    @RequestMapping(value = "/getEnergyTab", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getEnergyTab(@RequestBody EnergyReq energyReq);

	@ApiOperation("获取设备运行时间报表")
    @RequestMapping(value = "/getRuntimeTab", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getRuntimeTab(@RequestBody RuntimeReq2Runtime runtimeReq);

	@ApiOperation("上报小时设备电量")
	@RequestMapping(value = "/insertMinElectricityStatistics", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	boolean insertMinElectricityStatistics(@RequestBody List<ElectricityStatisticsReq> list);

	@ApiOperation("上报天设备电量")
	@RequestMapping(value = "/insertDailyElectricityStatistics", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean insertDailyElectricityStatistics(@RequestBody List<DailyElectricityStatisticsReq> list);

	@ApiOperation("上报月设备电量")
	@RequestMapping(value = "/insertMonthElectricityStatistics", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean insertMonthElectricityStatistics(@RequestBody List<MonthlyElectricityStatisticsReq> list);

	@ApiOperation("获取电量统计列表")
	@RequestMapping(value = "/getMinListByReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<ElectricityStatisticsRsp> getMinListByReq(@RequestBody ElectricityStatisticsReq req);

	@ApiOperation("获取天电量统计列表")
	@RequestMapping(value = "/getDailyListByReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<DailyElectricityStatisticsResp> getDailyListByReq(@RequestBody DailyElectricityStatisticsReq req);

	@ApiOperation("获取月电量统计列表")
	@RequestMapping(value = "/getMonthlyListByReq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	List<MonthlyElectricityStatisticsResp> getMonthlyListByReq(@RequestBody MonthlyElectricityStatisticsReq req);

}
