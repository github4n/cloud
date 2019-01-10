package com.iot.boss.vo.uuid;

import com.iot.common.beans.SearchParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询UUID订单列表请求
 */
@ApiModel(value = "查询UUID订单列表请求", description = "查询UUID订单列表请求")
public class GetUUIDRefundReq extends SearchParam {

    @ApiModelProperty(value="客户邮箱账号",name="userName")
    private String userName;

    @ApiModelProperty(value="企业名称",name="tenantName")
    private String tenantName;

    @ApiModelProperty(value="订单状态",name="applyStatus")
    private Integer applyStatus;

    @ApiModelProperty(value="支付状态",name="payStatus")
    private Integer payStatus;

    @ApiModelProperty(value="批次号",name="batchNumId")
    private Long batchNumId;
    
    public Long getBatchNumId() {
		return batchNumId;
	}

	public void setBatchNumId(Long batchNumId) {
		this.batchNumId = batchNumId;
	}

	public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

}
