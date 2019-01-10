package com.iot.schedule.service;

import com.iot.common.exception.BusinessException;
import com.iot.common.helper.ApplicationContextHelper;
import com.iot.control.activity.api.ActivityRecordApi;
import com.iot.control.activity.vo.req.ActivityRecordReq;
import com.iot.saas.SaaSContextHolder;
import com.iot.schedule.common.ScheduleConstants;
import com.iot.schedule.common.ScheduleExceptionEnum;
import com.iot.schedule.controller.ScheduleController;
import com.iot.schedule.vo.AddJobReq;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * 描述：计划任务逻辑类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 11:24
 */
@Service
public class ScheduleService {
    private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ActivityRecordApi activityRecordApi;

    /**
     * 添加任务
     *
     * @param req
     */
    public void addJob(AddJobReq req) {
        logger.debug("添加任务："+req.toString());

        //任务标识
        String taskKey = getTaskKey(req.getJobName());

        //任务执行类
        Class job_class;
        try {
            job_class = Class.forName(ScheduleConstants.JOB_PACKAGE_URL + req.getJobClass());
        } catch (ClassNotFoundException e) {
            throw new BusinessException(ScheduleExceptionEnum.JOB_NOT_FOUND_ERROR, e);
        }

        //任务详情
        JobDetail jobDetail = JobBuilder.newJob(job_class)
                .withIdentity(taskKey, ScheduleConstants.DEFAULT_JOB_GROUP_NAME)
                .build();
        //传参数
        jobDetail.getJobDataMap().put(ScheduleConstants.JOB_DATA_KEY, req.getData());

        // 创建Trigger对象
        CronTrigger trigger = buildTrigger(taskKey, req.getCron(), req.getTimeZone());

        // 调度容器设置JobDetail和Trigger
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            logger.info("任务【"+req.getJobName()+"】添加失败！"+e.getMessage());
            //throw new BusinessException(ScheduleExceptionEnum.ADD_JOB_ERROR, e);
        }
    }

    /**
     * 删除任务
     *
     * @param jobName
     */
    public void delJob(String jobName) {
        logger.debug("删除定时任务：【" + jobName + "】");

        //任务key
        String taskKey = getTaskKey(jobName);
        //触发器key
        TriggerKey triggerKey = TriggerKey.triggerKey(ScheduleConstants.getTriggerName(taskKey),
                ScheduleConstants.DEFAULT_TRIGGER_GROUP_NAME);

        try {
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(taskKey, ScheduleConstants.DEFAULT_JOB_GROUP_NAME));// 删除任务
        } catch (SchedulerException e) {
            throw new BusinessException(ScheduleExceptionEnum.DELETE_JOB__ERROR, e);
        }
    }

    /**
     * 修改任务时间
     *
     * @param jobName
     * @param cron
     */
    public void updateJob(String jobName, String cron) {
        try {
            logger.debug("修改任务时间：【" + jobName + "】，表达式：" + cron);

            //任务key
            String taskKey = getTaskKey(jobName);
            //触发器key
            TriggerKey triggerKey = TriggerKey.triggerKey(ScheduleConstants.getTriggerName(taskKey),
                    ScheduleConstants.DEFAULT_TRIGGER_GROUP_NAME);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                trigger = buildTrigger(taskKey, cron);
                //重新设置触发条件
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(ScheduleExceptionEnum.UPDATE_JOB__ERROR, e);
        }
    }

    /**
     * 获取任务key
     *
     * @param jobName
     * @return
     */
    public String getTaskKey(String jobName) {
        Long tenantId = null;
        try {
            tenantId = SaaSContextHolder.currentTenantId();
        } catch (Exception e) {
            logger.error("************************************");
            logger.error("获取tenantId失败,tenantId设置为-1L");
            logger.error("************************************");
            tenantId = -1L;
        }
        return ScheduleConstants.getTaskKey(jobName, tenantId);
    }

    /**
     * 创建触发器
     *
     * @param taskKey
     * @param cron
     * @return
     */
    public CronTrigger buildTrigger(String taskKey, String cron) {
        return buildTrigger(taskKey, cron, null);
    }

    public CronTrigger buildTrigger(String taskKey, String cron, TimeZone timeZone) {
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(ScheduleConstants.getTriggerName(taskKey), ScheduleConstants.DEFAULT_TRIGGER_GROUP_NAME);
        triggerBuilder.startNow();
        // 触发器时间设定
        if (timeZone != null) {
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron).inTimeZone(timeZone));
        } else {
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
        }
        // 创建Trigger对象
        return (CronTrigger) triggerBuilder.build();
    }

    /**
     * 启动所有定时任务
     */
    public void startJobs() {
        try {
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(String jobClass){
        try {
            Object[] methodParameters = new Object[]{null};

            Class[] parameterClasses = new Class[]{JobExecutionContext.class};
            Class job_class = Class.forName(ScheduleConstants.JOB_PACKAGE_URL + jobClass);
            Method method = null;
            try {
                method = job_class.getMethod("execute", parameterClasses);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            method.setAccessible(true);
            Object obj = ApplicationContextHelper.getBean(job_class);
            try {
                method.invoke(obj,methodParameters);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveActivityRecord(ActivityRecordReq req) {
        List<ActivityRecordReq> list = new ArrayList<>();
        list.add(req);
        activityRecordApi.saveActivityRecord(list);
        logger.info("save activity record .....");
    }

    public boolean checkJobIsExists(String jobName) {
        logger.debug("检验任务是否正在执行：【{}】", jobName);
        boolean exist = false;
        try {
            //任务key
            String taskKey = getTaskKey(jobName);
            //触发器key
            TriggerKey triggerKey = TriggerKey.triggerKey(ScheduleConstants.getTriggerName(taskKey),
                    ScheduleConstants.DEFAULT_TRIGGER_GROUP_NAME);
            if (scheduler.checkExists(triggerKey)) {
                exist = true;
            }
        } catch (SchedulerException e) {
            throw new BusinessException(ScheduleExceptionEnum.CHECK_JOB_ERROR, e);
        }
        logger.debug("检验任务【{}】是否正在执行结果：{}", jobName, exist);
        return exist;
    }
}
