package com.iot.device.vo.req.ota;

import com.iot.common.beans.SearchParam;

import java.util.Set;

/**
 *
 * 项目名称：IOT云平台
 * 模块名称：设备
 * 功能描述：StrategyReport分页查询参数
 * 创建人： nongchongwei
 * 创建时间：2018/7/25 13:54
 * 修改人： nongchongwei
 * 修改时间：2018/7/25 13:54
 * 修改描述：
 */
public class StrategyReportSearchReqDto extends SearchParam {

    /**
     * 计划id
     */
    private Long planId;

    /**
     * 策略组
     */
    private Integer strategyGroup;


    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getStrategyGroup() {
        return strategyGroup;
    }

    public void setStrategyGroup(Integer strategyGroup) {
        this.strategyGroup = strategyGroup;
    }
}