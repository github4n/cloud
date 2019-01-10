package com.iot.report.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
@ApiModel(value = "地区查询参数")
public class DevDistributionReq {
    @ApiModelProperty(name = "beginDate", value = "开始日期（查询包含）")
    private Date beginDate;
    @ApiModelProperty(name = "endDate", value = "结束日期（查询不包含）")
    private Date endDate;
    @ApiModelProperty(name = "activeOrActivate", value = "0:激活 1：活跃")
    private Integer activeOrActivate;
    @ApiModelProperty(name = "tenantId", value = "租户ID")
    private Long tenantId;


    public DevDistributionReq() {
    }

    public DevDistributionReq(Date beginDate, Date endDate, Integer activeOrActivate, Long tenantId) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.activeOrActivate = activeOrActivate;
        this.tenantId = tenantId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getActiveOrActivate() {
        return activeOrActivate;
    }

    public void setActiveOrActivate(Integer activeOrActivate) {
        this.activeOrActivate = activeOrActivate;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
