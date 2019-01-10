package com.iot.device.util;

/**
 * 描述：redis key 生成工具类
 * 创建人：chq
 * 创建时间： 2018/5/5 13:40
 */
public class RedisKeyUtil {
   
    public static final String DAILY_ELECTRICTY_STATISTICS_KEY ="daily_electricity:";
    public static final String WEEKLY_ELECTRICTY_STATISTICS_KEY ="weekly_electricity:";
    public static final String MONTHLY_ELECTRICTY_STATISTICS_KEY ="monthly_electricity:";
    public static final String DAILY_RUNTIME_KEY ="daily_runtime:";
    public static final String WEEKLY_RUNTIME_KEY ="weekly_runtime:";
    public static final String MONTHLY_RUNTIME_KEY ="monthly_runtime:";

    public static final String OTA_UPGRADE_PLAN_INFO_KEY ="ota:upgrade_plan_info:";

    public static final String OTA_UPGRADE_PLAN_PATH_KEY ="ota:upgrade_plan_path:";

    public static final String OTA_UPGRADE_PLAN_FIRMWARE_KEY ="ota:upgrade_plan_firmware:";

    public static final String OTA_UPGRADE_PLAN_TASK_KEY ="ota:upgrade_plan_task:";


    public static final String OTA_UPGRADE_LOCK ="ota:upgrade_lock:";
    /***下发升级指令时缓存计划参数，用于设备上报升级状态时记录相关日志和策略报告 不能删*/
    public static final String OTA_UPGRADE_INFO_DETAIL ="ota:upgrade_plan_info_detail:";
    /***计划编辑间隔时长 不能删*/
    public static final String OTA_UPGRADE_INFO_SPACE ="ota:upgrade_plan_info_space";
    /***设备上报升级状态 不能删*/
    public static final String OTA_UPGRADE_DEVICE_STATUS ="ota:upgrade_device_status:";

    public static final String OTA_UPGRADE_STRATEGY_CONFIG = "ota:upgrade_strategy_config:";
    /***策略组明细  计划暂停 不删除，启动计划重新初始化*/
    public static final String OTA_UPGRADE_STRATEGY_DETAIL_FULL = "ota:upgrade_strategy_detail_full:";
    /***批次升级明细  计划暂停 不删除，编辑计划重新初始化*/
    public static final String OTA_UPGRADE_STRATEGY_DETAIL_BATCH = "ota:upgrade_strategy_detail_batch:";
    /***uuid升级明细  计划暂停 不删除，编辑计划重新初始化*/
    public static final String OTA_UPGRADE_STRATEGY_DETAIL_UUID = "ota:upgrade_strategy_detail_uuid:";
    /***每个策略组的任务 计划暂停 不删除，启动计划删除并重新初始化*/
    public static final String OTA_UPGRADE_STRATEGY_TASK = "ota:upgrade_strategy_task:";
    /***当前策略组  计划暂停 不删除，启动计划重新初始化*/
    public static final String OTA_UPGRADE_STRATEGY_CURRENT_GROUP = "ota:upgrade_strategy_current_group:";
    /***策略组顺序  计划暂停 不删除，启动计划重新初始化*/
    public static final String OTA_UPGRADE_STRATEGY_GROUP_SEQUENCE = "ota:upgrade_strategy_group_sequence:";
    /***仅用于记录当前策略组升级成功个数,以便切换策略组 计划暂停不下发升级命令，不进行策略组切换 计划启动，全部缓存初始化，
     * 所以 计划暂停或启动删除掉 为确保正确删除干净，策略编辑保存时也删除,因为计划启动时，如果策略已经编辑过，查出来的已经是新的策略组（个数）*/
    public static final String OTA_UPGRADE_STRATEGY_GROUP_SUCCESS = "ota:upgrade_strategy_group_success:";
    /***仅用于记录当前策略组升级失败个数,以便切换策略组  计划暂停不下发升级命令，不进行策略组切换  计划启动，全部缓存初始化，
     * 所以 计划暂停或启动删除掉 为确保正确删除干净，策略编辑保存时也删除,因为计划启动时，如果策略已经编辑过，查出来的已经是新的策略组（个数）*/
    public static final String OTA_UPGRADE_STRATEGY_GROUP_FAIL = "ota:upgrade_strategy_group_fail:";
    /**策略是否已经触发阀值 y n**/
    public static final String OTA_UPGRADE_STRATEGY_TRIGGER_THRESHOLD = "ota:upgrade_strategy_trigger_threshold:";
    /**还没上线的固件map**/
    public static final String OTA_UPGRADE_NOT_ONLINE_FIRMWARE = "ota:upgrade_not_online_firmware:";

