package com.iot.device.model.ota;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
@Data
@ToString
public class UpgradeLog {
    /**
     * 日志id
     */
    private Long id;

    /**
     * 升级计划id
     */
    private Long planId;

    /**
     * 设备uuid
     */
    private String deviceUuid;

    /**
     * 升级方式  推送升级Push   强制升级Force
     */
    private String upgradeType;

    /**
     * 升级版本号
     */
    private String targetVersion;

    /**
     * 原版本号
     */
    private String originalVersion;

    /**
     * 升级结果
     */
    private String upgradeResult;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 升级信息
     */
    private String upgradeMsg;
    /**
     * 租户id
     */
    private Long tenantId;

    public UpgradeLog() {

    }

    public UpgradeLog(String upgradeResult, Date completeTime) {
        this.upgradeResult = upgradeResult;
        this.completeTime = completeTime;
    }

    public UpgradeLog(String upgradeResult, Date completeTime, String upgradeMsg) {
        this.upgradeResult = upgradeResult;
        this.completeTime = completeTime;
        this.upgradeMsg = upgradeMsg;
    }
}