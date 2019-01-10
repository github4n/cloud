package com.iot.device.vo.rsp.ota;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public class UpgradePlanResp {
    /**
     * 计划id
     */
    private Long id;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 启动Start 暂停Pause
     */
    private String planStatus;

    /**
     * 升级方式  推送升级Push(默认) 强制升级Force
     */
    private String upgradeType;

    /**
     * 升级版本号
     */
    private String targetVersion;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 编辑次数
     */
    private Integer editedTimes;

    /**
     * 策略开关
     */
    private Integer strategySwitch;

    /**
     * 升级范围
     */
    private Integer upgradeScope;

    /**
     * 计划启动时间
     */
    private Date startTime;

    /**
     * 升级计划明细
     */
    private List<UpgradePlanDetailResp> upgradePlanDetailRespList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
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

    public List<UpgradePlanDetailResp> getUpgradePlanDetailRespList() {
        return upgradePlanDetailRespList;
    }

    public void setUpgradePlanDetailRespList(List<UpgradePlanDetailResp> upgradePlanDetailRespList) {
        this.upgradePlanDetailRespList = upgradePlanDetailRespList;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEditedTimes() {
        return editedTimes;
    }

    public void setEditedTimes(Integer editedTimes) {
        this.editedTimes = editedTimes;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getStrategySwitch() {
        return strategySwitch;
    }

    public void setStrategySwitch(Integer strategySwitch) {
        this.strategySwitch = strategySwitch;
    }

    public Integer getUpgradeScope() {
        return upgradeScope;
    }

    public void setUpgradeScope(Integer upgradeScope) {
        this.upgradeScope = upgradeScope;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "UpgradePlanResp{" +
                "id=" + id +
                ", productId=" + productId +
                ", tenantId=" + tenantId +
                ", planStatus='" + planStatus + '\'' +
                ", upgradeType='" + upgradeType + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                ", updateTime=" + updateTime +
                ", editedTimes=" + editedTimes +
                ", strategySwitch=" + strategySwitch +
                ", upgradeScope=" + upgradeScope +
                ", startTime=" + startTime +
                ", upgradePlanDetailRespList=" + upgradePlanDetailRespList +
                '}';
    }
}