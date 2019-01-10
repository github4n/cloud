package com.iot.portal.report.controller;

import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.beans.SearchParam;
import com.iot.common.exception.ResultMsg;
import com.iot.portal.report.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：报表控制层
 * 创建人： maochengyuan
 * 创建时间：2019/1/4 10:20
 * 修改人： maochengyuan
 * 修改时间：2019/1/4 10:20
 * 修改描述：
 */
@Api(description = "报表服务")
@RestController
@RequestMapping("/reportController")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取首页概览", notes = "获取首页概览")
    @RequestMapping(value = "/getOverview", method = RequestMethod.GET)
    public CommonResponse getOverview() {
        return ResultMsg.SUCCESS.info(this.reportService.getOverview());
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询地区激活统计信息", notes = "查询地区激活统计信息")
    @RequestMapping(value = "/getActiveDistributionData", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "查询周期（1:前一天，7：近7天，14：近14天，30：近30天）", required = true, paramType = "query", dataType = "String")
    })
    public CommonResponse getActiveDistributionData(@RequestParam("cycle") String cycle) {
        return ResultMsg.SUCCESS.info(this.reportService.getActiveDistributionData(cycle));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询地区活跃统计信息", notes = "查询地区活跃统计信息")
    @RequestMapping(value = "/getActivateDistributionData", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "查询周期（1:前一天，7：近7天，14：近14天，30：近30天）", required = true, paramType = "query", dataType = "String")
    })
    public CommonResponse getActivateDistributionData(@RequestParam("cycle") String cycle) {
        return ResultMsg.SUCCESS.info(this.reportService.getActivateDistributionData(cycle));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取用户活跃页面数据", notes = "获取用户活跃页面数据")
    @RequestMapping(value = "/getUserActiveAll", method = RequestMethod.GET)
    public CommonResponse getUserActiveAll() {
        return ResultMsg.SUCCESS.info(this.reportService.getUserActiveAll());
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "按周期获取用户活跃数据", notes = "按周期获取用户活跃数据")
    @RequestMapping(value = "/getUserActiveByCycle", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "查询周期（1:前一天，7：近7天，14：近14天，30：近30天）", required = true, paramType = "query", dataType = "String")
    })
    public CommonResponse getUserActiveByCycle(@RequestParam("cycle") String cycle) {
        return ResultMsg.SUCCESS.info(this.reportService.getUserActiveByCycle(cycle));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取用户激活页面数据", notes = "获取用户激活页面数据")
    @RequestMapping(value = "/getUserActivatedAll", method = RequestMethod.GET)
    public CommonResponse getUserActivatedAll() {
        return ResultMsg.SUCCESS.info(this.reportService.getUserActivatedAll());
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "按周期获取用户激活数据", notes = "按周期获取用户激活数据")
    @RequestMapping(value = "/getUserActivatedByCycle", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "查询周期（1:前一天，7：近7天，14：近14天，30：近30天）", required = true, paramType = "query", dataType = "String")
    })
    public CommonResponse getUserActivatedByCycle(@RequestParam("cycle") String cycle) {
        return ResultMsg.SUCCESS.info(this.reportService.getUserActivatedByCycle(cycle));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取设备活跃页面数据", notes = "获取设备活跃页面数据")
    @RequestMapping(value = "/getDeviceActiveAll", method = RequestMethod.GET)
    public CommonResponse getDeviceActiveAll() {
        return ResultMsg.SUCCESS.info(this.reportService.getDeviceActiveAll());
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "按周期获取设备活跃数据", notes = "按周期获取设备活跃数据")
    @RequestMapping(value = "/getDeviceActiveByCycle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "查询周期（1:前一天，7：近7天，14：近14天，30：近30天）", required = true, paramType = "query", dataType = "String")
    })
    public CommonResponse getDeviceActiveByCycle(@RequestParam("cycle") String cycle) {
        return ResultMsg.SUCCESS.info(this.reportService.getDeviceActiveByCycle(cycle));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "设备活跃明细分页查询", notes = "设备活跃明细分页查询")
    @RequestMapping(value = "/getDeviceActivePage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDeviceActivePage(@RequestBody SearchParam param) {
        return ResultMsg.SUCCESS.info(this.reportService.getDeviceActivePage(param));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "获取设备激活页面数据", notes = "获取设备激活页面数据")
    @RequestMapping(value = "/getDeviceActivatedAll", method = RequestMethod.GET)
    public CommonResponse getDeviceActivatedAll(){
        return ResultMsg.SUCCESS.info(this.reportService.getDeviceActivatedAll());
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "按周期获取设备激活数据", notes = "按周期获取设备激活数据")
    @RequestMapping(value = "/getDeviceActivatedByCycle", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "查询周期（1:前一天，7：近7天，14：近14天，30：近30天）", required = true, paramType = "query", dataType = "String")
    })
    public CommonResponse getDeviceActivatedByCycle(@RequestParam("cycle") String cycle) {
        return ResultMsg.SUCCESS.info(this.reportService.getDeviceActivatedByCycle(cycle));
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "设备激活明细分页查询", notes = "设备激活明细分页查询")
    @RequestMapping(value = "/getDeviceActivatedByPage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getDeviceActivatedByPage(@RequestBody SearchParam param) {
        return ResultMsg.SUCCESS.info(this.reportService.getDeviceActivatedByPage(param));
    }

}
