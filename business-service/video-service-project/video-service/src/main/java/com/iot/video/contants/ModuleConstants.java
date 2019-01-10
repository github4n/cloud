package com.iot.video.contants;

import com.iot.common.constant.SystemConstants;

public class ModuleConstants extends SystemConstants {

    public static final String TABLE_VIDEO_FILE = "video_file";

    public static final String TABLE_VIDEO_EVENT = "video_event";

    public static final String PLAN_ORDER_KEY = "planOrder";

    public static final String COMPLETED = "completed";

    public static final String UN_COMPLETED = "un_completed";

    public static final long PLAN_DURATION_30_IN_SEC = 30 * 24 * 3600 * 1000L;

    public static final long ONE_DAY_IN_MS = 24 * 3600 * 1000L;

    public static final String DB_TABLE_VIDEO_PLAN = "max-row-id:video_plan";

    public static final String DB_TABLE_VIDEO_PAY_RECORD = "max-row-id:video_pay_record";

    public static final String DB_TABLE_VIDEO_FILE = "max-row-id:video_file";

    public static final String DB_TABLE_VIDEO_EVENT = "max-row-id:video_event";

    public static final String DB_TABLE_VIDEO_TASK = "max-row-id:video_task";

    public static final String AES_KEY = "Leedarson";

    /**IPC解绑任务队列*/
    public static final String IPC_UNBANDING_TASK_KEY_QUEUE = "ipc-unbanding-task-queue";

    /**IPC解绑任务KEY*/
    public static final String IPC_UNBANDING_TASK_KEY = "ipc-unbanding-task:";

    /**IPC文件删除KEY*/
    public static final String IPC_FILE_DELETE_TASK_KEY = "ipc-unbanding-subtask:file:";

    /**IPC文件删除KEY*/
    public static final String IPC_EVENT_DELETE_TASK_KEY = "ipc-unbanding-subtask:event:";

    /**IPC文件删除KEY*/
    public static final String IPC_VIDEO_FILE_DELETE_TASK_KEY = "ipc-unbanding-subtask:video:";

    /**IPC解绑任务VAL*/
    public static final String IPC_UNBANDING_TASK_VAL = "{\"status\":0,\"exec_time\":\"\",\"deviceId\":\"@deviceId\",\"planId\":\"@planId\",\"retry\":0,\"failedDesc\":\"\"}";

    /**--------------------- redis key前缀、字段值 start--------------------*/
    /**
     * 购买视频计划
     */
    public static final String REDIS_PRE_PLAN_ORDERID = "video:plan:orderId:";


    /**
     * 视频计划信息（video-plan-info:计划uuid）  格式video:plan-info: 23423423 { "deviceId":"22131","planType":1,"eventNum":0,"HadVideoEventNum":0,...}
     */
    public final static String VIDEO_PLAN_INFO = "video:plan-info:";

    /**
     * 视频计划信息-》key：设备id
     */
    public final static String VIDEO_PLAN_INFO_KEY_DEVICEID = "deviceId";

    /**
     * 视频计划信息-》key：计划类型0-全时录影，1-事件录影
     */
    public final static String VIDEO_PLAN_INFO_KEY_PLANTYPE = "planType";

    /**
     * 视频计划信息-》key：事件数量
     */
    public final static String VIDEO_PLAN_INFO_KEY_PACKAGE_EVENTNUM_FULLHOUR = "packageEventNumOrFullHour";

    /**
     * 视频计划信息-》key：用户id
     */
    public final static String VIDEO_PLAN_INFO_KEY_USER_ID = "userId";

    /**
     * 视频计划信息-》key：过期标志（0：未过期；1：已过期）
     */
    public final static String VIDEO_PLAN_INFO_KEY_EEPIREFLAG = "expireFlag";

    /**
     * 视频计划信息-》key：开启标志（0：停止；1：开启）
     */
    public final static String VIDEO_PLAN_INFO_KEY_PLANEXECSTATUS = "planExecStatus";

    /**
     * 视频计划信息-》key：已录制事件数量
     */
    public final static String VIDEO_PLAN_INFO_KEY_HADVIDEOEVENTNUM = "hadVideoEventNum";

    /**
     * 视频计划信息-》key：租户id
     */
    public final static String VIDEO_PLAN_INFO_KEY_TENANTID = "tenantId";

    /**
     * 某个时间里有录影的全时计划集合，格式video:plan-all-time:2018-06-11:05[  planId1,planId2,planId3….planIdn ]
     */
    public final static String VIDEO_PLAN_ALL_TIME = "video:plan-all-time:";

    /**
     * 计划类型为事件录影时，存放计划对应的已录事件数 格式 video:plan-used-event-num:planId  100
     */
    public final static String VIDEO_PLAN_USED_EVENT_NUM= "video:plan-used-event-num:";

    /**
     * 存放计划对应的已录事件数有效期（7天）
     */
    public final static Long VIDEO_PLAN_USED_EVENT_NUM_EXPIRETIME= 7 * 24 * 3600L;

    /**
     * 存放事件录影溢出的待删除计划集合(set类型)
     */
    public final static String VIDEO_PLAN_EVENT_OVER= "video:plan-event-over";



    /**--------------------- redis key前缀、字段值 end--------------------*/


    /**
     * 套餐类型 ：全时
     */
    public static final Integer PACKAGE_ALL = 0;

    /**
     * 套餐类型 ：事件
     */
    public static final Integer PACKAGE_EVENT = 1;

    /**
     * 全时录影 查询视频文件列表  只能查询距离当前时间7*24之内的
     */
    public static final int QUERY_VIDEO_FILE_HOUR_BEFORE = -168;



}
