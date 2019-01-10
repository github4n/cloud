package com.iot.ifttt.listener;

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
public class IftttListener implements ApplicationListener<ApplicationReadyEvent> {

    public static final String SCHEDULE_CRON_ASTRONOMY = "schedule.cron.astronomy";


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //TODO(laiguiming) 定时任务添加
        //scheduleInit();
    }

    /**
     * 定时任务初始化
     */
    public static void scheduleInit() {
        ScheduleApi scheduleApi = ApplicationContextHelper.getBean(ScheduleApi.class);
        Environment environment = ApplicationContextHelper.getBean(Environment.class);

        // 更新天文定时统计任务
        new Thread(() -> {
            String cron = environment.getProperty(SCHEDULE_CRON_ASTRONOMY);
            if (StringUtils.isNotBlank(cron)) {
                AddJobReq req = new AddJobReq();
                req.setCron(cron);
                req.setJobClass(ScheduleConstants.ASTRO_CLOCK_JOB);
                req.setJobName(ScheduleConstants.ASTRO_CLOCK_JOB);
                scheduleApi.delJob(ScheduleConstants.ASTRO_CLOCK_JOB);
                scheduleApi.addJob(req);
            }
        }).start();
    }
}
