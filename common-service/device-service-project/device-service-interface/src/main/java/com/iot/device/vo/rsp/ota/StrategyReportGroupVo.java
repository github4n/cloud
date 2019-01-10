package com.iot.device.vo.rsp.ota;

import com.iot.device.vo.req.ota.StrategyReportResp;

import java.util.Date;
import java.util.List;

public class StrategyReportGroupVo {
    /**
     * 计划id
     */
    private Long planId;

    /**
     * 策略组
     */
    private Integer strategyGroup;
    /**
     * 总数
     */
    private Integer upgradeTotal;
    /**
     * 阀值
     */
    private Integer threshold;
    /**
     * 升级成功数量
     */
    private Integer success;
    /**
     * 升级失败数量
     */
    private Integer fail;

    /**
     * 升级范围
     */
    private Integer upgradeScope;
    /**
     * 策略组是否成功
     */
    private String strategyStatus;
    /**
     * 全部完成时间
     */
    private Date allCompleteTime;


    public Integer getStrategyGroup() {
        return strategyGroup;
    }

    public void setStrategyGroup(Integer strategyGroup) {
        this.strategyGroup = strategyGroup;
    }

    public Integer getUpgradeTotal() {
        return upgradeTotal;
    }

    public void setUpgradeTotal(Integer upgradeTotal) {
        this.upgradeTotal = upgradeTotal;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Integer getUpgradeScope() {
        return upgradeScope;
    }

    public void setUpgradeScope(Integer upgradeScope) {
        this.upgradeScope = upgradeScope;
    }

    public String getStrategyStatus() {
        return strategyStatus;
    }

    public void setStrategyStatus(String strategyStatus) {
        this.strategyStatus = strategyStatus;
    }

    public Date getAllCompleteTime() {
        return allCompleteTime;
    }

    public void setAllCompleteTime(Date allCompleteTime) {
        this.allCompleteTime = allCompleteTime;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }
}