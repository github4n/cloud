package com.iot.report.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目名称：立达信IOT云平台
 * 模块名称：报表服务
 * 功能描述：激活/活跃查询参数
 * 创建人： mao2080@sina.com
 * 创建时间：2019/1/8 10:28
 * 修改人： mao2080@sina.com
 * 修改时间：2019/1/8 10:28
 * 修改描述：
 */
@ApiModel(value = "激活/活跃查询参数")
public class ActivateBaseReq {

    @ApiModelProperty(name = "beginDate", value = "开始日期", dataType = "Date")
    private Date beginDate;

    @ApiModelProperty(name = "endDate", value = "结束日期", dataType = "Date")
    private Date endDate;

    @ApiModelProperty(name = "tenantId", value = "租户ID", dataType = "Long")
    private Long tenantId;

    public ActivateBaseReq() {

    }
	
	public ActivateBaseReq(Date[] dateArray, Long tenantId) {
        this.beginDate = dateArray[0];
        this.endDate = dateArray[1];
        this.tenantId = tenantId;
    }

    public ActivateBaseReq(Date beginDate, Date endDate, Long tenantId) {
        this.beginDate = beginDate;
        this.endDate = endDate;
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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}
