package com.iot.shcs.contants;

import com.iot.common.constant.SystemConstants;

/**
 * 项目名称：IOT云平台
 * 模块名称：控制服务
 * 功能描述：模块常量
 * 创建人： mao2080@sina.com
 * 创建时间：2018年05月24日 11:34
 * 修改人： mao2080@sina.com
 * 修改时间：2018年04月05日 11:34
 */
public class ModuleConstants extends SystemConstants {

    /**IPC产品类别名称*/
    public static final String IPC_TYPE_NAME = "IPC";


    /**事件上报通知app redis记录是否已发送*/
    public static final String UPLOADEVENT_NOTICE_APP = "uploadEvent_notice_app:";

    /**ota升级模式 普通模式*/
    public static final int OTA_UPGRADE_MODE_NORMAL = 0;

    /**ota升级模式 静默模式*/
    public static final int OTA_UPGRADE_MODE_SILENT = 1;

    /**ota升级状态 stage*/
    public static final String OTA_UPGRADE_STATUS_STAGE  = "stage";
    /**ota升级状态 msg*/
    public static final String OTA_UPGRADE_STATUS_MSG  = "msg";
    /**ota升级状态 percent*/
    public static final String OTA_UPGRADE_STATUS_PERCENT = "percent";
    /**策略执行完成 普通升级组别**/
    public static final Integer OTA_UPGRADE_STRATEGY_COMPLETE_GROUP = -3000;

    /**ota计划启动任务执行秒数*/
    public static final long OTA_TASK_TIME = 60*60*24L;

    /**设备未激活*/
    public static final String DEV_NOT_ACTIVATED  = "N";
    /**设备已激活*/
    public static final String DEV_ACTIVATED  = "Y";

    public static final String DEVACTIVE = "dev-active:";
    public static final String USERACTIVE = "user-active:";
    public static final String DEVONLINE = "dev-online-all";
    public static final String USERONLINE = "user-online-all";
}