package com.iot.shcs.ota.vo;

import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称：IOT云平台
 * 模块名称：ota
 * 功能描述：ota
 * 创建人： nongchongwei
 * 创建时间：2018年07月24日 16:58
 * 修改人： nongchongwei
 * 修改时间：2018年07月24日 16:58
 */
public class StrategyInfoVo implements Serializable {

    public StrategyInfoVo() {
    }


    public StrategyInfoVo(Integer currentGroup, Integer upgradeTotal) {
        this.currentGroup = currentGroup;
        this.upgradeTotal = upgradeTotal;
    }

    public StrategyInfoVo(Integer currentGroup, Integer upgradeTotal, Integer threshold) {
        this.currentGroup = currentGroup;
        this.upgradeTotal = upgradeTotal;
        this.threshold = threshold;
    }

    private Integer currentGroup;

    private Integer upgradeTotal;

    private Integer threshold;

    private Integer success;

    private Integer fail;

    private Date completeTime;



    public Integer getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Integer currentGroup) {
        this.currentGroup = currentGroup;
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

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    @Override
    public String toString() {
        return "StrategyInfoVo{" +
                "currentGroup=" + currentGroup +
                ", upgradeTotal=" + upgradeTotal +
                '}';
    }
}
