package com.iot.device.vo.rsp.ota;

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
public class UpgradePlanDetailResp {
    /**
     * 明细id
     */
    private Long id;

    /**
     * 升级计划id
     */
    private Long planId;

    /**
     * 是否有过渡版本 true/false
     */
    private Integer hasTransition;

    /**
     * 替换版本号
     */
    private List<String> substituteVersionList;

    /**
     * 过度版本号
     */
    private List<String> transitionVersionList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getHasTransition() {
        return hasTransition;
    }

    public void setHasTransition(Integer hasTransition) {
        this.hasTransition = hasTransition;
    }

    public List<String> getSubstituteVersionList() {
        return substituteVersionList;
    }

    public void setSubstituteVersionList(List<String> substituteVersionList) {
        this.substituteVersionList = substituteVersionList;
    }

    public List<String> getTransitionVersionList() {
        return transitionVersionList;
    }

    public void setTransitionVersionList(List<String> transitionVersionList) {
        this.transitionVersionList = transitionVersionList;
    }
}