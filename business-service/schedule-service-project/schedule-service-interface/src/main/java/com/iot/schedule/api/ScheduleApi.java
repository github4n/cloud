package com.iot.schedule.api;

import com.iot.schedule.api.fallback.ScheduleApiFallbackFactory;
import com.iot.schedule.vo.AddJobReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 描述：计划调度接口
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 11:21
 */

@Api("计划调度接口")
@FeignClient(value = "schedule-service", fallbackFactory = ScheduleApiFallbackFactory.class)
@RequestMapping("schedule")
public interface ScheduleApi {

    @ApiOperation("添加任务")
    @RequestMapping(value = "/addJob", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void addJob(@RequestBody AddJobReq req);

    @ApiOperation("删除任务")
    @RequestMapping(value = "/delJob", method = RequestMethod.GET)
    void delJob(@RequestParam("jobName") String jobName);

    @ApiOperation("修改任务时间")
    @RequestMapping(value = "/updateJob", method = RequestMethod.GET)
    void updateJob(@RequestParam("jobName") String jobName,
                   @RequestParam("cron") String cron);

    @ApiOperation("启动所有任务")
    @RequestMapping(value = "/startJobs", method = RequestMethod.GET)
    void startJobs();

    @ApiOperation("执行任务")
    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    void execute(@RequestParam("jobClass") String jobClass);

    @ApiOperation("校验任务是否存在")
    @RequestMapping(value = "/checkJobIsExists", method = RequestMethod.GET)
    boolean checkJobIsExists(@RequestParam("jobName") String jobName);
}
