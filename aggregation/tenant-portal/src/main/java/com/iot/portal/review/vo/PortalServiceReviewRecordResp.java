package com.iot.portal.review.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：语音接入服务审核记录Resp
 * 功能描述：语音接入服务审核记录Resp
 * 创建人： 李帅
 * 创建时间：2018年10月25日 下午5:22:33
 * 修改人：李帅
 * 修改时间：2018年10月25日 下午5:22:33
 */
@ApiModel(description = "语音接入服务审核记录Resp")
public class PortalServiceReviewRecordResp {

    @ApiModelProperty(name = "operateTime", value = "操作时间", dataType = "Date")
    private Date operateTime;

    @ApiModelProperty(name = "processStatus", value = "流程状态", dataType = "String")
    private Byte processStatus;

    @ApiModelProperty(name = "operateDesc", value = "操作描述", dataType = "String")
    private String operateDesc;

    @ApiModelProperty(name = "operator", value = "操作人", dataType = "String")
    private String operator;

    @ApiModelProperty(name = "serviceId", value = "语音服务Id，goods表中对应的Id", dataType = "Long")
    private Long serviceId;

    public PortalServiceReviewRecordResp() {

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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}


}
