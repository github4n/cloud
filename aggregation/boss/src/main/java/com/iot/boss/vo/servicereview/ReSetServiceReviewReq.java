package com.iot.boss.vo.servicereview;

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
@ApiModel(value="ReSetServiceReviewReq", description="语音服务审核请求对象")
public class ReSetServiceReviewReq{

    @ApiModelProperty(name="语音服务id",value="serviceId")
    private Long serviceId;

    @ApiModelProperty(name="产品id",value="productId")
    private Long productId;

    @ApiModelProperty(name="租户id",value="tenantId")
    private Long tenantId;
    
    @ApiModelProperty(name="操作描述（提交审核，审核通过，审核不通过原因）",value="operateDesc")
    private String operateDesc;

    public ReSetServiceReviewReq() {

    }

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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

}
