package com.iot.video.config;


import com.iot.schedule.api.ScheduleApi;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.vo.AddJobReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目名称：cloud
 * 功能描述：视频服务任务通知
 * 创建人： yeshiyuan
 * 创建时间：2018/11/7 9:55
 * 修改人： yeshiyuan
 * 修改时间：2018/11/7 9:55
 * 修改描述：
 */
@Component
public class VideoTaskNotify implements CommandLineRunner{

    private static Logger logger = LoggerFactory.getLogger(VideoTaskNotify.class);

    @Autowired
    private ScheduleApi scheduleApi;

    @Value("${job.cron.dealInvalidVideoCron:0 0 0/1 * * ?}")
    private String dealInvalidVideoCron;

    @Value("${job.cron.dealVideoEventOverCron:0 0 0/1 * * ?}")
    private String dealVideoEventOverCron;

    @Value("${job.cron.dealVideoHourOverCron:0 0 0/1 * * ?}")
    private String dealVideoHourOverCron;

    @Value("${job.cron.dealPlanExpireCron:0 0 0/1 * * ?}")
    private String dealPlanExpireCron;

    private final static String invalidVideoTaskName = "dealInvalidVideoTask";
    private final static String videoEventTaskName = "dealVideoEventOverTask";
    private final static String videoHourTaskName = "dealVideoHourOverTask";
    private final static String planExpireTaskName = "dealPlanExpireTask";

    /**
      * @despriction：开启处理录影计划相关定时任务
      * @author  yeshiyuan
      * @created 2018/11/7 17:13
      * @return
      */
    @Override
    public void run(String... strings) throws Exception {
        try {
            if (!scheduleApi.checkJobIsExists(invalidVideoTaskName)) {
                AddJobReq jobReq = new AddJobReq();
                jobReq.setJobClass(ScheduleConstants.DEAL_INVALID_VIDEO_JOB);
                jobReq.setJobName(invalidVideoTaskName);
                jobReq.setCron(dealInvalidVideoCron);
                System.out.println("***************** add DealInvalidVideoDataScheduleJob start*****************************");
                scheduleApi.addJob(jobReq);
                System.out.println("***************** add DealInvalidVideoDataScheduleJob end*****************************");
            }

            if (!scheduleApi.checkJobIsExists(videoEventTaskName)) {
                System.out.println("***************** add UpdateEventScheduleJob start*****************************");
                AddJobReq eventJobReq = new AddJobReq();
                eventJobReq.setJobClass(ScheduleConstants.DEAL_VIDEO_EVENT_OVER_JOB);
                eventJobReq.setJobName(videoEventTaskName);
                eventJobReq.setCron(dealVideoEventOverCron);
                scheduleApi.addJob(eventJobReq);
                System.out.println("***************** add UpdateEventScheduleJob end*****************************");
            }

            if (!scheduleApi.checkJobIsExists(videoHourTaskName)) {
                System.out.println("***************** add UpdateHourScheduleJob start*****************************");
                AddJobReq hourJob = new AddJobReq();
                hourJob.setJobClass(ScheduleConstants.DEAL_VIDEO_HOUR_OVER_JOB);
                hourJob.setJobName(videoHourTaskName);
                hourJob.setCron(dealVideoHourOverCron);
                scheduleApi.addJob(hourJob);
                System.out.println("***************** add UpdateHourScheduleJob end*****************************");
            }

            if (!scheduleApi.checkJobIsExists(planExpireTaskName)) {
                System.out.println("***************** add UpdatePlanScheduleJob start*****************************");
                AddJobReq planExpireJob = new AddJobReq();
                planExpireJob.setJobClass(ScheduleConstants.DEAL_PLAN_EXPIRE_JOB);
                planExpireJob.setJobName(planExpireTaskName);
                planExpireJob.setCron(dealPlanExpireCron);
                scheduleApi.addJob(planExpireJob);
                System.out.println("***************** add UpdatePlanScheduleJob end*****************************");
            }
        } catch (Exception e) {
            logger.error("VideoTaskNotify run error", e);
        }
    }


}
