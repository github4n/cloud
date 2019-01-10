package com.iot.device.vo.req.ota;

import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：升级日志
 * 创建人： nongchongwei
 * 创建时间：2018/8/9 10:07
 * 修改人： nongchongwei
 * 修改时间：2018/8/9 10:07
 * 修改描述：
 */
public class UpgradeLogAddBatchReq {

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

    /**
     * model
     */
    private String model;

    /**
     * strategyGroup
     */
    private Integer strategyGroup;

    /**
     * productId
     */
    private Long productId;

    public UpgradeLogAddBatchReq() {

    }

    public UpgradeLogAddBatchReq(Long planId, List<String> deviceUuidList, String upgradeType, String targetVersion, String originalVersion) {
        this.planId = planId;
        this.deviceUuidList = deviceUuidList;
        this.upgradeType = upgradeType;
        this.targetVersion = targetVersion;
        this.originalVersion = originalVersion;
    }

    public UpgradeLogAddBatchReq(Long planId, List<String> deviceUuidList, String upgradeType,
                                 String targetVersion, String originalVersion, String model, Integer strategyGroup, Long productId) {
        this.planId = planId;
        this.deviceUuidList = deviceUuidList;
        this.upgradeType = upgradeType;
        this.targetVersion = targetVersion;
        this.originalVersion = originalVersion;
        this.model = model;
        this.strategyGroup = strategyGroup;
        this.productId = productId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
