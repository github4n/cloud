package com.iot.device.vo.req.ota;

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
public class UpgradePlanEditReq {
    /**
     * 计划id
     */
    private Long id;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 产品id
     */
    private Long productId;

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
     * 升级计划明细
     */
    private List<UpgradePlanDetailEditReq> upgradePlanDetailEditReqList;

    /***
     * 指定uuid升级 uuid列表
     */
    private List<String> deviceIdEditReqList;

    /**
     * 批次升级 批次号列表
     */
    private List<Long> batchNumEditReqList;

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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<UpgradePlanDetailEditReq> getUpgradePlanDetailEditReqList() {
        return upgradePlanDetailEditReqList;
    }

    public void setUpgradePlanDetailEditReqList(List<UpgradePlanDetailEditReq> upgradePlanDetailEditReqList) {
        this.upgradePlanDetailEditReqList = upgradePlanDetailEditReqList;
    }

    public Integer getEditedTimes() {
        return editedTimes;
    }

    public void setEditedTimes(Integer editedTimes) {
        this.editedTimes = editedTimes;
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

    public List<String> getDeviceIdEditReqList() {
        return deviceIdEditReqList;
    }

    public void setDeviceIdEditReqList(List<String> deviceIdEditReqList) {
        this.deviceIdEditReqList = deviceIdEditReqList;
    }

    public List<Long> getBatchNumEditReqList() {
        return batchNumEditReqList;
    }

    public void setBatchNumEditReqList(List<Long> batchNumEditReqList) {
        this.batchNumEditReqList = batchNumEditReqList;
    }
}