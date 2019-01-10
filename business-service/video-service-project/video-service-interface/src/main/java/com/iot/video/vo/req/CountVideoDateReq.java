package com.iot.video.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：cloud
 * 功能描述：统计录影日期入参
 * 创建人： yeshiyuan
 * 创建时间：2018/7/26 14:07
 * 修改人： yeshiyuan
 * 修改时间：2018/7/26 14:07
 * 修改描述：
 */
@ApiModel(value = "统计录影日期入参")
public class CountVideoDateReq {
    @ApiModelProperty(name = "planId", value = "计划id", dataType = "String")
    private String planId;

    @ApiModelProperty(name = "startDate", value = "起始时间", dataType = "Date")
    private Date startDate;

    @ApiModelProperty(name = "endDate", value = "结束时间", dataType = "Date")
    private Date endDate;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
