package com.iot.device.vo.req.servicereview;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：语音服务审核请求对象
 * 功能描述：语音服务审核请求对象
 * 创建人： 李帅
 * 创建时间：2018年10月25日 上午11:24:23
 * 修改人：李帅
 * 修改时间：2018年10月25日 上午11:24:23
 */
@ApiModel(value="SetServiceReviewReq", description="语音服务审核请求对象")
public class SetServiceReviewReq{

	@ApiModelProperty(name="租户主键",value="tenantId")
    private Long tenantId;
	
	@ApiModelProperty(name="用户ID",value="userId")
    private Long userId;
	
    @ApiModelProperty(name="语音服务id",value="serviceId")
    private Long serviceId;

    @ApiModelProperty(name="产品id",value="productId")
    private Long productId;

    @ApiModelProperty(name="审核状态（1:审核未通过 2:审核通过）",value="processStatus")
    private Byte processStatus;

    @ApiModelProperty(name="操作描述（提交审核，审核通过，审核不通过原因）",value="operateDesc")
    private String operateDesc;

    public SetServiceReviewReq() {

    }

    public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public SetServiceReviewReq(Long tenantId, Long userId, Long serviceId, Long productId, Byte processStatus, String operateDesc) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.serviceId = serviceId;
        this.productId = productId;
        this.processStatus = processStatus;
        this.operateDesc = operateDesc;
    }
}
