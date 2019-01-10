package com.iot.common.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * <p>定时任务管理</p>
 *
 * @author 项义涛
 * @version 1.00
 * @dateTime 2017/6/9 14:54
 */
public final class QuartzManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static QuartzManager quartzManager;

    public static QuartzManager getInstall() {
        if (quartzManager == null) {
            quartzManager = new QuartzManager();
        }
        return quartzManager;
    }

    public SchedulerFactory schedulerFactoryBean() {
        SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
        return gSchedulerFactory;
    }

    public Scheduler getSchedulerManager() {
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFactoryBean().getScheduler();
            return scheduler;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }

    /**
     * <p>检查job是否重复</p>
     *
     * @param jobName  任务名
     * @param jobGroup 任务组名
     * @return
     * @throws SchedulerException
     * @dateTime 2017/6/9 14:54
     * @author 项义涛
     * @version 1.00
     */
    public boolean checkExists(String jobName, String jobGroup) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return getSchedulerManager().checkExists(triggerKey);
    }

    /**
     * <p>添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 </p>
     *
     * @param jobName 任务名
     * @param cls     工作执行的类
     * @param time    定时执行的时间
     * @return
     * @dateTime 2017/6/9 14:56
     * @author 项义涛
     * @version 1.00
     */
    @SuppressWarnings("unchecked")
    public boolean addJob(String jobName, Class cls, String time) {
        try {
            if (checkExists(jobName, jobName)) {
                logger.info("addJob fail, job already exist, jobGroup:{}, jobName:{}", jobName, jobName);
                return false;
            }

            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, jobName).build();
            startJob(jobDetail, jobName, cls, time);
        } catch (SchedulerException e) {
            logger.info("addJob fail ", e);
        }
        return true;

    }


    /**
     * <p>添加联系人订阅和联系人组订阅的定时器</p>
     *
     * @param jobName 任务名
     * @param time    指定时间规则运行
     * @return
     * @dateTime 2017/6/10 10:11
     * @author 项义涛
     * @version 1.00
     */
    public boolean addJob(String jobName, Class cls, Map<String, Object> map, String time) {
        try {
            if (checkExists(jobName, jobName)) {
                logger.info("addJob fail, job already exist, jobGroup:{}, jobName:{} ", new Object[]{jobName, jobName});
                return false;
            }

            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, jobName).build();
            if (map != null && !map.isEmpty()) {
                map.forEach((k, v) -> jobDetail.getJobDataMap().put(k, v));
            }
            /*jobDetail.getJobDataMap().put("id",id);
            jobDetail.getJobDataMap().put("type",type);
            jobDetail.getJobDataMap().put("dataService",dataService);
            jobDetail.getJobDataMap().put("esHandHelper",esHandHelper);*/
            /** *0 0/5 * * * ? 5分种执行一次
             *0/5 * * * * ? 5秒种执行一次**/
            startJob(jobDetail, jobName, cls, time);
        } catch (SchedulerException e) {
            logger.info("addJob fail ", e);
        }
        return true;

    }


    /**
     * <p>添加联系人订阅和联系人组订阅的定时器</p>
     *
     * @param jobName   任务名
     * @param groupName 组名
     * @param time      指定时间规则运行
     * @return
     * @dateTime 2017/6/10 10:11
     * @author 项义涛
     * @version 1.00
     */
    public boolean addJob(String jobName, String groupName, Class cls, Map<String, Object> map, String time) {
        try {
            if (checkExists(jobName, groupName)) {
                logger.info("addJob fail, job already exist, jobName:{}, jobGroup:{} ", new Object[]{jobName, groupName});
                return false;
            }
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, groupName).build();
            if (map != null && !map.isEmpty()) {
                map.forEach((k, v) -> jobDetail.getJobDataMap().put(k, v));
            }
            /** *0 0/5 * * * ? 5分种执行一次
             *0/5 * * * * ? 5秒种执行一次**/
            startJob(jobDetail, jobName, cls, time);
        } catch (SchedulerException e) {
            logger.info("addJob fail ", e);
        }
        return true;

    }

    /**
     * <p>添加联系人订阅和联系人组订阅的定时器</p>
     *
     * @param jobName     任务名
     * @param groupName   组名
     * @param cls         执行任务的类
     * @param startTime   执行任务的时间
     * @param seconds     间隔多少秒执行
     * @param repeatCount 重复执行多少次
     * @return
     * @dateTime 2017/6/10 10:11
     * @author 项义涛
     * @version 1.00
     */
    //TODO(laiguiming) 七个参数不规范,最多6个参数，请相关人员调整，可采用map形式传参，暂时注释
    /*public boolean addJob(String jobName, String groupName, Class cls, Map<String, Object> map, Date startTime, int seconds, int repeatCount) {
        try {
            if (checkExists(jobName, groupName)) {
                logger.info("addJob fail, job already exist, jobName:{}, jobGroup:{} ", new Object[]{jobName, groupName});
                return false;
            }
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, groupName).build();
            if (map != null && !map.isEmpty()) {
                map.forEach((k, v) -> jobDetail.getJobDataMap().put(k, v));
            }
            SimpleTrigger trigger = newTrigger().withIdentity(jobName, groupName).startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(seconds).withRepeatCount(repeatCount)).build();
            Date date = getSchedulerManager().scheduleJob(jobDetail, trigger);
            // 启动
            if (!getSchedulerManager().isShutdown()) {
                getSchedulerManager().start();
            }
            logger.info("addJob success 1, jobDetail:{}, startTime:{}, date:{}", new Object[]{jobDetail, startTime, date});

        } catch (SchedulerException e) {
            logger.info("addJob fail ", e);
        }
        return true;
    }*/


    /**
     * <p>添加联系人订阅和联系人组订阅的定时器</p>
     *
     * @param jobName    任务名
     * @param groupName  组名
     * @param cls        执行任务的类
     * @param futureDate 在未来的多久执行, IntervalUnit.MINUTE 单位是分钟
     * @return
     * @dateTime 2017/6/10 10:11
     * @author 项义涛
     * @version 1.00
     */
    public boolean addJob(String jobName, String groupName, Class cls, Map<String, Object> map, int futureDate) {
        try {
            if (checkExists(jobName, groupName)) {
                logger.info("addJob fail, job already exist, jobName:{}, jobGroup:{} ", new Object[]{jobName, groupName});
                return false;
            }
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, groupName).build();
            if (map != null && !map.isEmpty()) {
                map.forEach((k, v) -> jobDetail.getJobDataMap().put(k, v));
            }
            SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(jobName, groupName).startAt(futureDate(futureDate, IntervalUnit.MINUTE)).build();
            Date date = getSchedulerManager().scheduleJob(jobDetail, trigger);
            // 启动
            if (!getSchedulerManager().isShutdown()) {
                getSchedulerManager().start();
            }
            logger.info("addJob success 1, jobDetail:{}, futureDate:{}, date:{}", new Object[]{jobDetail, futureDate, date});
        } catch (SchedulerException e) {
            logger.info("addJob fail ", e);
        }
        return true;
    }

    /**
     * <p>添加一个定时任务</p>
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param time             时间设置
     * @return
     * @dateTime 2017/6/9 14:57
     * @author 项义涛
     * @version 1.00
     */
    @SuppressWarnings("unchecked")
    public boolean addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String time) {
        try {
            if (checkExists(jobName, jobName)) {
                logger.info("addJob fail, job already exist, jobGroup:{}, jobName:{}", jobName, jobName);
                return false;
            }

            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time); //time 0 0/5 * * * ?
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName).withSchedule(scheduleBuilder).build();
            Date date = getSchedulerManager().scheduleJob(jobDetail, cronTrigger);
            // 启动
            /*if (!getSchedulerManager().isShutdown()) {
                getSchedulerManager().start();
            }*/
            logger.info("addJob success 2, jobDetail:{}, cronTrigger:{}, date:{}", new Object[]{jobDetail, cronTrigger, date});
        } catch (SchedulerException e) {
            logger.info("addJob fail ", e);
        }
        return true;
    }

    public void startJob(JobDetail jobDetail, String jobName, Class cls, String time) throws SchedulerException {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time); //time 0 0/5 * * * ?
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobName).withSchedule(scheduleBuilder).build();
        Date date = getSchedulerManager().scheduleJob(jobDetail, cronTrigger);
        // 启动
        if (!getSchedulerManager().isShutdown()) {
            getSchedulerManager().start();
        }
        logger.info("addJob success 1, jobDetail:{}, cronTrigger:{}, date:{}", new Object[]{jobDetail, cronTrigger, date});
    }

    /**
     * <p>修改一个任务的触发时间(使用默认的任务组名)</p>
     *
     * @param jobName 任务名
     * @param time    时间设置
     * @dateTime 2017/6/9 14:58
     * @author 项义涛
     * @version 1.00
     */
    @SuppressWarnings("unchecked")
    public void modifyJobTime(String jobName, String time) {
        try {
            Trigger trigger = getSchedulerManager().getTrigger(TriggerKey.triggerKey(jobName, jobName));
            if (trigger == null) {
                return;
            }
            CronTrigger ct = (CronTrigger) trigger;
            String oldTime = ct.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = getSchedulerManager().getJobDetail(JobKey.jobKey(jobName, jobName));
                Class objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (Exception e) {
            logger.info("modifyJobTime fail ", e);
        }
    }

    /**
     * <p>修改一个任务的触发时间</p>
     *
     * @param jobName  任务名
     * @param jobGroup 任务组名
     * @param time     时间
     * @dateTime 2017/6/9 14:59
     * @author 项义涛
     * @version 1.00
     */
    public void modifyJobTime(String jobName, String jobGroup, String time) {
        /*try {
            Trigger trigger = getSchedulerManager().getTrigger(TriggerKey.triggerKey(triggerName,triggerGroupName));
            if (trigger == null) {
                return;
            }
            CronTrigger ct = (CronTrigger) trigger;
            String oldTime = ct.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                // 修改时间
                // 重启触发器
                getSchedulerManager().resumeTrigger(TriggerKey.triggerKey(triggerName,triggerGroupName));
            }
        } catch (Exception e) {
            logger.info("modifyJobTime 2 fail ",e);
        }*/

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = new JobKey(jobName, jobGroup);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            JobDetail jobDetail = getSchedulerManager().getJobDetail(jobKey);
            HashSet<Trigger> triggerSet = new HashSet<Trigger>();
            triggerSet.add(cronTrigger);
            getSchedulerManager().scheduleJob(jobDetail, triggerSet, true);
            logger.info("updateJob success, JobGroup:{}, JobName:{}", jobGroup, jobName);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>移除一个任务(使用默认的任务组名)</p>
     *
     * @param jobName 任务组名
     * @dateTime 2017/6/9 15:00
     * @author 项义涛
     * @version 1.00
     */
    public void removeJob(String jobName) {
        try {
            getSchedulerManager().pauseTrigger(TriggerKey.triggerKey(jobName, jobName)); // 停止触发器
            getSchedulerManager().unscheduleJob(TriggerKey.triggerKey(jobName, jobName)); // 移除触发器
            getSchedulerManager().deleteJob(JobKey.jobKey(jobName, jobName)); // 删除任务

            logger.info("removeJob success 1,, jobName:{} jobDetail cronTrigger date", jobName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>移除一个任务</p>
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @dateTime 2017/6/9 15:00
     * @author 项义涛
     * @version 1.00
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            getSchedulerManager().pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName)); // 停止触发器
            getSchedulerManager().unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName)); // 移除触发器
            getSchedulerManager().deleteJob(JobKey.jobKey(jobName, jobGroupName)); // 删除任务
            logger.info("removeJob success 1,, jobName:{} jobGroupName:{} triggerName:{} triggerGroupName:{}", new Object[]{jobName, jobGroupName, triggerName, triggerGroupName});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>启动所有定时任务</p>
     *
     * @dateTime 2017/6/9 15:01
     * @author 项义涛
     * @version 1.00
     */
    public void startJobs() {
        try {
            getSchedulerManager().start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>关闭所有定时任务</p>
     *
     * @dateTime 2017/6/9 15:01
     * @author 项义涛
     * @version 1.00
     */
    public void shutdownJobs() {
        try {
            if (!getSchedulerManager().isShutdown()) {
                getSchedulerManager().shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