    public static String getDialyElectricityStatisticsKey(String deviceId, Long userId, Long tenantId) {
        return DAILY_ELECTRICTY_STATISTICS_KEY + tenantId + ":" + userId + ":" + deviceId;
    }

    public static String getWeeklyElectricityStatisticsKey(String deviceId, Long userId, Long tenantId) {
        return WEEKLY_ELECTRICTY_STATISTICS_KEY + tenantId + ":" + userId + ":" + deviceId;
    }

    public static String getMonthlyElectricityStatisticsKey(String deviceId, Long userId, Long tenantId) {
        return MONTHLY_ELECTRICTY_STATISTICS_KEY + tenantId + ":" + userId + ":" + deviceId;
    }

    public static String getDialyRuntimeKey(String deviceId, Long userId, Long tenantId) {
        return DAILY_RUNTIME_KEY + tenantId + ":" + userId + ":" + deviceId;
    }

    public static String getWeeklyRuntimeKey(String deviceId, Long userId, Long tenantId) {
        return WEEKLY_RUNTIME_KEY + tenantId + ":" + userId + ":" + deviceId;
    }

    public static String getMonthlyRuntimeKey(String deviceId, Long userId, Long tenantId) {
        return MONTHLY_RUNTIME_KEY + tenantId + ":" + userId + ":" + deviceId;
    }

    public static String getUpgradePlanKey(Long productId) {
        return OTA_UPGRADE_PLAN_INFO_KEY  + productId;
    }

    public static String getUpgradePlanPathKey(Long productId) {
        return OTA_UPGRADE_PLAN_PATH_KEY +  productId;
    }

    public static String getUpgradePlanTaskKey(Long productId) {
        return OTA_UPGRADE_PLAN_TASK_KEY + productId ;
    }

    public static String getUpgradePlanFirmwareKey(Long productId) {
        return OTA_UPGRADE_PLAN_FIRMWARE_KEY + productId;
    }
    /**
     * 功能描述:缓存设备升级过程 上报升级的状态
     * @param: [devId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:44
     */
    public static String getUpgradeDeviceStatusKey(String devId) {
        return OTA_UPGRADE_DEVICE_STATUS + devId;
    }
    /**
     * 功能描述:策略升级 策略配置
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:43
     */
    public static String getUpgradeStrategyConfigKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_CONFIG + planId;
    }
    /**
     * 功能描述:策略升级 策略明细 全量升级
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:42
     */
    public static String getUpgradeStrategyDetailFullKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_DETAIL_FULL + planId;
    }
    /**
     * 功能描述:策略升级 策略明细 批次升级
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:42
     */
    public static String getUpgradeStrategyDetailBatchKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_DETAIL_BATCH + planId;
    }
    /**
     * 功能描述:策略升级 策略明细 uuid升级
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:41
     */
    public static String getUpgradeStrategyDetailUuidKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_DETAIL_UUID + planId;
    }
    /**
     * 功能描述:策略升级 升级任务
     * @param: [planId, group]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:39
     */
    public static String getUpgradeStrategyTaskKey(Long planId,Integer group) {
        return OTA_UPGRADE_STRATEGY_TASK + planId + ":" + group;
    }
    /**
     * 功能描述:
     * 正在升级的策略组
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:38
     */
    public static String getUpgradeStrategyCurrentGroupKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_CURRENT_GROUP + planId;
    }
    /**
     * 功能描述:
     * 计划下，每个策略组的顺序
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:34
     */
    public static String getUpgradeStrategyGroupSequenceKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_GROUP_SEQUENCE + planId;
    }

    /**
     * 功能描述:
     * 策略组升级成功统计 存set
     * @param: [planId, group]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:37
     */
    public static String getUpgradeStrategyGroupSuccessKey(Long planId,Integer group) {
        return OTA_UPGRADE_STRATEGY_GROUP_SUCCESS + planId + ":" + group;
    }
    /**
     * 功能描述:
     * 策略组升级失败统计 存set
     * @param: [planId, group]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/26 13:37
     */
    public static String getUpgradeStrategyGroupFailKey(Long planId,Integer group) {
        return OTA_UPGRADE_STRATEGY_GROUP_FAIL + planId + ":" + group;
    }
    /**
     * 功能描述:策略是否已经触发阀值 y n
     * @param: [planId]
     * @return: java.lang.String
     * @auther: nongchongwei
     * @date: 2018/11/28 16:57
     */
    public static String getUpgradeStrategyTriggerThresholdKey(Long planId) {
        return OTA_UPGRADE_STRATEGY_TRIGGER_THRESHOLD + planId;
    }

    public static String getOtaUpgradeNotOnlineFirmwareKey(Long productId) {
        return OTA_UPGRADE_NOT_ONLINE_FIRMWARE + productId;
    }
    
}
