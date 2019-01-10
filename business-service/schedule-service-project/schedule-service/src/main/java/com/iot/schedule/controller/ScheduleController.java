package com.iot.schedule.controller;

import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.service.ScheduleService;
import com.iot.schedule.vo.AddJobReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：计划调度接口实现
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 9:56
 */
@RestController
public class ScheduleController implements ScheduleApi {

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void addJob(@RequestBody AddJobReq req) {
        scheduleService.addJob(req);
    }

    @Override
    public void delJob(@RequestParam("jobName") String jobName) {
        scheduleService.delJob(jobName);
    }

    @Override
    public void updateJob(@RequestParam("jobName") String jobName,
                          @RequestParam("cron") String cron) {
        scheduleService.updateJob(jobName, cron);
    }

    @Override
    public void startJobs() {
        scheduleService.startJobs();
    }

    @Override
    public void execute(@RequestParam("jobClass") String jobClass) {
        scheduleService.execute(jobClass);
    }

    @Override
    public boolean checkJobIsExists(@RequestParam("jobName")String jobName) {
        return scheduleService.checkJobIsExists(jobName);
    }
}
