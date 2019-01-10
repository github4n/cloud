package com.iot.tenant.listener;

import com.iot.common.helper.ApplicationContextHelper;
import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 描述：项目启动监听类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/9/29 19:58
 */
@Component
public class TenantServiceListener implements ApplicationListener<ApplicationReadyEvent> {

    public static final String SCHEDULE_CRON_RESTART = "0 30/30 * * * ?";


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //TODO(laiguiming) 定时任务添加
        scheduleInit();
    }

    /**
     * 定时任务初始化
     */
    public static void scheduleInit() {
        ScheduleApi scheduleApi = ApplicationContextHelper.getBean(ScheduleApi.class);

        new Thread(() -> {
                AddJobReq req = new AddJobReq();
                req.setCron(SCHEDULE_CRON_RESTART);
                req.setJobClass(ScheduleConstants.TENANT_JOB);
                req.setJobName(ScheduleConstants.TENANT_JOB);
                scheduleApi.addJob(req);
        }).start();
    }
}
