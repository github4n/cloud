package com.iot.device.vo.req.ota;

/**
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：升级日志
 * 创建人： mao2080@sina.com
 * 创建时间：2018/8/9 10:07
 * 修改人： mao2080@sina.com
 * 修改时间：2018/8/9 10:07
 * 修改描述：
 */
public class UpgradeLogAddReq {

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
     * model
     */
    private String model;
    /***
     * strategyGroup
     */
    private Integer strategyGroup;
    /***
     * productId
     */
    private Long productId;

    public UpgradeLogAddReq() {

    }

    public UpgradeLogAddReq(Long planId, String deviceUuid, String upgradeType, String targetVersion, String originalVersion) {
        this.planId = planId;
        this.deviceUuid = deviceUuid;
        this.upgradeType = upgradeType;
        this.targetVersion = targetVersion;
        this.originalVersion = originalVersion;
    }

    public UpgradeLogAddReq(Long planId, String deviceUuid, String upgradeType, String targetVersion,
                            String originalVersion, String model, Integer strategyGroup, Long productId) {
        this.planId = planId;
        this.deviceUuid = deviceUuid;
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

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
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

    @Override
    public String toString() {
        return "UpgradeLogAddReq{" +
                "planId=" + planId +
                ", deviceUuid='" + deviceUuid + '\'' +
                ", upgradeType='" + upgradeType + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                ", originalVersion='" + originalVersion + '\'' +
                ", model='" + model + '\'' +
                ", strategyGroup=" + strategyGroup +
                ", productId=" + productId +
                '}';
    }
}
