package com.iot.portal.review.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 项目名称：云平台
 * 功能描述：产品审核记录Resp
 * 创建人： maochengyuan
 * 创建时间：2018/10/23 13:52
 * 修改人： maochengyuan
 * 修改时间：2018/10/23 13:52
 * 修改描述：
 */
@ApiModel(description = "产品审核记录Resp")
public class PortalProductReviewRecordResp {

    @ApiModelProperty(name = "operateTime", value = "操作时间", dataType = "Date")
    private Date operateTime;

    @ApiModelProperty(name = "processStatus", value = "流程状态", dataType = "String")
    private Byte processStatus;

    @ApiModelProperty(name = "operateDesc", value = "操作描述", dataType = "String")
    private String operateDesc;

    @ApiModelProperty(name = "createBy", value = "创建人", dataType = "Long")
    private Long createBy;

    @ApiModelProperty(name = "operator", value = "操作人账号", dataType = "String")
    private String operator;

    public PortalProductReviewRecordResp() {

    }

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Byte getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Byte processStatus) {
        this.processStatus = processStatus;
    }

    public String getOperateDesc() {
        return operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }


}
