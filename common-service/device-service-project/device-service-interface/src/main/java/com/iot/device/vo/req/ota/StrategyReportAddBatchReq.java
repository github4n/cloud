package com.iot.device.vo.req.ota;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：策略报告
 * 创建人： nongchongwei
 * 创建时间：2018/8/9 10:07
 * 修改人： nongchongwei
 * 修改时间：2018/8/9 10:07
 * 修改描述：
 */
public class StrategyReportAddBatchReq {

    /**
     * 升级计划id
     */
    private Long planId;

    /**
     * 设备uuid
     */
    private List<String> deviceUuidList;

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

    private String model;

    private Integer strategyGroup;

    public StrategyReportAddBatchReq() {

    }

    public StrategyReportAddBatchReq(Long planId, List<String> deviceUuidList, String upgradeType, String targetVersion, String originalVersion, String model, Integer strategyGroup) {
        this.planId = planId;
        this.deviceUuidList = deviceUuidList;
        this.upgradeType = upgradeType;
        this.targetVersion = targetVersion;
        this.originalVersion = originalVersion;
        this.model = model;
        this.strategyGroup = strategyGroup;
    }



    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public List<String> getDeviceUuidList() {
        return deviceUuidList;
    }

    public void setDeviceUuidList(List<String> deviceUuidList) {
        this.deviceUuidList = deviceUuidList;
    }

    public String getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public String getOriginalVersion() {
        return originalVersion;
    }

    public void setOriginalVersion(String originalVersion) {
        this.originalVersion = originalVersion;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getStrategyGroup() {
        return strategyGroup;
    }

    public void setStrategyGroup(Integer strategyGroup) {
        this.strategyGroup = strategyGroup;
    }
}
