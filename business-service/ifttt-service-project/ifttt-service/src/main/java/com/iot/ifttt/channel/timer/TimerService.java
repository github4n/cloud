package com.iot.ifttt.channel.timer;

import com.google.common.collect.Maps;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 描述：定时任务服务
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/29 9:47
 */
@Service
public class TimerService {

    @Autowired
    private ScheduleApi scheduleApi;

    public void addJob(Long itemId, String cron) {
        AddJobReq addJobReq = new AddJobReq();
        addJobReq.setJobName("applet_" + itemId);
        addJobReq.setJobClass(ScheduleConstants.APPLET_JOB);
        //时间表达式
        addJobReq.setCron(cron);
        //data
        Map<String, Object> data = Maps.newHashMap();
        data.put("itemId", itemId);
        addJobReq.setData(data);
        SaaSContextHolder.setCurrentTenantId(-1l);
        scheduleApi.addJob(addJobReq);
    }

    public void updateJob(Long itemId, String cron) {
        //先删除，再新增
        delJob(itemId);
        addJob(itemId, cron);
        System.out.println("更新完成天文定时任务：itemId:" + itemId + ",cron:" + cron);
    }

    public void delJob(Long itemId) {
        String jobName = "applet_" + itemId;
        //TODO 清除脏数据
        SaaSContextHolder.setCurrentTenantId(2l);
        scheduleApi.delJob(jobName);
        SaaSContextHolder.setCurrentTenantId(0l);
        scheduleApi.delJob(jobName);
        SaaSContextHolder.setCurrentTenantId(1l);
        scheduleApi.delJob(jobName);
        SaaSContextHolder.setCurrentTenantId(-1l);
        scheduleApi.delJob(jobName);
    }


}
