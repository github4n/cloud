package com.iot.portal.ota.vo;

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
public class UpgradeLogVo {


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
     * 设备uuid
     *
     * @return device_uuid - 设备uuid
     */
    public String getDeviceUuid() {
        return deviceUuid;
    }

    /**
     * 设备uuid
     *
     * @param deviceUuid 设备uuid
     */
    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    /**
     * 升级方式  推送升级Push   强制升级Force
     *
     * @return upgrade_type - 升级方式  推送升级Push   强制升级Force
     */
    public String getUpgradeType() {
        return upgradeType;
    }

    /**
     * 升级方式  推送升级Push   强制升级Force
     *
     * @param upgradeType 升级方式  推送升级Push   强制升级Force
     */
    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    /**
     * 升级版本号
     *
     * @return target_version - 升级版本号
     */
    public String getTargetVersion() {
        return targetVersion;
    }

    /**
     * 升级版本号
     *
     * @param targetVersion 升级版本号
     */
    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    /**
     * 原版本号
     *
     * @return original _version - 原版本号
     */
    public String getOriginalVersion() {
        return originalVersion;
    }

    /**
     * 原版本号
     *
     * @param originalVersion 原版本号
     */
    public void setOriginalVersion(String originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * 升级结果
     *
     * @return upgrade_result - 升级结果
     */
    public String getUpgradeResult() {
        return upgradeResult;
    }

    /**
     * 升级结果
     *
     * @param upgradeResult 升级结果
     */
    public void setUpgradeResult(String upgradeResult) {
        this.upgradeResult = upgradeResult;
    }

    /**
     * 完成时间
     *
     * @return complete_time - 完成时间
     */
    public Date getCompleteTime() {
        return completeTime;
    }

    /**
     * 完成时间
     *
     * @param completeTime 完成时间
     */
    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
}