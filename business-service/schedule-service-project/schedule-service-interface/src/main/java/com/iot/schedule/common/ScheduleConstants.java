package com.iot.schedule.common;


import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 描述：常量类
 * 创建人： LaiGuiMing
 * 创建时间： 2018/4/28 14:58
 */
public class ScheduleConstants {
    /**
     * JOB类型
     */
    public static final String TFTTT_JOB = "SensorJob";
    public static final String APPLET_JOB = "AppletJob";
    public static final String TIMING_JOB = "TimingJob";
    public static final String WEATHER_JOB = "WeatherJob";
    public static final String PEOPLE_JOB = "PeopleJob";
    public static final String TEMPLATE_JOB = "TemplateJob";
    public static final String LOCATION_JOB = "LocationJob";
    public static final String ELECTRICITY_JOB = "ElectricityStatisticsJob";
    public static final String RUNTIME_JOB = "RuntimeJob";
    public static final String ASTRO_CLOCK_JOB = "AstroClockJob";
    public static final String ALLOCATION_JOB = "AllocationJob";
    public static final String BUSINESSTYPE_STATISTIC_JOB = "BusinessTypeStatisticJob";
    public static final String TENANT_JOB = "TenantJob";
    public static final String AIR_SWITCH_EVENT_JOB = "AirSwitchEventJob";
    public static final String AIR_SWITCH_HEART_BEAT_JOB = "AirSwitchHeartBeatJob";
    public static final String AIR_SWITCH_ELECTRICITY_JOB = "AirSwitchElectricityJob";

    public static final String DEAL_FILE_JOB = "DealUnUploadFileInfoScheduleJob";
    //处理失效文件任务
    public static final String DEAL_INVALID_VIDEO_JOB = "DealInvalidVideoDataScheduleJob";
    //处理录影事件溢出任务
    public static final String DEAL_VIDEO_EVENT_OVER_JOB = "UpdateEventScheduleJob";
    //处理全时录影溢出任务
    public static final String DEAL_VIDEO_HOUR_OVER_JOB = "UpdateHourScheduleJob";
    //处理计划过期、临期、到期任务
    public static final String DEAL_PLAN_EXPIRE_JOB = "UpdatePlanScheduleJob";

    /**IPC解绑任务KEY*/
    public static final String IPC_UNBANDING_TASK_KEY = "ipc-unbanding-task:";

    /**
     * 默认组名
     */
    public static final String DEFAULT_JOB_GROUP_NAME = "default_job_group_name";
    public static final String DEFAULT_TRIGGER_GROUP_NAME = "default_trigger_group_name";


    /**
     * 定义格式
     */
    public static final String TRIGGER_STRING = "trigger_";
    public static final String JOB_DATA_KEY = "job_data";
    public static final String JOB_PACKAGE_URL = "com.iot.schedule.job.";


    public static String getTriggerName(String taskId) {
        return TRIGGER_STRING + taskId;
    }

    public static String getTaskKey(String taskName, Long tenantId) {
        return taskName + "_" + tenantId;
    }

    public static final String SCHEDULE_SCENE = "scene";
    public static final String SCHEDULE_IFTTT = "ifttt";
    public static final String SCHEDULE_LOCATION = "location";
    public static final String SCHEDULE_IFTTT_TEMPLATE = "ifttt_template";

    /**
     * job execute result
     */
    public static final Integer EXE_RESULT_SUCCESS = 0;
    public static final Integer EXE_RESULT_FAIL = 1;
}
