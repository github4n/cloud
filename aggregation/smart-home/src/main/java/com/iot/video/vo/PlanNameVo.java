package com.iot.video.vo;

import io.swagger.annotations.ApiModel;

@ApiModel
public class PlanNameVo {

    /**
     * 计划id
     */
    private String planId;

    /**
     * 计划名称
     */
    private String planName;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

}
